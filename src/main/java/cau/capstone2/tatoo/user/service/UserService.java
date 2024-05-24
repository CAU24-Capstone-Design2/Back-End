package cau.capstone2.tatoo.user.service;

import cau.capstone2.tatoo.user.domain.User;
import cau.capstone2.tatoo.user.repository.UserRepository;
import cau.capstone2.tatoo.util.api.ResponseCode;
import cau.capstone2.tatoo.util.exception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    //유저 학습 여부 확인
    @Transactional(readOnly = true)
    public boolean checkTrained(Long userId) {
        User user = getUserById(userId);
        if(user.getScars().isEmpty()) {
            System.out.println("\n\n유저의 학습이력이 없습니다.\n\n");
            return false;
        }
        return true;
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
    }
}
