package utilities;

public class Step {
	private int num;
	private String instruction;
	public Step(int num, String instruction) {
		this.num = num;
		this.instruction = instruction;
	}
	public int getStepNum() {
		return num;
	}
	public String getStep() {
		return instruction;
	}
}