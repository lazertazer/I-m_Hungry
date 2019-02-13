package utilities;

public class Ingredient {
	private long ingredientID;
	private String name;
	private String amount;
	private String unit;
	private String originalString;
	public Ingredient(long ingredientID, String name, String amount, String unit, String original) {
		this.ingredientID = ingredientID;
		this.name = name;
		this.amount = amount;
		this.unit = unit;
		this.originalString = original;
	}
	public long getIngredientID() {
		return ingredientID;
	}
	public String getName() {
		return name;
	}
	public String getAmount() {
		return amount;
	}
	public String getUnit() {
		return unit;
	}
	public String getOriginalString() {
		return originalString;
	}
}
