-- Script SQL pour initialiser la base de données PostgreSQL
-- Base de données: gestion_livraison

-- Créer la base de données (à exécuter en tant que superutilisateur)
-- CREATE DATABASE gestion_livraison;

-- Se connecter à la base de données gestion_livraison
-- \c gestion_livraison;

-- Table Client
CREATE TABLE IF NOT EXISTS client (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(255) NOT NULL,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table Livreur
CREATE TABLE IF NOT EXISTS livreur (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(255) NOT NULL,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table Commande
CREATE TABLE IF NOT EXISTS commande (
    id BIGSERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    adresse_livraison VARCHAR(255) NOT NULL,
    statut VARCHAR(20) NOT NULL DEFAULT 'EN_COURS' CHECK (statut IN ('EN_COURS', 'LIVREE', 'ANNULEE')),
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    client_id BIGINT NOT NULL,
    livreur_id BIGINT NOT NULL,
    FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE,
    FOREIGN KEY (livreur_id) REFERENCES livreur(id) ON DELETE CASCADE
);

-- Créer les index pour améliorer les performances
CREATE INDEX IF NOT EXISTS idx_client_id ON commande(client_id);
CREATE INDEX IF NOT EXISTS idx_livreur_id ON commande(livreur_id);
CREATE INDEX IF NOT EXISTS idx_statut ON commande(statut);

-- Note: Les comptes de test seront créés automatiquement par l'application au démarrage
-- via la classe DataInitializer

