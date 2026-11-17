package com.cookbook.data;

import com.cookbook.model.Recipe;
import com.cookbook.model.RecipeType;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

// Temporary test recipes (moved here from the old in-memory RecipeStore).
// Runs once when the API starts, and only adds them if the recipes collection is empty,
// so restarting the API never creates duplicates.
// Turn it off with cookbook.seed-test-recipes=false (the tests do this, since they run without MongoDB).
// Remove this class once real recipes can be added through the app.
@Component
@ConditionalOnProperty(name = "cookbook.seed-test-recipes", havingValue = "true", matchIfMissing = true)
public class RecipeSeeder implements ApplicationRunner {

    private final RecipeRepository recipes;

    public RecipeSeeder(RecipeRepository recipes) {
        this.recipes = recipes;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (recipes.count() > 0) {
            return;
        }
        // Steps have no "Step 1" prefix: the recipe page numbers them itself.
        recipes.saveAll(List.of(
            new Recipe("Salted Flour", RecipeType.SNACK, 1,
                List.of("1/2 teaspoon salt", "1 cup flour"),
                List.of("Mix the salt into the flour.", "Eat the creation.")),

            new Recipe("Salted egg", RecipeType.SNACK, 1,
                List.of("1 egg", "1 pinch of salt", "1 Tbsp of oil"),
                List.of("Heat up oil in the pan.", "Add egg and the salt.", "Flip the egg.", "Eat the egg."))
        ));
    }
}
