package cnsa.demo.DTO.workspaceDTO;

import cnsa.demo.DTO.LLMModelDTO;
import cnsa.demo.DTO.User.UserDTO;
import cnsa.demo.domain.Workspace;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class WorkspaceDTO {
    private UUID id;
    private String workspaceName;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
    private LLMModelDTO llmModel;
    private UserDTO user;

    @Builder
    public WorkspaceDTO(UUID id, String workspaceName, LocalDateTime createdAt, LocalDateTime editedAt, LLMModelDTO llmModel, UserDTO user) {
        this.id=id;
        this.workspaceName=workspaceName;
        this.createdAt=createdAt;
        this.editedAt=editedAt;
        this.llmModel = llmModel;
        this.user=user;
    }

    public WorkspaceDTO(Workspace workspace) {
        this(workspace.getId(), workspace.getWorkspaceName(), workspace.getCreatedAt(), workspace.getEditedAt(), new LLMModelDTO(workspace.getLlmModel()), new UserDTO(workspace.getUser()));
    }
}
