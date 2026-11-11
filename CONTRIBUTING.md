# Contributing Guide

_This guide is a starting template. The team should agree on each section and fill in the TBDs._

## Getting Started

1. Read the [README](README.md) and complete the setup steps.
2. Copy `.env.example` to `.env` and use your own local values.
3. Ask in the team channel if anything is unclear.

## Branching

- `main` should always be in a working state.
- Create a new branch for each task. Suggested naming:
  - `feature/<short-description>` (e.g., `feature/recipe-search`)
  - `fix/<short-description>`
  - `docs/<short-description>`
- _TBD: Does the team want a `develop` branch, or branch directly off `main`?_

## Commits

- Write short, clear commit messages that say what changed (e.g., "Add recipe list page layout").
- Commit small, related changes together.

## Pull Requests

- Open a pull request (PR) to merge your branch into `main`.
- Describe what changed and how you tested it.
- _TBD: How many teammates must approve a PR before merging?_
- _TBD: Who merges: the author or the reviewer?_

## Code Review Checklist

- [ ] The code builds and runs locally
- [ ] Tests pass (once tests exist)
- [ ] No secrets, passwords, or `.env` files are committed
- [ ] Code is placed in the correct package or folder
- [ ] Docs are updated if behavior or API endpoints changed

## Coding Conventions

- _TBD: Java style guide (e.g., Google Java Style)_
- _TBD: Naming conventions for classes, endpoints, and MongoDB collections/fields_
- _TBD: Formatting tool or IDE settings_

## Task Tracking

- _TBD: Where tasks live (e.g., GitHub Issues, Trello, GitHub Projects)_
- _TBD: How tasks are assigned_

## Communication

- _TBD: Team communication channel_
- _TBD: Meeting schedule_
- _TBD: Expected response time_
