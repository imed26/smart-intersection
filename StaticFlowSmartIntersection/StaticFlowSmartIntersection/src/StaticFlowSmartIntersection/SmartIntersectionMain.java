package StaticFlowSmartIntersection;

import java.util.concurrent.atomic.*;

public class SmartIntersectionMain {
	public static int approachNumberPerLine = 3;
	public static int flowCarNumber = 5;
	public static int intersectionLineNumber = 4;
	public static int carNumber = (intersectionLineNumber * approachNumberPerLine * flowCarNumber);
	static AtomicInteger commitmentNumber;
	static AtomicInteger leavedCarsNumber;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String [] command = new String[3];
		String argument ="";
		int approachNumber = intersectionLineNumber * approachNumberPerLine;
		commitmentNumber = new AtomicInteger(0);
		leavedCarsNumber = new AtomicInteger(0);
		argument = argument+"FlowSimulationAgent:StaticFlowSmartIntersection.FlowSimulationAgent(FSA);";
		argument = argument+"IntersectionAgent:StaticFlowSmartIntersection.IntersectionAgent(IA);";
		for (int i = 1; i < (approachNumber+1); i++) {
			for (int j = 0; j < flowCarNumber; j++) {
				argument = argument + "C"+ i +"-"+ j + ":StaticFlowSmartIntersection.CarAgent(car);";
			}
		}// action
		// System.out.println(argument);

	    command [0]="-cp";
		command [1]="jade.boot";
		command [2]= argument;
		jade.Boot.main(command);
	}
}
