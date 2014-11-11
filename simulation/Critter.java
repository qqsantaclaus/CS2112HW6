package simulation;
import java.util.Random;

import console.WorldConstants;
import ast.ProgramImpl;

/**
 * A representation of critter
 *
 */
public class Critter extends Content{
	String species;
	int[] mem;
	public HexCoord location;
	int direction;
	World MyWorld;
	ProgramImpl ruleset;
	String latestRule;
	
	public Critter(String species, int[] mem, int dir, ProgramImpl ruleset, World MyWorld){
		this.species=species;
		this.mem=mem;
		this.direction=(dir % 6 +6) % 6;
		this.ruleset=ruleset;
		this.location=null;
		this.MyWorld=MyWorld;
		this.latestRule="";
	}
	
	/**
	 * Return the appearance of the current critter when the relative direction
	 *  from observer is dir;
	 * Requires: {@code dir} is an integer between 0 to 6.
	 * @param dir the relative direction from observer
	 * @return a integer number that represents appearance.
	 */
	int appearance(int dir){
		return 100000*mem[3]+1000*mem[6]+10*mem[7]+dir;
	}
	
	/**
	 * Return the complexity of the current critter.
	 * @return a integer number that represents complexity.
	 */
	int complexity(){
		return (int) (ruleset.getChildren().size()*WorldConstants.getConstant("RULE_COST")
					+(mem[1]+mem[2])*WorldConstants.getConstant("ABILITY_COST"));
	}
	
	/**
	 * get the given critter's neighbor at a given direction {@code i}
	 * @param i an integer that represents the relative direction to current critter,
	 * 			must be an integer from 0 to 5, so there are 6 possible directions
	 * 			0 means the direction critter faces, and other numbers go clockwise
	 * @return the hex that is at direction i to the current critter
	 */
	public Hex getNeighbor(int i){
		int[] change=HexCoord.oneMove[(this.direction+i)%6];
		return MyWorld.getHex(this.location.c+change[0], this.location.r+change[1]);
	}
	/**
	 * Report the contents of the hex in direction {@code dir} relative to it. 
	 * {@code}dir should be one of 0 to 5. If it is out of bounds, 
	 * take its remainder when divided by 6. 
	 * @param dir direction
	 * @return if the hex is empty, return 0;
	 * 		   if the hex contains a critter, return its appearance;
	 * 		   if the hex contains food with total energy (-n)-1, return n;
	 * 		   if the hex contains a rock, return -1.  
	 */
	public int nearby(int dir){
		Hex temp=getNeighbor(dir);
		if (temp.isEmpty()) return 0;
		if (temp.content.print().equals("F")) return (-1)*(((Food)temp.content).amt+1);
		if (temp.content.print().equals("#")) return -1;
		return ((Critter)temp.content).appearance(dir % 6);
	}
	/**
	 * Report the contents of hex that is directly ahead of teh creature at distance dist. 
	 * A negative distance is treated as a zero distance.
	 * @param dist distance
	 * @return if the hex is empty, return 0;
	 * 		   if the hex contains a critter, return its appearance;
	 * 		   if the hex contains food with total energy (-n)-1, return n;
	 * 		   if the hex contains a rock, return -1.  
	 * 		   if {@code dist}=0, return the appearance of the current critter.
	 */
	public int ahead(int dist){
		int[] change=HexCoord.oneMove[this.direction];
		Hex temp=MyWorld.getHex(this.location.c+dist*change[0], this.location.r+dist*change[1]);
		if (temp.isEmpty()) return 0;		
		if (temp.content.print().equals("F")) return (-1)*(((Food)temp.content).amt+1);
		if (temp.content.print().equals("#")) return -1;
		return ((Critter)temp.content).appearance(0);
	}
	/**
	 * Report smell.
	 * @return a value temporaily evaluated to zero.
	 */
	public int smell(){
		return 0;
	}
	/**
	 * Generate a random integer from 0 up to one less than {@code v}
	 * @param v the upper bound of random generation
	 * @return a randomly integer betweeen 0 (inclusive) and {@code v} (exclusive); 
	 * 		   return 0 if {@code v}<2.
	 */
	public int random(int v){
		if (v<2) return 0;
		Random r=new Random();
		return r.nextInt(v);
	}
	
	/**
	 * Return mem[{@code index}] when index is valid, from 0 to length of memory-1.
	 * @param index the index of mem to be returned.
	 * @return the value stored at mem[{@code index}]
	 */
	public int getMem(int index){
		if (0<=index && index<this.mem[0]) return this.mem[index];
		return -1;
	}
	/**
	 * Set mem[{@code index}] as value {@code v}.
	 * Only mem with index greater than 6 is settable.
	 * @param index index of mem to be assigned.
	 * @param v value assigned to mem[{@code index}].
	 * @return true if assignment is successful.
	 */
	public boolean setMem(int index, int v){
		if (index==7 && 0<=v && v<=99){
			mem[index]=v;
			return true;
		}
		if (7<index && index<this.mem[0]){
			mem[index]=v;
			return true;
		}
		return false;
	}

	
	@Override
	public String print() {
		return String.valueOf(this.direction);
	}

	@Override
	public void printDescription(StringBuilder sb) {
		sb.append("Species name: "+this.species+"\n");
		sb.append("\nMemory:\n");
		for (int i=0; i<8; i++){
			sb.append("mem["+i+"]="+this.mem[i]+"\n");
		}
		sb.append("\nRule set:\n");
		this.ruleset.prettyPrint(sb);
		sb.append("\n\nLatest rule carried out:\n");
		sb.append(this.latestRule);
	}
	/**
	 * Start a new turn, and store the latest rule carried out in the last turn.
	 * @param rule the latest rule carried out in the last turn
	 */
	public void nextPass(String rule){
		mem[5]++;
		this.latestRule=rule;
	}
	
}
