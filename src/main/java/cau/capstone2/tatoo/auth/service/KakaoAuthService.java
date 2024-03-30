package cau.capstone2.tatoo.auth.service;

import cau.capstone2.tatoo.auth.component.KakaoUserInfo;
import cau.capstone2.tatoo.auth.dto.KakaoUserInfoResponse;
import cau.capstone2.tatoo.user.domain.User;
import cau.capstone2.tatoo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {
    private final KakaoUserInfo kakaoUserInfo;
    private final UserRepository userRepository;

    @Transactional
    public Long userLogin(String token) {
        KakaoUserInfoResponse userInfo = kakaoUserInfo.getUserInfo(token);
        Optional<User> user = userRepository.findByKakaoId(userInfo.getId());
        if(user.isPresent()) {
            return user.get().getId();
        }
        else {
            User newUser = User.createUser(userInfo.getKakao_account().getProfile().getNickname(), userInfo.getId());
            return userRepository.save(newUser).getId();
        }
    }
}
