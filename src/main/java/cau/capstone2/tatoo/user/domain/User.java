package cau.capstone2.tatoo.user.domain;

import cau.capstone2.tatoo.scar.domain.Scar;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private boolean isUsed = false;

    private String nickname;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Scar> scars = new ArrayList<>();

    public static User createUser(String nickname) {
        User user = new User();
        user.nickname = nickname;
        return user;
    }

    public void updateUserState() {
        this.isUsed = true;
    }
}
