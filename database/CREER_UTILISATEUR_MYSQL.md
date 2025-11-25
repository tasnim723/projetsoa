# 🔐 Création de l'utilisateur MySQL

## Configuration dans application.properties

Le fichier `application.properties` est maintenant configuré avec :
- **Username** : `livraison_user`
- **Password** : `1234`
- **Database** : `gestion_livraison`

## 📝 Créer l'utilisateur MySQL

Si l'utilisateur `livraison_user` n'existe pas encore, exécutez ces commandes SQL dans MySQL :

### Option 1 : Via MySQL Command Line

```sql
-- Se connecter à MySQL en tant qu'administrateur
mysql -u root -p

-- Créer l'utilisateur
CREATE USER 'livraison_user'@'localhost' IDENTIFIED BY '1234';

-- Donner tous les privilèges sur la base de données
GRANT ALL PRIVILEGES ON gestion_livraison.* TO 'livraison_user'@'localhost';

-- Appliquer les changements
FLUSH PRIVILEGES;
```

### Option 2 : Via MySQL Workbench

1. Connectez-vous à MySQL Workbench
2. Ouvrez une nouvelle requête SQL
3. Exécutez les commandes ci-dessus

### Option 3 : Créer la base de données et l'utilisateur en une fois

```sql
-- Créer la base de données
CREATE DATABASE IF NOT EXISTS gestion_livraison;

-- Créer l'utilisateur
CREATE USER IF NOT EXISTS 'livraison_user'@'localhost' IDENTIFIED BY '1234';

-- Donner les privilèges
GRANT ALL PRIVILEGES ON gestion_livraison.* TO 'livraison_user'@'localhost';

-- Appliquer les changements
FLUSH PRIVILEGES;
```

## ✅ Vérification

Pour vérifier que l'utilisateur a été créé :

```sql
SELECT User, Host FROM mysql.user WHERE User = 'livraison_user';
```

## 🔧 Si vous avez des erreurs de connexion

### Erreur : "Access denied for user 'livraison_user'@'localhost'"

1. Vérifiez que l'utilisateur existe :
   ```sql
   SELECT User, Host FROM mysql.user WHERE User = 'livraison_user';
   ```

2. Si l'utilisateur n'existe pas, créez-le avec les commandes ci-dessus

3. Vérifiez les privilèges :
   ```sql
   SHOW GRANTS FOR 'livraison_user'@'localhost';
   ```

### Erreur : "Unknown database 'gestion_livraison'"

Créez la base de données :
```sql
CREATE DATABASE gestion_livraison;
```

## 📋 Script SQL Complet

Exécutez ce script pour tout configurer d'un coup :

```sql
-- Créer la base de données
CREATE DATABASE IF NOT EXISTS gestion_livraison;

-- Créer l'utilisateur
CREATE USER IF NOT EXISTS 'livraison_user'@'localhost' IDENTIFIED BY '1234';

-- Donner tous les privilèges
GRANT ALL PRIVILEGES ON gestion_livraison.* TO 'livraison_user'@'localhost';

-- Appliquer les changements
FLUSH PRIVILEGES;

-- Utiliser la base de données
USE gestion_livraison;

-- Créer les tables (optionnel, Spring Boot les créera automatiquement)
-- Ou exécutez le script database/init.sql
```

## 🚀 Après la configuration

Une fois l'utilisateur créé, vous pouvez lancer l'application Spring Boot :

```powershell
cd backend
mvn spring-boot:run
```

L'application créera automatiquement les tables grâce à `spring.jpa.hibernate.ddl-auto=update`.

