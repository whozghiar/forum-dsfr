package fr.dsfr.forum.repositories;

import fr.dsfr.forum.beans.Sujet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SujetRepository extends JpaRepository<Sujet, Long> {

    List<Sujet> findByForumId(Long forumId);

    List<Sujet> findByTitreContaining(String titre);

    Optional<Sujet> findById(Long id);

}
