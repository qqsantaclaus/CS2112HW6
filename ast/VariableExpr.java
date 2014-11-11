package ast;

import interpret.InterpreterImpl;

/**
 * A representation of a variable expression: Variable_Name[Expression]
 *
 */
public class VariableExpr extends SingleChild implements Expression {
	
	Message msg;
	/**
	 * Create an AST representation of msg[e]
	 * @param msg a message of variable name that is one of nearby, ahead, random, smell and mem;
	 * @param e an expression that is child of this node.
	 */
	public VariableExpr(Message msg, Expression e){
		this.msg=msg;
		this.e=e;
	}

	public Message getMsg(){
		return msg;
	}
	
	public Expression getExpr(){
		return e;
	}
	@Override
	public String toString(){
		String out=this.msg.toString();
		switch (this.msg){
			case smell: return out;
			default: out+="[";
					 out+=this.e.toString();
					 out+="]";
					 return out;
		}
	}
	
	@Override
	public void prettyPrint(StringBuilder sb) {
		sb.append(this.msg.toString());
		switch (this.msg){
			case smell: return;
			default: sb.append("[");
					 sb.append(this.e.toString());
					 sb.append("]");
					 return;
		}
	}

	@Override
	public Node copy() {
		if (this.msg.toString()=="smell") return new VariableExpr(this.msg, null);
		return new VariableExpr(this.msg, (Expression)this.e.copy());
	}
	/**
	 * An enumeration of all possible variable names.
	 */
	public enum Message{
		nearby,ahead,random,smell,mem;
	}
	
	@Override
	public int accept(InterpreterImpl i) {
		return i.getVar(this);
	}
	
}
