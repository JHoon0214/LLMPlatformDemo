package cnsa.demo.controller;

import cnsa.demo.DTO.messageDTO.GlobalMessageDTO;
import cnsa.demo.config.LLMConfig;
import cnsa.demo.service.message.IMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/chat")
public class MessageController {

    private final IMessageService messageService;

    public MessageController(IMessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage (@RequestBody Map<String, String> messageText) {
        messageService.saveMessage(GlobalMessageDTO.builder()
                .content(messageText.get("text"))
                .role(LLMConfig.ROLE_USER)
                .build()
        );

        return ResponseEntity.ok().build();
    }

}
