package cnsa.demo.controller.oldVer.LLM;

import cnsa.demo.domain.Workspace;
import cnsa.demo.service.oldVer.llm.ILLMService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
@RequestMapping("/chat/gpt4o")
public class GPT4oController implements ILLMController {
    private final ILLMService illmService;
    private final HttpSession httpSession;

    public GPT4oController(@Qualifier("gpt4oService") ILLMService illmService, HttpSession httpSession) {
        this.illmService = illmService;
        this.httpSession = httpSession;
    }

    @GetMapping("/stream")
    public SseEmitter streamMessages() {
        Workspace workspace = (Workspace) httpSession.getAttribute("workspace");
        return illmService.streamMessages(workspace);
    }
}
