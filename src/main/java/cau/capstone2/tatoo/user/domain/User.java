package cau.capstone2.tatoo.user.domain;

import cau.capstone2.tatoo.scar.domain.Scar;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@Getter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private Long kakaoId; //로그인 식별키

    private boolean isUsed = false;

    private String nickname;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Scar> scars = new ArrayList<>();

    public static User createUser(String nickname, Long kakaoId) {
        User user = new User();
        user.nickname = nickname;
        user.kakaoId = kakaoId;
        return user;
    }

    //Jwt 설정을 위한 UserDetails 메소드
    @ElementCollection(fetch = FetchType.EAGER) //roles 컬렉션
    private List<String> roles = new ArrayList<>();
    @Override   //사용자의 권한 목록 리턴
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return kakaoId.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    //Jwt 설정 종료
}
