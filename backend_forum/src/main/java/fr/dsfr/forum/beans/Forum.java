package fr.dsfr.forum.beans;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Forum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "forum", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Sujet> sujets = new ArrayList<>();
}
