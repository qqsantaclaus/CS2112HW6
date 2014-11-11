package ast;

import java.util.ArrayList;
/**
 * An abstraction of node with single child of expression type.
 */
public abstract class SingleChild extends NodeImpl {
	Expression e;
	/**
	 * Set the child node as {@code n} if valid.
	 * @param e the node to be set as child node.
	 * @return false if {@code e} is not compatible with requirement of child.
	 */
	public boolean setExpr(Node e){
		if (isPossibleChild(e)){
			this.e=(Expression)e;
			return true;
		}
		return false;
	}

	public boolean isPossibleChild(Node n) {
		return (n instanceof Expression);
	}
	
    @Override
    public int size() {
    	if (e==null) return 1;
        return e.size()+1;
    }
    
	@Override
	public boolean removeChild(Node n) {
		return false;
	}
    
    @Override
    public boolean replaceChild(Node c, Node n){
    	if (c==this.e){
    		return setExpr(n);
    	}
    	return false;
    }
    public Node nodeAt(int index) {
    	if (index>=this.size()){
    		return null;
    	}
    	if (index==0){
    		return this;
    	}
    	return this.e.nodeAt(index-1);
    }
	@Override
	public ArrayList<Node> getChildren() {
		ArrayList<Node> arr=new ArrayList<Node>();
		if (e!=null) arr.add(e);
		return arr;
	}   
}
