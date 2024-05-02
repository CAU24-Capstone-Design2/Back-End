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

    private String scarUri; //scar image filePath

    private String scarSegUri; //scar segmentation image filePath

    //AI 도안
    private String designUri; //tattoo design image filePath

    private String scarImage; //s3 - scarImage

    private String scarSegImage; //s3 - scarSegImage

    private String tattooImage; //s3 - tattoo image

    private String scarKeyWord;

    private String scarDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Scar createScar(String scarDescription, String scarKeyWord) {
        Scar scar = new Scar();
        scar.scarDescription = scarDescription;
        scar.scarKeyWord = scarKeyWord;
        return scar;
    }

    public void setScarImage(String scarImage) {
        this.scarImage = scarImage;
    }

    public void setScarUri(String scarUri) {
        this.scarUri = scarUri;
    }

    public void setScarSegImage(String scarSegImage) {
        this.scarSegImage = scarSegImage;
    }

    public void setTattooImage(String tattooImage) {
        this.tattooImage = tattooImage;
    }

}
