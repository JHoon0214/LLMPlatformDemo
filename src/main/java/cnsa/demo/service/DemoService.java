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

        for (Message message : messages) {
            messageDTOs.add(new GlobalMessageDTO(message));
        }

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

        for (GlobalMessageDTO messageDTO : conversations) {
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

        return webClient.post()
                .bodyValue(request)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class);
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
                        if (!content.isEmpty()) {
                            SseEmitter.SseEventBuilder eventBuilder = SseEmitter.event()
                                    .data(content)
                                    .name("message");
                            emitter.send(eventBuilder);
                            gptResponse.append(content);
                        }
                    } catch (IOException e) {
                        emitter.completeWithError(e);
                    }
                },
                error -> emitter.completeWithError(error),
                () -> {
                    try {
                        emitter.send(SseEmitter.event()
                                .data("")
                                .name("end"));
                        emitter.complete();

                        // Save the GPT response to the database
                        saveMessage(GlobalMessageDTO.builder()
                                .content(gptResponse.toString())
                                .role(GPT3_5Config.ROLE_ASSISTANT)
                                .build());
                    } catch (IOException e) {
                        emitter.completeWithError(e);
                    }
                }
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
}
