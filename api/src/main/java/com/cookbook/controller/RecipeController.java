package com.cookbook.controller;

import com.cookbook.data.RecipeRepository;
import com.cookbook.model.Recipe;
import com.cookbook.model.RecipeType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

//API endpoint for recipes, stored in the MongoDB "recipes" collection.
//Supports HTTP GET and POST requests for recipes, including searching by text, searching by id, and filtering by recipe type.
@RestController
@RequestMapping("api/recipes")
public class RecipeController {

    private final RecipeRepository recipes;

    public RecipeController(RecipeRepository recipes) {
        this.recipes = recipes;
    }

    // Represents the request body for creating a new recipe, with validation annotations to ensure required fields are present and valid.
    public record RecipeRequest(
        @NotBlank(message = "Title is required") String title,
        @NotNull(message = "Recipe type is required") RecipeType recipeType,
        // Long (not long) so a missing value arrives as null and gets the "Posted by is required" message
        @NotNull(message = "Posted by is required") @Positive(message = "Posted by is required") Long postedById,
        @NotNull(message = "At least one ingredient is required") List<String> ingredients,
        @NotNull(message = "At least one step is required") List<String> steps
    ) { }

    // GET /api/recipes?search=... returns all recipes, or those whose title contains the search text (case-insensitive)
    // GET /api/recipes returns all recipes
    @GetMapping
    public List<Recipe> getAll(@RequestParam(required = false) String search) {
        // @RequestParam(required = false): search is null when the URL has no ?search=
        if (search == null || search.isBlank()) {
            return recipes.findAll();
        }
        return recipes.findByTitleContainingIgnoreCase(search.trim());
    }

    // GET /api/recipes/{id} returns the recipe with that id, or 404 if not found
    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getOne(@PathVariable String id) {
        // @PathVariable: the {id} part of the URL. MongoDB ids look like /api/recipes/6745f1c2a9e4b83d2c1f0a7b
        return ResponseEntity.of(recipes.findById(id));   // 200 with the entry, or 404 if empty
    }

    // GET /api/recipes/category/{recipeType} returns all recipes of that type, e.g. /api/recipes/category/SNACK
    @GetMapping("/category/{recipeType}")
    public List<Recipe> getAllByCategory(@PathVariable RecipeType recipeType) {
        return recipes.findByRecipeType(recipeType);
    }

    // POST /api/recipes creates a new recipe, returning 201 Created with a Location header pointing at the new entry
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody RecipeRequest request) {
        Recipe created = recipes.save(new Recipe(request.title(), request.recipeType(), request.postedById(), request.ingredients(), request.steps()));
        // 201 Created, with a Location header pointing at the new entry
        return ResponseEntity.created(URI.create("/api/recipes/" + created.getId())).body(created);
    }
}
