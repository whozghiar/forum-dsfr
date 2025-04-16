package fr.dsfr.forum.repositories;

import fr.dsfr.forum.beans.Forum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForumRepository extends JpaRepository<Forum, Long> {

    List<Forum> findByTitreContaining(String titre);

}
