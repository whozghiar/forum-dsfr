package fr.dsfr.forum.repositories;

import fr.dsfr.forum.beans.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE m.sujet.id = :sujetId")
    List<Message> findBySujetId(Long sujetId);

    @Query("SELECT m FROM Message m WHERE m.auteur.id = :auteurId")
    List<Message> findByAuteurId(Long auteurId);

}
