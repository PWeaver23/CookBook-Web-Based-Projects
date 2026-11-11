# Backend (Spring Boot)

> **Implementation has not started.** This folder contains only the planned package layout. There is no `pom.xml` and there are no Java classes yet.

## Planned Layout

This follows the standard Maven directory layout, organized **by feature** so each teammate can work in one area with fewer merge conflicts.

| Path | Purpose |
| ---- | ------- |
| `src/main/java/com/cookbook/recipes/` | Everything related to recipes |
| `src/main/java/com/cookbook/users/` | User accounts, registration, login, and authentication |
| `src/main/java/com/cookbook/shoppinglists/` | Shopping lists |
| `src/main/java/com/cookbook/config/` | App-wide configuration (e.g., Spring Security setup) |
| `src/main/resources/` | `application.properties` and other non-Java resources |
| `src/main/resources/templates/` | HTML pages and htmx partial fragments (rendered by Spring Boot) |
| `src/main/resources/static/css/` | Stylesheets served as static files |
| `src/test/java/com/cookbook/` | Tests. Mirror the main package structure here |

Inside each feature package, the team will typically add classes in layers such as controller, service, repository, and entity. _TBD: Agree on naming conventions before writing code (see `docs/design-decisions.md`)._

## Frontend (htmx)

The team chose **htmx**, so the UI lives inside this Spring Boot app. There is no separate frontend project. Controllers return HTML pages, or small HTML fragments for htmx requests, from `templates/`. Styles go in `static/css/`.

_TBD: Choose the HTML templating engine (e.g., Thymeleaf) and how htmx is included (a downloaded file in `static/` or a CDN link). Record both in `docs/design-decisions.md`._

## Package Name

`com.cookbook` is a **placeholder** base package. Confirm or change it before generating the project. If you change it, rename the folders under `src/main/java/` and `src/test/java/` to match.

## Getting Started (when the team is ready)

1. Generate the Maven project (e.g., with Spring Initializr) using the agreed Java version, group ID, and dependencies.
2. Merge the generated files into this `backend/` folder. Keep the existing package folders and the placeholders in `application.properties`.
3. Delete each `.gitkeep` file once its folder contains real files.

## Configuration

`src/main/resources/application.properties` reads database settings from environment variables (see `/.env.example` in the project root). Never put real credentials in this file.
