package aston.homework.controller;

import java.util.List;
import aston.homework.model.User;
import aston.homework.repository.UserRepository;
import aston.homework.util.UserGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserGenerator userGenerator;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void testCreateUser() throws Exception {
        var data = Instancio.of(userGenerator.getUserModel()).create();

        var request = post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var user = userRepository.findByEmail(data.getEmail()).orElse(null);

        assertNotNull(user);
        assertThat(user.getName()).isEqualTo(data.getName());
        assertThat(user.getAge()).isEqualTo(data.getAge());
    }

    @Test
    public void testShowAllUsers() throws Exception {
        var data1 = Instancio.of(userGenerator.getUserModel()).create();
        var data2 = Instancio.of(userGenerator.getUserModel()).create();

        var request1 = post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data1));
        var request2 = post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data2));
        mockMvc.perform(request1)
                .andExpect(status().isCreated());
        mockMvc.perform(request2)
                .andExpect(status().isCreated());

        var response = mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();

        List<User> actual = objectMapper.readValue(body, new TypeReference<>() { });

        var expected = userRepository.findAll();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void testDelete() throws Exception {
        var data = Instancio.of(userGenerator.getUserModel()).create();

        var request1 = post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data));
        mockMvc.perform(request1)
                .andExpect(status().isCreated());

        var user = userRepository.findByEmail(data.getEmail()).orElse(null);
        assertNotNull(user);

        var request2 = delete("/api/users/" + user.getId());
        mockMvc.perform(request2)
                .andExpect(status().isNoContent());

        assertThat(userRepository.findById(user.getId())).isEmpty();
    }
}
