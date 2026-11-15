const RECIPE_API = "http://localhost:8080/api/recipes";
const ACCOUNT_API = "http://localhost:8080/api/accounts";

console.log("app.js loaded");

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
  body.textContent = `Type: ${String(recipe.recipeType ?? "").toLowerCase()} · Author: ${await authorName(recipe.postedById)}`;

  card.append(title, body);
  return card;
}

async function authorName(postedById) {
  try {
    const res = await fetch(`${ACCOUNT_API}/${postedById}`);
    if (!res.ok) return "unknown author";
    const data = await res.json();
    return data.email ?? "unknown author";
  } catch {
    return "unknown author";
  }
}

loadRecipeCards();
