package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.utilities.Step;

public class StepTest {
	private Step step;
	
	@Before
	public void setUp() {
		step = new Step(1, "Bring the pot of water to a boil");
	}

	@Test
	public void testGetStepNum() {
		assertEquals(step.getStepNum(), 1);
	}

	@Test
	public void testGetStep() {
		assertTrue(step.getStep().equals("Bring the pot of water to a boil"));
	}
}