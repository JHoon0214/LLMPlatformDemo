package cnsa.demo.service.llm.GPT;

import cnsa.demo.DTO.messageDTO.GPTMessageDTO;
import cnsa.demo.DTO.messageDTO.GlobalMessageDTO;
import cnsa.demo.DTO.requestDTO.GptRequestDTO;
import cnsa.demo.config.LLM.GPT3_5Config;
import cnsa.demo.service.llm.LLMService;
import cnsa.demo.service.message.IMessageService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Service("gpt3_5Service")
public class GPT3_5Service extends LLMService {

    @Value("${openai.api.key}")
    private String apiKey;

    protected GPT3_5Service(IMessageService messageService) {
        super(messageService);
    }

    @Override
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

    @Override
    public String extractContent(String jsonEvent) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(jsonEvent);
            return node.at(GPT3_5Config.RESPONSE_NODE_AT).asText();
        } catch (Exception e) {
            return "";
        }
    }
}
