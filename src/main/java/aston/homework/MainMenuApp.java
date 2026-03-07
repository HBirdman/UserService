package aston.homework;

import aston.homework.dao.UserDAO;
import aston.homework.mapper.UserMapper;
import aston.homework.service.InputService;
import aston.homework.service.UserService;
import aston.homework.util.HibernateUtil;
import lombok.AllArgsConstructor;

import java.util.Scanner;

@AllArgsConstructor
public class MainMenuApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static final InputService inputService = new InputService
            (new UserService(new UserDAO(), new UserMapper()));

    public static void start() {
        try {
            boolean exit = false;

            while (!exit) {
                System.out.println("""
                        
                        Вас приветствует учебный сервис для хранения информации о пользователях.
                        Выберите желаемое действие:
                        1. Создать нового пользователя
                        2. Обновить данные о пользователе
                        3. Найти пользователя по ID
                        4. Вывести список всех пользователей
                        5. Удалить пользователя
                        0. Выйти""");
                System.out.print("Введите ваш выбор: ");


                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        inputService.createUser(scanner);
                        break;
                    case 2:
                        inputService.updateUser(scanner);
                        break;
                    case 3:
                        inputService.findUserById(scanner);
                        break;
                    case 4:
                        inputService.showAllUsers();
                        break;
                    case 5:
                        inputService.deleteUser(scanner);
                        break;
                    case 0:
                        exit = true;
                        break;
                    default:
                        System.out.println("Ошибка при вводе, попробуйте еще раз.");
                }
            }
        } finally {
            scanner.close();
            HibernateUtil.shutdown();
        }
    }
}
