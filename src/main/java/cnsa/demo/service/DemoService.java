package cnsa.demo.service;

import cnsa.demo.DTO.messageDTO.GPTMessageDTO;
import cnsa.demo.DTO.messageDTO.GlobalMessageDTO;
import cnsa.demo.DTO.requestDTO.GptRequestDTO;
import cnsa.demo.config.GPT3_5Config;
import cnsa.demo.entity.Message;
import cnsa.demo.repository.MessageRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.*;

@Service
public class DemoService {

    private final MessageRepository messageRepository;

    @Value("${openai.api.key}")
    private String apiKey;

    public DemoService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void saveMessage(GlobalMessageDTO messageDTO) {
        messageRepository.save(messageDTO.convertToMessage());
    }

    public List<GlobalMessageDTO> getAllMessage() {
        List<Message> messages = messageRepository.findAll();
        List<GlobalMessageDTO> messageDTOs = new ArrayList<>();

        for(Message message: messages) messageDTOs.add(new GlobalMessageDTO(message));

        return messageDTOs;
    }

    // make a request and response to LLM with user inputs
    public Flux<String> getResponse(List<GlobalMessageDTO> conversations) {
        WebClient webClient = WebClient.builder()
                .baseUrl(GPT3_5Config.CHAT_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(GPT3_5Config.AUTHORIZATION, GPT3_5Config.BEARER + apiKey)
                .build();

        List<GPTMessageDTO> messages = new ArrayList<>();

        for(GlobalMessageDTO messageDTO:conversations) {
            GPTMessageDTO gptMessageDTO = new GPTMessageDTO();
            gptMessageDTO.convertMessageToIModelMessage(messageDTO);
            messages.add(gptMessageDTO);
        }

        GptRequestDTO request = GptRequestDTO.builder()
                .model(GPT3_5Config.CHAT_MODEL)
                .maxTokens(GPT3_5Config.MAX_TOKEN)
                .temperature(GPT3_5Config.TEMPERATURE)
                .stream(GPT3_5Config.STREAM)
                .messages(messages)
                .build();

        Flux<String> eventStream = webClient.post()
                .bodyValue(request)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class);

        return eventStream;
    }

    public SseEmitter streamMessages() {
        SseEmitter emitter = new SseEmitter();
        List<GlobalMessageDTO> allMessages = getAllMessage();

        Flux<String> eventStream = getResponse(allMessages);
        StringBuilder gptResponse = new StringBuilder();

        eventStream.subscribe(
                event -> {
                    try {
                        String content = extractContent(event);
                        emitter.send(content);
                    } catch (IOException e) {
                        emitter.completeWithError(e);
                    }
                },
                error -> emitter.completeWithError(error),
                emitter::complete
        );

        eventStream.toStream().forEach(event -> {
            String content = extractContent(event);
            gptResponse.append(content);
        });

        saveMessage(GlobalMessageDTO.builder()
                .content(gptResponse.toString())
                .role(GPT3_5Config.ROLE_ASSISTANT)
                .build()
        );

        return emitter;
    }

    private String extractContent(String jsonEvent) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(jsonEvent);
            return node.at("/choices/0/delta/content").asText();
        } catch (Exception e) {
            return "";
        }
    }

    public void handleStream(List<GlobalMessageDTO> allMessages) {
        // 스트리밍된 응답을 처리하는 로직 구현
        Flux<String> eventStream = getResponse(allMessages);
        StringBuilder gptResponse = new StringBuilder();

        eventStream.toStream().forEach(event -> {
            String content = extractContent(event);
            gptResponse.append(content);
        });

        saveMessage(GlobalMessageDTO.builder()
                .content(gptResponse.toString())
                .role(GPT3_5Config.ROLE_ASSISTANT)
                .build()
        );
    }

    //    private List<Map<String, String>> parseConversationToGPTInput(List<Message> conversation) {
//        List<Map<String, String>> ret = new ArrayList<>();
//
//        Map<String, String> system = new HashMap<>();
//        system.put("role", "system");
//        system.put("content", "you are chat gpt. You have to look at the user's question and give an appropriate answer. If necessary, you can refer to our previous conversation. Content with a role of user is input made by the user, and content with a role of assistant is a response made by gpt in the past.");
//
//        ret.add(system);
//
//        for(Message curr: conversation) {
//            Map<String, String> map = new HashMap<>();
//            if(curr.isUserInput()) map.put("role", "user");
//            else map.put("role", "assistant");
//
//            map.put("content", curr.getText());
//
//            ret.add(map);
//        }
//        return ret;
//    }

//    public String getGptResponse(List<GlobalMessageDTO> conversation) {
//        String url = "https://api.openai.com/v1/chat/completions";
//        RestTemplate restTemplate = new RestTemplate();
//
//        for(GlobalMessageDTO message:conversation) {
//            System.out.println(message.getText() + "\n");
//        }
//
//        Map<String, Object> request = new HashMap<>();
//        request.put("model", "gpt-3.5-turbo");
//        request.put("messages", parseConversationToGPTInput(conversation));
//        request.put("max_tokens", 4096);
//        request.put("temperature", 0.7);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + apiKey);
//
//        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(request, headers);
//
//        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, httpEntity, Map.class);
//        System.out.println(responseEntity);
//        List<Map<String, Object>> list = ((List) responseEntity.getBody().get("choices"));
//        String ret = ((Map<String, String>)list.get(0).get("message")).get("content");
//        System.out.println(ret);
//
//        return ret;
//    }
}
