package cnsa.demo.service.oldVer.workspace;

import cnsa.demo.DTO.workspaceDTO.WorkSpaceWithDateDTO;
import cnsa.demo.DTO.workspaceDTO.WorkspaceDTO;
import cnsa.demo.domain.LLMModel;
import cnsa.demo.domain.User;
import cnsa.demo.domain.Workspace;
import cnsa.demo.repository.LLMModelRepository;
import cnsa.demo.repository.UserRepository;
import cnsa.demo.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class WorkspaceService {
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final LLMModelRepository llmModelRepository;

    public UUID createWorkspace(String userEmail, Long llmId) {
        Optional<User> byEmail = userRepository.findByEmail(userEmail);
        if(byEmail.isEmpty()) throw new RuntimeException("Error on WorkspaceServiceClass-createWorkspace(). There is no user with email " + userEmail);

        Optional<LLMModel> byModelId = llmModelRepository.findByModelId(llmId);
        if(byModelId.isEmpty()) throw new RuntimeException("There is no model with id " + llmId);

        return workspaceRepository.save(Workspace.builder()
                .workspaceName("New Chat")
                .createdAt(LocalDateTime.now())
                .user(byEmail.get())
                .editedAt(LocalDateTime.now())
                .llmModel(byModelId.get())
                .build()
        ).getId();
    }

    public List<WorkSpaceWithDateDTO> getWorkspaces(String userEmail) {
        Optional<User> byEmail = userRepository.findByEmail(userEmail);

        if (byEmail.isEmpty()) {
            throw new RuntimeException("Error on WorkspaceServiceClass-getWorkspaces(). There is no user with email " + userEmail);
        }

        Optional<List<Workspace>> orderedWorkspaces = workspaceRepository.findAllByUserOrderByEditedAtDesc(byEmail.get());
        if (orderedWorkspaces.isEmpty()) {
            return new ArrayList<>();
        }

        LocalDateTime curr = null;
        List<WorkspaceDTO> workspaces = new ArrayList<>();

        List<WorkSpaceWithDateDTO> retList = new ArrayList<>();

        for (Workspace workspace : orderedWorkspaces.get()) {
            if (curr == null || !curr.toLocalDate().equals(workspace.getEditedAt().toLocalDate())) {
                if (curr != null) {
                    WorkSpaceWithDateDTO workSpaceWithDateDTO = WorkSpaceWithDateDTO.builder()
                            .localDate(curr.toLocalDate())
                            .workspaceDTOS(workspaces)
                            .build();
                    retList.add(workSpaceWithDateDTO);
                }
                curr = workspace.getEditedAt();
                workspaces = new ArrayList<>();
            }
            workspaces.add(new WorkspaceDTO(workspace));
        }

        // 마지막 작업공간 리스트 추가
        if (!workspaces.isEmpty()) {
            WorkSpaceWithDateDTO workSpaceWithDateDTO = WorkSpaceWithDateDTO.builder()
                    .localDate(curr.toLocalDate())
                    .workspaceDTOS(workspaces)
                    .build();
            retList.add(workSpaceWithDateDTO);
        }

        return retList;
    }

    public Workspace getWorkspace(UUID workspaceId) {
        Optional<Workspace> workspace = workspaceRepository.findById(workspaceId);
        if(workspace.isEmpty()) throw new RuntimeException("There is no workspace id with " + workspaceId);

        return workspace.get();
    }

    public UUID deleteWorkspace(UUID workspaceId) {
        Optional<Workspace> workspace = workspaceRepository.findById(workspaceId);
        if (workspace.isPresent()) {
            workspaceRepository.deleteById(workspaceId);
            return workspaceId;
        } else {
            return null;
        }
    }

    public void updateEditedTime(UUID workspaceId) {
        Optional<Workspace> workspace = workspaceRepository.findById(workspaceId);
        if(workspace.isEmpty()) throw new RuntimeException("Error on WorkspaceServiceClass-updateEditedTime(). There is no workspace with id " + workspace);

        Workspace currWorkspace = workspace.get();
        currWorkspace.setEditedAt(LocalDateTime.now());
        workspaceRepository.save(currWorkspace);
    }
}
