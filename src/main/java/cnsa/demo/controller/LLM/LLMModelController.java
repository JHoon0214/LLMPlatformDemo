package cnsa.demo.controller.LLM;

import cnsa.demo.DTO.LLMModelDTO;
import cnsa.demo.service.llm.LLMModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/model")
public class LLMModelController {
    private final LLMModelService llmModelService;
    public List<LLMModelDTO> getAllLLMInfo() {
        return llmModelService.getAllLLMInfo();
    }
}
