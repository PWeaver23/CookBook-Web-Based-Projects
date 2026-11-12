// Dear Diary page: list, add, edit, delete and search entries through the REST API.
// Every change goes to the server first; then we reload the list from the server, so
// the page always shows what the API really has.

// The API is a different program on a different port, so this is a full address.
// The browser only allows it because the API allows http://localhost:8081 (CORS).
const API = "http://localhost:8080/api";

let speciesNames = {};      // species id -> name, e.g. {1: "Human"}, filled by loadSpecies()
let planetNames = {};      // planet id -> name, e.g. {1: "Earth"}, filled by loadPlanets()
let editingId = null;    // null = the form adds a new entry; a number = it edits that entry

// --- Talking to the API ---------------------------------------------------------

// Sends one request and returns {ok, status, data}. "data" is the JSON the server sent
// back, or null when there's no body (a 204 after DELETE, or a 404).
async function callApi(method, path, body) {
    const options = { method: method, headers: {} };
    if (body !== undefined) {
        options.headers["Content-Type"] = "application/json";   // tell Spring it's JSON
        options.body = JSON.stringify(body);                      // object -> JSON text
    }
    const response = await fetch(API + path, options);
    const text = await response.text();
    return { ok: response.ok, status: response.status, data: text ? JSON.parse(text) : null };
}

async function loadPlanets() {
    const result = await callApi("GET", "/planets");
    const select = document.getElementById("planet");
    for (const planet of result.data) {
        planetNames[planet.id] = planet.name;
        const option = document.createElement("option");
        option.value = planet.id;
        option.textContent = planet.name;
        select.appendChild(option);
    }
}

async function loadSpecies() {
    const result = await callApi("GET", "/species");
    const select = document.getElementById("species");
    for (const species of result.data) {
        speciesNames[species.id] = species.name;
        const option = document.createElement("option");
        option.value = species.id;
        option.textContent = species.name;
        select.appendChild(option);
    }
}

async function loadAbilities() {
    const result = await callApi("GET", "/abilities");
    const container = document.getElementById("abilitiesOptions");
    container.replaceChildren();
    for (const ability of result.data) {
        const label = document.createElement("label");
        const checkbox = document.createElement("input");
        checkbox.type = "checkbox";
        checkbox.name = "abilities";
        checkbox.value = ability.id;
        label.appendChild(checkbox);
        label.appendChild(document.createTextNode(ability.name));
        container.appendChild(label);
    }
}

async function loadPeople() {
    const search = document.getElementById("search").value.trim();
    const path = search ? "/persons?search=" + encodeURIComponent(search) : "/persons";
    const result = await callApi("GET", path);
    showEntries(result.data);
}


// --- Showing entries ------------------------------------------------------------

function showEntries(entries) {
    const list = document.getElementById("entries");
    list.innerHTML = "";   // clear the old list (safe: we put no user text here)

    if (entries.length === 0) {
        list.textContent = "No entries.";
        return;
    }
    for (const entry of entries) {
        list.appendChild(entryItem(entry));
    }
}



// One <li> for one entry. textContent (never innerHTML) for anything a user typed,
// so "<b>hi</b>" shows up as text instead of turning bold.
function entryItem(entry) {
    const item = document.createElement("li");

    const meta = document.createElement("div");
    meta.className = "meta";
    const planet = entry.planetId ? " · " + planetNames[entry.planetId] : "";
    const species = entry.speciesId ? " · " + speciesNames[entry.speciesId] : "";
    meta.textContent = entry.name + planet + species;

    const text = document.createElement("div");
    text.textContent = entry.text;

    const actions = document.createElement("div");
    actions.className = "actions";
    actions.appendChild(button("Edit", () => startEditing(entry)));
    actions.appendChild(button("Delete", () => deleteEntry(entry)));

    item.append(meta, text, actions);
    return item;
}


function button(label, onClick) {
    const b = document.createElement("button");
    b.type = "button";
    b.textContent = label;
    b.addEventListener("click", onClick);
    return b;
}

// --- Add, edit, delete ----------------------------------------------------------

async function saveEntry(event) {
    event.preventDefault();   // stop the browser from reloading the page
    const planetValue = document.getElementById("planet").value;
    const speciesValue = document.getElementById("species").value;
    const body = {
        text: document.getElementById("text").value,
        planetId: planetValue ? Number(planetValue) : null,
        speciesId: speciesValue ? Number(speciesValue) : null
    };

    // No id yet -> POST creates. Editing -> PUT replaces that entry.
    // The button is off while we wait, so a double-click can't save twice.
    const saveButton = document.getElementById("saveButton");
    saveButton.disabled = true;
    const result = editingId === null
        ? await callApi("POST", "/persons", body)
        : await callApi("PUT", "/persons/" + editingId, body);
    saveButton.disabled = false;

    if (!result.ok) {
        showError(result);   // e.g. 400 {"error":"text is required"}
        return;
    }
    stopEditing();
    await loadPeople();
}

function startEditing(entry) {
    editingId = entry.id;
    document.getElementById("text").value = entry.name;
    document.getElementById("planet").value = entry.planetId ?? "";
    document.getElementById("species").value = entry.speciesId ?? "";
    document.getElementById("formTitle").textContent = "Edit entry #" + entry.id;
    document.getElementById("saveButton").textContent = "Save changes";
    document.getElementById("cancelButton").hidden = false;
    document.getElementById("text").focus();
}

function stopEditing() {
    editingId = null;
    document.getElementById("entryForm").reset();
    document.getElementById("formTitle").textContent = "New entry";
    document.getElementById("saveButton").textContent = "Add entry";
    document.getElementById("cancelButton").hidden = true;
    document.getElementById("error").textContent = "";
}

async function deleteEntry(entry) {
    if (!confirm("Delete this entry?")) {
        return;
    }
    const result = await callApi("DELETE", "/persons/" + entry.id);
    if (!result.ok) {
        showError(result);
        return;
    }
    if (editingId === entry.id) {
        stopEditing();
    }
    await loadPeople();
}

function showError(result) {
    const message = result.data && result.data.error ? result.data.error : "Request failed";
    document.getElementById("error").textContent = message + " (status " + result.status + ")";
}

// --- Wire up the page -----------------------------------------------------------

document.getElementById("entryForm").addEventListener("submit", saveEntry);
document.getElementById("cancelButton").addEventListener("click", stopEditing);
document.getElementById("searchForm").addEventListener("submit", (event) => {
    event.preventDefault();
    loadPeople();
});
document.getElementById("clearSearch").addEventListener("click", () => {
    document.getElementById("search").value = "";
    loadPeople();
});

// Moods first (so entries can show mood names), then the entries.
loadPlanets()
.then(loadSpecies)
.then(loadAbilities)
.then(loadPeople);

