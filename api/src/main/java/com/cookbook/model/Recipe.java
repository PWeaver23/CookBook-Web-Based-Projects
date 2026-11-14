package com.cookbook.model;

import java.util.List;

// Represents a recipe, with an id, title, recipe type, posted by id, list of ingredients, and list of steps.
public class Recipe {
    
    long id;

    String title;
    RecipeType recipeType;
    long postedById;
    List<String> ingredients;
    List<String> steps;

    public Recipe() { }

    public Recipe(long id, String title, RecipeType recipeType, long postedById, List<String> ingredients, List<String> steps) {
        this.id = id;
        this.title = title;
        this.recipeType = recipeType;
        this.postedById = postedById;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RecipeType getRecipeType() {
        return recipeType;
    }

    public void setRecipeType(RecipeType recipeType) {
        this.recipeType = recipeType;
    }

    public long getPostedById() {
        return postedById;
    }

    public void setPostedById(long postedById) {
        this.postedById = postedById;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }
}
