package utilities;

import java.util.ArrayList;

public class RecipeInstructions {
	private long recipeID;
	private ArrayList<InstructionSet> instructions;
	
	public RecipeInstructions(long recipeID, ArrayList<InstructionSet> instructions) {
		this.recipeID = recipeID;
		this.instructions = instructions;
	}
	public RecipeInstructions(long recipeID) {
		this.recipeID = recipeID;
		this.instructions = new ArrayList<InstructionSet>();
	}
	public void addInstructionSet(InstructionSet is) {
		instructions.add(is);
	}
	public long getRecipeID() {
		return recipeID;
	}
	public ArrayList<InstructionSet> getInstructionSets() {
		return instructions;
	}
	public InstructionSet getInstructionSet(int instructionSetIndex) {
		return instructions.get(instructionSetIndex);
	}
}