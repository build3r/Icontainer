package com.builder.icontainer.models;

import com.google.gson.annotations.Expose;

public class Nutrition {

@Expose
private String item;
@Expose
private float energy;
@Expose
private float protein;
@Expose
private float fat;
@Expose
private float carbs;
@Expose
private float sugar;
@Expose
private float kcal;

/**
* 
* @return
* The item
*/
public String getItem() {
return item;
}

/**
* 
* @param item
* The item
*/
public void setItem(String item) {
this.item = item;
}

/**
* 
* @return
* The energy
*/
public float getEnergy() {
return energy;
}

/**
* 
* @param energy
* The energy
*/
public void setEnergy(float energy) {
this.energy = energy;
}

/**
* 
* @return
* The protein
*/
public float getProtein() {
return protein;
}

/**
* 
* @param protein
* The protein
*/
public void setProtein(float protein) {
this.protein = protein;
}

/**
* 
* @return
* The fat
*/
public float getFat() {
return fat;
}

/**
* 
* @param fat
* The fat
*/
public void setFat(float fat) {
this.fat = fat;
}

/**
* 
* @return
* The carbs
*/
public float getCarbs() {
return carbs;
}

/**
* 
* @param carbs
* The carbs
*/
public void setCarbs(float carbs) {
this.carbs = carbs;
}

/**
* 
* @return
* The sugar
*/
public float getSugar() {
return sugar;
}

/**
* 
* @param sugar
* The sugar
*/
public void setSugar(float sugar) {
this.sugar = sugar;
}

/**
* 
* @return
* The kcal
*/
public float getKcal() {
return kcal;
}

/**
* 
* @param kcal
* The kcal
*/
public void setKcal(float kcal) {
this.kcal = kcal;
}

}
