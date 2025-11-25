package com.gestionlivraison.security;

import com.gestionlivraison.entities.Client;
import com.gestionlivraison.entities.Livreur;
import com.gestionlivraison.repositories.ClientRepository;
import com.gestionlivraison.repositories.LivreurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private LivreurRepository livreurRepository;

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        String email = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        
        if ("CLIENT".equals(role)) {
            return clientRepository.findByEmail(email)
                    .map(Client::getId)
                    .orElse(null);
        } else if ("LIVREUR".equals(role)) {
            return livreurRepository.findByEmail(email)
                    .map(Livreur::getId)
                    .orElse(null);
        }
        return null;
    }

    public String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
    }
}

