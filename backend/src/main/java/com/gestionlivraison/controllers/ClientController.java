package com.gestionlivraison.controllers;

import com.gestionlivraison.entities.Client;
import com.gestionlivraison.repositories.ClientRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/clients")
@Tag(name = "Clients", description = "API de gestion des clients")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping
    @Operation(summary = "Liste des clients", description = "Permet d'obtenir la liste de tous les clients")
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return ResponseEntity.ok(clients);
    }
}

