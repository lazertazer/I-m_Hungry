package main.utilities;

import java.io.Serializable;

public class Recipe extends Item implements Serializable {
	private static final long serialVersionUID = 4441727804406141516L;
	private String name;
	private String sourceURL;
	private int prepMinutes;
	private int cookMinutes;
	private int totalMinutes;
	private short servings;
	private String imageURL;
	private short score;
	private RecipeInstructions instructions;
	private RecipeIngredients ingredients;
	
	public Recipe(long ID, String name, String sourceURL, int prepMinutes,
				int cookMinutes, int totalMinutes, short servings, String imageURL,
				short score, RecipeInstructions inst, RecipeIngredients ingr) {
		setID(ID);
		this.name = name;
		this.sourceURL = sourceURL;
		this.prepMinutes = prepMinutes;
		this.cookMinutes = cookMinutes;
		this.totalMinutes = totalMinutes;
		this.servings = servings;
		this.imageURL = imageURL;
		this.score = score;
		//Helper classes contain instructions and ingredients
		this.instructions = inst;
		this.ingredients = ingr;
		setType("recipe");
	}
	public String getName() {
		return name;
	}
	public String getSourceURL() {
		return sourceURL;
	}
	//When API doesn't provide prep/cook minutes, total time is used
	public boolean useTotalMinutes() {
		return (prepMinutes == 0 || cookMinutes == 0);
	}
	public int getTotalMinutes() {
		return totalMinutes;
	}
	public int getPrepMinutes() {
		return prepMinutes;
	}
	public int getCookMinutes() {
		return cookMinutes;
	}
	public short getServings() {
		return servings;
	}
	public String getImageURL() {
		return imageURL;
	}
	public short getScore() {
		return score;
	}
	public RecipeInstructions getInstructions() {
		return instructions;
	}
	public RecipeIngredients getIngredientInfo() {
		return ingredients;
	}
}