package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import main.utilities.InstructionSet;
import main.utilities.RecipeInstructions;
import main.utilities.Step;

public class RecipeInstructionsTest {
	private RecipeInstructions RI;

	@Before
	public void setUp() {
		ArrayList<Step> steps = new ArrayList<Step>();
		ArrayList<InstructionSet> instructions = new ArrayList<InstructionSet>();
		steps.add(new Step(1, "Boil the water"));
		instructions.add(new InstructionSet("testSet", steps));
		RI = new RecipeInstructions((long)5, instructions);
		new RecipeInstructions((long)6);
	}
	
	@Test
	public void testGetRecipeID() {
		assertEquals(5, RI.getRecipeID());
	}

	@Test
	public void testGetInstructionSets() {
		assertTrue(RI.getInstructionSets().get(0).getName().equals("testSet"));
	}

	@Test
	public void testGetInstructionSet() {
		assertTrue(RI.getInstructionSet(0).getName().equals("testSet"));
	}

	@Test
	public void testAddInstructionSet() {
		InstructionSet inst = new InstructionSet("testSet2");
		RI.addInstructionSet(inst);
		assertTrue(RI.getInstructionSet(1).getName().equals("testSet2"));
	}
}