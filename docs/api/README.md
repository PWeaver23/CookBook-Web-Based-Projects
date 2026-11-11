# API Documentation

> **No endpoints exist yet.** Document each endpoint here once it is implemented or agreed on.

The app uses **htmx**, so most endpoints return HTML (full pages or partial fragments) rather than JSON. For each endpoint, note whether it returns a full page or a fragment, and which htmx attributes trigger it. _TBD: Decide whether any JSON endpoints are needed._

## Conventions

- **Base URL:** _TBD_
- **Authentication:** _TBD (how requests are authenticated)_
- **Error format:** _TBD_

## Endpoints

Group endpoints by feature area.

### Recipes
_None yet._

### Users / Authentication
_None yet._

### Shopping Lists
_None yet._

---

## Endpoint Template

Copy this block for each endpoint.

```
#### <METHOD> <path>

**Description:**
**Auth required:** Yes / No
**Returns:** Full page / htmx fragment / JSON
**Template:** (e.g., templates/recipes/list.html)
**Triggered by:** (e.g., hx-get on the recipe search box)

**Request parameters / body:**

**Response:**

**Error responses:**
```
