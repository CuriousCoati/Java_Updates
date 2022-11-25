package de.breyer.java8;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

    public static List<Recipe> generateRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        Recipe recipe = new Recipe();
        recipe.getIngredients().add(new Ingredient("egg"));
        recipe.getIngredients().add(new Ingredient("salt"));
        recipes.add(recipe);

        recipe = new Recipe();
        recipe.getIngredients().add(new Ingredient("oatmeal"));
        recipe.getIngredients().add(new Ingredient("strawberries"));
        recipe.getIngredients().add(new Ingredient("honey"));
        recipe.getIngredients().add(new Ingredient("yoghurt"));
        recipes.add(recipe);

        recipe = new Recipe();
        recipe.getIngredients().add(new Ingredient("eggs"));
        recipe.getIngredients().add(new Ingredient("ham"));
        recipe.getIngredients().add(new Ingredient("cheese"));
        recipe.getIngredients().add(new Ingredient("chives"));
        recipe.getIngredients().add(new Ingredient("salt"));
        recipes.add(recipe);

        return recipes;
    }

    private final List<Ingredient> ingredients = new ArrayList<>();

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
}
