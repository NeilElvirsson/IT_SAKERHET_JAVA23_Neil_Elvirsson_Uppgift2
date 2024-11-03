package application.repositories;

import domain.TimeCapsule;

import java.sql.Time;
import java.util.List;

public interface TimeCapsuleRepository {

     TimeCapsule getTimeCapsuleById(String id, String userName);
     boolean addTimeCapsule(TimeCapsule timeCapsule);
}
