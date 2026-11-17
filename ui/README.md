# Frontend (HTMX)

## Planned Layout

| Path | Purpose |
| ---- | ------- |
| `src/main/resources/` | `application.properties` and other non-Java resources |
| `src/main/resources/static/templates/` | HTML pages and htmx partial fragments (rendered by Spring Boot) |
| `src/test/java/com/cookbook/` | Tests. Mirror the main package structure here |


## Package Name

`com.cookbook` is a base package.

## Configuration

`src/main/resources/application.properties` reads database settings from environment variables (see `/.env.example` in the project root). Never put real credentials in this file.


