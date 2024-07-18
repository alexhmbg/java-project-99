package hexlet.code.app.service;

import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.util.UserGenerator;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class CustomUserDetailsServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private UserGenerator modelGenerator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService userService;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = Instancio.of(modelGenerator.getUserModel())
                .create();
    }

    @Test
    void loadUserByUsername() {
        userRepository.save(testUser);
        var userDetails = userService.loadUserByUsername(testUser.getEmail());
        assertThat(testUser.getEmail()).isEqualTo(userDetails.getUsername());
    }

    @Test
    void createUser() {
        userService.createUser(testUser);
        var user = userRepository.findByEmail(testUser.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        assertThat(testUser.getEmail()).isEqualTo(user.getEmail());
    }
}