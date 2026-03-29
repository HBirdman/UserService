package aston.homework.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {
    private Long userId;
    private String email;
    private EventType type;

    public enum EventType {
        CREATED, DELETED
    }
}
