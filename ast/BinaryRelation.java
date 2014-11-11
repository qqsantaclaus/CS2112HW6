package ast;

import interpret.InterpreterImpl;

/**
 * A representation of a binary comparable relation: '>', '<', '=', '>=', '<=' or '!='.
 *
 */
public class BinaryRelation extends BinaryOperations<Expression> implements Condition {

	Operator op;
	/**
	 * Create a AST representation of l op r
	 * @param l an Expression that is the left child of this node
	 * @param op an Operator that is one of SMALLER, BIGGER, EQUAL, NOTEQUAL, SMALLEREQ, BIGGEREQ
	 * @param r an Expression that the right child of this node
	 */
	public BinaryRelation(Expression l, Operator op, Expression r){
		this.l=l;
		this.r=r;
		this.op=op;
	}
	
	@Override
	public void prettyPrint(StringBuilder sb) {
		sb.append(this.l.toString());
		String oper="";
		switch (this.op){
			case BIGGER: oper=" > "; break;
			case SMALLER: oper=" < "; break;
			case BIGGEREQ: oper=" >= "; break;
			case SMALLEREQ: oper=" <= "; break;
			case EQUAL: oper=" = "; break;
			case NOTEQUAL: oper=" != "; break;
		}
		sb.append(oper);
		sb.append(this.r.toString());
	}
	
	@Override
	public boolean isPossibleChild(Node n) {
		return (n instanceof Expression);
	}
	@Override
	public Node copy() {
		return new BinaryRelation((Expression)this.l.copy(), this.op, (Expression)this.r.copy());
	}
    /**
     * An enumeration of all possible binary comparison operators.
     */
	public enum Operator{
		SMALLER,BIGGER,EQUAL,NOTEQUAL,SMALLEREQ,BIGGEREQ;
	}
	@Override
	public boolean accept(InterpreterImpl i) {
		switch (this.op){
			case BIGGER: return this.l.accept(i) > this.r.accept(i);
			case SMALLER: return this.l.accept(i) < this.r.accept(i);
			case BIGGEREQ: return this.l.accept(i) >= this.r.accept(i);
			case SMALLEREQ: return this.l.accept(i) <= this.r.accept(i);
			case EQUAL: return this.l.accept(i) == this.r.accept(i);
			default: return this.l.accept(i) != this.r.accept(i);
		}
	}



}
