package ast;

import java.util.ArrayList;
import java.util.Random;

/**
 * A mutation that removes given node along with its descendants, and replace the vacancy
 * with one of the removed node's children if necessary. 
 */
public class Remove implements Mutation {

	@Override
	public boolean equals(Mutation m) {
		return (m instanceof Remove);
	}
	
	public boolean getMutated(Node n, Node p) {
		if (n instanceof Program) return false;
		if (p instanceof BinaryOperations || p instanceof SingleChild){
			Node temp=ReplaceByChild(n, p);
			if (temp==null) return false;
			return p.replaceChild(n, temp);
		}
		else if (p instanceof Rule && n==((Rule)p).cdn){
			return ((Rule)p).replaceChild(n, ReplaceByChild(n, p));
		}
		else {
			return DirectRemove(n, p);
		}
	}
	/**
	 * Directly remove node {@code n} from its parent {@code p}
	 * @param n node to be removed
	 * @param p parent node of {@code n}
	 * @return true if the removal is successful.
	 */
	private boolean DirectRemove(Node n, Node p) {
		if (p instanceof ProgramImpl){
			return ((ProgramImpl)p).removeChild(n);
		}
		if (p instanceof Rule){
			return ((Rule)p).removeChild(n);
		}
		return false;
	}
	
	/**
	 * Find one of node {@code n}'s children of the correct kind that can replace node {@code n} and its descendants.
	 * Requires: {@code n} is a child of {@code p} 
	 * @param n the node, along with its descendants, to be replaced
	 * @param p the parent node of {@code n}
	 * @return the node to replace {@code n}; if not exist, return null.
	 */
	public Node ReplaceByChild(Node n, Node p){
		//Special: if n is number, replace by a random number.
		if (n instanceof Number){
			Random r=new Random();
			return new Number(Integer.MAX_VALUE/r.nextInt());
		}
		ArrayList<Node> arr=n.getChildren();
		ArrayList<Node> choice=new ArrayList<Node>();
		if (p instanceof Rule && ((Rule)p).cdn==n){
			for (Node i:arr){					
				if (((Rule)p).isPossibleCond(i)) choice.add(i);
			}
		}
		else{
			for (Node i:arr){					
				if (p.isPossibleChild(i)) choice.add(i);
			}
		}
		if (choice.size()==0) return null;
		Random r=new Random();
		return choice.get(r.nextInt(choice.size()));
	}

	@Override
	public boolean getMutated(Node n, Node p, Node root) {
		return getMutated(n, p);
	}
}
