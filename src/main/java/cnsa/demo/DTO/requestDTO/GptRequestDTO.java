package cnsa.demo.DTO.requestDTO;

import cnsa.demo.DTO.messageDTO.GPTMessageDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GptRequestDTO {
    private String model;
    @JsonProperty("max_tokens")
    private Integer maxTokens;
    private Double temperature;
    private Boolean stream;
    private List<GPTMessageDTO> messages;

    @Builder
    public GptRequestDTO(String model, Integer maxTokens, Double temperature, Boolean stream, List<GPTMessageDTO> messages) {
        this.model = model;
        this.maxTokens = maxTokens;
        this.temperature = temperature;
        this.stream = stream;
        this.messages = messages;
    }
}
