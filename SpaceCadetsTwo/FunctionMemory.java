import java.util.ArrayList;

public class FunctionMemory {

	ArrayList<Function> fMemory = new ArrayList<Function>();
	
	public int getFunctionStart(String functionName) {
		return fMemory.get(this.getFunctionIndex(functionName)).startLine;
	}
	
	public void newFunction(String name, int start, int end) {
		Function newf = new Function(start, end, name);
		fMemory.add(newf);
	}
	
	public int getFunctionEnd(String functionName) {
		return fMemory.get(this.getFunctionIndex(functionName)).endLine;
	}
	
	public int getFunctionIndex(String name) {
		for(int i = 0; i < fMemory.size(); i++) {
			if(fMemory.get(i).functionName.equals(name)) {
				return i;
			}
		}
		System.out.println("Tried to retrieve non-existent function");
		System.exit(0);
		return -1;
	}
	
	public boolean contains(String name) {
		boolean contained = false;
		for(int i = 0; i < fMemory.size(); i++) {
			if(fMemory.get(i).functionName.equals(name)) {
				contained = true;
			}
		}
		return contained;
	}
	
	public void run(TextReader tr, String name) {
		tr.runLines(this.getFunctionStart(name), this.getFunctionEnd(name));
	}
}
