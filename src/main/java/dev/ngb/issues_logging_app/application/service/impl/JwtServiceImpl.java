package dev.ngb.issues_logging_app.application.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import dev.ngb.issues_logging_app.application.service.JwtService;
import dev.ngb.issues_logging_app.common.util.StringUtils;
import dev.ngb.issues_logging_app.infrastructure.cache.redis.RedisCacheService;
import dev.ngb.issues_logging_app.infrastructure.property.JwtProperty;
import dev.ngb.issues_logging_app.infrastructure.security.AuthConstant;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class JwtServiceImpl implements JwtService {

    private final JwtProperty property;

    public JwtServiceImpl(JwtProperty property, RedisCacheService redisCacheService) {
        this.property = property;
    }

    public String generateAccessToken(UserDetails user) {
        long currentTime = System.currentTimeMillis();

        JWTClaimsSet jwtClaims = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issueTime(new Date(currentTime))
                .expirationTime(new Date(currentTime + property.accessTokenExpirationDuration()))
                .claim(AuthConstant.ROLE_CLAIMS_NAME, getRolesClaims(user))
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(getSignerAlgorithm()), jwtClaims);
        try {
            signedJWT.sign(getSignerKey());
        } catch (JOSEException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return signedJWT.serialize();
    }

    @Override
    public Long getAccessTokenExpiration() {
        return property.accessTokenExpirationDuration();
    }

    private JWSSigner getSignerKey() {
        try {
            PrivateKey privateKey = loadPrivateKey(property.secretKey());
            return new RSASSASigner(privateKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    private PrivateKey loadPrivateKey(String privateKeyPem) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (StringUtils.isBlank(privateKeyPem)) {
            throw new IllegalArgumentException("Private key is empty");
        }
        String privateKeyContent = privateKeyPem
                .replaceAll("\\n", "")
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("[^A-Za-z0-9+/=]", "")
                .trim();
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyContent);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    private JWSAlgorithm getSignerAlgorithm() {
        return JWSAlgorithm.RS256;
    }

    private List<String> getPermissionsClaims(UserDetails user) {
        return user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> !authority.startsWith(AuthConstant.ROLE_PREFIX))
                .toList();
    }

    private List<String> getRolesClaims(UserDetails user) {
        return user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith(AuthConstant.ROLE_PREFIX))
                .map(authority -> authority.replace(AuthConstant.ROLE_PREFIX, ""))
                .toList();
    }
}
