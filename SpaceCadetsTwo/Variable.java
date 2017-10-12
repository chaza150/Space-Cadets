
public class Variable{
	
	String varName;
	int value;
	
	public Variable(String varName) {
		this.value = 0;
		this.varName = varName;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Variable)
		{
			if(((Variable) o).varName.equals(this.varName)) {
				return true;
			}
		}
		return false;
	}
}
