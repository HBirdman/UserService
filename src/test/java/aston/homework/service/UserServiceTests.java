package aston.homework.service;

import aston.homework.dao.UserDAO;
import aston.homework.dto.UserCreateDTO;
import aston.homework.dto.UserDTO;
import aston.homework.mapper.UserMapper;
import aston.homework.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserDAO userDAO;

    @Mock
    private UserMapper mapper;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userDAO, mapper);
    }

    @Test
    void testCreateSuccess() {
        UserCreateDTO createDTO = new UserCreateDTO();
        createDTO.setName("Тестовый User");
        createDTO.setEmail("test@example.com");
        createDTO.setAge(25);

        User mappedUser = new User();
        mappedUser.setName("Тестовый User");
        mappedUser.setEmail("test@example.com");
        mappedUser.setAge(25);

        when(mapper.map(createDTO)).thenReturn(mappedUser);

        userService.create(createDTO);

        verify(mapper, times(1)).map(createDTO);
        verify(userDAO, times(1)).createUser(mappedUser);
        verifyNoMoreInteractions(userDAO, mapper);
    }

    @Test
    void testCreateFail() {
        UserCreateDTO createDTO = new UserCreateDTO();
        when(mapper.map(createDTO)).thenReturn(null);

        userService.create(createDTO);

        verify(mapper).map(createDTO);
        verify(userDAO).createUser(null);
    }

    @Test
    void testShowAllSuccess() {
        User user1 = new User("User1", "u1@mail.com", 20, null);
        user1.setId(1L);
        User user2 = new User("User2", "u2@mail.com", 30, null);
        user2.setId(2L);
        List<User> users = Arrays.asList(user1, user2);

        when(userDAO.getAllUsers()).thenReturn(users);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        userService.showAll();

        verify(userDAO, times(1)).getAllUsers();

        String output = outputStream.toString();
        assertTrue(output.contains(user1.toString()));
        assertTrue(output.contains(user2.toString()));
        assertFalse(output.contains("Пользователи не найдены"));

        System.setOut(System.out);
    }

    @Test
    void testShowAllFail() {
        when(userDAO.getAllUsers()).thenReturn(Collections.emptyList());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        userService.showAll();

        verify(userDAO).getAllUsers();
        assertTrue(outputStream.toString().contains("Пользователи не найдены"));

        System.setOut(System.out);
    }

    @Test
    void testShowSuccess() {
        // 1. Подготовка
        Long userId = 100L;
        User user = new User("Test", "test@mail.com", 25, null);
        user.setId(userId);

        when(userDAO.getUserById(userId)).thenReturn(user);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        userService.show(userId);

        verify(userDAO).getUserById(userId);
        assertTrue(outputStream.toString().contains("Найден пользователь: " + user));

        System.setOut(System.out);
    }

    @Test
    void testShowFail() {
        Long userId = 999L;
        when(userDAO.getUserById(userId)).thenReturn(null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        userService.show(userId);

        verify(userDAO).getUserById(userId);
        assertTrue(outputStream.toString().contains("Пользователь с этим ID не найден: " + userId));

        System.setOut(System.out);
    }

    @Test
    void testGetSuccess() {
        Long userId = 1L;
        User user = new User("Test", "test@mail.com", 25, null);
        user.setId(userId);
        UserDTO expectedDto = new UserDTO(userId, "Test", "test@mail.com", 25, null);

        when(userDAO.getUserById(userId)).thenReturn(user);
        when(mapper.map(user)).thenReturn(expectedDto);

        UserDTO result = userService.get(userId);

        assertNotNull(result);
        assertEquals(expectedDto, result);

        verify(userDAO).getUserById(userId);
        verify(mapper).map(user);
        verifyNoMoreInteractions(userDAO, mapper);
    }

    @Test
    void testGetFail() {
        Long userId = 999L;
        when(userDAO.getUserById(userId)).thenReturn(null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        UserDTO result = userService.get(userId);

        assertNull(result);
        assertTrue(outputStream.toString().contains("Пользователь с этим ID не найден: " + userId));

        verify(userDAO).getUserById(userId);
        verify(mapper, never()).map(any(User.class)); // Маппер не должен вызываться

        System.setOut(System.out);
    }

    @Test
    void testUpdateSuccess() {
        // 1. Подготовка
        UserDTO updateDto = new UserDTO(1L, "Updated", "up@mail.com", 30, null);
        User mappedUser = new User("Updated", "up@mail.com", 30, null);
        mappedUser.setId(1L);

        when(mapper.map(updateDto)).thenReturn(mappedUser);
        when(userDAO.updateUser(mappedUser)).thenReturn(true);

        boolean result = userService.update(updateDto);

        assertTrue(result);

        verify(mapper).map(updateDto);
        verify(userDAO).updateUser(mappedUser);
        verifyNoMoreInteractions(userDAO, mapper);
    }

    @Test
    void testUpdateFail() {
        UserDTO updateDto = new UserDTO(1L, "Updated", "up@mail.com", 30, null);
        User mappedUser = new User("Updated", "up@mail.com", 30, null);
        mappedUser.setId(1L);

        when(mapper.map(updateDto)).thenReturn(mappedUser);
        when(userDAO.updateUser(mappedUser)).thenReturn(false);

        boolean result = userService.update(updateDto);

        assertFalse(result);

        verify(mapper).map(updateDto);
        verify(userDAO).updateUser(mappedUser);
    }


    @Test
    void testDeleteSuccess() {
        Long userId = 5L;
        when(userDAO.deleteUserById(userId)).thenReturn(true);

        boolean result = userService.delete(userId);

        assertTrue(result);
        verify(userDAO).deleteUserById(userId);
    }

    @Test
    void testDeleteFail() {
        Long userId = 999L;
        when(userDAO.deleteUserById(userId)).thenReturn(false);

        boolean result = userService.delete(userId);

        assertFalse(result);
        verify(userDAO).deleteUserById(userId);
    }
}