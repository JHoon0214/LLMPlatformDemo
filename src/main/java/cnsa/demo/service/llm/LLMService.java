package cnsa.demo.service.llm;

import cnsa.demo.DTO.messageDTO.GlobalMessageDTO;
import cnsa.demo.config.LLM.LLMConfig;
<<<<<<< HEAD
import cnsa.demo.repository.MessageRepository;
import cnsa.demo.service.message.IMessageService;
=======
import cnsa.demo.domain.Workspace;
import cnsa.demo.repository.WorkspaceRepository;
import cnsa.demo.service.message.IMessageService;
import jakarta.servlet.http.HttpSession;
>>>>>>> 1e02964cb5f70bcdb27bd00c6552a0d0376db58c
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
<<<<<<< HEAD
import java.util.ArrayList;
=======
import java.time.LocalDateTime;
>>>>>>> 1e02964cb5f70bcdb27bd00c6552a0d0376db58c
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public abstract class LLMService implements ILLMService {

    private final IMessageService messageService;
<<<<<<< HEAD
    private final MessageRepository messageRepository;

    protected LLMService(IMessageService messageService, MessageRepository messageRepository) {
        this.messageService = messageService;
        this.messageRepository = messageRepository;
    }
=======
    private final HttpSession httpSession;
>>>>>>> 1e02964cb5f70bcdb27bd00c6552a0d0376db58c

    @Override
    public SseEmitter streamMessages(Workspace workspace) {
        SseEmitter emitter = new SseEmitter();
<<<<<<< HEAD
        List<GlobalMessageDTO> allMessages = getLLMInputs();
=======
        List<GlobalMessageDTO> allMessages = messageService.getAllMessage(workspace);
>>>>>>> 1e02964cb5f70bcdb27bd00c6552a0d0376db58c

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
                                .createdAt(LocalDateTime.now())
                                .workspace(workspace)
                                .build());
                    } catch (IOException e) {
                        emitter.completeWithError(e);
                    }

                }
        );

        return emitter;
    }

    @Override
    public List<GlobalMessageDTO> getLLMInputs() {
        List<GlobalMessageDTO> globalMessageDTOS = messageRepository
        if(globalMessageDTOS == null) throw new RuntimeException("The Messages is Null");

        int size = globalMessageDTOS.size();
        int startIndex = Math.max(size-10, 0);
        List<GlobalMessageDTO> parsedDatas = new ArrayList<>(globalMessageDTOS.subList(startIndex, size));

        return parsedDatas;
    }

    // make a request and response to LLM with user inputs
    @Override
    public abstract Flux<String> getResponse(List<GlobalMessageDTO> conversations);

    @Override
    public abstract String extractContent(String jsonEvent);
}
