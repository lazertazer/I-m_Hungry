package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import main.utilities.InstructionSet;
import main.utilities.Step;

public class InstructionSetTest {
	private InstructionSet instructions;
	
	@Before
	public void setUp() {
		new InstructionSet("nameOnlyConstructor");
		ArrayList<Step> steps = new ArrayList<Step>();
		steps.add(new Step(1, "Turn on the stove"));
		instructions = new InstructionSet("testInstructions", steps);
	}

	@Test
	public void testGetName() {
		assertTrue(instructions.getName().equals("testInstructions"));
	}

	@Test
	public void testGetSteps() {
		assertEquals(instructions.getSteps().get(0).getStepNum(), 1);
	}

	@Test
	public void testGetStep() {
		assertEquals(instructions.getStep(0).getStepNum(), 1);
	}

	@Test
	public void testAddStep() {
		Step s = new Step(2, "Mix the ingredients");
		instructions.addStep(s);
		assertEquals(instructions.getStep(1).getStepNum(), 2);
	}
}