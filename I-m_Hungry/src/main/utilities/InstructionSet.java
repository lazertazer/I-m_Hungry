package main.utilities;

import java.util.ArrayList;

public class InstructionSet {
	private String name;
	private ArrayList<Step> steps;
	public InstructionSet(String name, ArrayList<Step> steps) {
		this.name = name;
		this.steps = steps;
	}
	public InstructionSet(String name) {
		this.name = name;
		this.steps = new ArrayList<Step>();
	}
	public String getName() {
		return name;
	}
	public ArrayList<Step> getSteps() {
		return steps;
	}
	public Step getStep(int stepIndex) {
		return steps.get(stepIndex);
	}
	public void addStep(Step s) {
		steps.add(s);
	}
}