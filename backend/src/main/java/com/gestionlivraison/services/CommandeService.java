package com.gestionlivraison.services;

import com.gestionlivraison.dto.CommandeDTO;
import com.gestionlivraison.entities.Client;
import com.gestionlivraison.entities.Commande;
import com.gestionlivraison.entities.Commande.StatutCommande;
import com.gestionlivraison.entities.Livreur;
import com.gestionlivraison.repositories.ClientRepository;
import com.gestionlivraison.repositories.CommandeRepository;
import com.gestionlivraison.repositories.LivreurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommandeService {
    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private LivreurRepository livreurRepository;

    public CommandeDTO createCommande(CommandeDTO commandeDTO, Long livreurId) {
        Long clientId = commandeDTO.getClientId();
        if (clientId == null) {
            throw new IllegalArgumentException("Client ID cannot be null");
        }
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        if (livreurId == null) {
            throw new IllegalArgumentException("Livreur ID cannot be null");
        }
        Livreur livreur = livreurRepository.findById(livreurId)
                .orElseThrow(() -> new RuntimeException("Livreur non trouvé"));

        Commande commande = new Commande();
        commande.setDescription(commandeDTO.getDescription());
        commande.setAdresseLivraison(commandeDTO.getAdresseLivraison());
        commande.setStatut(StatutCommande.EN_COURS);
        commande.setClient(client);
        commande.setLivreur(livreur);

        Commande savedCommande = commandeRepository.save(commande);
        return convertToDTO(savedCommande);
    }

    public CommandeDTO updateCommande(Long id, CommandeDTO commandeDTO, Long livreurId) {
        if (id == null) {
            throw new IllegalArgumentException("Commande ID cannot be null");
        }
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));

        if (!commande.getLivreur().getId().equals(livreurId)) {
            throw new AccessDeniedException("Vous n'êtes pas autorisé à modifier cette commande");
        }

        commande.setDescription(commandeDTO.getDescription());
        commande.setAdresseLivraison(commandeDTO.getAdresseLivraison());
        if (commandeDTO.getStatut() != null) {
            commande.setStatut(commandeDTO.getStatut());
        }

        Commande updatedCommande = commandeRepository.save(commande);
        return convertToDTO(updatedCommande);
    }

    public void deleteCommande(Long id, Long livreurId) {
        if (id == null) {
            throw new IllegalArgumentException("Commande ID cannot be null");
        }
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));

        if (!commande.getLivreur().getId().equals(livreurId)) {
            throw new AccessDeniedException("Vous n'êtes pas autorisé à supprimer cette commande");
        }

        commandeRepository.delete(commande);
    }

    public CommandeDTO getCommandeById(Long id, String role, Long userId) {
        if (id == null) {
            throw new IllegalArgumentException("Commande ID cannot be null");
        }
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));

        if ("CLIENT".equals(role) && !commande.getClient().getId().equals(userId)) {
            throw new AccessDeniedException("Vous n'êtes pas autorisé à consulter cette commande");
        }

        if ("LIVREUR".equals(role) && !commande.getLivreur().getId().equals(userId)) {
            throw new AccessDeniedException("Vous n'êtes pas autorisé à consulter cette commande");
        }

        return convertToDTO(commande);
    }

    public Page<CommandeDTO> getCommandesByClient(Long clientId, Pageable pageable) {
        Page<Commande> commandes = commandeRepository.findByClientId(clientId, pageable);
        return commandes.map(this::convertToDTO);
    }

    public Page<CommandeDTO> getCommandesByLivreur(Long livreurId, Pageable pageable) {
        Page<Commande> commandes = commandeRepository.findByLivreurId(livreurId, pageable);
        return commandes.map(this::convertToDTO);
    }

    public List<CommandeDTO> getAllCommandes(String role, Long userId) {
        List<Commande> commandes;
        if ("CLIENT".equals(role)) {
            commandes = commandeRepository.findByClientId(userId);
        } else {
            commandes = commandeRepository.findByLivreurId(userId);
        }
        return commandes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private CommandeDTO convertToDTO(Commande commande) {
        CommandeDTO dto = new CommandeDTO();
        dto.setId(commande.getId());
        dto.setDescription(commande.getDescription());
        dto.setAdresseLivraison(commande.getAdresseLivraison());
        dto.setStatut(commande.getStatut());
        dto.setDateCreation(commande.getDateCreation());
        dto.setClientId(commande.getClient().getId());
        dto.setClientNom(commande.getClient().getNom());
        dto.setLivreurId(commande.getLivreur().getId());
        dto.setLivreurNom(commande.getLivreur().getNom());
        return dto;
    }
}
