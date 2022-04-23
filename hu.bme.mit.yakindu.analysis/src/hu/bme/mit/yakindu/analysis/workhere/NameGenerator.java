package hu.bme.mit.yakindu.analysis.workhere;

import java.util.HashMap;

import org.yakindu.sct.model.sgraph.State;


public class NameGenerator {
	// Singleton
	private NameGenerator() {}
	private static NameGenerator instance = null;
	public static NameGenerator getInstance() {
		if(instance == null) {
			instance = new NameGenerator();
		}
		return instance;
	}
	
	//Name Generation
	private HashMap<String,State> names = new HashMap();
	private static long uid = 0;
	public String generateName(State stateToName) {
		String name = "DefaultStateName"+uid;
		uid++;
		return name;
	}
	
	public void saveAllocatedName(State state) {
		names.put(state.getName(), state);
	}
	
	public boolean nameIsAllocated(String name) {
		return names.containsKey(name);
	}
	

}
