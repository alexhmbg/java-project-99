package hexlet.code.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserCreateDTO {
    @Email
    private String email;

    private String firstName;

    private String lastName;

    @Size(min = 3, max = 100)
    private String password;
}
