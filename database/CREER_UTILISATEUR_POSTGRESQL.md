# 🔐 Configuration PostgreSQL

## Configuration dans application.properties

Le fichier `application.properties` est configuré avec :
- **Host** : `localhost`
- **Port** : `5432`
- **Database** : `gestion_livraison`
- **Username** : `postgres`
- **Password** : `ROOT`

## 📝 Créer la base de données

### Option 1 : Via psql (ligne de commande)

```bash
# Se connecter à PostgreSQL
psql -U postgres

# Créer la base de données
CREATE DATABASE gestion_livraison;

# Se connecter à la base de données
\c gestion_livraison;

# Exécuter le script de création des tables (optionnel)
\i database/init_postgresql.sql
```

### Option 2 : Via pgAdmin

1. Ouvrez pgAdmin
2. Connectez-vous au serveur PostgreSQL
3. Clic droit sur "Databases" → "Create" → "Database"
4. Nom : `gestion_livraison`
5. Cliquez sur "Save"

### Option 3 : Via ligne de commande (une seule commande)

```bash
# Créer la base de données directement
psql -U postgres -c "CREATE DATABASE gestion_livraison;"
```

## ✅ Vérification

Pour vérifier que la base de données a été créée :

```sql
-- Se connecter à PostgreSQL
psql -U postgres

-- Lister toutes les bases de données
\l

-- Vous devriez voir gestion_livraison dans la liste
```

## 🔧 Si vous avez des erreurs de connexion

### Erreur : "FATAL: password authentication failed"

1. Vérifiez que le mot de passe est correct (ROOT en majuscules)
2. Si vous avez oublié le mot de passe, réinitialisez-le :
   ```bash
   # Windows (en tant qu'administrateur)
   net stop postgresql-x64-XX
   # Modifier le fichier pg_hba.conf pour permettre les connexions sans mot de passe
   # Puis redémarrer PostgreSQL
   net start postgresql-x64-XX
   ```

### Erreur : "FATAL: database 'gestion_livraison' does not exist"

Créez la base de données :
```sql
CREATE DATABASE gestion_livraison;
```

### Erreur : "Connection refused"

1. Vérifiez que PostgreSQL est démarré :
   ```bash
   # Windows
   net start postgresql-x64-XX
   
   # Linux
   sudo systemctl start postgresql
   
   # Mac
   brew services start postgresql
   ```

2. Vérifiez que PostgreSQL écoute sur le port 5432 :
   ```bash
   # Windows
   netstat -an | findstr :5432
   
   # Linux/Mac
   netstat -an | grep :5432
   ```

## 📋 Script SQL Complet

Exécutez ce script pour tout configurer d'un coup :

```sql
-- Se connecter à PostgreSQL
psql -U postgres

-- Créer la base de données
CREATE DATABASE gestion_livraison;

-- Se connecter à la base de données
\c gestion_livraison;

-- Les tables seront créées automatiquement par Spring Boot
-- grâce à spring.jpa.hibernate.ddl-auto=update
```

## 🚀 Après la configuration

Une fois la base de données créée, vous pouvez lancer l'application Spring Boot :

```powershell
cd backend
mvn clean install
mvn spring-boot:run
```

L'application créera automatiquement les tables grâce à `spring.jpa.hibernate.ddl-auto=update`.

## 💡 Notes Importantes

- **Mot de passe** : Le mot de passe dans `application.properties` est `ROOT` (en majuscules)
- **Port par défaut** : PostgreSQL utilise le port 5432
- **Création automatique** : Spring Boot créera automatiquement les tables si elles n'existent pas (grâce à `ddl-auto=update`)

