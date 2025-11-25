@echo off
echo ========================================
echo   Verification des donnees PostgreSQL
echo ========================================
echo.

REM Vérifier si psql est disponible
where psql >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERREUR: psql n'est pas installe ou n'est pas dans le PATH
    echo Veuillez installer PostgreSQL ou ajouter psql au PATH
    pause
    exit /b 1
)

echo Connexion a PostgreSQL...
echo Mot de passe: ROOT
echo.

REM Changer vers le répertoire du script
cd /d %~dp0

REM Exécuter le script SQL
psql -U postgres -d gestion_livraison -f verifier_donnees.sql

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERREUR lors de la verification
    echo Assurez-vous que:
    echo 1. PostgreSQL est demarre
    echo 2. La base de donnees gestion_livraison existe
    echo 3. Le mot de passe est ROOT
    pause
    exit /b 1
)

echo.
echo Verification terminee!
pause

