package ast;

import interpret.InterpreterImpl;

/**
 * A representation of a command that takes in expression.
 */
public class FunctionNode extends SingleChild implements Action {

	Message msg;
	/**
	 * Create an AST representation of Command_Word[Expression]
	 * @param msg an command word that is one of tag or serve.
	 * @param e an expression that is child of this node.
	 */
	public FunctionNode(Message msg, Expression e){
		this.msg=msg;
		this.e=e;
	}
	
	@Override
	public void prettyPrint(StringBuilder sb) {
		sb.append(this.msg.toString());
		sb.append("[");
		sb.append(this.e.toString());
		sb.append("]");
	}

	@Override
	public boolean isPossibleChild(Node n) {
		return (n instanceof Expression);
	}

	@Override
	public Node copy() {
		return new FunctionNode(this.msg, (Expression) this.e.copy());
	}
	/**
	 * An enumeration of all possible function command words.
	 */
	public enum Message{
		tag, serve;
	}
	
	@Override
	public String getMsg(){
		if(msg.equals(Message.tag)) return "tag";
		return "serve";
	}
	
	public int accept(InterpreterImpl i) {
		return e.accept(i);
		
	}
}
