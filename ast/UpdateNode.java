package ast;

import interpret.InterpreterImpl;

/**
 * A representation of an update command.
 *
 */
public class UpdateNode extends BinaryOperations<Expression> implements Command{
	/**
	 * Create an AST representation of mem[l]:=r
	 * @param l an expression that is left child of this node
	 * @param e an expression that is right child of this node.
	 */
	public UpdateNode(Expression IndexExpr, Expression assign){
		this.l=IndexExpr;
		this.r=assign;
	}
	
	@Override
	public void prettyPrint(StringBuilder sb) {
		sb.append("mem[");
		sb.append(this.l.toString());
		sb.append("] := ");
		sb.append(this.r.toString());
		
	}

	@Override
	public boolean isPossibleChild(Node n) {
		return (n instanceof Expression);
	}


	@Override
	public Node copy() {
		return new UpdateNode((Expression)this.l.copy(), (Expression)this.r.copy());
	}


	public int[] accept(InterpreterImpl i) {
		return new int[]{l.accept(i), r.accept(i)};
	}

}
