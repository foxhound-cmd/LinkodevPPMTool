package io.linkodev.ppmtool.security;

import io.jsonwebtoken.*;
import io.linkodev.ppmtool.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.linkodev.ppmtool.security.SecurityConstants.EXPIRATION_TIME;
import static io.linkodev.ppmtool.security.SecurityConstants.SECRET;

@Component
public class JwtTokenProvider {

    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());

        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        String userId = Long.toString(user.getId());
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("username", user.getUsername());
        claims.put("fullName", user);

        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (SignatureException signatureException) {
            System.out.println("Invalid JWT Signature");
        } catch (MalformedJwtException malformedJwtException) {
            System.out.println("Invalid JWT Token");
        } catch (ExpiredJwtException expiredJwtException) {
            System.out.println("Expired JWT Token");
        } catch (UnsupportedJwtException unsupportedJwtException) {
            System.out.println("Unsupported JWT token");
        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("JWT claims string is empty");
        }
        return false;
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        String id = (String) claims.get("id");

        return Long.parseLong(id);
    }

}
