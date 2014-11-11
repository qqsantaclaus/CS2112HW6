package simulation;

/**
 * Nodes of LList, each of which contains a reference to critter.
 */
public class LListNode{
	public Critter value;
	public LListNode next;
	public LListNode prev;
	
	/**
	 * Constructor
	 * @param value
	 */
	LListNode(Critter value){
		this.value=value;
		next=null;
		prev=null;
	}
	/**
	 * Constructor without specified value
	 */
	LListNode(){
		this(null);
	}
}
