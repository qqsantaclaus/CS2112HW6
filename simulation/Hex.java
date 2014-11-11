package simulation;

public class Hex {
	public Content content;
	
	public Hex(Content c){
		this.content=c;
	}
	
	public Hex(){
		this.content=null;
	}
	/**
	 * print the representation of the content in the hex
	 * empty hex is represented by "-"
	 * hex contains food is represented by "F"
	 * hex contains rock is represented by "#"
	 * hex contains critter is represented by an integer that is the direction the critter faces
	 */
	public String print(){
		if (isEmpty()) return "-";
		return content.print();
	}
	/**
	 * print the description of the content of the hex
	 * hex contains food prints out the amount of food
	 * hex contains critter prints out the following as description of critter:
	 * -its species
	 * -the contents of at least its first eight memory locations
	 * -its rule set, using the pretty-printer from Assignment 4
	 * -the last rule executed
	 */
	void printDescription(StringBuilder sb){
		if (isEmpty()){
			return;
		}
		this.content.printDescription(sb);
	}
	/**
	 * Clear the content of the hex
	 */
	void clear(){
		this.content=null;
	}
	/**
	 * Check whether the hex is empty.
	 */
	public boolean isEmpty(){
		if(this.content==null) return true;
		return false;
	}
	/**
	 * Add critter {@code critter} to the hex if valid
	 * @param critter
	 * @return true if successful
	 */
	boolean addCritter(Critter critter){
		if (isEmpty()){
			this.content=critter;
			return true;
		}
		return false;
	}
	/**
	 * Add {@code amt} of food to the hex if valid
	 * @param amt
	 * @return true if successful
	 */
	boolean addFood(int amt){
		if (isEmpty()){
			this.content=new Food(amt);
			return true;
		}
		if (this.content instanceof Food){
			((Food)content).amt+=amt;
			return true;
		}
		return false;	
	}
	/**
	 * Add rock to the hex if valid
	 * @return true if successful.
	 */
	boolean addRock(){
		if (isEmpty()){
			this.content=new Rock();
			return true;
		}
		return false;
	}
}
