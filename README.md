# CookBook Web App


RUN
    docker compose build --no-cache (use if recetly made changes aren't appearing on site)
    
    docker compose build (use after changing aything to rebuild the project)
    docker compose up -d
    docker compose down (add -v to remove all stored volume data)

To query database specifically
    docker exec -it PRO150-cookbook-mongo mongosh cookbook_db 
    db.recipes.find()
    db.accounts.find()


## Project Summary

We will be making an online cookbook that collects and stores recipes found around the web.  
Recipes will be categorized for breakfast, lunch, dinner, snack, and dessert. 
Users will be able to select ingredients from the recipe to add to an onsite shopping list.  
Users will be able to add and edit their own recipe to the site, choosing to make them public or keep them private. 


## Planned Stack

| Layer          | Choice                                   | Status      |
| -------------- | ---------------------------------------- | ----------- |
| Language       | Java                                     | Decided     |
| Framework      | Spring Boot                              | Decided     |
| Build tool     | Maven                                    | Decided     |
| Database       | MongoDB                                  | Decided     |
| Auth/Security  | Spring Security                          | Decided     |
| Frontend       | htmx (server-rendered HTML)              | Decided     |



## Prerequisites

Install these before setup begins:

- **JDK**: version 21+
- **Maven**: version 3.9.16 
- **MongoDB**: version 8.0 (installs through docker)
- **Git**
- **IDE**: IntelliJ IDEA recommended

No Node.js is needed. htmx pages are served directly by Spring Boot.

## Folder Guide

```
CookBook-Web-Based-Projects/
├── README.md              ← You are here
├── CONTRIBUTING.md        ← How the team works together (branches, PRs, reviews)
├── .env.example           ← Template for local database settings (copy to .env)
├── .gitignore
│
├── backend/               ← Spring Boot application (Maven project goes here)
│   ├── README.md          ← Explains what each package is for
│   └── src/
│       ├── main/
│       │   ├── java/com/cookbook/
│       │   │   ├── controller/        ← All app controllers
│       │   │   ├── data/          ← mongoDB repositories
│       │   │   ├── model/  ← POJO's for database documents
│       │   │   └── config/         ← App-wide config (e.g., Spring Security setup)
│       │   └── resources/
│       │       ├── application.properties  ← DB config placeholders (no secrets)
│       │       ├── templates/              ← HTML pages and htmx fragments
│       │       └── static/css/             ← Stylesheets
│       └── test/java/com/cookbook/         ← Tests mirror the main packages
│                 └── controller  ← Test cases for our api controllers
│
├── docs/
│   ├── architecture.md        ← How the system fits together
│   ├── design-decisions.md    ← Log of key decisions (including frontend choice)
│   └── technical-risks.md     ← Risks and mitigation plans
│
└── ui/
    └── src/
        ├── main/
        │   ├── java/com/cookbook/
        │   └── resources/
        │       ├── application.properties  ← ui app properties
        │       ├── templates/              
        │       └── static/             ← JS, Stylesheets, HTML pages and htmx fragments
        └── test/java/com/cookbook/
```


## Documentation

- [Architecture](docs/architecture.md)
- [Design Decisions](docs/design-decisions.md)
- [Technical Risks](docs/technical-risks.md)
- [API Documentation](docs/api/README.md)
- [Weekly Progress](docs/progress/README.md)
- [Contributing Guide](CONTRIBUTING.md)

## Team

| Name | 
| ---- |
| Jaxon Shaw | 
| Kenshin Revelo | 
| Preston Weaver |
