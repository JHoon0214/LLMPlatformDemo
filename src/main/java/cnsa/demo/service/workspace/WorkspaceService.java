package cnsa.demo.service.workspace;

import cnsa.demo.DTO.workspaceDTO.WorkspaceDTO;
import cnsa.demo.domain.User;
import cnsa.demo.domain.Workspace;
import cnsa.demo.repository.UserRepository;
import cnsa.demo.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkspaceService {
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;

    public Long createWorkspace(String userEmail) {
        Optional<User> byEmail = userRepository.findByEmail(userEmail);
        if(byEmail.isEmpty()) throw new RuntimeException("Error on WorkspaceServiceClass-createWorkspace(). There is no user with email " + userEmail);
        return workspaceRepository.save(Workspace.builder()
                .workspaceName("New Chat")
                .createdAt(LocalDateTime.now())
                .user(byEmail.get())
                .editedAt(LocalDateTime.now())

                .build()
        ).getId();
    }

    public List<WorkspaceDTO> getWorkspaces(String userEmail) {
        Optional<User> byEmail = userRepository.findByEmail(userEmail);
        if(byEmail.isEmpty()) throw new RuntimeException("Error on WorkspaceServiceClass-getWorkspaces(). There is no user with email " + userEmail);

        Optional<List<Workspace>> orderedWorkspaces = workspaceRepository.findAllByUserOrderByEditedAt(byEmail.get());
        if(orderedWorkspaces.isEmpty()) return new ArrayList<WorkspaceDTO>();

        List<WorkspaceDTO> workspaces = new ArrayList<WorkspaceDTO>();
        for(Workspace workspace:orderedWorkspaces.get()) {
            workspaces.add(new WorkspaceDTO(workspace));
        }
        return workspaces;
    }

    public Workspace getWorkspace(Long workspaceId) {
        Optional<Workspace> workspace = workspaceRepository.findById(workspaceId);
        if(workspace.isEmpty()) throw new RuntimeException("There is no workspace id with " + workspaceId);

        return workspace.get();
    }

    public Long deleteWorkspace(Long workspaceId) {
        Optional<Workspace> workspace = workspaceRepository.findById(workspaceId);
        if (workspace.isPresent()) {
            workspaceRepository.deleteById(workspaceId);
            return workspaceId;
        } else {
            return null;
        }
    }

    public void updateEditedTime(Long workspaceId) {
        Optional<Workspace> workspace = workspaceRepository.findById(workspaceId);
        if(workspace.isEmpty()) throw new RuntimeException("Error on WorkspaceServiceClass-updateEditedTime(). There is no workspace with id " + workspace);

        Workspace currWorkspace = workspace.get();
        currWorkspace.setEditedAt(LocalDateTime.now());
        workspaceRepository.save(currWorkspace);
    }
}
