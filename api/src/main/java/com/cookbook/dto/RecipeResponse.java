package com.cookbook.dto;

import java.util.List;

import com.cookbook.model.Recipe;
import com.cookbook.model.RecipeType;

public record RecipeResponse(String id, String title, RecipeType recipeType, Long postedById, List<String> ingredients, List<String> steps) {

    public static RecipeResponse from(Recipe recipe) {
        return new RecipeResponse(
            recipe.getId(),
            recipe.getTitle(),
            recipe.getRecipeType(),
            recipe.getPostedById(),
            recipe.getIngredients(),
            recipe.getSteps()
        );

    }
}