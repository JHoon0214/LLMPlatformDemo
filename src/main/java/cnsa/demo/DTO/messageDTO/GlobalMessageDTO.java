package cnsa.demo.DTO.messageDTO;

import cnsa.demo.domain.Message;
import cnsa.demo.domain.Workspace;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GlobalMessageDTO {
    private Long messageId;
    private String content;
    private String keyContent;
    private String role;
    private Workspace workspace;
    private LocalDateTime createdAt;

    @Builder
    public GlobalMessageDTO(Long messageId, String content, String keyContent, String role, Workspace workspace, LocalDateTime createdAt) {
        this.messageId = messageId;
        this.content = content;
        this.keyContent = keyContent;
        this.role = role;
        this.workspace = workspace;
        this.createdAt = createdAt;
    }

    public GlobalMessageDTO(Message message) {
        this(message.getId(), message.getContent(), message.getKeyContent(), message.getRole(), message.getWorkspace(), message.getCreatedAt());
    }

    public Message convertToMessage() {
        Message message = new Message();
        message.setId(messageId);
        message.setContent(content);
        message.setKeyContent(keyContent);
        message.setRole(role);
        message.setCreatedAt(createdAt);
        message.setWorkspace(workspace);
        return message;
    }
}
