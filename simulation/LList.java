package simulation;

/**
 * A linked list that is only able to append element but able to delete elements anywhere
 */

public class LList{
	public LListNode head;
   	public LListNode end;
   	public int length;
   /**
    * Append a node that contains {@code c} to the end of LList
    * @param c
    */
	public void Append(Critter c){
		LListNode wrap=new LListNode(c);
		if (!isEmpty()){
			end.next=wrap;
			wrap.prev=end;
			end=wrap;
		}
		else {
			head=wrap;
			end=wrap;
		}
		length++;
	}
	/**
	 * Delete the node that contains {@code key} in LList
	 * Return null if {@code key} doesn't exist
	 * Otherwise return the value which was paired with {@code key}
	 * @param key the key of the node in LList to be deleted
	 */
   	public void Delete(Critter c){ //Can't be the beginning or the end
   		LListNode loc=Find(c);
   		if (loc.next==null && loc.prev==null){
   			head=null;
   			end=null;
   		}
   		else if (loc.prev==null){
   			head=loc.next;
   	   		head.prev=null;
   		}
   		else if (loc.next==null){
   			end=loc.prev;
   	   		end.next=null;
   		}
   		else {
   			loc.next.prev=loc.prev;
   			loc.prev.next=loc.next;
   		}
   		length--;
	}
   	/**
   	 * Delete all the nodes in LList, so that it becomes empty.
   	 */
   	public void Clear(){
   		length=0;
   		head=null;
   		end=null;
   	}
   	/**
   	 * Default constructor
   	 */
	public LList(){
		head=null;
		end=null;
		length=0;
	}
	/**
	 * Return the node that contains {@code c} in LList
	 * Return null if such node doesn't exist.
	 * @param c
	 * @return the node that contains {@code c}
	 */
	public LListNode Find(Critter c){
		LListNode temp=head;
		while (temp!=null && !temp.value.equals(c)){
			temp=temp.next;	
		}
		return temp;
	}
	/**
	 * Check whether LList is empty.
	 * @return true if LList is empty; false otherwise.
	 */
	public boolean isEmpty(){
		return (length==0);
	}
	/**
	 * @return number of nodes in LList
	 */
	public int Size(){
		return length;
	}

}

