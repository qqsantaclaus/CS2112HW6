package ast;

import java.util.Random;
/**
 * A mutation that insert a node as the parent of the given node. 
 *
 */
public class Insert implements Mutation{

	@Override
	public boolean equals(Mutation m) {
		return (m instanceof Insert);
	}

	@Override
	public boolean getMutated(Node n, Node p, Node root) {
		if (n instanceof Condition){
			return BinaryCondInsert((Condition) n, p, root);
		}
		else if (n instanceof Expression){
			return ExpressionInsert((Expression)n, p, root);
		}
		return false;
	}
	/**
	 * Insert binary parent node when given node is of Condition type. 
	 * @param n Condition node to be mutated.
	 * @param p the original parent of {@code n}
	 * @param root the root of the entire AST tree.
	 * @return true if the insertion is successful
	 */
	public boolean BinaryCondInsert(Condition n, Node p, Node root){
		Random r=new Random();
		BinaryCondition.Operator op = null;
		switch (r.nextInt(2)){
			case 0: op=BinaryCondition.Operator.AND; break;
			case 1: op=BinaryCondition.Operator.OR; break;
		}
		BinaryCondition newBC=new BinaryCondition(n, op, null);
		Mutation m=MutationFactory.getReplace();
		if (m.getMutated(newBC.r, newBC, root)){
			p.replaceChild(n, newBC);
			return true;
		}
		return false;
	}
	/**
	 * Insert a binary parent node when given node is of Expression type. 
	 * @param n Expression node to be mutated.
	 * @param p the original parent of {@code n}
	 * @param root the root of the entire AST tree.
	 * @return true if the insertion is successful
	 */
	public boolean BinaryExprInsert(Expression e, Node p, Node root){
		Random r=new Random();
		BinaryExpr.Operator op=null;
		switch(r.nextInt(5)){
			case 0: op=BinaryExpr.Operator.PLUS; break;
			case 1: op=BinaryExpr.Operator.MINUS; break;
			case 2: op=BinaryExpr.Operator.TIMES; break;
			case 3: op=BinaryExpr.Operator.DIVIDE; break;
			case 4: op=BinaryExpr.Operator.MOD; break;
		}
		BinaryExpr newBE=new BinaryExpr(e, op, null);
		Mutation m=MutationFactory.getReplace();
		if (m.getMutated(newBE.r, newBE, root)){
			return p.replaceChild(e, newBE);
		}
		return false;
	}
	/**
	 * Insert parent node when given node is of Expression type. 
	 * @param n Expression node to be mutated.
	 * @param p the original parent of {@code n}
	 * @param root the root of the entire AST tree.
	 * @return true if the insertion is successful
	 */
	public boolean ExpressionInsert(Expression e, Node p, Node root){
		Random r=new Random();
		int c=r.nextInt(9);
		if (c<=4){
			if (BinaryExprInsert((Expression)e, p, root)) return true;
		}
		VariableExpr.Message msg=null;
		switch (c){
			case 5: msg=VariableExpr.Message.ahead; break;
			case 6: msg=VariableExpr.Message.mem; break;
			case 7: msg=VariableExpr.Message.nearby; break;
			case 8: msg=VariableExpr.Message.random; break;
		}
		VariableExpr newVE=new VariableExpr(msg, e);
		p.replaceChild(e, newVE);
		return true;
	}
}
