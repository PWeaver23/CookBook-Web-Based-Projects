package com.cookbook.model;

// Represents the type of a recipe, e.g. BREAKFAST, LUNCH, DINNER, SNACK, DESSERT, BEVERAGE
public enum RecipeType {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner"),
    SNACK("Snack"),
    DESSERT("Dessert"),
    BEVERAGE("Beverage");

    private final String text;

    RecipeType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static RecipeType textToType(String text) {
        return switch (text) {
            case "Breakfast" -> BREAKFAST;
            case "Lunch" -> LUNCH;
            case "Dinner" -> DINNER;
            case "Dessert" -> DESSERT;
            case "Beverage" -> BEVERAGE;
            case "Snack" -> SNACK;
            default -> null;
        };
    }
}
