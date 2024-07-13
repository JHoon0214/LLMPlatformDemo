package cnsa.demo.service.message;

import cnsa.demo.DTO.messageDTO.GlobalMessageDTO;
import cnsa.demo.DTO.workspaceDTO.WorkspaceDTO;
import cnsa.demo.domain.Message;
import cnsa.demo.domain.Workspace;
import cnsa.demo.repository.MessageRepository;
import cnsa.demo.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IMessageServiceImpl implements IMessageService {
    private final MessageRepository messageRepository;
    private final WorkspaceRepository workspaceRepository;

    @Override
    public void saveMessage(GlobalMessageDTO messageDTO) {
        Optional<Workspace> workspace = workspaceRepository.findById(messageDTO.getWorkspace().getId());
        if(workspace.isEmpty()) throw new RuntimeException("There is no workspace id + " + messageDTO.getMessageId());

        Workspace newWorkspace = workspace.get();
        newWorkspace.setEditedAt(LocalDateTime.now());
        workspaceRepository.save(newWorkspace);

        messageRepository.save(messageDTO.convertToMessage());
    }

    @Override
    public List<GlobalMessageDTO> getAllMessage(Workspace workspace) {
        Optional<List<Message>> messages = messageRepository.findAllByWorkspaceOrderByCreatedAt(workspace);
        if(messages.isEmpty()) return new ArrayList<GlobalMessageDTO>();

        List<GlobalMessageDTO> messageDTOs = new ArrayList<>();

        for (Message message : messages.get()) {
            messageDTOs.add(new GlobalMessageDTO(message));
        }

        return messageDTOs;
    }
}
