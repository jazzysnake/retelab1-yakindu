package hu.bme.mit.yakindu.analysis.workhere;

import java.io.IOException;
import java.util.Scanner;

import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
// import hu.bme.mit.yakindu.analysis.RuntimeService;
// import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;



public class RunStatechart {
	private static String[] validInputs = {"start","white","black"};
	private static boolean isValidInput(String input) {
		for(int i = 0;i<validInputs.length;++i) {
			if(input.equals(validInputs[i])) {
				return true;
			}
		}
		return false;
	}
	public static void main(String[] args) throws IOException {
		ExampleStatemachine s = new ExampleStatemachine();
		s.setTimer(new TimerService());
		System.out.println("Registering ChessClock statemachine...");
		RuntimeService.getInstance().registerStatemachine(s, 200);
		System.out.println("Initializing...");
		s.init();
		s.enter();
		s.runCycle();
		System.out.println("Initialized with starting times:");
		print(s);
        Scanner in = new Scanner(System.in);
		System.out.println("Please input which event to fire ( start,white,black )");
		System.out.println("Or type exit to terminate the program!");
		String input = in.nextLine().toLowerCase();
		while(!input.equals("exit")) {
			if(!isValidInput(input)) {
				System.out.println("Invalid input");
				input = in.nextLine().toLowerCase();
				continue;
			}else if(input.equals("start")) {
				System.out.println("Start event raised!");
				s.raiseStart();
			}else if(input.equals("white")) {
				System.out.println("White event raised!");
				s.raiseWhite();
			}else if(input.equals("black")) {
				System.out.println("Black event raised!");
				s.raiseBlack();
			}
			s.runCycle();
			System.out.println("Remaining times:");
			print(s);
			input = in.nextLine().toLowerCase();
		}
	}

	public static void print(IExampleStatemachine s) {
		System.out.println("W = " + s.getSCInterface().getWhiteTime());
		System.out.println("B = " + s.getSCInterface().getBlackTime());
	}
}
