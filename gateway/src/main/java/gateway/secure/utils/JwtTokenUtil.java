package gateway.secure.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Map;


@Slf4j
public class JwtTokenUtil {
    private static Key hmacKey = null;

    static {
        try {

            String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";

            hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
                    SignatureAlgorithm.HS256.getJcaName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String generateToken(Map<String, Object> claims, String subject, int expiration) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;
        // 生成签名密钥
        final Date createdDate = Date.from(Instant.now());
        final Date expirationDate = calculateExpirationDate(createdDate, expiration);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(hmacKey)
                .compact();
    }

    private static Date calculateExpirationDate(Date createdDate, int expiration) {
        return new Date(createdDate.getTime() + expiration);
    }

    public static Claims parseJwtRsa256(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(jwt).getBody();
    }
}

