package application.timecapsulecontroller;

import application.authorization.Authorization;
import application.encryption.AesEncryption;
import domain.TimeCapsule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sqlitetimecapsulerepository.SqliteTimeCapsuleRepository;

import java.util.List;
import java.util.UUID;

@RestController
public class TimeCapsuleController {

    @Autowired
    private Authorization authorization;

    @PostMapping("/timecapsules/{id}")
    public ResponseEntity<TimeCapsule> getTimeCapsule
            (@RequestHeader("Authorization") String token,
             @RequestBody GetTimeCapsule getTimeCapsule,
             @PathVariable("id") String id) {

        String userName = authorization.validateToken(token);

        if(userName == null) {
            return ResponseEntity.status(401).build();
        }

        SqliteTimeCapsuleRepository sqliteTimeCapsuleRepository = new SqliteTimeCapsuleRepository();

        TimeCapsule timeCapsule = sqliteTimeCapsuleRepository.getTimeCapsuleById(id, userName);


        if(timeCapsule == null) {
            return ResponseEntity.notFound().build();
        }

        String title = timeCapsule.getTitle();
        System.out.println("Timecapsule titel: " + timeCapsule.getTitle());
        String text = timeCapsule.getText();
        System.out.println("Timecapsule text: " + timeCapsule.getText());

        String decryptedTitle = AesEncryption.decrypt(getTimeCapsule.getPassword(), title);
        String decryptedText = AesEncryption.decrypt(getTimeCapsule.getPassword(), text);

        System.out.println("Password in byId method : " + getTimeCapsule.getPassword());
        timeCapsule.setTitle(decryptedTitle);
        timeCapsule.setText(decryptedText);
        System.out.println("Decrypterad titel och text: " + timeCapsule.getTitle() + timeCapsule.getText());

        return new ResponseEntity<TimeCapsule>(timeCapsule, HttpStatusCode.valueOf(200));

    }

    @PostMapping("/timecapsules/create")
    public ResponseEntity<TimeCapsule> createTimeCapsule (
            @RequestHeader("Authorization")String token,
            @RequestBody CreateTimeCapsule createTimeCapsule) {

        String userName = authorization.validateToken(token);

        if(userName == null) {
             return ResponseEntity.status(401).build();
        }
        SqliteTimeCapsuleRepository sqliteTimeCapsuleRepository = new SqliteTimeCapsuleRepository();
        TimeCapsule timeCapsule = new TimeCapsule();
        UUID uuid = UUID.randomUUID();

        timeCapsule.setId(uuid.toString());
        System.out.println(uuid);
        String encryptedText = AesEncryption.encrypt(createTimeCapsule.getPassword(), createTimeCapsule.getText());
        String encryptedTitle = AesEncryption.encrypt(createTimeCapsule.getPassword(),createTimeCapsule.getTitle());
        timeCapsule.setTitle(encryptedTitle);
        timeCapsule.setText(encryptedText);
        timeCapsule.setUserName(userName);

        boolean added =sqliteTimeCapsuleRepository.addTimeCapsule(timeCapsule);

        if(added) {
            return new ResponseEntity<TimeCapsule>(timeCapsule, HttpStatusCode.valueOf(200));
        }
        return ResponseEntity.internalServerError().build();
    }
}
