package cau.capstone2.tatoo.scar.domain;


import cau.capstone2.tatoo.tattoo.domain.Tattoo;
import cau.capstone2.tatoo.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Scar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scar_id")
    private Long id;

    private String scarUri; //scar image filePath

    private String scarSegUri; //scar segmentation image filePath

    private String scarImage; //s3 - scarImage

    private String scarSegImage; //s3 - scarSegImage

    private String scarKeyWord;

    private String scarDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "scar", cascade = CascadeType.REMOVE)
    private List<Tattoo> tatooList = new ArrayList<>();

    public static Scar createScar(String scarDescription, String scarKeyWord) {
        Scar scar = new Scar();
        scar.scarDescription = scarDescription;
        scar.scarKeyWord = scarKeyWord;
        return scar;
    }
}
