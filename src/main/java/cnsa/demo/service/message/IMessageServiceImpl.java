package cnsa.demo.service.message;

import cnsa.demo.DTO.messageDTO.GlobalMessageDTO;
import cnsa.demo.entity.Message;
import cnsa.demo.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IMessageServiceImpl implements IMessageService {
    private final MessageRepository messageRepository;

    public IMessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void saveMessage(GlobalMessageDTO messageDTO) {
        messageRepository.save(messageDTO.convertToMessage());
    }

    @Override
    public List<GlobalMessageDTO> getAllMessage() {
        List<Message> messages = messageRepository.findAll();
        List<GlobalMessageDTO> messageDTOs = new ArrayList<>();

        for (Message message : messages) {
            messageDTOs.add(new GlobalMessageDTO(message));
        }

        return messageDTOs;
    }
}
