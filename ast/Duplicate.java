package ast;

import java.util.ArrayList;
import java.util.Random;

/**
 * A mutation that adds an additional copy of one of the children of the given node 
 * to the end of the list of children
 */
public class Duplicate implements Mutation{

	@Override
	public boolean equals(Mutation m) {
		return (m instanceof Duplicate);
	}
	
	/**
     * Apply this mutation to the given node.
     * @param n the node to be mutated.
     * @param p the parent node of {@code n};
     * @return true if the mutation is successful.
     */
	public boolean getMutated(Node n, Node p) {
		Random r=new Random();
		if (n instanceof ProgramImpl){
			ArrayList<Node> arr=n.getChildren();
			if (arr.size()==0) return false;
			return ((ProgramImpl) n).addRule(arr.get(r.nextInt(arr.size())).copy());
		}
		if (n instanceof Rule){
			if (((Rule)n).containsAction()){
				return false;
			}
			ArrayList<Command> cmds=((Rule)n).getCommand();
			if (cmds.size()==0) return false;
			int i=r.nextInt(cmds.size());
			return ((Rule) n).addCommand(cmds.get(i).copy());
		}
		return false;
	}

	@Override
	public boolean getMutated(Node n, Node p, Node root) {
		return getMutated(n,p);
	}

}
