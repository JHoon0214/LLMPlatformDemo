package cnsa.demo.service.llm;

import cnsa.demo.DTO.LLMModelDTO;
import cnsa.demo.domain.LLMModel;
import cnsa.demo.repository.LLMModelRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LLMModelService {
    private final LLMModelRepository llmModelRepository;
    public LLMModelDTO getLLMInfo(Long llmId) {
        Optional<LLMModel> llmModel = llmModelRepository.findByModelId(llmId);
        if(llmModel.isEmpty()) throw new RuntimeException("There is no llm with id " + llmId);
        return new LLMModelDTO(llmModel.get());
    }

    public List<LLMModelDTO> getAllLLMInfo() {
        List<LLMModel> llmModels = llmModelRepository.findAll();
        List<LLMModelDTO> llmModelDTOS = new ArrayList<>();

        for(LLMModel llmModel:llmModels) {
            llmModelDTOS.add(new LLMModelDTO(llmModel));
        }

        return llmModelDTOS;
    }
}
