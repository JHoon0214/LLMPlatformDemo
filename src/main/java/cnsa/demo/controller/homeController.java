package cnsa.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class homeController {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("welcomeMessage", "This is Homepage");
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}