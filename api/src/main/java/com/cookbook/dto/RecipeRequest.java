// Represents the request body for creating a new recipe, with validation annotations to ensure required fields are present and valid.
package com.cookbook.dto;

import java.util.List;

import com.cookbook.model.RecipeType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RecipeRequest(
        @NotBlank(message = "Title is required") String title,
        @NotNull(message = "Recipe type is required") RecipeType recipeType,
        @NotNull(message = "Posted by is required") @Positive(message = "Posted by is required") Long postedById,
        @NotNull(message = "At least one ingredient is required") List<String> ingredients,
        @NotNull(message = "At least one step is required") List<String> steps
    ) { }