package cnsa.demo.service.message;

import cnsa.demo.DTO.messageDTO.GlobalMessageDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IMessageService {
    public void saveMessage(GlobalMessageDTO messageDTO);
    public List<GlobalMessageDTO> getAllMessage();
}
