package application.usercontroller;

import application.authorization.Authorization;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import passwordhasher.PasswordHasher;
import sqliteuserrepository.SqliteUserRepository;

import java.net.http.HttpResponse;
import java.security.NoSuchAlgorithmException;

@RestController
public class UserController {

    @Autowired
    private Authorization authorization;
    @PostMapping("/users/create")
    public ResponseEntity<Void> createUser(@RequestBody CreateUser createUser) {

        String username = createUser.getUserName();
        String password = createUser.getPassword();

        if(username.isEmpty() || username.length() > 15) {

            return ResponseEntity.badRequest().build();

        }
        if(password.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            String hashedPassword = PasswordHasher.hashPassword(password);
            SqliteUserRepository sqliteUserRepository = new SqliteUserRepository();
            User user = new User(username, hashedPassword);

            User foundUser = sqliteUserRepository.getUser(username);

            if(foundUser == null) {
                boolean successfullyAddedUser = sqliteUserRepository.addUser(user);

                if (!successfullyAddedUser) {
                    System.out.println("Could not add user to database");

                    return ResponseEntity.internalServerError().build();
                }

            } else {

                return ResponseEntity.badRequest().build();
            }

        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());

            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/me")
    public ResponseEntity<Me> me (@RequestHeader("Authorization") String token) {

        String userName = authorization.validateToken(token);

        if(userName == null) {
            return ResponseEntity.status(401).build();
        }

        Me me = new Me();
        me.setUserName(userName);
        return  new ResponseEntity<Me>(me, HttpStatusCode.valueOf(200));

    }

}
