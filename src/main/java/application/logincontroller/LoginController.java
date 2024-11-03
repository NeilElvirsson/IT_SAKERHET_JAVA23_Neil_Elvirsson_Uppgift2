package application.logincontroller;

import application.authorization.Authorization;
import application.responses.TokenResponse;
import domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Autowired
    public Authorization authorization;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody Login login) {

        SqliteUserRepository sqliteUserRepository = new SqliteUserRepository();
       // Authorization authorization = new Authorization();

        String hashedPassword = "";

        try {
            hashedPassword = PasswordHasher.hashPassword(login.getPassword());

        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
        User user = sqliteUserRepository.getUser(login.getUserName(), hashedPassword);

        if(user != null) {

            TokenResponse tokenResponse = new TokenResponse(authorization.createToken(user.getUserName()));

            return new ResponseEntity<>(tokenResponse, HttpStatus.OK);

        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

}
