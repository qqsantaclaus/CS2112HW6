package ast;

import interpret.InterpreterImpl;

/**
 * A representation of a binary arithmetic evaluation: 'plus', 'minus', 'divide', 'times' or 'mod'.
 */
public class BinaryExpr extends BinaryOperations<Expression> implements Expression {

	Operator op;
	/** 
	 * Create a AST representation of l op r
	 * @param l an Expression that is the left child of this node
	 * @param op an Operator that is one of plus, multiply, time, divide, mod。
	 * @param r an Expression that is the right child of this ode
	 */
	public BinaryExpr(Expression l, Operator op, Expression r){
		this.l=l;
		this.r=r;
		this.op=op;
	}

	@Override
	public void prettyPrint(StringBuilder sb) {
		sb.append("(");
		sb.append(this.l.toString());
		String oper="";
		switch (this.op){
			case PLUS: oper="+"; break;
			case MINUS: oper="-"; break;
			case TIMES: oper="*"; break;
			case DIVIDE: oper="/"; break;
			case MOD: oper=" mod "; break;
		}
		sb.append(oper);
		sb.append(this.r.toString());
		sb.append(")");
	}
	
	@Override
	public boolean isPossibleChild(Node n) {
		return (n instanceof Expression);
	}

	@Override
	public Node copy() {
		return new BinaryExpr((Expression) this.l.copy(), this.op, (Expression) this.r.copy());
	}
    /**
     * An enumeration of all possible binary arithmetic operators.
     */
	public enum Operator{
		PLUS,MINUS,TIMES,DIVIDE,MOD;
	}
	@Override
	public int accept(InterpreterImpl i) {
		int v=this.l.accept(i);
		int rvalue=r.accept(i);
		switch (this.op){		
		case PLUS: v+=rvalue; break;
		case MINUS: v-=rvalue; break;
		case TIMES: v*=rvalue; break;
		case DIVIDE: 
			if (rvalue!=0) v/=this.r.accept(i); else v=0; 
			break;
		case MOD: 
			if(rvalue!=0) v%=this.r.accept(i); else v=0;
			break;
		}
		return v;
	}


}
