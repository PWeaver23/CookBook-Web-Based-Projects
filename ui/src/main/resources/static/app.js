const RECIPE_API = "http://localhost:8080/api/recipes";
const ACCOUNT_API = "http://localhost:8080/api/auth";

console.log("app.js loaded");

// ---------- Account API ----------
function token() {
    return sessionStorage.getItem("token");
}

async function callApi(method, path, body) {
    const options = { method: method, headers: {} };
    if (token()) {
        options.headers["Authorization"] = "Bearer " + token();
    }
    if (body !== undefined) {
        options.headers["Content-Type"] = "application/json";
        options.body = JSON.stringify(body);
    }
    const response = await fetch(ACCOUNT_API + path, options);   // path is "/login", not "/auth/login"
    const text = await response.text();
    let data = null;
    try { data = text ? JSON.parse(text) : null; } catch { data = null; }
    const result = { ok: response.ok, status: response.status, data: data };

    // 401 on anything except the login itself: token missing or expired
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
    const result = await callApi("POST", "/login", credentials);
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
    const result = await callApi("POST", "/register", credentials);
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
}

// Connect the HTML to the functions
document.getElementById("loginForm").addEventListener("submit", logIn);
document.getElementById("registerButton").addEventListener("click", register);
document.getElementById("logoutButton").addEventListener("click", () => logOut());
updateAuthUI();   // on page load, restore the logged-in look if a token is saved


//------------

async function loadRecipeCards() {
  const container = document.getElementById("recipeCards");
  container.innerHTML = "";

  try {
    const response = await fetch(RECIPE_API);
    if (!response.ok) throw new Error(`Recipes request failed: ${response.status}`);

    const recipes = await response.json();
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
  body.textContent = `Type: ${String(recipe.recipeType ?? "").toLowerCase()}${await authorName(recipe.postedById)}`;
  
  card.append(title, body);
  return card;
}

async function authorName(postedById) {
  // console.log('Fetching author name for id:', postedById);
  try {
    const res = await fetch(`${ACCOUNT_API}/name/${postedById}`);
    if (!res.ok) return " · Author: unknown author";
    const text = await res.text();
    const data = JSON.parse(text);
    username = data.username;
    if (username == "admin") return "";
    return " · Author: " + (username ?? "unknown author");
  } catch {
    return "";
  }
}

loadRecipeCards();
