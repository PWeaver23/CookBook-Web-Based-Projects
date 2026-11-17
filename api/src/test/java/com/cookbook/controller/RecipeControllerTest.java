package com.cookbook.controller;

import com.cookbook.data.RecipeRepository;
import com.cookbook.model.Recipe;
import com.cookbook.model.RecipeType;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for the 4 recipe endpoints in RecipeController.
 *
 * @WebMvcTest starts only the web layer (this controller + ApiErrors), not the whole app.
 * MockMvc sends fake HTTP requests to it, and @MockitoBean swaps the real MongoDB
 * RecipeRepository for a mock, so these tests run fast and do NOT need MongoDB running.
 * Each test tells the mock what to return ("when ... thenReturn ..."), calls an endpoint,
 * and checks the HTTP status and JSON that come back.
 */
@WebMvcTest(RecipeController.class)
class RecipeControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    RecipeRepository recipes;

    // Builds a recipe that looks like one loaded from MongoDB (it has an id).
    private static Recipe recipe(String id, String title, RecipeType type) {
        Recipe recipe = new Recipe(title, type, 1, List.of("1 egg", "1 pinch of salt"), List.of("Crack the egg.", "Add the salt."));
        recipe.setId(id);
        return recipe;
    }

    // ---------------------------------------------------------------------
    // 1. GET /api/recipes  (all recipes, or ?search=text)
    // ---------------------------------------------------------------------
    @Nested
    class GetAllRecipes {

        @Test
        void returnsEveryRecipe() throws Exception {
            when(recipes.findAll()).thenReturn(List.of(
                recipe("a1", "Salted Flour", RecipeType.SNACK),
                recipe("b2", "Salted egg", RecipeType.SNACK)));

            mvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("a1"))
                .andExpect(jsonPath("$[0].title").value("Salted Flour"))
                .andExpect(jsonPath("$[0].recipeType").value("SNACK"))
                .andExpect(jsonPath("$[0].ingredients", hasSize(2)))
                .andExpect(jsonPath("$[0].steps", hasSize(2)))
                .andExpect(jsonPath("$[1].title").value("Salted egg"));
        }

        @Test
        void returnsEmptyListWhenThereAreNoRecipes() throws Exception {
            when(recipes.findAll()).thenReturn(List.of());

            mvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        }

        @Test
        void searchReturnsOnlyMatchingRecipes() throws Exception {
            when(recipes.findByTitleContainingIgnoreCase("egg")).thenReturn(List.of(
                recipe("b2", "Salted egg", RecipeType.SNACK)));

            mvc.perform(get("/api/recipes").param("search", "egg"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Salted egg"));

            verify(recipes, never()).findAll();
        }

        @Test
        void searchTextIsTrimmedBeforeSearching() throws Exception {
            when(recipes.findByTitleContainingIgnoreCase("egg")).thenReturn(List.of());

            mvc.perform(get("/api/recipes").param("search", "  egg  "))
                .andExpect(status().isOk());

            verify(recipes).findByTitleContainingIgnoreCase("egg");
        }

        @Test
        void blankSearchReturnsEveryRecipe() throws Exception {
            when(recipes.findAll()).thenReturn(List.of(recipe("a1", "Salted Flour", RecipeType.SNACK)));

            mvc.perform(get("/api/recipes").param("search", "   "))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

            verify(recipes, never()).findByTitleContainingIgnoreCase(anyString());
        }
    }

    // ---------------------------------------------------------------------
    // 2. GET /api/recipes/{id}  (one recipe)
    // ---------------------------------------------------------------------
    @Nested
    class GetRecipeById {

        @Test
        void returnsTheRecipeWhenItExists() throws Exception {
            when(recipes.findById("6af93bea3e1938f7e77987fd"))
                .thenReturn(Optional.of(recipe("6af93bea3e1938f7e77987fd", "Salted egg", RecipeType.SNACK)));

            mvc.perform(get("/api/recipes/6af93bea3e1938f7e77987fd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("6af93bea3e1938f7e77987fd"))
                .andExpect(jsonPath("$.title").value("Salted egg"))
                .andExpect(jsonPath("$.ingredients[0]").value("1 egg"))
                .andExpect(jsonPath("$.steps[1]").value("Add the salt."));
        }

        @Test
        void returns404WhenTheRecipeDoesNotExist() throws Exception {
            when(recipes.findById("missing")).thenReturn(Optional.empty());

            mvc.perform(get("/api/recipes/missing"))
                .andExpect(status().isNotFound());
        }
    }

    // ---------------------------------------------------------------------
    // 3. GET /api/recipes/category/{recipeType}  (recipes of one type)
    // ---------------------------------------------------------------------
    @Nested
    class GetRecipesByCategory {

        @Test
        void returnsRecipesOfThatType() throws Exception {
            when(recipes.findByRecipeType(RecipeType.BREAKFAST)).thenReturn(List.of(
                recipe("c3", "Buttered Toast", RecipeType.BREAKFAST)));

            mvc.perform(get("/api/recipes/category/BREAKFAST"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Buttered Toast"))
                .andExpect(jsonPath("$[0].recipeType").value("BREAKFAST"));
        }

        @Test
        void returnsEmptyListWhenNoRecipesHaveThatType() throws Exception {
            when(recipes.findByRecipeType(RecipeType.DESSERT)).thenReturn(List.of());

            mvc.perform(get("/api/recipes/category/DESSERT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        }

        @Test
        void returns400ForAnUnknownType() throws Exception {
            mvc.perform(get("/api/recipes/category/PIZZA"))
                .andExpect(status().isBadRequest());

            verify(recipes, never()).findByRecipeType(any());
        }
    }

    // ---------------------------------------------------------------------
    // 4. POST /api/recipes  (create a recipe)
    // ---------------------------------------------------------------------
    @Nested
    class CreateRecipe {

        // MongoDB sets the id when a recipe is saved; the mock does the same.
        private void saveAssignsId(String id) {
            when(recipes.save(any(Recipe.class))).thenAnswer(call -> {
                Recipe saved = call.getArgument(0);
                saved.setId(id);
                return saved;
            });
        }

        @Test
        void savesTheRecipeAndReturns201WithLocation() throws Exception {
            saveAssignsId("new123");

            mvc.perform(post("/api/recipes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                          "title": "Buttered Toast",
                          "recipeType": "BREAKFAST",
                          "postedById": 1,
                          "ingredients": ["1 slice bread", "1 Tbsp butter"],
                          "steps": ["Toast the bread.", "Spread the butter."]
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/recipes/new123"))
                .andExpect(jsonPath("$.id").value("new123"))
                .andExpect(jsonPath("$.title").value("Buttered Toast"));

            // Check exactly what was handed to the database.
            ArgumentCaptor<Recipe> saved = ArgumentCaptor.forClass(Recipe.class);
            verify(recipes).save(saved.capture());
            assertThat(saved.getValue().getTitle()).isEqualTo("Buttered Toast");
            assertThat(saved.getValue().getRecipeType()).isEqualTo(RecipeType.BREAKFAST);
            assertThat(saved.getValue().getPostedById()).isEqualTo(1);
            assertThat(saved.getValue().getIngredients()).containsExactly("1 slice bread", "1 Tbsp butter");
            assertThat(saved.getValue().getSteps()).containsExactly("Toast the bread.", "Spread the butter.");
        }

        @Test
        void returns400WhenTitleIsMissing() throws Exception {
            mvc.perform(post("/api/recipes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {"recipeType": "SNACK", "postedById": 1, "ingredients": ["salt"], "steps": ["Eat."]}
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Title is required"));

            verify(recipes, never()).save(any());
        }

        @Test
        void returns400WhenRecipeTypeIsMissing() throws Exception {
            mvc.perform(post("/api/recipes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {"title": "Salt", "postedById": 1, "ingredients": ["salt"], "steps": ["Eat."]}
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Recipe type is required"));

            verify(recipes, never()).save(any());
        }

        @Test
        void returns400WhenPostedByIdIsNotPositive() throws Exception {
            mvc.perform(post("/api/recipes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {"title": "Salt", "recipeType": "SNACK", "postedById": 0, "ingredients": ["salt"], "steps": ["Eat."]}
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Posted by is required"));

            verify(recipes, never()).save(any());
        }

        @Test
        void returns400WhenPostedByIdIsMissing() throws Exception {
            mvc.perform(post("/api/recipes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {"title": "Salt", "recipeType": "SNACK", "ingredients": ["salt"], "steps": ["Eat."]}
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Posted by is required"));

            verify(recipes, never()).save(any());
        }

        @Test
        void returns400WhenIngredientsOrStepsAreMissing() throws Exception {
            mvc.perform(post("/api/recipes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {"title": "Salt", "recipeType": "SNACK", "postedById": 1}
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("At least one ingredient is required")))
                .andExpect(jsonPath("$.error", containsString("At least one step is required")));

            verify(recipes, never()).save(any());
        }

        @Test
        void returns400ForAnUnknownRecipeType() throws Exception {
            mvc.perform(post("/api/recipes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {"title": "Pizza", "recipeType": "PIZZA", "postedById": 1, "ingredients": ["dough"], "steps": ["Bake."]}
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("The request body isn't valid JSON for this endpoint"));

            verify(recipes, never()).save(any());
        }

        @Test
        void returns400ForBrokenJson() throws Exception {
            mvc.perform(post("/api/recipes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ this is not json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("The request body isn't valid JSON for this endpoint"));

            verify(recipes, never()).save(any());
        }
    }
}
