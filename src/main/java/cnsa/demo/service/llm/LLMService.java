package cnsa.demo.service.llm;

import cnsa.demo.DTO.messageDTO.GlobalMessageDTO;
import cnsa.demo.config.LLM.GPT3_5System;
import cnsa.demo.config.LLM.LLMConfig;

import cnsa.demo.domain.Message;
import cnsa.demo.domain.Workspace;
import cnsa.demo.repository.MessageRepository;
import cnsa.demo.service.message.IMessageService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RequiredArgsConstructor
public abstract class LLMService implements ILLMService {

    private final IMessageService messageService;
    private final MessageRepository messageRepository;
    private final HttpSession httpSession;
    @Override
    public SseEmitter streamMessages(Workspace workspace) {
        SseEmitter emitter = new SseEmitter();
        List<GlobalMessageDTO> inputMessages = getLLMInputs();

        Flux<String> eventStream = getResponse(inputMessages);
        StringBuilder llmResponse = new StringBuilder();

        eventStream.subscribe(
                event -> {
                    try {
                        String content = extractContent(event);
                        System.out.println("Received content: '" + content + "'");
                        content = content.replaceAll("&", "&amp;");
                        content = content.replaceAll(" ", "&nbsp;");
                        content = content.replaceAll("<", "&lt;");
                        content = content.replaceAll(">", "&gt;");
                        content = content.replaceAll("\n", "<br>");
                        content = content.replaceAll("\"", "&quot;");
                        if (!content.isEmpty()) {
                            if(llmResponse.isEmpty()) {
                                System.out.println("starting : " + "'" + content + "'");
                            }
                            System.out.println("content");
                            SseEmitter.SseEventBuilder eventBuilder = SseEmitter.event()
                                    .data(content)
                                    .name("message");

                            System.out.println("Sending event: '" + content + "'");

                            emitter.send(eventBuilder);
                            llmResponse.append(content);
                        }
                    } catch (IOException e) {
                        messageService.saveMessage(GlobalMessageDTO.builder()
                                .content(llmResponse.toString())
                                .role(LLMConfig.ROLE_ASSISTANT)
                                .createdAt(LocalDateTime.now())
                                .workspace(workspace)
                                .build()
                        );
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
        List<Message> messages = messageRepository.findTop10ByWorkspaceOrderByCreatedAtDesc((Workspace) httpSession.getAttribute("workspace"));
        if(messages == null) throw new RuntimeException("The Messages is Null");
        if(messages.isEmpty()) throw new RuntimeException("User input is not saved");

        List<GlobalMessageDTO> parsedDatas = new ArrayList<>();

        parsedDatas.add(GlobalMessageDTO.builder()
                .createdAt(messages.get(0).getCreatedAt())
                .content(GPT3_5System.SYSTEM_PROMPT)
                .role("system")
                .workspace(messages.get(0).getWorkspace())
                .build()
        );

        for(int i=messages.size()-1; i>=0; i--) parsedDatas.add(new GlobalMessageDTO(messages.get(i)));

        return parsedDatas;
    }

    // make a request and response to LLM with user inputs
    @Override
    public abstract Flux<String> getResponse(List<GlobalMessageDTO> conversations);

    @Override

    public abstract String extractContent(String jsonEvent);
}
