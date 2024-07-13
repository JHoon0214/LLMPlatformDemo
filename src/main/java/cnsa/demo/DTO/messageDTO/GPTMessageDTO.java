package cnsa.demo.DTO.messageDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GPTMessageDTO implements IModelDTO{
    private String content;
    private String role;
    @Override
    public IModelDTO convertMessageToIModelMessage(GlobalMessageDTO messageDTO) {
        this.content=messageDTO.getKeyContent();
        this.role=messageDTO.getRole();

        return this;
    }
}
