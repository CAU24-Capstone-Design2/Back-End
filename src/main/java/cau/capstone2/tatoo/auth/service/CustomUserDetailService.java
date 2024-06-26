package cau.capstone2.tatoo.auth.service;

import cau.capstone2.tatoo.user.repository.UserRepository;
import cau.capstone2.tatoo.util.api.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String kakaoId) throws UsernameNotFoundException {
        log.info("loadUserByUsername: {}", kakaoId);
        return userRepository.findById(Long.parseLong(kakaoId))
                .orElseThrow(() -> new UsernameNotFoundException(ResponseCode.USER_NOT_FOUND.getMessage()));
    }
}
