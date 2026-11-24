// Represents the request body for creating a new recipe, with validation annotations to ensure required fields are present and valid.
package com.cookbook.dto;

import java.util.List;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// {"title": "Spaghetti", "recipeType": "DINNER", "postedById": 2, "ingredients", ["pasta", "water"], "steps": ["boil", "eat"]}
public record RecipeRequest(
        @NotBlank(message = "Title is required") String title,
        @NotNull(message = "Recipe type is required") String recipeType,
        @NotNull(message = "Posted by is required") @Positive(message = "Posted by is required") Long postedById,
        @NotNull(message = "At least one ingredient is required") List<String> ingredients,
        @NotNull(message = "At least one step is required") List<String> steps
    ) { }