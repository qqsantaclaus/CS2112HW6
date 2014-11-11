package ast;



/**
 * 
 * A representation of pure word command: Command_Word.
 */
public class MessageNode extends NoChild implements Action {
	Message msg;
	/**
	 * Create an AST representation of pure word command that is one of 
	 * wait, forward, backward, left, right, eat, attack, grow, bud or mate.
	 * @param msg the pure word command to be stored
	 */
	public MessageNode(Message msg){
		this.msg=msg;
	}
	
	@Override
	public void prettyPrint(StringBuilder sb) {
		sb.append(this.msg.toString());
	}


	@Override
	public Node copy() {
		return new MessageNode(this.msg);
	}
	/**
	 * An enumeration of all possible pure word commands.
	 *
	 */
	public enum Message{
		wait,forward,backward,left,right,eat,attack,grow,bud,mate;
	}
@Override
	public String getMsg(){
		switch(msg){
		case wait:
			return "wait";
		case forward:
			return "forward";
		case backward:
			return "backward";
		case left:
			return "left";
		case right:
			return "right";
		case eat:
			return "eat";
		case attack:
			return "attack";
		case grow:
			return "grow";
		case bud:
			return "bud";
		case mate:
			return "mate";
		default:
			return "";
				
		}
	}

	
}
