package cnsa.demo.service.llm;

import cnsa.demo.DTO.messageDTO.GlobalMessageDTO;
import cnsa.demo.config.LLMConfig;
import cnsa.demo.service.message.IMessageService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;

public abstract class LLMService implements ILLMService {

    private final IMessageService messageService;

    protected LLMService(IMessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public SseEmitter streamMessages() {
        SseEmitter emitter = new SseEmitter();
        List<GlobalMessageDTO> allMessages = messageService.getAllMessage();

        Flux<String> eventStream = getResponse(allMessages);
        StringBuilder llmResponse = new StringBuilder();

        eventStream.subscribe(
                event -> {
                    try {
                        String content = extractContent(event);
                        if (!content.isEmpty()) {
                            SseEmitter.SseEventBuilder eventBuilder = SseEmitter.event()
                                    .data(content)
                                    .name("message");
                            emitter.send(eventBuilder);
                            llmResponse.append(content);
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
                        messageService.saveMessage(GlobalMessageDTO.builder()
                                .content(llmResponse.toString())
                                .role(LLMConfig.ROLE_ASSISTANT)
                                .build());
                    } catch (IOException e) {
                        emitter.completeWithError(e);
                    }

                }
        );

        return emitter;
    }

    // make a request and response to LLM with user inputs
    @Override
    public abstract Flux<String> getResponse(List<GlobalMessageDTO> conversations);

    @Override
    public abstract String extractContent(String jsonEvent);
}
