package fr.dsfr.forum.beans;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @ToString.Exclude
    private Auteur auteur;

    @ManyToOne
    @ToString.Exclude
    private Sujet sujet;

    @Column(columnDefinition = "TEXT")
    private String contenu;

    private LocalDateTime dateCreation;
}