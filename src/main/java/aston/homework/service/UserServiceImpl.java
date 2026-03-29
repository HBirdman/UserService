package aston.homework.service;

import aston.homework.dto.UserRequestDTO;
import aston.homework.dto.UserResponseDTO;
import aston.homework.event.UserEvent;
import aston.homework.exception.UserNotFoundException;
import aston.homework.kafka.UserEventProducer;
import aston.homework.mapper.UserMapper;
import aston.homework.model.User;
import aston.homework.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final UserEventProducer eventProducer;

    @Override
    @Transactional
    public UserResponseDTO create(UserRequestDTO dto) {
        User user = mapper.map(dto);
        User savedUser = userRepository.save(user);
        log.info("Пользователь сохранен: {}", savedUser.getEmail());

        eventProducer.sendUserEvent(UserEvent.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .type(UserEvent.EventType.CREATED)
                .build());

        return mapper.map(savedUser);
    }

    @Override
    public List<UserResponseDTO> showAll() {
        return userRepository.findAll().stream().map(mapper::map).collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO show(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return mapper.map(user);
    }

    @Override
    @Transactional
    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());

        User updatedUser = userRepository.save(user);
        log.info("Пользователь с ID {} обновлен", id);

        return mapper.map(updatedUser);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        String email = user.getEmail();
        userRepository.deleteById(id);
        log.info("Пользователь с ID {} удален", id);

        eventProducer.sendUserEvent(UserEvent.builder()
                .userId(id)
                .email(email)
                .type(UserEvent.EventType.DELETED)
                .build());
    }
}
