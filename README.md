# Poseidon Capital Solutions

Application back-end de gestion d'actifs financiers développée dans le cadre de la formation **OpenClassrooms – Développeur d'application Java**.

![Présentation](/.readme/presentation.png)

## Stack technique

| Composant              | Technologie                       |
|------------------------|-----------------------------------|
| Langage                | Java 25                           |
| Framework              | Spring Boot 4.0.4                 |
| Persistance            | Spring Data JPA / Hibernate       |
| Sécurité               | Spring Security 6 (session-based) |
| Encodage mot de passe  | BCrypt                            |
| Template engine        | Thymeleaf  |
| CSS                    | Bootstrap 4.3.1                   |
| Base de données (prod) | MySQL               |
| Base de données (test/demo) | H2 in-memory                 |
| Build                  | Maven                             |
| Tests                  | JUnit 5 + Mockito + MockMvc       |

## Fonctionnalités

- CRUD complet sur 6 entités financières : **BidList**, **CurvePoint**, **Rating**, **RuleName**, **Trade**, **User**
- Authentification session-based avec formulaire de login personnalisé
- Gestion des rôles : `ADMIN` (accès complet) et `USER` (accès aux entités financières)
- Validation des données via Bean Validation (`@NotBlank`, `@Pattern`, `@Digits`…)
- Gestion centralisée des erreurs (404, 403, erreur générique)
- Protection CSRF sur tous les formulaires

## Lancer la démo

Aucune installation de base de données requise. L'application démarre avec une base H2 en mémoire pré-chargée (~10 entrées par entité).

**Linux / macOS / Git Bash**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=demo
```

**Windows PowerShell**
```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=demo"
```

**Windows CMD**
```cmd
mvn spring-boot:run -Dspring-boot.run.profiles=demo
```

Accès : [http://localhost:8080](http://localhost:8080)

| Compte  | Mot de passe | Rôle  | Accès                          |
|---------|--------------|-------|--------------------------------|
| `admin` | `Admin123!`  | ADMIN | Toutes les pages               |
| `user`  | `User1234!`  | USER  | Entités financières uniquement |

## Lancer les tests

```bash
mvn verify
```

167 tests couvrant les services (unitaires Mockito), les controllers (MockMvc), la validation des entités et la sécurité.

## Environnement de développement (MySQL)

Créer la base `demo` sur MySQL, définir les variables d'environnement puis démarrer :

```bash
export DB_URL=jdbc:mysql://localhost:3306/demo
export DB_USERNAME=root
export DB_PASSWORD=root
mvn spring-boot:run
```

## Structure du projet

```
src/main/java/com/nnk/springboot/
├── advice/        ← Gestion globale des exceptions
├── config/        ← Spring Security, BCrypt, données de démo
├── controllers/   ← Couche MVC
├── domain/        ← Entités JPA
├── dto/           ← Objets de formulaire (UserForm)
├── exceptions/    ← Exceptions métier
├── repositories/  ← Interfaces JpaRepository
└── services/      ← Logique métier
```
