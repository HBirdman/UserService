package aston.homework.dao;

import aston.homework.model.User;

import java.util.List;

public interface UserDAO {
    User createUser(User user);
    User getUserById(Long id);
    List<User> getAllUsers();
    boolean updateUser(User user);
    boolean deleteUserById(Long id);

}
