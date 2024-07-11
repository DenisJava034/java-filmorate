package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class User {
    private Long id;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
    @Getter(AccessLevel.NONE)
    private Set<Long> friends;

    public User() {
    }

    public Set<Long> getFriends() {
        if (friends != null) {
            return new HashSet<>(friends);
        }
        return null;
    }
}
