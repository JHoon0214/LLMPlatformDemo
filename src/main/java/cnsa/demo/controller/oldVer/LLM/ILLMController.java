package cnsa.demo.controller.oldVer.LLM;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface ILLMController {
    public SseEmitter streamMessages();
}
