package hu.bme.mit.yakindu.analysis.workhere;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;
import org.yakindu.sct.model.stext.stext.EventDefinition;
import org.yakindu.sct.model.stext.stext.VariableDefinition;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}
	
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		NameGenerator nameGen = NameGenerator.getInstance();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		ArrayList<VariableDefinition> vars = new ArrayList<>();
		ArrayList<EventDefinition> events = new ArrayList<>();

		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof State) {
				State state = (State) content;
				System.out.println(state.getName());
				if(state.getOutgoingTransitions().size()==0) {
					System.out.println("State: "+state.getName()+ " no outgoing transitions!");
				}
				if(!state.getName().isEmpty()) {
					nameGen.saveAllocatedName(state);
				}else {
					String genName = nameGen.generateName(state);
					while(nameGen.nameIsAllocated(genName)) {
						genName=nameGen.generateName(state);
					}
					state.setName(genName);
					nameGen.saveAllocatedName(state);
					System.out.println("State with empty name found! suggested name: "+genName);
				}
				
			}
			if(content instanceof Transition) {
				Transition trs = (Transition)content;
				System.out.println(trs.getSource().getName()+" -> "+trs.getTarget().getName());
			}
			if(content instanceof EventDefinition) {
				EventDefinition event = (EventDefinition) content;
				System.out.println("Event: "+event.getName());
				events.add(event);
			}
			if(content instanceof VariableDefinition) {
				VariableDefinition var = (VariableDefinition) content;
				System.out.println("Variable: "+var.getName());
				vars.add(var);
			}
		}
		String str = 
				"				System.out.println(\"Start event raised!\");\r\n" + 
				"				s.raiseStart();\r\n" + 
				"			}else if(input.equals(\"white\")) {\r\n" + 
				"				System.out.println(\"White event raised!\");\r\n" + 
				"				s.raiseWhite();\r\n" + 
				"			}else if(input.equals(\"black\")) {\r\n" + 
				"				System.out.println(\"Black event raised!\");\r\n" + 
				"				s.raiseBlack();\r\n" + 
				"			}\r\n" + 
				"			s.runCycle();\r\n" + 
				"			System.out.println(\"Remaining times:\");\r\n" + 
				"			print(s);\r\n" + 
				"			input = in.nextLine().toLowerCase();\r\n" + 
				"		}\r\n" + 
				"	}";
		System.out.println("public class RunStatechart {\r\n");
		System.out.println("\tpublic static void print(IExampleStatemachines) {");
		for(VariableDefinition var:vars) {
			System.out.println("\t\tSystem.out.println(\"W = \" + s.getSCInterface().get"+var.getName()+"();");
		}
		for(EventDefinition event:events) {
			System.out.println("\t\tSystem.out.println(\"W = \" + s.getSCInterface().get"+event.getName()+"();");
		}
		System.out.println("\t}");
		System.out.print("\tprivate static String[] validInputs = {");
		for(EventDefinition event:events) {
			if(event==events.get(events.size()-1)) {
				System.out.print("\""+event.getName()+"\"");
			}else {
				System.out.print("\""+event.getName()+"\",");
			}
		}
		System.out.println("};\r\n");
		System.out.println("}");
		System.out.println(				"	private static boolean isValidInput(String input) {\r\n" + 
				"		for(int i = 0;i<validInputs.length;++i) {\r\n" + 
				"			if(input.equals(validInputs[i])) {\r\n" + 
				"				return true;\r\n" + 
				"			}\r\n" + 
				"		}\r\n" + 
				"		return false;\r\n" + 
				"	}\r\n");
		System.out.println(
				"	public static void main(String[] args) throws IOException {\r\n" + 
				"		ExampleStatemachine s = new ExampleStatemachine();\r\n" + 
				"		s.setTimer(new TimerService());\r\n" + 
				"		System.out.println(\"Registering statemachine...\");\r\n" + 
				"		RuntimeService.getInstance().registerStatemachine(s, 200);\r\n" + 
				"		System.out.println(\"Initializing...\");\r\n" + 
				"		s.init();\r\n" + 
				"		s.enter();\r\n" + 
				"		s.runCycle();\r\n" + 
				"		System.out.println(\"Initialized with starting times:\");\r\n" + 
				"		print(s);\r\n" + 
				"\t\tScanner in = new Scanner(System.in);\r\n" + 
				"		System.out.println(\"Please input which event to fire!\");\r\n" + 
				"		System.out.println(\"Or type exit to terminate the program!\");\r\n" + 
				"		String input = in.nextLine().toLowerCase();\r\n" + 
				"		while(!input.equals(\"exit\")) {\r\n" + 
				"			if(!isValidInput(input)) {\r\n" + 
				"				System.out.println(\"Invalid input\");\r\n" + 
				"				input = in.nextLine().toLowerCase();\r\n" + 
				"				continue;\r\n" + 
				"			}\r");
		for (int i = 0; i < events.size(); i++) {
			System.out.println("\t\t\telse if(input.equals(validInputs["+i+"]){");
			System.out.println("\t\t\t\tSystem.out.println(validInputs["+i+"]+ \"event raised!\");");
			System.out.println("\t\t\t\ts.raise"+events.get(i).getName()+"();");
			System.out.println("\t\t\t}");
		}
		System.out.println(
				"			s.runCycle();\r\n" + 
				"			System.out.println(\"StateMachine variables:\");\r\n" + 
				"			print(s);\r\n" + 
				"			input = in.nextLine().toLowerCase();\r\n" + 
				"		}");
		System.out.println("\t}");
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}
