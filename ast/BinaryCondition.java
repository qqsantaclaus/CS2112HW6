package ast;

import interpret.InterpreterImpl;

/**
 * A representation of a binary Boolean condition: 'and' or 'or'
 *
 */
public class BinaryCondition extends BinaryOperations<Condition> implements Condition  {
    Operator op;
	/**
     * Create an AST representation of l op r.
     * @param l a Condition that is the left child of this node
     * @param op an Operator that is one of OR and AND
     * @param r a Condition that is the right child of this node
     */
    public BinaryCondition(Condition l, Operator op, Condition r) {
    	this.l=l;
    	this.r=r;
    	this.op=op;
    }
    
    @Override
    public void prettyPrint(StringBuilder sb) {
    	sb.append("{");
    	sb.append(this.l.toString());
    	sb.append(" ");
    	sb.append(this.op.toString().toLowerCase());
    	sb.append(" ");
    	sb.append(this.r.toString());
    	sb.append("}");
    }

	@Override
	public boolean isPossibleChild(Node n) {
		return (n instanceof Condition);
	}


	@Override
	public Node copy() {
		return new BinaryCondition((Condition)this.l.copy(), this.op, (Condition)this.r.copy());
	}
	
    /**
     * An enumeration of all possible binary condition operators.
     */
    public enum Operator {
        OR, AND;
    }

	@Override
	public boolean accept(InterpreterImpl i) {
		switch (this.op){
			case OR: return (this.l.accept(i) || this.r.accept(i));  
			default: return (this.l.accept(i) && this.r.accept(i));  
		}
	}


}
