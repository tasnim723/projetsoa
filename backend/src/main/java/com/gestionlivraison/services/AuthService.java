package com.gestionlivraison.services;

import com.gestionlivraison.dto.JwtResponse;
import com.gestionlivraison.dto.LoginRequest;
import com.gestionlivraison.entities.Client;
import com.gestionlivraison.entities.Livreur;
import com.gestionlivraison.repositories.ClientRepository;
import com.gestionlivraison.repositories.LivreurRepository;
import com.gestionlivraison.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private LivreurRepository livreurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public JwtResponse authenticate(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getMotDePasse();
        String role = loginRequest.getRole();

        if ("CLIENT".equals(role)) {
            Client client = clientRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Email ou mot de passe incorrect"));

            if (!passwordEncoder.matches(password, client.getMotDePasse())) {
                throw new RuntimeException("Email ou mot de passe incorrect");
            }

            String token = jwtTokenUtil.generateToken(client.getEmail(), "CLIENT", client.getId());
            return new JwtResponse(token, client.getId(), client.getEmail(), client.getNom(), "CLIENT");
        } else if ("LIVREUR".equals(role)) {
            Livreur livreur = livreurRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Email ou mot de passe incorrect"));

            if (!passwordEncoder.matches(password, livreur.getMotDePasse())) {
                throw new RuntimeException("Email ou mot de passe incorrect");
            }

            String token = jwtTokenUtil.generateToken(livreur.getEmail(), "LIVREUR", livreur.getId());
            return new JwtResponse(token, livreur.getId(), livreur.getEmail(), livreur.getNom(), "LIVREUR");
        } else {
            throw new RuntimeException("Rôle invalide");
        }
    }

    public JwtResponse register(com.gestionlivraison.dto.SignUpRequest signUpRequest) {
        String email = signUpRequest.getEmail();
        String role = signUpRequest.getRole();

        // Vérifier si l'email existe déjà
        if ("CLIENT".equals(role)) {
            if (clientRepository.existsByEmail(email)) {
                throw new RuntimeException("Cet email est déjà utilisé");
            }
            
            Client client = new Client();
            client.setNom(signUpRequest.getNom());
            client.setEmail(email);
            client.setMotDePasse(passwordEncoder.encode(signUpRequest.getMotDePasse()));
            
            Client savedClient = clientRepository.save(client);
            
            String token = jwtTokenUtil.generateToken(savedClient.getEmail(), "CLIENT", savedClient.getId());
            return new JwtResponse(token, savedClient.getId(), savedClient.getEmail(), savedClient.getNom(), "CLIENT");
        } else if ("LIVREUR".equals(role)) {
            if (livreurRepository.existsByEmail(email)) {
                throw new RuntimeException("Cet email est déjà utilisé");
            }
            
            Livreur livreur = new Livreur();
            livreur.setNom(signUpRequest.getNom());
            livreur.setEmail(email);
            livreur.setMotDePasse(passwordEncoder.encode(signUpRequest.getMotDePasse()));
            
            Livreur savedLivreur = livreurRepository.save(livreur);
            
            String token = jwtTokenUtil.generateToken(savedLivreur.getEmail(), "LIVREUR", savedLivreur.getId());
            return new JwtResponse(token, savedLivreur.getId(), savedLivreur.getEmail(), savedLivreur.getNom(), "LIVREUR");
        } else {
            throw new RuntimeException("Rôle invalide");
        }
    }
}

