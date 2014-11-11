package ast;

import java.util.Random;
/**
 * A mutation that transforms the given node into a randomly chosen node of the same kind, 
 * but its children remain the same.
 */
public class Transform implements Mutation{

	@Override
	public boolean equals(Mutation m) {
		return (m instanceof Transform);
	}
	/**
     * Apply this mutation to the given node.
     * @param n the node to be mutated.
     * @return true if the mutation is successful.
     */
	public boolean getMutated(Node n) {
		Random r=new Random();
		if (n instanceof Number){
			Random rt=new Random();
			int delta=Integer.MAX_VALUE/r.nextInt();
			switch (rt.nextInt(2)){
				case 0: ((Number) n).value+=delta; break;
				case 1: ((Number) n).value-=delta; break;
			}
			return true;
		}
		else if (n instanceof MessageNode){
			MessageNode.Message[] m=MessageNode.Message.values();
			int temp=r.nextInt(m.length);
			while (m[temp].equals(((MessageNode)n).msg)){
				temp=r.nextInt(m.length);
			}
			((MessageNode)n).msg=m[temp];
			return true;
		}
		else if (n instanceof BinaryExpr){
			BinaryExpr.Operator[] m=BinaryExpr.Operator.values();
			int temp=r.nextInt(m.length);
			while (m[temp].equals(((BinaryExpr)n).op)){
				temp=r.nextInt(m.length);
			}
			((BinaryExpr)n).op=m[temp];
			return true;
		}
		else if (n instanceof BinaryCondition){
			BinaryCondition.Operator[] m=BinaryCondition.Operator.values();
			int temp=r.nextInt(m.length);
			while (m[temp].equals(((BinaryCondition)n).op)){
				temp=r.nextInt(m.length);
			}
			((BinaryCondition)n).op=m[temp];
			return true;
		}
		else if (n instanceof BinaryRelation){
			BinaryRelation.Operator[] m=BinaryRelation.Operator.values();
			int temp=r.nextInt(m.length);
			while (m[temp].equals(((BinaryRelation)n).op)){
				temp=r.nextInt(m.length);
			}
			((BinaryRelation)n).op=m[temp];
			return true;
		}
		else if (n instanceof FunctionNode){
			FunctionNode.Message[] m=FunctionNode.Message.values();
			int temp=r.nextInt(m.length);
			while (m[temp].equals(((FunctionNode)n).msg)){
				temp=r.nextInt(m.length);
			}
			((FunctionNode)n).msg=m[temp];
			return true;
		}
		else if (n instanceof VariableExpr){
			if (((VariableExpr)n).msg.name().equals("smell")) return false;
			VariableExpr.Message[] m=VariableExpr.Message.values();
			int temp=r.nextInt(m.length);
			while (m[temp].equals(((VariableExpr)n).msg) || m[temp].name().equals("smell")){
				temp=r.nextInt(m.length);
			}
			((VariableExpr)n).msg=m[temp];
			return true;
		}
		return false;
	}

	@Override
	public boolean getMutated(Node n, Node p, Node root) {
		return getMutated(n);
	}

}
