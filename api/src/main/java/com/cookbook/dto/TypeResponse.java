package com.cookbook.dto;

import com.cookbook.model.RecipeType;

/** A type as the API shows it: {"typeString": "Breakfast"}. */
public record TypeResponse(String type) {
    
    public static TypeResponse from(RecipeType type) {
        return new TypeResponse(type.getText());
    }
}
