package application.logincontroller;

import application.responses.TokenResponse;
import domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import passwordhasher.PasswordHasher;
import sqliteuserrepository.SqliteUserRepository;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

@RestController
public class LoginController {
    @Value("${jwt.secretkey}")
    private String secretKey;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody Login login) {

        SqliteUserRepository sqliteUserRepository = new SqliteUserRepository();

        String hashedPassword = "";

        try {
            hashedPassword = PasswordHasher.hashPassword(login.getPassword());

        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
        User user = sqliteUserRepository.getUser(login.getUserName(), hashedPassword);

        if(user != null) {

            String issuer = "NeilToken";
            Date now = new Date();
            Date expiration = new Date(now.getTime() + 15*60*1000);


            String token = Jwts.builder()
                    .subject(user.getUserName())
                    .expiration(expiration)
                    .issuer(issuer)
                    .signWith(SignatureAlgorithm.HS256, secretKey)
                    .compact();



            TokenResponse tokenResponse = new TokenResponse(token);
            return new ResponseEntity<>(tokenResponse, HttpStatus.OK);

        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

}
