# CookBook Web App


RUN
    docker compose build --no-cache (use if recetly made changes aren't appearing on site)
    
    docker compose build (use after changing aything to rebuild the project)
    docker compose up -d
    docker compose down (add -v to remove all stored volume data)


> **Status: Implementation has not started.**
> This repository currently contains only the folder structure, starter documentation, and configuration placeholders. There is no application code, no Maven build file (`pom.xml`), and no database collections or data model yet.

## Project Summary

A web-based cookbook application where users can create an account, manage recipes, and build shopping lists from those recipes.



## Planned Stack

| Layer          | Choice                                   | Status      |
| -------------- | ---------------------------------------- | ----------- |
| Language       | Java                                     | Decided     |
| Framework      | Spring Boot                              | Decided     |
| Build tool     | Maven                                    | Decided     |
| Database       | MongoDB                                  | Decided     |
| Auth/Security  | Spring Security                          | Decided     |
| Frontend       | htmx (server-rendered HTML from Spring Boot) | Decided |
| HTML templating | _TBD_ (e.g., Thymeleaf)                 | **Undecided** |

Decisions are tracked in [docs/design-decisions.md](docs/design-decisions.md).

## Prerequisites

Install these before setup begins (versions are placeholders; the team should confirm them):

- **JDK**: version _TBD_ (e.g., 17 or 21 LTS)
- **Maven**: version _TBD_ (or use the Maven Wrapper once it is added)
- **MongoDB**: version _TBD_ (local install, Docker, or a free MongoDB Atlas cluster; team to decide)
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
│
├── docs/
│   ├── architecture.md        ← How the system fits together
│   ├── design-decisions.md    ← Log of key decisions (including frontend choice)
│   ├── technical-risks.md     ← Risks and mitigation plans
│   ├── api/README.md          ← API endpoint documentation
│   └── progress/              ← Weekly progress reports
│       ├── README.md
│       └── week-template.md
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

`.gitkeep` files are empty placeholders that let Git track otherwise-empty folders. Delete one once its folder has real files.

## Setup

_These steps are placeholders and will be filled in once implementation starts._

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd CookBook-Web-Based-Projects
   ```
2. **Create your local environment file**
   ```bash
   cp .env.example .env
   ```
   Then edit `.env` with your own local database settings. **Never commit `.env`.**
3. **Set up MongoDB**: _TODO: steps to start MongoDB locally (or connect to the shared cluster) and set `MONGODB_URI`._
4. **Build the backend**: _TODO: Maven command (e.g., `mvn clean install`) once `pom.xml` exists._
5. **Run the app**: _TODO: run command and the local URL. Spring Boot serves both the backend and the htmx pages, so there is no separate frontend server._

> Note: Spring Boot does not read `.env` files automatically. The team should decide how to load these variables, for example with IntelliJ run-configuration environment variables, an EnvFile plugin, or exporting them in your shell. Record the choice in `docs/design-decisions.md`.

## Documentation

- [Architecture](docs/architecture.md)
- [Design Decisions](docs/design-decisions.md)
- [Technical Risks](docs/technical-risks.md)
- [API Documentation](docs/api/README.md)
- [Weekly Progress](docs/progress/README.md)
- [Contributing Guide](CONTRIBUTING.md)

## Team

| Name | Role / Focus Area |
| ---- | ----------------- |
| _TBD_ | _TBD_            |
