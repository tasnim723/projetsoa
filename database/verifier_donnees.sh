#!/bin/bash

echo "========================================"
echo "  Vérification des données PostgreSQL"
echo "========================================"
echo ""

# Vérifier si psql est disponible
if ! command -v psql &> /dev/null; then
    echo "ERREUR: psql n'est pas installé ou n'est pas dans le PATH"
    echo "Veuillez installer PostgreSQL ou ajouter psql au PATH"
    exit 1
fi

echo "Connexion à PostgreSQL..."
echo "Mot de passe: ROOT"
echo ""

# Exécuter le script SQL
psql -U postgres -d gestion_livraison -f database/verifier_donnees.sql

if [ $? -ne 0 ]; then
    echo ""
    echo "ERREUR lors de la vérification"
    echo "Assurez-vous que:"
    echo "1. PostgreSQL est démarré"
    echo "2. La base de données gestion_livraison existe"
    echo "3. Le mot de passe est ROOT"
    exit 1
fi

echo ""
echo "Vérification terminée!"

