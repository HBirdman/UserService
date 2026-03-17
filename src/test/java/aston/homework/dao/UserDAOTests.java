package aston.homework.dao;

import aston.homework.model.User;
import aston.homework.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class UserDAOTests {

    @Container
    protected static final PostgreSQLContainer<?> POSTGRES_CONTAINER =
            new PostgreSQLContainer<>("postgres:latest");

    private UserDAO userDAO;

    @BeforeAll
    static void setUpContainer() {
        POSTGRES_CONTAINER.start();
        SessionFactory testFactory = new Configuration()
                .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
                .setProperty("hibernate.connection.url", POSTGRES_CONTAINER.getJdbcUrl())
                .setProperty("hibernate.connection.username", POSTGRES_CONTAINER.getUsername())
                .setProperty("hibernate.connection.password", POSTGRES_CONTAINER.getPassword())
                .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                .setProperty("hibernate.hbm2ddl.auto", "create-drop")
                .setProperty("hibernate.show_sql", "true")
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
        HibernateUtil.setTestSessionFactory(testFactory);
    }

    @BeforeEach
    void setUp() {
        userDAO = new UserDAO();
    }

    @AfterAll
    static void clearProperties() {
        HibernateUtil.resetToDefault();
    }

    @AfterEach
    void cleanDatabase() {
        // Получаем sessionFactory из HibernateUtil
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.createMutationQuery("DELETE FROM User").executeUpdate();

            session.getTransaction().commit();
        }
    }

    @Test
    void testCreateUser() {
        User user = new User("Иван", "ivan@gmail.com", 25, LocalDateTime.now());

        User savedUser = userDAO.createUser(user);
        assertEquals("Иван", savedUser.getName());
        assertEquals("ivan@gmail.com", savedUser.getEmail());
        assertEquals(25, savedUser.getAge());
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User("Петр", "petr@mail.com", 30, LocalDateTime.now());
        User user2 = new User("Анна", "anna@mail.com", 28, LocalDateTime.now());

        userDAO.createUser(user1);
        userDAO.createUser(user2);

        List<User> users = userDAO.getAllUsers();
        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    void testUpdateUser() {
        User user = new User("Иван", "ivan@gmail.com", 25, LocalDateTime.now());

        User savedUser = userDAO.createUser(user);
        savedUser.setName("Ваня");
        boolean result = userDAO.updateUser(savedUser);
        assertTrue(result);
        assertEquals("Ваня", savedUser.getName());
    }

    @Test
    void testDeleteUserById() {
        User user = new User("Иван", "ivan@gmail.com", 25, LocalDateTime.now());

        User savedUser = userDAO.createUser(user);
        boolean result = userDAO.deleteUserById(savedUser.getId());
        assertTrue(result);
        assertNull(userDAO.getUserById(savedUser.getId()));
    }
}
