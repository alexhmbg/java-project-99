package hexlet.code.app.contoller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper om;

    private User generateUser() {
        var user = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .create();
        return user;
    }

    @Test
    void testIndex() throws Exception {
        var result = mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).isArray();
    }

    @Test
    void testShow() throws Exception {
        var user = generateUser();
        userRepository.save(user);

        var request = get("/api/users/{id}", user.getId());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("email").isEqualTo(user.getEmail()),
                v -> v.node("firstName").isEqualTo(user.getFirstName()),
                v -> v.node("lastName").isEqualTo(user.getLastName())
        );
    }

    @Test
    void testCreate() throws Exception {
        var data = generateUser();
        userRepository.save(data);

        var request = post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var user = userRepository.findById(data.getId()).get();

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(data.getId());
        assertThat(user.getEmail()).isEqualTo(data.getEmail());
    }

    @Test
    void testUpdate() throws Exception {
        var user = generateUser();
        userRepository.save(user);

        var data = new HashMap<>();
        data.put("firstName", "testNewName");

        var request = put("/api/users/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        user = userRepository.findById(user.getId()).get();
        assertThat(user.getFirstName()).isEqualTo(data.get("firstName"));
    }

    @Test
    void testDelete() throws Exception {
        var user = generateUser();
        userRepository.save(user);

        var request = delete("/api/users/{id}", user.getId());

        mockMvc.perform(request)
                .andExpect(status().isOk());

        user = userRepository.findById(user.getId()).orElse(null);
        assertThat(user).isNull();
    }
}