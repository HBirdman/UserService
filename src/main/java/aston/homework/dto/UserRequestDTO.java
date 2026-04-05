package aston.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "DTO для создания и обновления пользователя")
@Data
public class UserRequestDTO {
    @Schema(description = "Имя пользователя", example = "Иван Петров")
    @NotBlank(message = "Имя обязательно")
    @Size(min = 2, max = 20, message = "Имя должно содержать от 2 до 20 символов")
    private String name;

    @Schema(description = "Email пользователя", example = "ivan@example.com")
    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
    private String email;

    @Schema(description = "Возраст", example = "30", minimum = "0", maximum = "120")
    @Min(value = 0, message = "Возраст должен быть положительным")
    @Max(value = 120, message = "Возраст не может быть больше 120")
    private int age;
}
