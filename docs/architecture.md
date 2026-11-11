# Architecture

_Status: Draft template. Fill in as design decisions are made._

## Overview

_TODO: One or two paragraphs describing how the system works at a high level._

## Components

| Component | Technology | Responsibility |
| --------- | ---------- | -------------- |
| Backend | Java, Spring Boot | _TODO_ |
| Security | Spring Security | _TODO_ |
| Database | MongoDB | _TODO_ |
| Frontend | htmx + server-rendered HTML templates (engine TBD) | _TODO_ |

## Feature Areas

### Recipes
_TODO: What this area covers and its main responsibilities._

### Users and Authentication
_TODO: Registration, login, sessions vs tokens, roles._

### Shopping Lists
_TODO: How shopping lists relate to recipes and users._

## Diagram

This diagram shows the **planned** design and should be updated to match the implementation as the code is built.

The source file is [`architecture.mmd`](architecture.mmd). The copy below is included so GitHub displays it as a picture. **If you change the diagram, update both places.**

```mermaid
flowchart TB
    note["<b>Draft: planned architecture</b><br/>Implementation has not started.<br/>Update this diagram to match the real code.<br/>Hosting: undecided"]

    user(["User"])

    subgraph browser["Web Browser"]
        ui["HTML pages + htmx<br/><i>frontend: htmx (decided)</i><br/><i>templating engine: TBD</i>"]
    end

    subgraph app["Spring Boot Application (one app, not separate services)"]
        security["Spring Security<br/>login, sessions, access rules"]

        subgraph features["Feature Areas"]
            browse["Recipe Browsing<br/>and Details"]
            edit["Recipe Creation<br/>and Editing"]
            categories["Categories"]
            search["Search"]
            shopping["Shopping List"]
            accounts["User Accounts"]
        end

        views["HTML Templates + CSS<br/>server-rendered pages and fragments"]
        data["Data Access Layer<br/><i>approach TBD</i>"]
    end

    db[("MongoDB<br/>collections: recipes, categories,<br/>users, shopping lists")]

    %% Request path
    user -->|"clicks, forms, search text"| ui
    ui -->|"HTTP request (GET / POST)<br/>sent by htmx or page load"| security
    security -->|"authenticated request"| features
    security -.->|"verify credentials"| accounts

    %% Relationships between features
    search -.->|"finds"| browse
    categories -.->|"group"| browse
    shopping -.->|"ingredients from recipes"| browse
    edit -.->|"assign to"| categories

    %% Data path
    features -->|"recipe, list, and user data"| data
    data -->|"MongoDB queries"| db
    db -->|"documents (JSON-like)"| data

    %% Response path
    features -->|"data for the page"| views
    views -->|"HTTP response:<br/>HTML page or fragment"| ui

    classDef noteStyle fill:#fff8c5,stroke:#d4a72c,color:#333
    classDef dbStyle fill:#e3f2fd,stroke:#1e88e5,color:#333
    class note noteStyle
    class db dbStyle
```

## Data Model

_TODO: Main MongoDB collections, what each document looks like, and what is embedded vs referenced. Add this once the team agrees on it._

## Request Flow

_TODO: Describe what happens when a user performs a typical action, from browser to database and back._

## Deployment

_TODO: Where and how the app will run (local only, or a hosting provider)._
