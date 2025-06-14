package bitnagil.bitnagil_backend.auth.jwt.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import bitnagil.bitnagil_backend.auth.jwt.entity.RefreshToken;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
