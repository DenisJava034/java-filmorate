package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private Long id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;
    @Getter(AccessLevel.NONE)
    private Set<Long> likes;
    private List<Genre> genres;
    private Mpa mpa;


    public Set<Long> getLikes() {
        if (likes != null) {
            return new HashSet<>(likes);
        }
        return null;
    }
}
