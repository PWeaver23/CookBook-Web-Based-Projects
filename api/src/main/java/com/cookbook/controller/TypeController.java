package com.cookbook.controller;

import com.cookbook.dto.TypeResponse;
import com.cookbook.model.RecipeType;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/types")
public class TypeController {

    @GetMapping
    public List<TypeResponse> getAll() {
        return Arrays.stream(RecipeType.values()).map(s -> new TypeResponse(s.getText())).collect(Collectors.toList());
    }
    
}
