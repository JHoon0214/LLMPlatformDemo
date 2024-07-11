package cnsa.demo.service.llm;

import cnsa.demo.DTO.messageDTO.GlobalMessageDTO;
import cnsa.demo.domain.Workspace;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ILLMService {
    SseEmitter streamMessages(Workspace workspace);

    Flux<String> getResponse(List<GlobalMessageDTO> conversations);
    String extractContent(String jsonEvent);
    List<GlobalMessageDTO> getLLMInputs();
}
