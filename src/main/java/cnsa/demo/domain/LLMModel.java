package cnsa.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class LLMModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long modelId;

    @Column(nullable = false)
    private String modelName;

    @Column(nullable = false)
    private String modelDesc;

    @Column(nullable = false)
    private Integer maxToken;

    @Column(nullable = false)
    private Boolean streamable;

    @Column(nullable = false)
    private Double temperature;

    @Column(nullable = false)
    private String chatUrl;

    @Column(nullable = false)
    private String responseNodeAt;

    @Column(nullable = false)
    private String resultTokenAt;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private Integer price;
}
