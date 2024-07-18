package cnsa.demo.controller;

import cnsa.demo.DTO.LLMModelDTO;
import cnsa.demo.DTO.Security.SessionUser;
import cnsa.demo.DTO.workspaceDTO.WorkSpaceWithDateDTO;
import cnsa.demo.DTO.workspaceDTO.WorkspaceDTO;
import cnsa.demo.domain.Workspace;
import cnsa.demo.service.llm.LLMModelService;
import cnsa.demo.service.llm.LLMService;
import cnsa.demo.service.workspace.WorkspaceService;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/workspace")
public class workspaceController {
    private final WorkspaceService workspaceService;
    private final HttpSession httpSession;
    private final LLMModelService llmModelService;

//    @GetMapping("/infos")
//    public List<WorkspaceDTO> getWorkspaces() {
//        SessionUser user = (SessionUser) httpSession.getAttribute("user");
//        return workspaceService.getWorkspaces(user.getEmail());
//    }

    @GetMapping("/infos")
    public String getWorkspaces(Model model) {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        List<WorkSpaceWithDateDTO> workspaces = workspaceService.getWorkspaces(user.getEmail());
        model.addAttribute("workspaces", workspaces);
        model.addAttribute("models", llmModelService.getAllLLMInfo());
        model.addAttribute("user", user);
        return "workspace";
    }

    @GetMapping("/refreshedWorkspace")
    public String refreshWorkspace(Model model) {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        List<WorkSpaceWithDateDTO> workSpaceWithDateDTOS = workspaceService.getWorkspaces(user.getEmail());
        model.addAttribute("workspaces", workSpaceWithDateDTOS);
        return "workspace::sidebar-content";
    }

    @PostMapping("/creation")
    public ResponseEntity<UUID> createWorkspace(@RequestParam Long llmModelId) {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        return ResponseEntity.ok(workspaceService.createWorkspace(user.getEmail(), llmModelId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Void> getWorkspaceInfo(@PathVariable UUID id) {
        Workspace workspace = workspaceService.getWorkspace(id);
        System.out.println("model id: " + workspace.getLlmModel().getModelId());
        LLMModelDTO llmModelDTO = llmModelService.getLLMInfo(workspace.getLlmModel().getModelId());
        httpSession.setAttribute("workspace", workspace);
        httpSession.setAttribute("model", llmModelDTO);
        return ResponseEntity.ok(null);
    }
}
