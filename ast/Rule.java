package ast;

import interpret.InterpreterImpl;

import java.util.ArrayList;

import exceptions.SyntaxError;

/**
 * A representation of a critter rule.
 */
public class Rule extends NodeImpl {
	/**
	 * Requires: At least one command node is subscribed after parsing.
	 * 			 Contain at most one action command at the end of command list
	 */
	Condition cdn;
	ArrayList<UpdateNode> updates;
	Action action;
	/**
	 *Create an AST representation of rule: cdn --> cmds
	 * @param cdn Condition of the rule
	 * @param cmds a list of commands of the rule
	 */
	public Rule(Condition cdn, ArrayList<Command> cmds){
		this(cdn);
		for (Command c:cmds){
			addCommand(c);
		}
	}
	/**
	 * Create a rule with an empty list of commands
	 * @param cdn Condition of the rule.
	 */
	public Rule(Condition cdn){
		this.cdn=cdn;
		this.updates=new ArrayList<UpdateNode>();
		this.action=null;
	}
	
	/**
	 * Append new command {@code cmd} to the rule's command list.
	 * If the appending is invalid, return false; otherwise, return true.
	 * @param node The command to be appended
	 * @return true if appending is successful.
	 * @throws SyntaxError 
	 */
	public boolean addCommand(Node node){
		 if (node instanceof UpdateNode && action==null){
			 updates.add((UpdateNode)node);
			 return true;
		 }
		 if (node instanceof Action && action==null){
			 this.action=(Action)node;
			 return true;
		 }
		 return false;
	}
	
    @Override
    public int size() {
        int size=1+this.cdn.size();
        for (Command c:updates){
        	size+=c.size();
        }
        if (this.action!=null){
        	size+=this.action.size();
        }
        return size;
    }
    @Override
    public void prettyPrint(StringBuilder sb) {
        sb.append(this.cdn.toString());
        sb.append(" --> ");
        int ind=sb.length()-sb.lastIndexOf("\n")-1;
        String blanket="\n";
        for (int i=0; i<ind; i++){
        	blanket+=" ";
        }
        ArrayList<Command> cmds=getCommand();
       	for (int i=0; i<cmds.size()-1;i++){
       		sb.append(cmds.get(i).toString());
        	sb.append(blanket);
        }
        sb.append(cmds.get(cmds.size()-1).toString());
        sb.append(";");
    }
    
    @Override
    public Node nodeAt(int index) {
    	if (index>=this.size()){
    		return null;
    	}
    	if (index==0){
    		return this;
    	}
        ArrayList<Node> ch=getChildren();
        int i=0;
        int temp=index;
        while (temp>ch.get(i).size()){
        	temp-=ch.get(i).size();
            i++;
        }
        return ch.get(i).nodeAt(temp-1);
    }

    /**
     * Check whether node {@code n} can be a possible command.
     */
	@Override
	public boolean isPossibleChild(Node n) {
		return (n instanceof Command);
	}
	/**
	 * Check whether node {@code n} can be a possible condition.
	 * @param n the node to be checked
	 * @return true if {@code n} is the right type.
	 */
	public boolean isPossibleCond(Node n){
		return (n instanceof Condition);
	}
	
	/**
	 * Returns the entire array of commands on the right side of "-->" expression.
	 * @return an array list of command nodes which will definitely be not empty.
	 */
	public ArrayList<Command> getCommand(){
		ArrayList<Command> cmds=new ArrayList<Command>();
		cmds.addAll(this.updates);
		if (this.action!=null) cmds.add(this.action);
		return cmds;
	}
	
	@Override
	public ArrayList<Node> getChildren() {
		ArrayList<Node> arr=new ArrayList<Node>();
		arr.add(this.cdn);
		arr.addAll(this.updates);
		if (this.action!=null) arr.add(this.action);
		return arr;
	}


	@Override
	public boolean removeChild(Node n) {
		if (this.cdn==n) return false;
		int s=0;
		s+=this.updates.size();
		if (this.action!=null) s++;
		if (s<=1) return false;
		if (this.updates.remove(n)) return true;
		if (this.action==n){ 
			this.action=null;
			return true;
		}
		return false;
	}
	
	@Override
	public Node copy() {
		ArrayList<Command> arr=new ArrayList<Command>();
		for (Command c:this.updates){
			arr.add((Command) c.copy());
		}
		if (this.action!=null){
			arr.add((Command) this.action.copy());
		}
		return new Rule((Condition)this.cdn.copy(), arr); 
	}
	
	@Override
	public boolean replaceChild(Node c, Node n) {
		if (c==this.cdn && n instanceof Condition){
			this.cdn=(Condition)n;
			return true;
		}
		if (c==this.action){
			if (n instanceof Action){
				this.action=(Action)n;
				return true;
			}
			else if (n instanceof UpdateNode){
				this.updates.add((UpdateNode)n);
				this.action=null;
				return true;
			}
		}
		int index=this.updates.indexOf(c);
		if (index>=0){
			if (n instanceof UpdateNode) {
				this.updates.set(index, (UpdateNode) n);
				return true;
			}
			else if (n instanceof Action){
				if (index==this.updates.size()-1 && this.action==null){
					this.updates.remove(index);
					this.action=(Action)n;
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * Check whether there is action type of command in command list.
	 * @return true if action command is contained.
	 */
	public boolean containsAction(){
		return (this.action!=null);
	}
	/**
	 * find if the condition of this rule is true
	 * @return true if the condition is true
	 */
	public boolean accept(InterpreterImpl i){
		return this.cdn.accept(i);
	}
	
	

	
}
