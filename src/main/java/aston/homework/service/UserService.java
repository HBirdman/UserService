package aston.homework.service;

import aston.homework.dto.UserCreateDTO;
import aston.homework.dto.UserDTO;

public interface UserService {
    void create(UserCreateDTO userDTO);
    void showAll();
    void show(Long id);
    UserDTO get(Long id);
    boolean update(UserDTO dto);
    boolean delete(Long id);
}
