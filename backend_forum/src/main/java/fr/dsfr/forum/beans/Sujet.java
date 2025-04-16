package fr.dsfr.forum.beans;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Sujet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @ManyToOne
    @JoinColumn(name = "forum_id")
    private Forum forum;

    @OneToMany(mappedBy = "sujet", cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();
}

