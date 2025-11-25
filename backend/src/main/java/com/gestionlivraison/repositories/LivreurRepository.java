package com.gestionlivraison.repositories;

import com.gestionlivraison.entities.Livreur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LivreurRepository extends JpaRepository<Livreur, Long> {
    Optional<Livreur> findByEmail(String email);
    boolean existsByEmail(String email);
}

