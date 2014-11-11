package ast;

import java.util.ArrayList;
/**
 * An abstraction of terminal nodes.
 */
public abstract class NoChild extends NodeImpl {
    @Override
    public int size() {
        return 1;
    }
	
	@Override
	public boolean isPossibleChild(Node n) {
		return false;
	}

	@Override
	public ArrayList<Node> getChildren() {
		return new ArrayList<Node>();
	}

	@Override
	public boolean removeChild(Node n) {
		return false;
	}
	@Override
	public boolean replaceChild(Node c, Node n){
		return false;
	}
	
	 public Node nodeAt(int index) {
		 if (index==0) return this;
		 return null;
	 }
}
