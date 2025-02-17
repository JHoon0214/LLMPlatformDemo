package cnsa.demo.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.GenericGenerators;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
public class Workspace {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name="workspace_id", nullable = false)
    private UUID id;

    @Column(name="workspace_name", nullable = false)
    private String workspaceName;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Setter
    @Column(nullable = false)
    private LocalDateTime editedAt;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name="llm_model", nullable = false)
    private LLMModel llmModel;

    @Builder
    public Workspace(String workspaceName, LocalDateTime createdAt, LocalDateTime editedAt, User user, LLMModel llmModel) {
        this.workspaceName=workspaceName;
        this.createdAt=createdAt;
        this.editedAt=editedAt;
        this.user=user;
        this.llmModel=llmModel;
    }

}
