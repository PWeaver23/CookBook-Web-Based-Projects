const RECIPE_API = "http://localhost:8080/api/recipes";
const ACCOUNT_API = "http://localhost:8080/api/auth";
const TYPE_API = "http://localhost:8080/api/types";

let currentUser = null;   // filled in once we know who is logged in

console.log("profile.js loaded");


function token() {
  return sessionStorage.getItem("token");
}

// fetch() that sends the login token. Optionally a method and a JSON body.
function authedFetch(url, method = "GET", body) {
  const headers = { Authorization: "Bearer " + token() };
  const options = { method: method, headers: headers };
  if (body !== undefined) {
    headers["Content-Type"] = "application/json";
    options.body = JSON.stringify(body);
  }
  return fetch(url, options);
}

const authorCache = new Map();
async function authorName(postedById) {
  if (authorCache.has(postedById)) return authorCache.get(postedById);
  let name = "";
  try {
    const res = await fetch(`${ACCOUNT_API}/name/${postedById}`);
    if (res.ok) {
      const text = await res.text();
      try { name = JSON.parse(text).username; }   
      catch { name = text.trim(); }               
    }
  } catch { /* leave the name blank */ }
  if (name === "admin") name = "";
  authorCache.set(postedById, name);
  return name;
}

// ---------- Building the cards ----------

async function buildRecipeCard(recipe) {
  const card = document.createElement("section");
  card.className = "card";

  const title = document.createElement("h3");
  const link = document.createElement("a");
  link.href = `recipe.html?id=${recipe.id}`;
  link.textContent = recipe.title;
  title.append(link);

  const author = await authorName(recipe.postedById);
  const info = document.createElement("p");
  info.className = "meta";
  info.textContent =
    `Type: ${(recipe.recipeType ?? "").toLowerCase()}` +
    (author ? ` · Author: ${author}` : "");

  card.append(title, info);
  return card;
}

async function renderList(container, countEl, recipes, emptyText) {
  container.innerHTML = "";
  countEl.textContent = `(${recipes.length})`;

  if (recipes.length === 0) {
    const p = document.createElement("p");
    p.className = "note";
    p.textContent = emptyText;
    container.append(p);
    return;
  }
  const cards = await Promise.all(recipes.map(buildRecipeCard));
  container.append(...cards);
}

function showMessage(container, text) {
  container.innerHTML = "";
  const p = document.createElement("p");
  p.className = "error";
  p.textContent = text;
  container.append(p);
}

// ---------- The two lists ----------

async function loadMade(me) {
  const list = document.getElementById("madeList");
  const count = document.getElementById("madeCount");
  try {
    const response = await fetch(RECIPE_API);
    if (!response.ok) throw new Error(`Request failed: ${response.status}`);
    const all = await response.json();
    const mine = all.filter(r => r.postedById === me.id);
    await renderList(list, count, mine, "You haven't added any recipes yet.");
  } catch (err) {
    console.error(err);
    showMessage(list, "Couldn't load your recipes right now.");
  }
}

async function loadSaved() {
  const list = document.getElementById("savedList");
  const count = document.getElementById("savedCount");
  try {
    const response = await authedFetch(`${ACCOUNT_API}/favorites`);
    if (response.status === 404) {
      showMessage(list, "Saved recipes aren't available yet (the API has no /favorites endpoint).");
      return;
    }
    if (!response.ok) throw new Error(`Request failed: ${response.status}`);
    const saved = await response.json();
    await renderList(list, count, saved, "You haven't saved any recipes yet.");
  } catch (err) {
    console.error(err);
    showMessage(list, "Couldn't load your saved recipes right now.");
  }
}

// ---------- Add recipe ----------


async function loadTypes() {
  try {
    const response = await fetch(TYPE_API);
    if (!response.ok) throw new Error(`Request failed: ${response.status}`);
    const types = await response.json();
    const select = document.getElementById("recipeType");
    for (const type of types) {
      const option = document.createElement("option");
      option.value = type.type;
      option.textContent = type.type;
      select.append(option);
    }
  } catch (err) {
    console.error(err);
    showFormMessage("Couldn't load the recipe types.");
  }
}

function showFormMessage(text) {
  document.getElementById("formMessage").textContent = text;
}

function addRow(containerId, rowClass, placeholders) {
  const row = document.createElement("span");
  row.className = rowClass;
  for (const placeholder of placeholders) {
    const input = document.createElement("input");
    input.placeholder = placeholder;
    input.required = true;
    row.append(input);
  }
  document.getElementById(containerId).append(row);
}

// Never remove the first row
function dropRow(containerId, rowClass) {
  const rows = document.querySelectorAll(`#${containerId} .${rowClass}`);
  if (rows.length > 1) rows[rows.length - 1].remove();
}

function closeAddRecipe() {
  document.getElementById("addRecipeForm").reset();
  document.querySelectorAll("#ingredientRows .ingredient-row").forEach((row, i) => { if (i > 0) row.remove(); });
  document.querySelectorAll("#stepRows .step-row").forEach((row, i) => { if (i > 0) row.remove(); });
  showFormMessage("");
  const popover = document.getElementById("addRecipePopover");
  if (popover.matches(":popover-open")) popover.hidePopover();
}

async function submitRecipe(event) {
  event.preventDefault();
  showFormMessage("");

  const ingredients = [...document.querySelectorAll("#ingredientRows .ingredient-row")]
    .map(row => {
      const [name, measure] = row.querySelectorAll("input");
      return { name: name.value.trim(), measure: measure.value.trim() };
    })
    .filter(i => i.name !== "" && i.measure !== "")
    .map(i => `${i.measure} of ${i.name}`);

  const steps = [...document.querySelectorAll("#stepRows .step-row input")]
    .map(input => input.value.trim())
    .filter(step => step !== "");

  if (ingredients.length === 0 || steps.length === 0) {
    showFormMessage("Please add at least one ingredient and one step.");
    return;
  }

  const recipe = {
    title: document.getElementById("recipeName").value.trim(),
    recipeType: document.getElementById("recipeType").value,
    postedById: currentUser.id,        // the API also uses the token, so this can't be faked
    ingredients: ingredients,
    steps: steps
  };

  try {
    const response = await authedFetch(RECIPE_API, "POST", recipe);
    if (response.status === 401) {
      showFormMessage("Your login expired. Please log in again.");
      return;
    }
    if (!response.ok) {
      showFormMessage(`Couldn't add the recipe (status ${response.status}).`);
      return;
    }
  } catch (err) {
    console.error(err);
    showFormMessage("Couldn't reach the server. Please try again.");
    return;
  }

  closeAddRecipe();
  loadMade(currentUser);               // refresh "Recipes I made" so the new one shows up
}

document.getElementById("addIngredientRow").addEventListener("click",
  () => addRow("ingredientRows", "ingredient-row", ["Ingredient name (Flour)", "Ingredient measure (1/3 Cup)"]));
document.getElementById("dropIngredientRow").addEventListener("click",
  () => dropRow("ingredientRows", "ingredient-row"));
document.getElementById("addStepRow").addEventListener("click",
  () => addRow("stepRows", "step-row", ["Recipe step"]));
document.getElementById("dropStepRow").addEventListener("click",
  () => dropRow("stepRows", "step-row"));
document.getElementById("closeAddButton").addEventListener("click", closeAddRecipe);
document.getElementById("addRecipeForm").addEventListener("submit", submitRecipe);

// ---------- Start ----------

function showLoggedOut() {
  document.getElementById("loggedOutCard").hidden = false;
  document.getElementById("profileContent").hidden = true;
}

async function loadProfile() {
  if (!token()) {
    showLoggedOut();
    return;
  }

  let me;
  try {
    const response = await authedFetch(`${ACCOUNT_API}/me`);
    if (response.status === 401) {          // token expired or invalid
      sessionStorage.removeItem("token");
      sessionStorage.removeItem("username");
      showLoggedOut();
      return;
    }
    if (!response.ok) throw new Error(`Request failed: ${response.status}`);
    me = await response.json();
  } catch (err) {
    console.error(err);
    showLoggedOut();
    return;
  }

  currentUser = me;
  document.getElementById("loggedOutCard").hidden = true;
  document.getElementById("profileContent").hidden = false;
  document.getElementById("profileName").textContent = `Logged in as ${me.username}`;

  loadMade(me);
  loadSaved();
}

loadTypes();
loadProfile();
