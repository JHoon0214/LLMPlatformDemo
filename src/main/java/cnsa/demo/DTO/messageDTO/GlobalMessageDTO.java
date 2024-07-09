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
    private String content;
    private String role;
    private Workspace workspace;
    private LocalDateTime createdAt;

    @Builder
    public GlobalMessageDTO(String content, String role, Workspace workspace, LocalDateTime createdAt) {
        this.content = content;
        this.role = role;
        this.workspace = workspace;
        this.createdAt = createdAt;
    }

    public GlobalMessageDTO(Message message) {
        this(message.getContent(), message.getRole(), message.getWorkspace(), message.getCreatedAt());
    }

    public Message convertToMessage() {
        Message message = new Message();
        message.setContent(content);
        message.setRole(role);
        message.setCreatedAt(createdAt);
        message.setWorkspace(workspace);
        return message;
    }
}
