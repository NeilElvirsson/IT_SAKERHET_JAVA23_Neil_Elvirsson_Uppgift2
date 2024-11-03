package application.authorization;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component("authorization")
@PropertySource("classpath:application.properties")
public class Authorization {

    @Value("${jwt.secretkey}")
    private String secretKey;
    public String createToken(String subject) {



        String issuer = "NeilToken";
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 15*60*1000);


        String token = Jwts.builder()
                .subject(subject)
                .expiration(expiration)
                .issuer(issuer)
                .signWith(getSecretKey())
                .compact();

        return token;
    }
    public String validateToken(String token) {

        try {
            JwtParser verifiedToken = Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build();

            return verifiedToken.parseSignedClaims(token).getPayload().getSubject();

        } catch (JwtException e) {
            System.out.println("validateToken JWT Exception error" + e.getMessage());
            return null;

        } catch (IllegalArgumentException e) {
            System.out.println("validateToken illegal argument" +e.getMessage());
            return null;

        }
    }
    private SecretKey getSecretKey() {
        try {
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        } catch (WeakKeyException e) {
            System.out.println(e.getMessage());
        } catch (DecodingException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

}
