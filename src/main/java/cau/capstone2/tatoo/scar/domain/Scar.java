package cau.capstone2.tatoo.scar.domain;


import cau.capstone2.tatoo.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Scar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scar_id")
    private Long id;

    private String scar_url;

    private String scar_seg_url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Scar createScar(String scar_url, String scar_seg_url) {
        Scar scar = new Scar();
        scar.scar_url = scar_url;
        scar.scar_seg_url = scar_seg_url;
        return scar;
    }
}
