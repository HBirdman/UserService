package aston.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class UserCreateDTO {

    private String name;

    private String email;

    private int age;

    private LocalDateTime createdAt;

}
