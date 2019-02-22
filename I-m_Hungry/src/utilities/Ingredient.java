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
		fixAmount();
	}
	private void fixAmount() {
		Float flt = Float.parseFloat(amount);
		int ipart = (int)(float)flt;
		float fpart = flt - ipart;
		String frac = "";
		if (fpart - 0.5 < 0.001) {
			frac = "1/2";
		}
		else if (fpart - 0.25 < 0.001) {
			frac = "1/4";
		}
		else if (fpart - 0.75 < 0.001) {
			frac = "3/4";
		}
		else if (fpart - 0.3333333 < 0.01) {
			frac = "1/3";
		}
		else if (fpart - 0.6666666 < 0.01) {
			frac = "2/3";
		}
		else if (fpart > 0.001) {
			frac += fpart;
		}
		
		if (ipart == 0) {
			amount = frac;
		}
		else if (fpart > 0.001) {
			amount = "" + ipart + " + " + frac;
		}
		else {
			amount = "" + ipart;
		}
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
