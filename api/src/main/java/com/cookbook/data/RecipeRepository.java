package com.cookbook.data;

import com.cookbook.model.Recipe;
import com.cookbook.model.RecipeType;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

// Reads and writes recipes in the MongoDB "recipes" collection.
// Spring Data writes the code for us: findAll(), findById(), save(), count(), etc. come from MongoRepository,
// and the methods below are built from their names (e.g. "TitleContainingIgnoreCase" = title contains text, any case).
public interface RecipeRepository extends MongoRepository<Recipe, String> {

    // Recipes whose title contains the search text, ignoring upper/lower case.
    List<Recipe> findByTitleContainingIgnoreCase(String searchText);

    // All recipes of the given type, e.g. RecipeType.SNACK
    List<Recipe> findByRecipeType(RecipeType recipeType);
}
