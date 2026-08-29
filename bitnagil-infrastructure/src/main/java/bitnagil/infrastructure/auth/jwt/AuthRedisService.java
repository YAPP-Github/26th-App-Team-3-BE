package bitnagil.infrastructure.auth.jwt;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthRedisService {
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final StringRedisTemplate stringRedisTemplate;

    public void saveRefreshToken(Long userId, String token) {
        RefreshToken refreshToken = RefreshToken.builder()
            .userId(String.valueOf(userId))
            .refreshToken(token)
            .build();

        refreshTokenRedisRepository.save(refreshToken);
    }

    public Optional<RefreshToken> getRefreshTokenByUserId(Long userId) {
        return refreshTokenRedisRepository.findById(String.valueOf(userId));
    }

    public Optional<RefreshToken> getRefreshTokenByToken(String token) {
        return refreshTokenRedisRepository.findByRefreshToken(token);
    }

    public void deleteRefreshToken(Long userId) {
        refreshTokenRedisRepository.deleteById(String.valueOf(userId));
    }

    public void addAccessTokenToBlacklist(String accessToken, long expirationMillis) {
        stringRedisTemplate.opsForValue()
            .set("blacklist:" + accessToken, "logout", expirationMillis, TimeUnit.MILLISECONDS);
    }

    public boolean isAccessTokenBlacklisted(String accessToken) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey("blacklist:" + accessToken));
    }
}
