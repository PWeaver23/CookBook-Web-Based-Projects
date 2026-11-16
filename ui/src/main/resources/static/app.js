const API = "http://localhost:8080/api/recipes";

console.log("app.js loaded");

async function loadRecipes() {
  const response = await fetch(API);          // GET /api/recipes
  const recipes = await response.json();      // an array now

  const container = document.getElementById("recipes");
  container.innerHTML = "";
  recipes.forEach(recipe => container.append(buildRecipeCard(recipe)));
}

function buildRecipeCard(recipe) {
  const card = document.createElement("section");
  card.className = "card";

  const title = document.createElement("h2");
  title.textContent = recipe.title;

  const heading = document.createElement("h3");
  heading.textContent = "Ingredients";

  const list = document.createElement("ul");

  recipe.ingredients.forEach((name, i) => {
    const li = document.createElement("li");
    const id = `r${recipe.id}-ing-${i}`;      // unique across recipes

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

   const stepsHeading = document.createElement("h3");
  stepsHeading.textContent = "Steps";

  const stepsList = document.createElement("ol");
  stepsList.className = "steps";

  recipe.steps.forEach(step => {
    const li = document.createElement("li");
    li.textContent = step;
    stepsList.append(li);
  });

  card.append(title, heading, list, stepsHeading, stepsList);
  return card;
}

function onToggle(event) {
  const box = event.target;
  const items = document.getElementById("shopping-items");

  if (box.checked) {
    const li = document.createElement("li");
    li.dataset.for = box.id;            // remember which checkbox this came from
    li.textContent = box.value;
    items.append(li);
  } else {
    items.querySelector(`[data-for="${box.id}"]`)?.remove();
  }

  document.getElementById("shopping-list").hidden = items.children.length === 0;
}

loadRecipes();