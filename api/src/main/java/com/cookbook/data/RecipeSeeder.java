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
                List.of("Heat up oil in the pan.", "Add egg and the salt.", "Flip the egg.", "Eat the egg.")),

            new Recipe("Sinigang Pork Ribs", RecipeType.DINNER, 1,
            List.of(
                "2 lbs (900g) pork stew meat",
                "4 cups (1L) water, for cooking",
                "4 cups pork broth (saved from cooking)",
                "1-2 tomatoes",
                "1 thumb-sized piece ginger",
                "1 medium yellow onion",
                "1 large daikon radish",
                "1-2 packets Mama Sita's sinigang mix, to taste",
                "1 bunch of spinach",
                "1 bundle string beans",
                "1 tbsp salt, for initial boil",
                "Fish sauce"),
            List.of(
                "Cube the pork. Quarter the tomatoes and the onion, and dice the ginger.",
                "Slice the daikon into 1/4-inch rounds, cut the string beans into 2-inch pieces, and rinse the spinach.",
                "In a large pot, bring salted water to a boil over high heat.",
                "Add the pork and cook for 30 minutes to an hour, until the meat is tender. Remove any scum that rises to the top.",
                "Drain the cooked pork and save 4 cups of the broth.",
                "In the same pot, heat oil over medium heat. Sauté the tomatoes, ginger, and onions for 1-2 minutes until fragrant.",
                "Add the cooked pork, 4 cups of fresh water, and the daikon.",
                "Bring to a boil, then simmer for about 20-30 minutes.",
                "Stir in the sinigang mix packets to taste.",
                "Add the remaining vegetables and cook for 1-2 minutes until just tender.",
                "Remove from heat. Serve hot with steamed white rice."))
        ));
    }
}
