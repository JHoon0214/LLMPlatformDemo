package cnsa.demo.controller;

import cnsa.demo.service.llm.ILLMService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
@RequestMapping("/chat/gpt3_5")
public class GPT3_5Controller implements ILLMController{
    private final ILLMService illmService;

    public GPT3_5Controller(@Qualifier("gpt3_5Service") ILLMService illmService) {
        this.illmService = illmService;
    }

    @GetMapping("/stream")
    public SseEmitter streamMessages() {
        System.out.println("stream is called");
        return illmService.streamMessages();
    }
}
