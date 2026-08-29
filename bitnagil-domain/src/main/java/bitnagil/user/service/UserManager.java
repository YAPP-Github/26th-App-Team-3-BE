package bitnagil.user.service;

import bitnagil.user.domain.User;
import bitnagil.user.repository.UserRepository;
import bitnagil.errorcode.ErrorCode;
import bitnagil.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 외부에서 사용되는 유저에 대한 공통 로직을 관리하는 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class UserManager {

    private final UserRepository userRepository;

    // User 엔티티를 영속 상태로 변경하여 user 정보를 업데이트를 하기 위한 메서드
    public User getPersistedUser(User user) {
        return userRepository.findByUserId(user.getUserId()).orElseThrow(
            () -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }
}