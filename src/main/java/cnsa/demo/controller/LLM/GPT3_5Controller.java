package cnsa.demo.controller.LLM;

import cnsa.demo.DTO.Security.SessionUser;
import cnsa.demo.domain.Workspace;
import cnsa.demo.service.llm.ILLMService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
@RequestMapping("/chat/gpt3_5")
public class GPT3_5Controller implements ILLMController{
    private final ILLMService illmService;
    private final HttpSession httpSession;

    public GPT3_5Controller(@Qualifier("gpt3_5Service") ILLMService illmService, HttpSession httpSession) {
        this.illmService = illmService;
        this.httpSession = httpSession;
    }

    @GetMapping("/stream")
    public SseEmitter streamMessages() {
        Workspace workspace = (Workspace) httpSession.getAttribute("workspace");
        //if(workspace.getUser().getId().equals(((SessionUser)httpSession.getAttribute("user")).get))
        return illmService.streamMessages(workspace);
    }
}
