package utilities;

public class Recipe extends Item {
	private long ID;
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
		this.ID = ID;
		this.name = name;
		this.sourceURL = sourceURL;
		this.prepMinutes = prepMinutes;
		this.cookMinutes = cookMinutes;
		this.totalMinutes = totalMinutes;
		this.servings = servings;
		this.imageURL = imageURL;
		this.score = score;
		this.instructions = inst;
		this.ingredients = ingr;
		setType("recipe");
	}

	public long getID() {
		return ID;
	}
	public String getName() {
		return name;
	}
	public String getSourceURL() {
		return sourceURL;
	}
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
