package fr.dsfr.forum.beans;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Forum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;

    @OneToMany(mappedBy = "forum", cascade = CascadeType.ALL)
    private List<Sujet> sujets = new ArrayList<>();
}
