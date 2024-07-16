package cnsa.demo.controller;

import cnsa.demo.DTO.messageDTO.GlobalMessageDTO;
import cnsa.demo.DTO.workspaceDTO.WorkspaceDTO;
import cnsa.demo.domain.Workspace;
import cnsa.demo.service.message.IMessageService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DemoWebController {
    private final IMessageService messageService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String getChatPage(Model model) {
        Workspace workspace = (Workspace) httpSession.getAttribute("workspace");
        List<GlobalMessageDTO> messages = messageService.getAllMessage(workspace);
        model.addAttribute("messages", messages);
        return "index";
    }
}
