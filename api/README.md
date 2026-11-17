# Backend (Spring Boot)

## Planned Layout

| Path | Purpose |
| ---- | ------- |
| `src/main/java/com/cookbook/controller/` | Everything related to controllers |
| `src/main/java/com/cookbook/data/` | MongoDB repositories and data storage |
| `src/main/java/com/cookbook/model/` | POJOs |
| `src/main/java/com/cookbook/config/` | App-wide configuration (e.g., Spring Security setup) |
| `src/main/resources/` | `application.properties` and other non-Java resources |
| `src/main/resources/templates/` | HTML pages and htmx partial fragments (rendered by Spring Boot) |
| `src/test/java/com/cookbook/` | Tests. Mirror the main package structure here |


## Frontend (htmx)

The team chose **htmx**, so the UI lives inside this Spring Boot app. There is no separate frontend project. Controllers return HTML pages, or small HTML fragments for htmx requests, from `templates/`. Styles go in `static/css/`.

## Package Name

`com.cookbook` is a base package.

## Configuration

`src/main/resources/application.properties` reads database settings from environment variables (see `/.env.example` in the project root). Never put real credentials in this file.


