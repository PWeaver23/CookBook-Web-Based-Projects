package com.cookbook.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Represents a recipe, with an id, title, recipe type, posted by id, list of ingredients, and list of steps.
// Stored in the MongoDB "recipes" collection. MongoDB creates the id (a String) when a recipe is first saved.
@Document(collection = "recipes")
public class Recipe {

    @Id
    private String id;

    private String title;
    private RecipeType recipeType;
    private Long postedById;
    private List<String> ingredients;
    private List<String> steps;

    public Recipe() { }

    // Used when creating a new recipe: no id yet, MongoDB assigns one on save.
    public Recipe(String title, RecipeType recipeType, long postedById, List<String> ingredients, List<String> steps) {
        this.title = title;
        this.recipeType = recipeType;
        this.postedById = postedById;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
