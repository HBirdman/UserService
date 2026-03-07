package aston.homework.service;

import aston.homework.dto.UserCreateDTO;
import aston.homework.dto.UserDTO;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Scanner;

@AllArgsConstructor
public class InputService {

    private final UserService userService;

    public void createUser(Scanner scanner) {
        System.out.println("\nСоздание нового пользователя");
        System.out.print("Введите имя: ");
        String name = scanner.nextLine();

        System.out.print("Введите почту: ");
        String email = scanner.nextLine();

        System.out.print("Введите возраст: ");
        int age = scanner.nextInt();

        LocalDateTime createdAt = LocalDateTime.now();

        UserCreateDTO userDTO = new UserCreateDTO(name, email, age, createdAt);
        userService.create(userDTO);
    }

    public void showAllUsers() {
        System.out.println("\nВывожу список всех пользователей");
        userService.showAll();
    }

    public void findUserById(Scanner scanner) {
        System.out.println("\nПоиск пользователя по ID");
        System.out.print("Введите пользователя ID: ");
        Long userId = scanner.nextLong();
        scanner.nextLine();

        userService.show(userId);
    }

    public void updateUser(Scanner scanner) {
        System.out.println("\nОбновление данных о пользователе");
        System.out.print("Введите ID пользователя: ");
        Long userId = scanner.nextLong();
        scanner.nextLine();

        UserDTO userDTO = userService.get(userId);
        if (userDTO == null) {
            System.out.println("Пользователь с ID '" + userId + "' не найден.");
            return;
        }

        System.out.println("Данные текущего пользователя: " + userDTO);

        System.out.print("Введите новое имя (или нажмите Enter, чтобы оставить прежнее): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            userDTO.setName(name);
        }

        System.out.print("Введите новую почту (или нажмите Enter, чтобы оставить прежнюю): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) {
            userDTO.setEmail(email);
        }

        System.out.print("Введите новый возраст (или введите 0, чтобы оставить прежний): ");
        int age = scanner.nextInt();
        if (age != 0) {
            userDTO.setAge(age);
        }
        scanner.nextLine();

        boolean success = userService.update(userDTO);
        if (success) {
            System.out.println("Пользователь успешно обновлен.");
        } else {
            System.out.println("Не получилось обновить пользователя.");
        }
    }

    public void deleteUser(Scanner scanner) {
        System.out.println("\nУдаление данных пользователя");
        System.out.print("Введите ID пользователя: ");
        Long userId = scanner.nextLong();
        scanner.nextLine();

        boolean success = userService.delete(userId);
        if (success) {
            System.out.println("Пользователь успешно удален.");
        } else {
            System.out.println("Не удалось удалить пользователя.");
        }
    }
}
