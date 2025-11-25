package com.gestionlivraison.repositories;

import com.gestionlivraison.entities.Commande;
import com.gestionlivraison.entities.Commande.StatutCommande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {
    Page<Commande> findByClientId(Long clientId, Pageable pageable);
    Page<Commande> findByLivreurId(Long livreurId, Pageable pageable);
    List<Commande> findByClientId(Long clientId);
    List<Commande> findByLivreurId(Long livreurId);
    Page<Commande> findByStatut(StatutCommande statut, Pageable pageable);
}

