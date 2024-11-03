package application.repositories;

import domain.User;

public interface UserRepository {

    User getUser(String username, String password);
    User getUser(String username);

    boolean addUser(User user);



}
