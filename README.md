# API-LeBonSandwich

## A propos

Ce projet regroupe Les TD 1 à 4 du cours de programmation Web/Server de la LP CISIIE de l'année 2017-2018.

## Comment faire fonctionner le projet

Pour faire fonctionner le projet, il faut d'abord cloner le projet :

```bash
git clone git@github.com:demarbre1u/API-LeBonSandwich.git
```

Une fois le projet cloné, on se déplace dans le repertoire, puis on construit notre fichier `.war` :

```bash
cd API-LeBonSandwich/
mvn clean install
```

Une fois notre fichier généré, il faut le déplacer dans le bon dossier de sorte que WildFly puisse le déployer.

Une fois ceci fait, il ne reste plus qu'à tester !

## Les fonctionnalités disponibles

### Les catégories

Liste des catégories : localhost:8080/projet/api/categories (GET)

Voir une catégorie : localhost:8080/projet/api/categories/1 (GET)

Ajouter une catégorie : localhost:8080/projet/api/categories (POST)

Modifier une catégorie : localhost:8080/projet/api/categories/1 (PUT)

Supprimer une catégorie : localhost:8080/projet/api/categories/1 (DELETE)

Liste des sandwichs d'une catégorie : localhost:8080/projet/api/categories/1/sandwichs (GET)

### Les sandwichs

Liste des sandwichs : localhost:8080/projet/api/sandwichs (GET)

Voir un sandwich : localhost:8080/projet/api/sandwichs/1 (GET)

Ajouter un sandwich : localhost:8080/projet/api/sandwichs (POST)

Modifier un sandwich : localhost:8080/projet/api/sandwichs/1 (PUT)

Supprimer un sandwich : localhost:8080/projet/api/sandwichs/1 (DELETE)

Filtrer par type de pain : localhost:8080/projet/api/sandwichs?type=baguette (GET)

Filtrer par image : localhost:8080/projet/api/sandwichs?img=1 (GET)

Filtrer par type de pain ET image : localhost:8080/projet/api/sandwichs?type=baguette&img=1 (GET)

Changer de page : localhost:8080/projet/api/sandwichs?page=2 (GET)

Changer de taille : localhost:8080/projet/api/sandwichs&size=50 (GET)

Changer de taille ET de page : localhost:8080/projet/api/sandwichs?page=2&size=50 (GET)

Liste des catégories d'un sandwich : localhost:8080/projet/api/sandwichs/1/categories (GET)

### Les tailles

Consulter la liste des tailles : localhost:8080/projet/api/tailles (GET)

Consulter une taille en particulier : localhost:8080/projet/api/tailles/1 (GET)

Consulter la liste des sandwichs d'une taille : localhost:8080/projet/api/tailles/1/sandwichs (GET)

Ajouter une taille : localhost:8080/projet/api/tailles (POST)

Supprimer une taille : localhost:8080/projet/api/tailles/1 (DELETE)

Modifier une taille : localhost:8080/projet/api/tailles/1 (PUT)

### Les commandes

Créer une commande : localhost:8080/projet/api/commandes/ (POST)

Consulter une commande : localhost:8080/projet/api/commandes/id?token=token __OU__ localhost:8080/projet/api/commandes/id avec X-lbs-token=token dans le Header de la requête (GET)
