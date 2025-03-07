package fr.dsfr.forum.beans;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sujet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;

    @ManyToOne
    @JoinColumn(name = "forum_id")
    private Forum forum;

    @OneToMany(mappedBy = "sujet", cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();
}

