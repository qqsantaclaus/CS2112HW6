package ast;

import interpret.InterpreterImpl;

/**
 * 
 * A representation of integer value.
 */
public class Number extends NoChild implements Expression {
	int value;
	/**
	 * Create an AST representation of integer v
	 * @param v integer value of the node.
	 */
	public Number(int v){
		this.value=v;
	}
	
	@Override
	public void prettyPrint(StringBuilder sb) {
		sb.append(String.valueOf(this.value));
	}
	
	@Override
	public Node copy() {
		return new Number(this.value);
	}

	@Override
	public int accept(InterpreterImpl i) {
		return this.value;
	}


}
