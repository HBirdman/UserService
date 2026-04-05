package aston.homework.controller;

import aston.homework.dto.UserRequestDTO;
import aston.homework.dto.UserResponseDTO;
import aston.homework.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Controller")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Создает пользователя и возвращает его данные")
    @ApiResponse(responseCode = "201", description = "Пользователь успешно создан")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO createUser(@Valid @RequestBody UserRequestDTO dto) {
        return userService.create(dto);
    }

    @Operation(summary = "Получить пользователя по ID")
    @GetMapping("/{id}")
    public UserResponseDTO showUserById(@Parameter(description = "ID пользователя") @PathVariable Long id) {
        return userService.show(id);
    }

    @Operation(summary = "Получить список всех пользователей в системе")
    @GetMapping
    public CollectionModel<UserResponseDTO> showAllUsers() {
        List<UserResponseDTO> users = userService.showAll();

        Link selfLink = linkTo(methodOn(UserController.class)
                .showAllUsers()).withSelfRel();
        Link createLink = linkTo(methodOn(UserController.class)
                .createUser(null)).withRel("create");

        return CollectionModel.of(users, selfLink, createLink);
    }

    @Operation(summary = "Обновить данные пользователя по ID")
    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@Parameter(description = "ID пользователя") @PathVariable Long id,
                                      @Parameter(description = "Новые данные")@Valid @RequestBody UserRequestDTO dto) {
        return userService.update(id, dto);
    }

    @Operation(summary = "Удалить пользователя по ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }
}
