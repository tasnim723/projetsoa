package com.gestionlivraison.controllers;

import com.gestionlivraison.dto.CommandeDTO;
import com.gestionlivraison.security.UserDetailsServiceImpl;
import com.gestionlivraison.services.CommandeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/commandes")
@Tag(name = "Commandes", description = "API de gestion des commandes")
@SecurityRequirement(name = "bearerAuth")
public class CommandeController {
    @Autowired
    private CommandeService commandeService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping
    @PreAuthorize("hasRole('LIVREUR')")
    @Operation(summary = "Créer une commande", description = "Permet à un livreur de créer une nouvelle commande")
    public ResponseEntity<CommandeDTO> createCommande(@Valid @RequestBody CommandeDTO commandeDTO) {
        Long livreurId = userDetailsService.getCurrentUserId();
        if (livreurId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CommandeDTO createdCommande = commandeService.createCommande(commandeDTO, livreurId);
        return new ResponseEntity<>(createdCommande, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('LIVREUR')")
    @Operation(summary = "Modifier une commande", description = "Permet à un livreur de modifier une commande")
    public ResponseEntity<CommandeDTO> updateCommande(@PathVariable Long id, @Valid @RequestBody CommandeDTO commandeDTO) {
        Long livreurId = userDetailsService.getCurrentUserId();
        if (livreurId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CommandeDTO updatedCommande = commandeService.updateCommande(id, commandeDTO, livreurId);
        return ResponseEntity.ok(updatedCommande);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('LIVREUR')")
    @Operation(summary = "Supprimer une commande", description = "Permet à un livreur de supprimer (annuler) une commande")
    public ResponseEntity<Void> deleteCommande(@PathVariable Long id) {
        Long livreurId = userDetailsService.getCurrentUserId();
        if (livreurId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        commandeService.deleteCommande(id, livreurId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT', 'LIVREUR')")
    @Operation(summary = "Obtenir une commande", description = "Permet d'obtenir les détails d'une commande")
    public ResponseEntity<CommandeDTO> getCommandeById(@PathVariable Long id) {
        String role = userDetailsService.getCurrentUserRole();
        Long userId = userDetailsService.getCurrentUserId();
        if (userId == null || role == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CommandeDTO commande = commandeService.getCommandeById(id, role, userId);
        return ResponseEntity.ok(commande);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENT', 'LIVREUR')")
    @Operation(summary = "Liste des commandes", description = "Permet d'obtenir la liste des commandes avec pagination")
    public ResponseEntity<Map<String, Object>> getAllCommandes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateCreation") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        String role = userDetailsService.getCurrentUserRole();
        Long userId = userDetailsService.getCurrentUserId();
        if (userId == null || role == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CommandeDTO> commandes;
        if ("CLIENT".equals(role)) {
            commandes = commandeService.getCommandesByClient(userId, pageable);
        } else {
            commandes = commandeService.getCommandesByLivreur(userId, pageable);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("commandes", commandes.getContent());
        response.put("currentPage", commandes.getNumber());
        response.put("totalItems", commandes.getTotalElements());
        response.put("totalPages", commandes.getTotalPages());

        return ResponseEntity.ok(response);
    }
}

