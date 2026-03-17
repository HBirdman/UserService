package aston.homework.service;

import aston.homework.dao.UserDAO;
import aston.homework.dto.UserCreateDTO;
import aston.homework.dto.UserDTO;
import aston.homework.mapper.UserMapper;
import aston.homework.model.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;
    private final UserMapper mapper;

    public void create(UserCreateDTO userDTO) {
        User user = mapper.map(userDTO);
        userDAO.createUser(user);
    }

    public void showAll() {
        List<User> users = userDAO.getAllUsers();

        if (users != null && !users.isEmpty()) {
            for (User user : users) {
                System.out.println(user);
            }
        } else {
            System.out.println("Пользователи не найдены.");
        }
    }

    public void show(Long id) {
        User user = userDAO.getUserById(id);
        if (user != null) {
            System.out.println("Найден пользователь: " + user);
        } else {
            System.out.println("Пользователь с этим ID не найден: " + id);
        }
    }

    public UserDTO get(Long id) {
        User user = userDAO.getUserById(id);
        if (user != null) {
            return mapper.map(user);
        }
        System.out.println("Пользователь с этим ID не найден: " + id);
        return null;
    }

    public boolean update(UserDTO dto) {
        User user = mapper.map(dto);
        return userDAO.updateUser(user);
    }

    public boolean delete(Long id) {
        return userDAO.deleteUserById(id);
    }
}
