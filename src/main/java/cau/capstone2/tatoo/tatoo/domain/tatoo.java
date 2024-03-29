package cau.capstone2.tatoo.tatoo.domain;

import cau.capstone2.tatoo.scar.domain.Scar;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class tatoo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tatoo_id")
    private Long id;

    private boolean isFinished = false;

    //AI 도안
    private String designUri;

    //피부 tryon
    private String outputUri;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scar_id")
    private Scar scar;
}
