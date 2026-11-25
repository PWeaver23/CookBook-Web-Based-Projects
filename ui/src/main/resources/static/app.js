const RECIPE_API = "http://localhost:8080/api/recipes";
const ACCOUNT_API = "http://localhost:8080/api/auth";
const TYPE_API = "http://localhost:8080/api/types"

console.log("app.js loaded");

// ---------- Recipe Input ----------

async function loadTypes() {
    const response = await fetch(TYPE_API);
    if (!response.ok) throw new Error(`Recipes request failed: ${response.status}`);
    const types = await response.json();
    const select = document.getElementById("recipeType");
    for (const type of types) {
        const option = document.createElement("option");
        option.value = type.type;
        option.textContent = type.type;
        select.appendChild(option);
    }
}

function addIngredientRow() {
  const row = document.createElement('span');
  row.className = 'ingredient-row';
  row.innerHTML = `
    <input placeholder="Ingredient name (Flour)" required>
    <input placeholder="Ingredient measure (1/3 Cup)" required>
  `;
  document.getElementById('ingredientRows').append(row);
}

function dropIngredientRow() {
  const stepRows = document.getElementById('ingredientRows');

  const rows = stepRows.querySelectorAll('.ingredient-row');
  const length = rows.length;

  if (length > 1) {
    rows[length - 1].remove();
  }
}

function addStepRow() {
  const row = document.createElement('span');
  row.className = 'step-row';
  row.innerHTML = `
  <input id="step" placeholder="Recipe step" required>  
  `;
  document.getElementById('stepRows').append(row);
}

function dropStepRow() {
  const stepRows = document.getElementById('stepRows');

  const rows = stepRows.querySelectorAll('.step-row');
  const length = rows.length;

  if (length > 1) {
    rows[length - 1].remove();
  }
}

function closeRecipeAddPopover() {
  const form = document.getElementById('addRecipeForm');
  const ingredientRows = document.getElementById('ingredientRows');
  const stepRows = document.getElementById('stepRows');

  form.reset();

  // keep only the first row in each container
  ingredientRows.querySelectorAll('.ingredient-row').forEach((row, i) => {
    if (i > 0) row.remove();
  });
  stepRows.querySelectorAll('.step-row').forEach((row, i) => {
    if (i > 0) row.remove();
  });

  const popover = document.getElementById('addRecipePopover');
  if (popover && popover.matches(':popover-open')) popover.hidePopover();
}

async function finishRecipe(event) {
  if (event) event.preventDefault();

  // get the active users Id from the API
  const me = await callApi("GET", "/me", ACCOUNT_API);
  if (!me.ok) {
    showFormMessage(messageOf(me));
    return;
  }
  const postedById = me.data.id;

  // collecting ingredients form the two inputs
  const ingredients = [...document.querySelectorAll("#ingredientRows .ingredient-row")]
    .map(row => {
        const [name, measure] = row.querySelectorAll("input");
        return { name: name?.value.trim() ?? "", measure: measure?.value.trim() ?? "" };
    })
    .filter(i => i.name !== "" && i.measure !== "")
    .map(i => `${i.measure} of ${i.name}`);

  // collecting steps
  const steps = [...document.querySelectorAll("#stepRows .step-row input")]
        .map(input => input.value.trim())
        .filter(s => s !== "");

  // building recipe body
  const recipe = {
    title: document.getElementById("recipeName").value.trim(),
    recipeType: document.getElementById("recipeType").value,   // already "DINNER" etc.
    postedById: postedById,
    ingredients: ingredients,
    steps: steps
  };
  console.log("recipe: ", recipe)

  // POST recipe to API
  const result = await callApi("POST", "", RECIPE_API, recipe);
  if (!result.ok) {
    showFormMessage(messageOf(result));
    return;
  } 
  else {
    // reload recipe list to include added recipe and close the popover
    loadRecipeCards();
    closeRecipeAddPopover();
  }  
}

//event listeners for adding recipe buttons
document.getElementById("addIngredientRow").addEventListener("click", () => addIngredientRow())
document.getElementById("addStepRow").addEventListener("click", () => addStepRow())
document.getElementById('closeAddButton').addEventListener('click', () => closeRecipeAddPopover());
document.getElementById('addRecipeForm').addEventListener('submit', finishRecipe);
document.getElementById('dropIngredientRow').addEventListener('click', () => dropIngredientRow());
document.getElementById('dropStepRow').addEventListener('click', () => dropStepRow());

// ---------- Account API ----------

function token() {
    return sessionStorage.getItem("token");
}

async function callApi(method, path, base, body) {
    const options = { method: method, headers: {} };
    if (token()) {
        options.headers["Authorization"] = "Bearer " + token();
    }
    if (body !== undefined) {
        options.headers["Content-Type"] = "application/json";
        options.body = JSON.stringify(body);
    }
    const response = await fetch(base + path, options);
    const text = await response.text();
    let data = null;
    try { data = text ? JSON.parse(text) : null; } catch { data = null; }
    const result = { ok: response.ok, status: response.status, data: data };

    if (response.status === 401 && path !== "/login") {
        logOut("Your session ended. Please log in again.");
    }
    return result;
}

function messageOf(result) {
    return (result.data && result.data.detail) || ("Request failed (status " + result.status + ")");
}

async function logIn(event) {
    event.preventDefault();
    const credentials = {
        username: document.getElementById("username").value,
        password: document.getElementById("password").value
    };
    const result = await callApi("POST", "/login", ACCOUNT_API, credentials);
    if (!result.ok) {
        document.getElementById("loginMessage").textContent = messageOf(result);
        return;
    }
    sessionStorage.setItem("token", result.data.token);
    sessionStorage.setItem("username", result.data.username);
    document.getElementById("password").value = "";
    document.getElementById("loginMessage").textContent = "";
    document.getElementById("loginPopover").hidePopover();   // close the popup
    updateAuthUI();
}

async function register() {
    const credentials = {
        username: document.getElementById("username").value,
        password: document.getElementById("password").value
    };
    const result = await callApi("POST", "/register", ACCOUNT_API, credentials);
    if (!result.ok) {
        document.getElementById("loginMessage").textContent = messageOf(result);
        return;
    }
    await logIn(new Event("submit"));      // registered: log straight in
}

function logOut(message) {
    sessionStorage.removeItem("token");
    sessionStorage.removeItem("username");
    updateAuthUI();
    document.getElementById("loginMessage").textContent = message || "";
}

// Show "Logged in as ..." + Log out when we have a token, otherwise the Log in button
function updateAuthUI() {
    const loggedIn = Boolean(token());
    const welcome = document.getElementById("welcome");
    welcome.hidden = !loggedIn;
    welcome.textContent = loggedIn ? "Logged in as " + sessionStorage.getItem("username") + " " : "";
    document.getElementById("openLogin").hidden = loggedIn;
    document.getElementById("logoutButton").hidden = !loggedIn;
    document.getElementById("addRecipeButton").hidden = !loggedIn;
}

// Connect the HTML to the functions
document.getElementById("loginForm").addEventListener("submit", logIn);
document.getElementById("registerButton").addEventListener("click", register);
document.getElementById("logoutButton").addEventListener("click", () => logOut());
updateAuthUI();   // on page load, restore the logged-in look if a token is saved


// ---------- Loading Information ----------

async function loadRecipeCards() {
  const container = document.getElementById("recipeCards");
  container.innerHTML = "";

  try {
    const response = await callApi("GET", "", RECIPE_API);
    if (!response.ok) throw new Error(`Recipes request failed: ${response.status}`);

    const recipes = await response.data;
    const cards = await Promise.all(recipes.map(buildRecipeCard));
    container.append(...cards);
  } catch (err) {
    console.error(err);
    const msg = document.createElement("p");
    msg.textContent = "Couldn't load recipes right now.";
    container.append(msg);
  }
}

async function buildRecipeCard(recipe) {
  const card = document.createElement("section");
  card.className = "card";

  const title = document.createElement("h2");
  const recipeLink = document.createElement("a");
  recipeLink.href = `recipe.html?id=${recipe.id}`;
  recipeLink.textContent = recipe.title;
  title.appendChild(recipeLink);

  const body = document.createElement("p");
  body.textContent = `Type: ${(recipe.recipeType ?? "").toLowerCase()}${await authorName(recipe.postedById)}`;
  
  card.append(title, body);
  return card;
}

async function authorName(postedById) {
  // console.log('Fetching author name for id:', postedById);
  try {
    // const res = await fetch(`${ACCOUNT_API}/name/${postedById}`);
    const res = await callApi("GET", `/name/${postedById}`, ACCOUNT_API)
    if (!res.ok) return " · Author: unknown author";
    username = res.data.username;
    if (username == "admin") return "";
    return " · Author: " + (username ?? "unknown author");
  } catch {
    return "";
  }
}

loadTypes().then(loadRecipeCards);
