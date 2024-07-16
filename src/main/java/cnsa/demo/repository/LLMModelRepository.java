package cnsa.demo.repository;

import cnsa.demo.domain.LLMModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LLMModelRepository extends JpaRepository<LLMModel, Long> {
    Optional<LLMModel> findByModelId(Long modelId);
}
