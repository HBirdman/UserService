package aston.homework.mapper;

import aston.homework.dto.UserCreateDTO;
import aston.homework.dto.UserDTO;
import aston.homework.model.User;

public class UserMapper {

    public User map(UserCreateDTO dto) {
        return new User(dto.getName(), dto.getEmail(), dto.getAge(), dto.getCreatedAt());
    }

    public UserDTO map(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getAge(), user.getCreatedAt());
    }

    public User map(UserDTO dto) {
        User user = new User(dto.getName(), dto.getEmail(), dto.getAge(), dto.getCreatedAt());
        user.setId(dto.getId());
        return user;
    }
}
