package fr.dsfr.forum.beans;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Auteur auteur;

    @ManyToOne
    private Sujet sujet;

    @Column(columnDefinition = "TEXT")
    private String contenu;

    private LocalDateTime dateCreation;
}