package aston.homework.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private int age;
    private LocalDateTime createdAt;
}
