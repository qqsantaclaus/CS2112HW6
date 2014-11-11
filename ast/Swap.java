package ast;

import java.util.ArrayList;
import java.util.Random;
/**
 * A mutation that swaps order of two children of the given node.
 */
public class Swap implements Mutation{

	@Override
	public boolean equals(Mutation m) {
		return (m instanceof Swap);
	}
	
	public boolean getMutated(Node n, Node p) {
		if (n instanceof BinaryOperations){
			Node temp=((BinaryOperations<?>)n).l;
			if (temp!=null && ((BinaryOperations<?>)n).r!=null){
				((BinaryOperations<?>)n).setLeft(((BinaryOperations<?>)n).r);
				((BinaryOperations<?>)n).setRight(temp);
				return true;
			}
			return false;
		}
		if (n instanceof ProgramImpl){
			return randomSwap(((ProgramImpl)n).rules);
		}
		if (n instanceof Rule){
				return randomSwap(((Rule)n).updates);
		}
		return false;
	}
	/**
	 * Randomly swap two elements in {@code arr}.
	 * @param arr an arrayList of elements.
	 * @return True if the swap is successful.
	 */
	<T> boolean randomSwap(ArrayList<T> arr){
		Random r=new Random();
		int s=arr.size();
		if (s<=1) return false;
		int i=r.nextInt(s-1);
		int j=r.nextInt(s-i-1)+i+1;
		T temp=arr.get(i);
		arr.set(i, arr.get(j));
		arr.set(j, temp);
		return true;
	}
	
	@Override
	public boolean getMutated(Node n, Node p, Node root){
		return getMutated(n, null);
	}
}
