package hexlet.code.app.controller.api;

import hexlet.code.app.dto.UserCreateDTO;
import hexlet.code.app.dto.UserDTO;
import hexlet.code.app.dto.UserUpdateDTO;
import hexlet.code.app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(path = "/{id}")
    public UserDTO show(@PathVariable Long id) {
        return userService.show(id);
    }

    @GetMapping(path = "")
    public List<UserDTO> index() {
        return userService.getAll();
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody UserCreateDTO data) {
        return userService.create(data);
    }

    @PutMapping(path = "/{id}")
    public UserDTO update(@Valid @RequestBody UserUpdateDTO data, @PathVariable Long id) {
        return userService.update(data, id);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        userService.destroy(id);
    }
}