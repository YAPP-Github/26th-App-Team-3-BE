package bitnagil.bitnagil_backend.auth.oauth2.service;

import java.time.Duration;
import java.util.Optional;

import org.springframework.stereotype.Component;

import bitnagil.bitnagil_backend.auth.jwt.entity.RefreshToken;
import bitnagil.bitnagil_backend.auth.jwt.repository.RefreshTokenRedisRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisService {
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    // 🔸 저장
    public void saveRefreshToken(Long userId, String token) {
        RefreshToken refreshToken = RefreshToken.builder()
            .userId(String.valueOf(userId))
            .refreshToken(token)
            .build();
        refreshTokenRedisRepository.save(refreshToken);
    }

    // 🔸 조회 by userId
    public Optional<RefreshToken> getRefreshTokenByUserId(Long userId) {
        return refreshTokenRedisRepository.findById(String.valueOf(userId));
    }

    // 🔸 조회 by refreshToken
    public Optional<RefreshToken> getRefreshTokenByToken(String token) {
        return refreshTokenRedisRepository.findByRefreshToken(token);
    }

    // 🔸 삭제
    public void deleteRefreshToken(Long userId) {
        refreshTokenRedisRepository.deleteById(String.valueOf(userId));
    }
}
