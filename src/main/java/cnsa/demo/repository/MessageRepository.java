package cnsa.demo.repository;

import cnsa.demo.domain.Message;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @PersistenceContext
    private EntityManager entityManager;
    public List<Message> findLimitedEntities(int limit) {
        TypedQuery<Message> query = entityManager.createQuery()
    }
}
