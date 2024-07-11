package cnsa.demo.repository;

import cnsa.demo.domain.Message;
import cnsa.demo.domain.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<List<Message>> findAllByWorkspaceOrderByCreatedAt(Workspace workspace);
}
