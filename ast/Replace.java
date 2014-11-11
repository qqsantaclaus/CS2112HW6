package ast;

import java.util.ArrayList;
import java.util.Random;
/**
 * A mutation that replace the given node and its children 
 * with a copy of another randomly selected node of the right kind, 
 * found somewhere in the entire AST tree.
 */
public class Replace implements Mutation{

	@Override
	public boolean equals(Mutation m) {
		return m instanceof Replace;
	}

	public boolean getMutated(Node n, Node p, Node root) {
		if (n instanceof Program) return false;
		ArrayList<Node> rightList=new ArrayList<Node>();
		if (p instanceof Rule){
			if (((Rule) p).cdn==n){
				findRightKindForCond(rightList, (Rule)p, root);
			}
			else if (((Rule)p).action==n || (!((Rule)p).containsAction() && n==((Rule)p).updates.get(((Rule)p).updates.size()-1))){
				findRightKind(rightList, p, root);
			}
			else {
				findRightKindWithoutAction(rightList, (Rule)p, root);
			}
		}
		else {
			findRightKind(rightList, p, root);
		}
		rightList.remove(n);
		if (rightList.size()==0){
			return false;
		}
		Random r=new Random();
		Node newTree=rightList.get(r.nextInt(rightList.size())).copy();
		return p.replaceChild(n, newTree);
	}
	/**
	 * Find a list of nodes in the entire AST that can be {@code p}'s child nodes.
	 * Especially, it find possible command nodes when p is Rule. 
	 * @param rightList an array list to contain possible children nodes in the entire AST
	 * @param p the node whose possible children nodes to be found.
	 * @param root the root of the entire AST.
	 */
	void findRightKind(ArrayList<Node>rightList, Node p, Node root){
		if (p.isPossibleChild(root)){
			rightList.add(root);
		}
		ArrayList<Node> arr=root.getChildren();
		if (arr.size()==0) return;
		for (Node i:arr){
			findRightKind(rightList, p, i);
		}
	}
	/**
	 * Find a list of nodes in the entire AST that can be child update node of {@code p}.
	 * @param rightList an array list to contain possible children nodes in the entire AST.
	 * @param p the node whose possible children nodes to be found.
	 * @param root the root of the entire AST.
	 */
	void findRightKindWithoutAction(ArrayList<Node>rightList, Rule p, Node root){
		if (root instanceof UpdateNode){
			rightList.add(root);
		}
		ArrayList<Node> arr=root.getChildren();
		if (arr.size()==0) return;
		for (Node i:arr){
			findRightKindWithoutAction(rightList, p, i);
		}
	}
	/**
	 * Find a list of nodes in the entire AST that can be child Condition node of {@code p}.
	 * @param rightList an array list to contain possible children nodes in the entire AST.
	 * @param p the node whose possible children nodes to be found.
	 * @param root the root of the entire AST.
	 */
	void findRightKindForCond(ArrayList<Node>rightList, Rule p, Node root){
		if (root instanceof Condition){
			rightList.add(root);
		}
		ArrayList<Node> arr=root.getChildren();
		for (Node i:arr){
			findRightKindForCond(rightList, p, i);
		}
	}
	
}
