package ast;

import interpret.InterpreterImpl;

public class UnaryExpr extends SingleChild implements Expression {
    protected Operator op;

    public UnaryExpr(Operator op, Expression e) {
    	this.op = op;
        this.e = e;
    }

	@Override
	public void prettyPrint(StringBuilder sb) {
		sb.append("(");
		sb.append("-");
		sb.append(e.toString());
		sb.append(")");
	}

	@Override
	public Node copy() {
		return new UnaryExpr(this.op, (Expression)e.copy());
	}
    
    /**
     * An enumeration of all possible unary expression operators.
     */
    public enum Operator {
        NEGATE;
    }

	@Override
	public int accept(InterpreterImpl i) {
		return -1*this.e.accept(i);
	}


}

