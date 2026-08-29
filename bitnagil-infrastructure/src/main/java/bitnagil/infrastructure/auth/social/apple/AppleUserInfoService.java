package bitnagil.infrastructure.auth.social.apple;

import bitnagil.errorcode.ErrorCode;
import bitnagil.exception.CustomException;
import bitnagil.infrastructure.auth.social.apple.dto.AppleIdTokenPayload;
import bitnagil.infrastructure.auth.social.apple.dto.AppleSocialTokenInfoResponse;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.PrivateKey;
import java.security.Security;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppleUserInfoService {
    private static final String REFRESH_TOKEN_HINT = "refresh_token";

    private final AppleAuthClient appleAuthClient;
    private final AppleProperties appleProperties;

    public AppleIdTokenPayload get(String authorizationCode) {
        AppleSocialTokenInfoResponse appleSocialResponse = appleAuthClient.getIdToken(
            appleProperties.getClientId(),
            generateClientSecret(),
            appleProperties.getGrantType(),
            authorizationCode
        );

        String idToken = appleSocialResponse.getIdToken();
        AppleIdTokenPayload payload = TokenDecoder.decodePayload(idToken, AppleIdTokenPayload.class);
        payload.setRefreshToken(appleSocialResponse.getRefreshToken());
        return payload;
    }

    public void unlink(String refreshToken) {
        appleAuthClient.revoke(
            appleProperties.getClientId(),
            generateClientSecret(),
            refreshToken,
            REFRESH_TOKEN_HINT
        );
    }

    private String generateClientSecret() {
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(5);

        return Jwts.builder()
            .setHeaderParam(JwsHeader.KEY_ID, appleProperties.getKeyId())
            .setIssuer(appleProperties.getTeamId())
            .setAudience(appleProperties.getAudience())
            .setSubject(appleProperties.getClientId())
            .setExpiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
            .setIssuedAt(new Date())
            .signWith(getPrivateKey(), SignatureAlgorithm.ES256)
            .compact();
    }

    private PrivateKey getPrivateKey() {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(appleProperties.getPrivateKey());
            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(privateKeyBytes);
            return converter.getPrivateKey(privateKeyInfo);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.APPLE_PRIVATE_KEY_CONVERT_ERROR);
        }
    }
}
