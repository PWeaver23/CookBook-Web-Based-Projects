const API = "http://localhost:8080/api/recipes";

console.log("recipe.js loaded");

async function loadRecipe() {
  const container = document.getElementById("recipeContainer");
  container.innerHTML = "";

  const id = new URLSearchParams(window.location.search).get("id");
  if (!id) {
    container.textContent = "No recipe id in the URL.";
    return;
  }

  try {
    const response = await fetch(`${API}/${id}`);
    if (response.status === 404) {
      container.textContent = "That recipe doesn't exist.";
      return;
    }
    if (!response.ok) throw new Error(`Request failed: ${response.status}`);

    const recipe = await response.json();   // a single object now
    const title = document.getElementById("pageTitle");
    title.textContent = recipe.title;
    container.append(buildRecipeCard(recipe));
  } catch (err) {
    console.error(err);
    container.textContent = "Couldn't load that recipe right now.";
  }
}

function buildRecipeCard(recipe) {
  const card = document.createElement("section");
  card.className = "card";

  const title = document.createElement("h2");
  title.textContent = recipe.title;

  const heading = document.createElement("h3");
  heading.textContent = "Ingredients";

  const list = document.createElement("ul");

  (recipe.ingredients ?? []).forEach((name, i) => {
    const li = document.createElement("li");
    const id = `r${recipe.id}-ing-${i}`;

    const box = document.createElement("input");
    box.type = "checkbox";
    box.id = id;
    box.value = name;
    box.addEventListener("change", onToggle);

    const label = document.createElement("label");
    label.htmlFor = id;
    label.textContent = name;

    li.append(box, label);
    list.append(li);
  });

  card.append(title, heading, list);
  return card;
}

function onToggle(event) {
  const box = event.target;
  const items = document.getElementById("shopping-items");

  if (box.checked) {
    const li = document.createElement("li");
    li.dataset.for = box.id;
    li.textContent = box.value;
    items.append(li);
  } else {
    items.querySelector(`[data-for="${box.id}"]`)?.remove();
  }

  document.getElementById("shopping-list").hidden = items.children.length === 0;
}

loadRecipe();
