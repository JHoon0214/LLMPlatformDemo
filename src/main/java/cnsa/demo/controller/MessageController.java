package cnsa.demo.controller;

import cnsa.demo.DTO.messageDTO.GlobalMessageDTO;
import cnsa.demo.config.LLM.LLMConfig;
import cnsa.demo.domain.Workspace;
import cnsa.demo.service.message.IMessageService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class MessageController {

    private final IMessageService messageService;
    private final HttpSession httpSession;

    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage (@RequestBody Map<String, String> messageText) {
        messageService.saveMessage(GlobalMessageDTO.builder()
                .content(messageText.get("text"))
                .role(LLMConfig.ROLE_USER)
                .createdAt(LocalDateTime.now())
                .workspace((Workspace)httpSession.getAttribute("workspace"))
                .build()
        );

        return ResponseEntity.ok().build();
    }

}
