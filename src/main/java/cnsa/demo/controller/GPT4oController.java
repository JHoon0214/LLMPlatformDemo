package cnsa.demo.controller;

import cnsa.demo.service.llm.ILLMService;
import cnsa.demo.service.message.IMessageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
@RequestMapping("/chat/gpt4o")
public class GPT4oController implements ILLMController {
    private final ILLMService illmService;

    public GPT4oController(@Qualifier("gpt4oService") ILLMService illmService) {
        this.illmService = illmService;
    }

    @GetMapping("/stream")
    public SseEmitter streamMessages() {
        System.out.println("stream is called");
        return illmService.streamMessages();
    }
}
