package cnsa.demo.controller;

import cnsa.demo.DTO.messageDTO.GlobalMessageDTO;
import cnsa.demo.service.message.IMessageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DemoWebController {
    private final IMessageService messageService;

    public DemoWebController(IMessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/")
    public String getChatPage(Model model) {
        List<GlobalMessageDTO> messages = messageService.getAllMessage();
        model.addAttribute("messages", messages);
        return "index";
    }
}
