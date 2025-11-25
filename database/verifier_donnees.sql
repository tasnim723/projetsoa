-- Script pour vérifier les données dans la base de données PostgreSQL
-- Exécutez ce script avec: psql -U postgres -d gestion_livraison -f verifier_donnees.sql

-- Se connecter à la base de données
\c gestion_livraison;

-- Vérifier les tables existantes
\dt

-- Compter les clients
SELECT '=== NOMBRE DE CLIENTS ===' AS info;
SELECT COUNT(*) AS nombre_clients FROM client;

-- Afficher tous les clients
SELECT '=== LISTE DES CLIENTS ===' AS info;
SELECT id, nom, email, date_creation FROM client ORDER BY id;

-- Compter les livreurs
SELECT '=== NOMBRE DE LIVREURS ===' AS info;
SELECT COUNT(*) AS nombre_livreurs FROM livreur;

-- Afficher tous les livreurs
SELECT '=== LISTE DES LIVREURS ===' AS info;
SELECT id, nom, email, date_creation FROM livreur ORDER BY id;

-- Compter les commandes
SELECT '=== NOMBRE DE COMMANDES ===' AS info;
SELECT COUNT(*) AS nombre_commandes FROM commande;

-- Afficher toutes les commandes
SELECT '=== LISTE DES COMMANDES ===' AS info;
SELECT 
    c.id,
    c.description,
    c.adresse_livraison,
    c.statut,
    c.date_creation,
    cl.nom AS client_nom,
    l.nom AS livreur_nom
FROM commande c
LEFT JOIN client cl ON c.client_id = cl.id
LEFT JOIN livreur l ON c.livreur_id = l.id
ORDER BY c.id;

-- Vérifier les comptes de test
SELECT '=== COMPTES DE TEST ===' AS info;
SELECT 'Client de test:' AS compte, email, nom FROM client WHERE email = 'client@example.com'
UNION ALL
SELECT 'Livreur de test:' AS compte, email, nom FROM livreur WHERE email = 'livreur@example.com';

