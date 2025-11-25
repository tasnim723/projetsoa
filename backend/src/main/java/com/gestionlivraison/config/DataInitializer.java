package com.gestionlivraison.config;

import com.gestionlivraison.entities.Client;
import com.gestionlivraison.entities.Livreur;
import com.gestionlivraison.repositories.ClientRepository;
import com.gestionlivraison.repositories.LivreurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private LivreurRepository livreurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Créer un client de test s'il n'existe pas
        if (!clientRepository.existsByEmail("client@example.com")) {
            Client client = new Client();
            client.setNom("Client Test");
            client.setEmail("client@example.com");
            client.setMotDePasse(passwordEncoder.encode("password123"));
            clientRepository.save(client);
            System.out.println("Client de test créé : client@example.com / password123");
        }

        // Créer un livreur de test s'il n'existe pas
        if (!livreurRepository.existsByEmail("livreur@example.com")) {
            Livreur livreur = new Livreur();
            livreur.setNom("Livreur Test");
            livreur.setEmail("livreur@example.com");
            livreur.setMotDePasse(passwordEncoder.encode("password123"));
            livreurRepository.save(livreur);
            System.out.println("Livreur de test créé : livreur@example.com / password123");
        }
    }
}

