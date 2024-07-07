package cnsa.demo.controller;

import cnsa.demo.DTO.messageDTO.GlobalMessageDTO;
import cnsa.demo.config.GPT3_5Config;
import cnsa.demo.entity.Message;
import cnsa.demo.service.DemoService;
import com.mysql.cj.protocol.MessageSender;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class DemoController {
    private final DemoService demoService;

    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping("/")
    public String getChatPage(Model model) {
        List<GlobalMessageDTO> messages = demoService.getAllMessage();
        model.addAttribute("messages", messages);
        return "index";
    }

    @PostMapping("/test/send")
    @ResponseBody
    public void testSendMessage(@RequestBody Map<String, String> message) {
        System.out.println(message.get("text"));
        demoService.saveMessage(GlobalMessageDTO.builder()
                .content(message.get("text"))
                .role(GPT3_5Config.ROLE_USER)
                .build()
        );

        List<GlobalMessageDTO> allMessages = demoService.getAllMessage();
//        demoService.handleStream(allMessages);
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage (@RequestBody Map<String, String> messageText) {
        demoService.saveMessage(GlobalMessageDTO.builder()
                .content(messageText.get("text"))
                .role(GPT3_5Config.ROLE_USER)
                .build()
        );

        return ResponseEntity.ok().build();


//        List<GlobalMessageDTO> allMessages = demoService.getAllMessage();
//
//        String gptResponse = demoService.handleStream(allMessages);
//        demoService.saveMessage(gptResponse, false);
//
//        Map<String, String> response = new HashMap<>();
//        response.put("response", gptResponse);
//        return ResponseEntity.ok(response);
    }

    @GetMapping("/stream")
    public SseEmitter streamMessages() {
        System.out.println("stream is called");
        return demoService.streamMessages();
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}