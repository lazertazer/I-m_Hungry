package test;

import java.util.ArrayList;

import main.utilities.Ingredient;
import main.utilities.InstructionSet;
import main.utilities.Location;
import main.utilities.Recipe;
import main.utilities.RecipeIngredients;
import main.utilities.RecipeInstructions;
import main.utilities.Restaurant;
import main.utilities.Step;

public class GenerateTestUtil {
	public static Recipe getTestRecipe(long ID) {
		ArrayList<Step> steps = new ArrayList<Step>();
		steps.add(new Step(1, "Boil the water"));
		
		ArrayList<InstructionSet> inst_list = new ArrayList<InstructionSet>();
		inst_list.add(new InstructionSet("testSet", steps));
		
		RecipeInstructions inst = new RecipeInstructions(ID, inst_list);
		
		ArrayList<Ingredient> ingr_list = new ArrayList<Ingredient>();
		ingr_list.add(new Ingredient(ID, "onion", "1", "whole", "1 whole onion"));
		
		RecipeIngredients ingr = new RecipeIngredients(1, ingr_list);
		Recipe r = new Recipe(ID, "testRecipe", "testURL",
								5, 5, 10, (short)4, "testURL",
								(short)5, inst, ingr);
		return r;
	}
	public static Recipe getTestRecipePrepCook(long ID, int prepTime, int cookTime) {
		ArrayList<Step> steps = new ArrayList<Step>();
		steps.add(new Step(1, "Boil the water"));
		
		ArrayList<InstructionSet> inst_list = new ArrayList<InstructionSet>();
		inst_list.add(new InstructionSet("testSet", steps));
		
		RecipeInstructions inst = new RecipeInstructions(ID, inst_list);
		
		ArrayList<Ingredient> ingr_list = new ArrayList<Ingredient>();
		ingr_list.add(new Ingredient(ID, "onion", "1", "whole", "1 whole onion"));
		
		RecipeIngredients ingr = new RecipeIngredients(1, ingr_list);
		Recipe r = new Recipe(ID, "testRecipe", "testURL",
								prepTime, cookTime, prepTime+cookTime, (short)4, "testURL",
								(short)5, inst, ingr);
		return r;
	}
	public static Restaurant getTestRestaurant(long ID) {
		Location location = new Location("test address", "testLocality",
										"Los Angeles", 10.5, 20.5, "90007");
		
		Restaurant r = new Restaurant(ID, "testRestaurant", "testURL", "testURL",
										"(425) 895-8542", location, (float)2.5, (short)2);
		return r;
	}
	public static Restaurant getTestRestaurantLatLon(long ID, double lat, double lon) {
		Location location = new Location("test address", "testLocality",
										"Los Angeles", lat, lon, "90007");
		
		Restaurant r = new Restaurant(ID, "testRestaurant", "testURL", "testURL",
										"(425) 895-8542", location, (float)2.5, (short)2);
		return r;
	}
}