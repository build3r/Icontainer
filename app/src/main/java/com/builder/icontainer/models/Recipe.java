package com.builder.icontainer.models;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;


public class Recipe {

@Expose
private long recipeId;
@Expose
private String recipeName;
@Expose
private List<String> ingredients = new ArrayList<String>();

/**
* 
* @return
* The recipeId
*/
public long getRecipeId() {
return recipeId;
}

/**
* 
* @param recipeId
* The recipeId
*/
public void setRecipeId(long recipeId) {
this.recipeId = recipeId;
}

/**
* 
* @return
* The recipeName
*/
public String getRecipeName() {
return recipeName;
}

/**
* 
* @param recipeName
* The recipeName
*/
public void setRecipeName(String recipeName) {
this.recipeName = recipeName;
}

/**
* 
* @return
* The ingredients
*/
public List<String> getIngredients() {
return ingredients;
}

/**
* 
* @param ingredients
* The ingredients
*/
public void setIngredients(List<String> ingredients) {
this.ingredients = ingredients;
}

}
