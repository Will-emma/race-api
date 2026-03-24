# TP - Creation d'une API REST : Gestion d'inscriptions a une course

## Objectif

Ce projet a pour objectif de developper une API REST permettant de gerer :

- les coureurs
- les courses
- les inscriptions a une course

Les donnees sont persistees dans une base de donnees PostgreSQL.

## Stack technique

- Java 25
- Spring Boot 4
- Spring Web
- Spring Data JPA
- Flyway
- PostgreSQL
- Docker
- Adminer

## Lancer le projet

### 1. Demarrer la base de donnees

```bash
docker compose up -d
```

### 2. Lancer l'application

```bash
mvn spring-boot:run
```

L'API sera disponible sur :

```txt
http://localhost:8080
```

### 3. Acceder a Adminer

URL :

```txt
http://localhost:8081
```

Parametres de connexion :

- Systeme : `PostgreSQL`
- Serveur : `race_postgres`
- Utilisateur : `race`
- Mot de passe : `race`
- Base de donnees : `race_db`

## Endpoints implementes

### Gestion des coureurs

- `GET /runners` : lister tous les coureurs
- `GET /runners/{id}` : recuperer un coureur par son identifiant
- `POST /runners` : creer un coureur
- `PUT /runners/{id}` : modifier un coureur existant
- `DELETE /runners/{id}` : supprimer un coureur
- `GET /runners/{runnerId}/races` : lister les courses auxquelles un coureur est inscrit

### Gestion des courses

- `GET /races` : lister toutes les courses
- `GET /races?location=Paris` : filtrer les courses par lieu
- `GET /races/{id}` : recuperer une course par son identifiant
- `POST /races` : creer une course
- `PUT /races/{id}` : modifier une course existante
- `GET /races/{raceId}/participants/count` : compter le nombre de participants d'une course
- `GET /races/{raceId}/registrations` : lister les participants d'une course

### Gestion des inscriptions

- `POST /races/{raceId}/registrations` : inscrire un coureur a une course

## Regles metier implementees

- un coureur inexistant renvoie `404 Not Found`
- une course inexistante renvoie `404 Not Found`
- un email invalide pour un coureur renvoie `400 Bad Request`
- un coureur ne peut pas etre inscrit deux fois a la meme course : `409 Conflict`
- une course complete renvoie `409 Conflict`

## Codes HTTP utilises

- `200 OK`
- `201 Created`
- `400 Bad Request`
- `404 Not Found`
- `409 Conflict`

## Exemples de requetes

### Creer un coureur

```http
POST /runners
Content-Type: application/json

{
  "firstName": "Alice",
  "lastName": "Martin",
  "email": "alice.martin@example.com",
  "age": 30
}
```

### Creer une course

```http
POST /races
Content-Type: application/json

{
  "name": "Semi-marathon de Paris",
  "date": "2026-06-01",
  "location": "Paris",
  "maxParticipants": 500
}
```

### Modifier une course

```http
PUT /races/1
Content-Type: application/json

{
  "name": "Marathon de Lyon",
  "date": "2026-09-20",
  "location": "Lyon",
  "maxParticipants": 800
}
```

### Inscrire un coureur a une course

```http
POST /races/1/registrations
Content-Type: application/json

{
  "runnerId": 1
}
```

## Tests

Pour lancer les tests :

```bash
./mvnw.cmd test
```

Tous les tests passent actuellement.
