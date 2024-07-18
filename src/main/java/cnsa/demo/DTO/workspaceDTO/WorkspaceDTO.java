package cnsa.demo.DTO.workspaceDTO;

import cnsa.demo.DTO.LLMModelDTO;
import cnsa.demo.DTO.Security.SessionUser;
import cnsa.demo.domain.LLMModel;
import cnsa.demo.domain.User;
import cnsa.demo.domain.Workspace;
import jakarta.persistence.*;
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
    private SessionUser user;

    @Builder
    public WorkspaceDTO(UUID id, String workspaceName, LocalDateTime createdAt, LocalDateTime editedAt, LLMModelDTO llmModel, SessionUser user) {
        this.id=id;
        this.workspaceName=workspaceName;
        this.createdAt=createdAt;
        this.editedAt=editedAt;
        this.llmModel = llmModel;
        this.user=user;
    }

    public WorkspaceDTO(Workspace workspace) {
        this(workspace.getId(), workspace.getWorkspaceName(), workspace.getCreatedAt(), workspace.getEditedAt(), new LLMModelDTO(workspace.getLlmModel()), new SessionUser(workspace.getUser()));
    }
}
