package ast;

import java.util.ArrayList;

public abstract class BinaryOperations<T extends Node> extends NodeImpl{
	/**
	 * Left child and right child of current node
	 */
	T l,r;
	
	@Override
	public int size() {
		int size=1;
		if (l!=null) size+=l.size();
		if (r!=null) size+=r.size();
		return size;
	}
	/**
	 * Set left node of current node as node {@code n} if valid.
	 * @param n the node to be set as left node
	 * @return False if the type of node {@code n} is incompatible with the requirement of left child.
	 */
	@SuppressWarnings("unchecked")
	public boolean setLeft(Node n){
		if (isPossibleChild(n)){
			l=(T) n;
			return true;
		}
		return false;
	}
	/**
	 * Set right node of current node as node {@code n} if valid.
	 * @param n the node to be set as right node
	 * @return False if the type of node {@code n} is incompatible with the requirement of right child.
	 */
	@SuppressWarnings("unchecked")
	public boolean setRight(Node n){
		if (isPossibleChild(n)){
			r=(T) n;
			return true;
		}
		return false;
	}
	@Override
	public boolean replaceChild(Node c, Node n){
		if (c==this.l){
			return setLeft(n);
		}
		else if (c==this.r){
			return setRight(n);
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
		if (this.l==null){
			return this.r.nodeAt(index-1);
		}
		if (index<=this.l.size()){
			return this.l.nodeAt(index-1);
		}
		else {
			return this.r.nodeAt(index-this.l.size()-1);
		}
	}
	
	@Override
	public boolean removeChild(Node n) {
		return false;
	}
	
	@Override
	public ArrayList<Node> getChildren() {
		ArrayList<Node> arr=new ArrayList<Node>();
		if (l!=null) arr.add(l);
		if (r!=null) arr.add(r);
		return arr;
	}
}
