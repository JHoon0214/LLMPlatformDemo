package cnsa.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String keyContent;

    @Column(nullable = false)
    private String role;

    @JoinColumn(name="workspace_id", nullable = false)
    @ManyToOne
    private Workspace workspace;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
