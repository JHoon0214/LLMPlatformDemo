package cnsa.demo.repository;

import cnsa.demo.domain.User;
import cnsa.demo.domain.Workspace;
import io.micrometer.common.lang.NonNullApi;
import jakarta.annotation.Nullable;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    Optional<List<Workspace>> findAllByUserOrderByEditedAt(User user);
    Optional<Workspace> findById(Long workspaceID);
}
