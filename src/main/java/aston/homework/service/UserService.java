package aston.homework.service;

import aston.homework.dto.UserRequestDTO;
import aston.homework.dto.UserResponseDTO;

import java.util.List;

public interface UserService {
    UserResponseDTO create(UserRequestDTO userDTO);

    List<UserResponseDTO> showAll();

    UserResponseDTO show(Long id);

    UserResponseDTO update(Long id, UserRequestDTO dto);

    void delete(Long id);
}
