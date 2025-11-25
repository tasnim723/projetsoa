# Projet de Gestion de Livraison

Application complète de gestion de livraison avec Spring Boot (back-end) et React (front-end).

## 📋 Fonctionnalités

### Livreur
- ✅ Ajouter une commande
- ✅ Modifier une commande
- ✅ Supprimer (annuler) une commande
- ✅ Consulter ses commandes

### Client
- ✅ Consulter l'état de ses commandes

## 🏗️ Architecture

- **Back-end**: Spring Boot 3.2.0 avec JWT, JPA, PostgreSQL
- **Front-end**: React 18 avec React Router
- **Base de données**: PostgreSQL
- **Documentation API**: Swagger/OpenAPI

## 📦 Prérequis

- Java 17 ou supérieur
- Maven 3.6+
- Node.js 16+ et npm
- PostgreSQL 12+

## 🚀 Installation et Démarrage

### 1. Base de données PostgreSQL

Créez la base de données PostgreSQL :

```sql
-- Se connecter à PostgreSQL en tant que superutilisateur
-- psql -U postgres

-- Créer la base de données
CREATE DATABASE gestion_livraison;

-- Se connecter à la base de données
\c gestion_livraison;
```

Exécutez ensuite le script `database/init_postgresql.sql` pour créer les tables, ou laissez Spring Boot les créer automatiquement avec `spring.jpa.hibernate.ddl-auto=update`.

### 2. Configuration Back-end

1. Naviguez vers le dossier `backend` :
```bash
cd backend
```

2. Configurez la base de données dans `src/main/resources/application.properties` :
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/gestion_livraison
spring.datasource.username=postgres
spring.datasource.password=ROOT
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

3. Compilez et lancez le back-end :
```bash
mvn clean install
mvn spring-boot:run
```

Le serveur démarre sur `http://localhost:8080`

### 3. Configuration Front-end

1. Dans un nouveau terminal, naviguez vers le dossier `frontend` :
```bash
cd frontend
```

2. Installez les dépendances :
```bash
npm install
```

3. Lancez l'application React :
```bash
npm start
```

L'application démarre sur `http://localhost:3000`

## 🔐 Comptes de Test

L'application crée automatiquement des comptes de test au démarrage :

### Client
- **Email**: `client@example.com`
- **Mot de passe**: `password123`

### Livreur
- **Email**: `livreur@example.com`
- **Mot de passe**: `password123`

## 📚 Documentation API

Une fois le back-end démarré, accédez à la documentation Swagger :
- **URL**: `http://localhost:8080/swagger-ui.html`
- **API Docs**: `http://localhost:8080/v3/api-docs`

## 🎯 Utilisation

1. **Connexion** :
   - Ouvrez `http://localhost:3000`
   - Sélectionnez votre rôle (Client ou Livreur)
   - Connectez-vous avec un des comptes de test

2. **Client** :
   - Consultez la liste de vos commandes
   - Visualisez le statut de chaque commande

3. **Livreur** :
   - Consultez vos commandes
   - Créez une nouvelle commande (bouton "+ Nouvelle Commande")
   - Modifiez une commande existante
   - Supprimez (annulez) une commande

## 📁 Structure du Projet

```
miniprojetsoa/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/gestionlivraison/
│   │   │   │   ├── entities/          # Entités JPA
│   │   │   │   ├── repositories/      # Repositories Spring Data
│   │   │   │   ├── services/          # Services métier
│   │   │   │   ├── controllers/       # Controllers REST
│   │   │   │   ├── security/          # Configuration JWT
│   │   │   │   ├── config/            # Configuration Spring
│   │   │   │   └── dto/               # Data Transfer Objects
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── pom.xml
│   └── README.md
├── frontend/
│   ├── src/
│   │   ├── components/                # Composants React
│   │   ├── services/                  # Services API
│   │   ├── context/                   # Context React
│   │   └── App.js
│   ├── public/
│   └── package.json
└── README.md
```

## 🔧 Technologies Utilisées

### Back-end
- Spring Boot 3.2.0
- Spring Security avec JWT
- Spring Data JPA
- PostgreSQL Driver
- Swagger/OpenAPI 3
- Maven

### Front-end
- React 18.2.0
- React Router 6.20.0
- Axios 1.6.2
- CSS3

## 🛠️ Développement

### Compiler le back-end
```bash
cd backend
mvn clean package
```

### Tester le back-end
```bash
cd backend
mvn test
```

### Build de production (front-end)
```bash
cd frontend
npm run build
```

## ⚠️ Notes Importantes

1. **Base de données** : Assurez-vous que PostgreSQL est démarré avant de lancer le back-end
2. **Ports** : Le back-end utilise le port 8080 et le front-end le port 3000. PostgreSQL utilise le port 5432 par défaut
3. **CORS** : La configuration CORS est déjà activée pour permettre la communication entre front-end et back-end
4. **JWT** : Les tokens JWT sont valides pendant 24 heures par défaut

## 🐛 Résolution de Problèmes

### Erreur de connexion à la base de données
- Vérifiez que PostgreSQL est démarré
- Vérifiez les identifiants dans `application.properties` (username: postgres, password: ROOT)
- Assurez-vous que la base de données `gestion_livraison` existe
- Vérifiez que PostgreSQL écoute sur le port 5432

### Erreur CORS
- Vérifiez que le back-end est bien démarré sur le port 8080
- Vérifiez la configuration CORS dans `SecurityConfig.java`

### Erreur d'authentification
- Vérifiez que vous utilisez les bons identifiants
- Vérifiez que le token JWT est bien stocké dans le localStorage

## 📝 Licence

Ce projet est un projet éducatif.

## 👨‍💻 Auteur

Projet de gestion de livraison - Spring Boot + React

