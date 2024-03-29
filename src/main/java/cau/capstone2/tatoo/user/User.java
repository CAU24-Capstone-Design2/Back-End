package cau.capstone2.tatoo.user;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private boolean isUsed = false;

    private String nickname;

    public static User createUser(String nickname) {
        User user = new User();
        user.nickname = nickname;
        return user;
    }

    public void updateUserState() {
        this.isUsed = true;
    }
}
