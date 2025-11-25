-- Script SQL pour initialiser la base de données
-- Base de données: gestion_livraison

CREATE DATABASE IF NOT EXISTS gestion_livraison;
USE gestion_livraison;

-- Table Client
CREATE TABLE IF NOT EXISTS client (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(255) NOT NULL,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table Livreur
CREATE TABLE IF NOT EXISTS livreur (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(255) NOT NULL,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table Commande
CREATE TABLE IF NOT EXISTS commande (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    adresse_livraison VARCHAR(255) NOT NULL,
    statut ENUM('EN_COURS', 'LIVREE', 'ANNULEE') DEFAULT 'EN_COURS',
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    client_id BIGINT NOT NULL,
    livreur_id BIGINT NOT NULL,
    FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE,
    FOREIGN KEY (livreur_id) REFERENCES livreur(id) ON DELETE CASCADE,
    INDEX idx_client_id (client_id),
    INDEX idx_livreur_id (livreur_id),
    INDEX idx_statut (statut)
);

-- Note: Les comptes de test seront créés automatiquement par l'application au démarrage
-- via la classe DataInitializer

