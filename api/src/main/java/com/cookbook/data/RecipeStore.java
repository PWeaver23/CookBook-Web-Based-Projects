package com.cookbook.data;

import org.springframework.stereotype.Component;

import com.cookbook.model.Recipe;
import com.cookbook.model.RecipeType;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

// In-memory store for recipes, with a temporary seeder for a test recipe. 
// Will be updated to use database once database is hooked up.
@Component
public class RecipeStore {

    private final List<Recipe> recipes = new CopyOnWriteArrayList<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    //temporary seeder for a test recipe
    //remove later once database is hooked up
    public RecipeStore() {
        add("Salted Flour", RecipeType.SNACK, 1, List.of("1/2 teaspoon salt", "1 cup flour"), List.of("Step 1 mix the salt into the flour.", "Step 2 eat the creation."));
        add("Salted egg", RecipeType.SNACK, 2, List.of("1 egg", "1 pinch of salt", "1 Tbsp of oil"), List.of("Step 1 heat up oil in the pan.", "Step 2 add egg and the salt.", "Step 3 flip the egg", "Step 4 eat the egg"));
    }

    public List<Recipe> findAll() {
        return List.copyOf(recipes);
    }

    /** Optional: either the entry, or "empty" when there is no entry with that id. */
    public Optional<Recipe> findById(int id) {
        return recipes.stream().filter(recipe -> recipe.getId() == id).findFirst();
    }

    /** Recipes whose title contains the search text, ignoring upper/lower case. */
    public List<Recipe> searchByText(String searchText) {
        String lowerSearch = searchText.toLowerCase();
        return recipes.stream()
                .filter(recipe -> recipe.getTitle().toLowerCase().contains(lowerSearch))
                .toList();
    }

    // Add a new recipe to the store, returning the created recipe with its new id.
    public Recipe add(String title, RecipeType recipeType, long postedById, List<String> ingredients, List<String> steps) {
        Recipe recipe = new Recipe(nextId.getAndIncrement(),
                title, recipeType, postedById, ingredients, steps);
        recipes.add(recipe);
        return recipe;
    }

    // Returns all recipes of the given type, e.g. RecipeType.SNACK
    public List<Recipe> findAllByType(RecipeType recipeType) {
        return recipes.stream()
                .filter(recipe -> recipe.getRecipeType() == recipeType)
                .toList();
    }

}
