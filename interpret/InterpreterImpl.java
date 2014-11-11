package interpret;

import simulation.Critter;
import ast.Action;
import ast.Command;
import ast.Condition;
import ast.Expression;
import ast.FunctionNode;
import ast.MessageNode;
import ast.Program;
import ast.Rule;
import ast.UpdateNode;
import ast.VariableExpr;

public class InterpreterImpl implements Interpreter{
	
	public InterpreterImpl(){	
	}
	Critter critter=null;

	
	 /**
     * Execute program {@code p} until either the maximum number of rules per
     * turn is reached or some rule whose command contains an action is
     * executed.
     * @param p
     * @return a result containing the action to be performed;
     * the action may be null if the maximum number of rules
     * per turn was exceeded.
     */
	@Override
	public Outcome interpret(Program p, Critter critter) {
		this.critter=critter;
		Outcome outcome=new OutcomeImpl("",-1);
		while(critter.getMem(5)<999 && outcome.getString()==""){
			Rule r=p.accept(this);
			if(r==null) return new OutcomeImpl("wait",-1); //all conditions false
			for(Command c:r.getCommand()){
				outcome=interpret(c);
			}
			critter.nextPass(r.toString());
		}
		if(outcome.getString()!="")return outcome;
		return new OutcomeImpl("wait",-1);
	}
	
/**
 * Interpret a given command {@code c} and return the outcome
 * If the command is update, update the critter's memory and return an empty outcome
 * If the command is action, the outcome contains the representation of the action
 * @param c the given command that may be update or action
 * @return an Outcome of interpreting the command, set to default value if the command is update
 */
	public Outcome interpret(Command c){
		Outcome outcome=new OutcomeImpl("",-1);
		if(c instanceof UpdateNode){
			int[] update=((UpdateNode)c).accept(this);
			int index=update[0];
			int assign=update[1];
			//POSTURE
			if(index==7 && assign>=0 && assign <=99){
				critter.setMem(index,assign);
			}
			if(index>7 && index<critter.getMem(0)){
				critter.setMem(index,assign);
			}
			critter.setMem(index,assign);
		}
		if(c instanceof FunctionNode){			
			outcome=new OutcomeImpl(((Action) c).getMsg(),((FunctionNode) c).accept(this));
		}
		if(c instanceof MessageNode){
			outcome=new OutcomeImpl(((Action)c).getMsg(),-1);
		}
		return outcome;
	}
	 /**
     * Evaluate the given condition {@code c}.
     * @param c the given condition to be evaluated
     * @return a boolean that results from evaluating c.
     */
	@Override
	public boolean eval(Condition c) {	
		return c.accept(this);
	}

	/**
     * Evaluate the given expression {@code e}.
     * @param e the given expression to be evaluated
     * @return an integer that results from evaluating e.
     */
	@Override
	public int eval(Expression e) {
		return e.accept(this);
	}
	
	public int getVar(VariableExpr v){
		switch(v.getMsg()){
		case nearby:
			int n=eval(v.getExpr());
			return critter.nearby(n);
		case ahead:
			n=eval(v.getExpr());
			return critter.ahead(n);
		case random:
			n=eval(v.getExpr());
			if(n>=2) return critter.random(n);
			return 0;
		case smell:
			return critter.smell();
		case mem:
			n=eval(v.getExpr());
			return critter.getMem(n);
		default:
			return 0;	
		}
		
	}
	
	
}
