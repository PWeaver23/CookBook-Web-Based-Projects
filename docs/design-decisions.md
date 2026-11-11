# Design Decisions

A log of key decisions and why the team made them. Add a new entry whenever the team makes a decision that affects the whole project.

## Decision Summary

| # | Decision | Status | Date |
| - | -------- | ------ | ---- |
| 1 | Backend: Java + Spring Boot + Maven | Decided | _TBD_ |
| 2 | Database: PostgreSQL | Decided | _TBD_ |
| 3 | Authentication: Spring Security | Decided | _TBD_ |
| 4 | Frontend: htmx (served by Spring Boot) | Decided | 2026-11-11 |

## Open Questions

- [ ] HTML templating engine (e.g., Thymeleaf)
- [ ] How htmx is included (downloaded file in `static/` or CDN link)
- [ ] Java version
- [ ] Base package / Maven group ID (placeholder is `com.cookbook`)
- [ ] Database migration tool (e.g., Flyway, Liquibase, or none)
- [ ] Authentication style (session-based or token-based)
- [ ] How developers load `.env` variables locally
- [ ] Hosting / deployment target

---

## Decision Template

Copy this block for each new decision.

```
### <Number>. <Title>

- **Status:** Proposed | Decided | Replaced
- **Date:**
- **Decided by:**

**Context:** What problem or question needed a decision?

**Options considered:**
1.
2.

**Decision:** What did the team choose?

**Reasons:** Why this option?

**Consequences:** What does this make easier or harder?
```

---

## Decisions

### 4. Frontend: htmx vs React

- **Status:** Decided
- **Date:** 2026-11-11 (date recorded)
- **Decided by:** Team

**Context:** The team needs to choose how to build the user interface.

**Options considered:**
1. **htmx**: server-rendered HTML from Spring Boot with small dynamic updates
2. **React**: separate JavaScript frontend that calls a JSON API

**Decision:** htmx, with pages rendered by Spring Boot.

**Reasons:** _TBD: Team to fill in._

**Consequences:**
- No separate frontend project and no Node.js. Pages live in `backend/src/main/resources/templates/` and styles in `backend/src/main/resources/static/css/`.
- Endpoints mostly return HTML pages or fragments instead of JSON.
- Spring Security's default session-based login works naturally with server-rendered pages.
- The team still needs to pick an HTML templating engine (see Open Questions).
