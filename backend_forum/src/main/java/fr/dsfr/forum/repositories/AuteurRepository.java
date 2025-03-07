package fr.dsfr.forum.repositories;

import fr.dsfr.forum.beans.Auteur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuteurRepository extends JpaRepository<Auteur, Long> {

    Optional<Auteur> findByPseudo(String pseudo);

    List<Auteur> findByPseudoContaining(String pseudo);
}
