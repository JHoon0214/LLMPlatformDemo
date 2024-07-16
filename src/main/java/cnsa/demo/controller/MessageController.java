package cnsa.demo.controller;

import cnsa.demo.DTO.messageDTO.GlobalMessageDTO;
import cnsa.demo.config.LLM.LLMConfig;
import cnsa.demo.domain.Workspace;
import cnsa.demo.service.message.IMessageService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;
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

    @GetMapping("/message")
    public ResponseEntity<List<GlobalMessageDTO>> getChatPage() {
        Workspace workspace = (Workspace) httpSession.getAttribute("workspace");
        List<GlobalMessageDTO> messages = messageService.getAllMessage(workspace);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/refreshedChat")
    public String getChatPage(Model model) {
        Workspace workspace = (Workspace) httpSession.getAttribute("workspace");
        List<GlobalMessageDTO> messages = messageService.getAllMessage(workspace);
        model.addAttribute("messages", messages);
        return "workspace::chat-container";
    }
}
