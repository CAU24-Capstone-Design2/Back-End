package cau.capstone2.tatoo.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class User {
    @Id
    private Long id;
}
