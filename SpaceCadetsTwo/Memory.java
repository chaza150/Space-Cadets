import java.util.*;

public class Memory {
	
	ArrayList<Variable> memory = new ArrayList<Variable>();
	
	//Check if memory already contains variable of same name.
	public boolean containsVarName(String varName) {
		
		for(int i = 1; i <= memory.size(); i++) {
			if(memory.get(i-1).equals(new Variable(varName))) {
				return true;
			}
		}
		return false;
	}
	
	//find the variable's index in memory
	public int fetchVarIndex(String varName) {
		for(int i = 1; i <= memory.size(); i++) {
			if(memory.get(i-1).equals(new Variable(varName))) {
				return i-1;
			}
		}
		return 0;
	}
	
	//Get a variable's value
	public int getVarValue(String varName) {
		return memory.get(this.fetchVarIndex(varName)).value;
	}
	
	public void setVarValue(String varName, int varValue) {
		if(!this.containsVarName(varName)) {
			memory.add(new Variable(varName));
			System.out.println("First instance of \"" + varName + "\" created.");
			memory.get(this.fetchVarIndex(varName)).value = varValue;
			System.out.println("Variable \"" + varName + "\" = " + memory.get(this.fetchVarIndex(varName)).value);
		} else {
			memory.get(this.fetchVarIndex(varName)).value = varValue;
			System.out.println("Variable \"" + varName + "\" = " + memory.get(this.fetchVarIndex(varName)).value);
		}
	}
	
	
	//Add variable to memory if not already existing and set to zero.
	public void clear(String varName) {
		if(!this.containsVarName(varName)) {
			memory.add(new Variable(varName));
			System.out.println("First instance of \"" + varName + "\" created.");
		} else {
			memory.get(this.fetchVarIndex(varName)).value = 0;
			System.out.println("Variable \"" + varName + "\" has been cleared.");
		}
	}
	
	//Increment Variable
	public void incr(String varName) {
		if(!this.containsVarName(varName)) {
			memory.add(new Variable(varName));
			System.out.println("First instance of \"" + varName + "\" created.");
			memory.get(this.fetchVarIndex(varName)).value++;
			System.out.println("Variable \"" + varName + "\" = " + memory.get(this.fetchVarIndex(varName)).value);
		} else {
			memory.get(this.fetchVarIndex(varName)).value++;
			System.out.println("Variable \"" + varName + "\" = " + memory.get(this.fetchVarIndex(varName)).value);
		}
	}

	//Decrement Variable
	public void decr(String varName) {
		if(!this.containsVarName(varName)) {
			memory.add(new Variable(varName));
			System.out.println("First instance of \"" + varName + "\" created.");
			memory.get(this.fetchVarIndex(varName)).value--;
			System.out.println("Variable \"" + varName + "\" = " + memory.get(this.fetchVarIndex(varName)).value);
		} else {
			memory.get(this.fetchVarIndex(varName)).value--;
			System.out.println("Variable \"" + varName + "\" = " + memory.get(this.fetchVarIndex(varName)).value);
		}
	}
	
}
