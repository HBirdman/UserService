package aston.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

@Schema(description = "DTO ответа от БД")
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "users", itemRelation = "user")
public class UserResponseDTO extends RepresentationModel<UserResponseDTO> {
    @Schema(description = "ID пользователя", example = "1")
    private Long id;
    @Schema(description = "Имя пользователя", example = "Иван Петров")
    private String name;
    @Schema(description = "Email пользователя", example = "ivan@example.com")
    private String email;
    @Schema(description = "Возраст", example = "30")
    private int age;
    @Schema(description = "Дата и время создания", example = "2026-04-05T14:51:00.123")
    private LocalDateTime createdAt;
}
