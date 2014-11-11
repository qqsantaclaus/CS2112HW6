package ast;

import interpret.InterpreterImpl;

import java.util.ArrayList;
import java.util.Random;

import exceptions.SyntaxError;

/**
 * A data structure representing a critter program.
 *
 */
public class ProgramImpl extends NodeImpl implements Program {
	ArrayList<Rule> rules;
	/**
     * Create an AST representation of a collection of rules.
	 * @param rules an array list of rules.
	 */
	public ProgramImpl(ArrayList<Rule> rules){
		this.rules=rules;
	}
	/**
	 * Create an empty list of rule.
	 */
	public ProgramImpl(){
		this.rules=new ArrayList<Rule>();
	}
	
    @Override
    public int size() {
        int size=1;
        for (Rule r:rules){
        	size+=r.size();
        }
        return size;
    }
    
    @Override
    public void prettyPrint(StringBuilder sb) {
    	if (this.rules.size()==0) return;
    	for (int i=0; i<this.rules.size()-1; i++){
        	sb.append(this.rules.get(i).toString());
        	sb.append("\n");
        }
    	sb.append(this.rules.get(this.rules.size()-1).toString());
    }

    @Override
    public Node nodeAt(int index) {
    	if (index>=this.size()){
    		return null;
    	}
    	if (index==0){
    		return this;
    	}
        int temp=index;
        int i=0;
        while (temp>this.rules.get(i).size()){
        	temp-=this.rules.get(i).size();
        	i++;
        }
        return this.rules.get(i).nodeAt(temp-1);
    }

    @Override
    public Program mutate() throws SyntaxError {
    	int s=this.size();
    	Random r=new Random();
    	int i=r.nextInt(s);
    	int j=r.nextInt(6);
    	Mutation m = null;
    	switch (j) {
    		case 0: m=MutationFactory.getRemove(); break;
    		case 1: m=MutationFactory.getSwap(); break;
    		case 2: m=MutationFactory.getReplace(); break;
    		case 3: m=MutationFactory.getTransform(); break;
    		case 4: m=MutationFactory.getInsert(); break;
    		case 5: m=MutationFactory.getDuplicate(); break;
    	}
    	mutate(i, m);
        return this;
    }

    @Override
    public Node mutate(int index, Mutation m) throws SyntaxError {
    	Node n=nodeAt(index);
        Node p=findParent(index);
        if (m.getMutated(n, p, this)){
        	return n;
        }
        else return null;
    }
    /**
     * Return the parent node of the node with index {@code index} 
     * @param index the index of the node whose parent node to be found
     * @return the parent node of the node of index {@code index}. 
     * Return null if index is out of bound or the node at {@code index} doesn't have a parent node.
     */
    public Node findParent(int index){
    	Node n=nodeAt(index);
    	for (int i=index-1; i>=0; i--){
    		Node pp=nodeAt(i);
    		if (pp.getChildren().contains(n)) return pp;
    	}
    	return null;
    }
   
	@Override
	public boolean isPossibleChild(Node n) {
		return (n instanceof Rule);
	}

	@Override
	public ArrayList<Node> getChildren() {
		ArrayList<Node> arr=new ArrayList<Node>();
		for (Rule r:this.rules){
			if (r!=null) arr.add(r);
		}
		return arr;
	}

	@Override
	public boolean removeChild(Node n) {
		return this.rules.remove(n);
	}

	@Override
	public Node copy() {
		ArrayList<Rule> newRules=new ArrayList<Rule>();
		for (int i=0; i<this.rules.size(); i++){
			newRules.add((Rule)this.rules.get(i).copy());
		}
		return new ProgramImpl(newRules); 
	}
	/**
	 * Add rule node {@code n} to the rule list if valid.
	 * @param n rule to be added
	 * @return true if the addition is successful
	 */
	public boolean addRule(Node n){
		if (n instanceof Rule) {
			return this.rules.add((Rule) n);
		}
		return false;
	}
	@Override
	public boolean replaceChild(Node c, Node n) {
		int index=this.rules.indexOf(c);
		if (index>=0 && isPossibleChild(n)){
			this.rules.set(index, (Rule) n);
			return true;
		}
		return false;
	}
	////////////////////////////
	@Override
	public Rule accept(InterpreterImpl i) {
		for (Rule r: this.rules){
			if (r.accept(i)){
				return r;
			}
		}
		return null;
	}
	
	
}
