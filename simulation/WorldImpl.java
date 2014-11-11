package simulation;

import interpret.Interpreter;
import interpret.InterpreterImpl;
import interpret.Outcome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import ast.Node;
import ast.ProgramImpl;
import ast.Rule;
import console.WorldConstants;
import exceptions.SyntaxError;

/**
 * Implementation of world
 */
public class WorldImpl implements World {
	private LList ordering;
	private Hex[][] WorldMap;
	int hexC, hexR;
	public int mapC;
	public int mapR;
	private HashMap<Critter, Critter> toMate;
	private int timeStep;
	public String name;
	
	public WorldImpl(String name, int c,int r){
		this(c,r);
		this.name=name;
	}
	public WorldImpl(int c,int r){
		hexR=r;
		hexC=c;
		if(r<=(int)c/2){
			r=c/2+1;
		}
		mapC=c;
		mapR=r-(int)(c/2);
		this.WorldMap=new Hex[mapR][mapC];
		for (int i=0; i<mapR; i++){
			for (int j=0; j<mapC; j++){
				this.WorldMap[i][j]=new Hex();
			}
		}
		if(mapC%2==1){
			for(int i=1;i<mapC;i+=2){
				HexCoord mc=MatrixCoord.ConversionFromMatrixToHex(mapR-1, i);
				System.out.println(setRock(mc));
				System.out.println(MatrixCoord.ConversionFromMatrixToHex(mapR-1, i).toString());
			}
		}
		this.timeStep=0;
		this.ordering=new LList();
		this.toMate=new HashMap<Critter, Critter>();
		this.name="";
	}
	//Problem
	public WorldImpl(Hex[][] map){
		this.WorldMap=map;
		this.timeStep=0;
		this.ordering=new LList();
		this.toMate=new HashMap<Critter, Critter>();
		this.name="";
	}
	@Override
	public void printMap(StringBuilder sb) {
		int rMax;
		if (mapC % 2==1) rMax=2*mapR-1;
		else rMax=2*mapR;
		Hex[][] mapPrint=new Hex[rMax][mapC];
		for(int i=0;i<rMax;i++){
			for(int j=i % 2;j<mapC;j+=2){
				Hex h=getHexWithMatrixCoord(i/2,j);
				mapPrint[rMax-1-i][j]=h;
			}
			for(int j=(i+1) % 2;j<mapC;j+=2){
				mapPrint[rMax-1-i][j]=null;
			}
		}
		for (int i=0;i<rMax;i++){
			for(int j=0;j<mapC;j++){
				if (mapPrint[i][j]==null) sb.append(" ");
				else sb.append(mapPrint[i][j].print());
			}
			sb.append("\n");
		}
	}
	
	@Override
	public String printDescription(int c, int r) {	
		Hex h=getHex(c,r);
		StringBuilder sb=new StringBuilder();
		h.printDescription(sb);
		return sb.toString();
	}
	
	@Override
	public Hex getHex(int c, int r) {
		MatrixCoord temp=HexCoord.ConversionFromHexToMatrix(c, r);
		if (temp.c<0 || temp.c>=mapC || temp.r<0 || temp.r>=mapR){
			return new Hex(new Rock());
		}
		return WorldMap[temp.r][temp.c];
	}
	public Hex getHex(HexCoord location){
		return getHex(location.c, location.r);
	}
	/**
	 * Set {@code critter} to {@code newlocation}
	 * The critter can either be a new critter, or already exists in the world. 
	 * @param critter
	 * @param newlocation
	 * @return true if successful.
	 */
	public boolean setCritter(Critter critter, HexCoord newlocation){
		Hex h=getHex(newlocation);
		if (!h.isEmpty()) return false;
		if (critter.location!=null){
			getHex(critter.location).clear();
		}
		else {
			ordering.Append(critter);
		}
		h.addCritter(critter);
		critter.location=newlocation;
		return true;
	}
	/**
	 * Add food to {@code location} by amount {@code amt}
	 * The location can either be empty or have food
	 * @param amt amount of food to add
	 * @param location
	 * @return true if successful
	 */
	public boolean addFood(int amt, HexCoord location){
		return this.getHex(location).addFood(amt);
	}
	/**
	 * Set a rock at {@code location} if valid
	 * @param location place to put a rock
	 * @return true if successful
	 */
	public boolean setRock(HexCoord location){
		Hex temp=this.getHex(location);
		return temp.addRock();
	}
	/**
	 * Set {@code c}'s direction to {@code dir}
	 * @param c critter
	 * @param dir direction
	 */
	public void setDirection(Critter c, int dir){
		c.direction=dir;
	}
	
	/**
	 * Get the hex at given matrix coordinate
	 * @param x matrix coordinate
	 * @param y matrix coordinate
	 * @return the corresponding hex
	 */
	private Hex getHexWithMatrixCoord(int x,int y){
		return WorldMap[x][y];
	}

	/**
	 * Move critter {@code c} either forward or backward. 
	 * {@code dir} specifies the direction to move: 
	 * a positive integer represents move forward by one.
	 * a negative integer represents move backward by one.
	 * zero represents no move.
	 * @param c critter to be moved
	 * @param dir an integer that specifies forward or backward.
	 * @return true if the move is successful.
	 */
	boolean	move(Critter c, int dir){
		if (dir<0) dir=-1;
		else if (dir>0) dir=1;
		else return false;
		c.mem[4]-=(c.mem[3]*WorldConstants.getConstant("MOVE_COST"));
		if (!isAlive(c)){ 
			destroy(c);
			return false;
		}
		int[] change=HexCoord.oneMove[c.direction];
		HexCoord coord=new HexCoord(c.location.c+(change[0])*dir, c.location.r+(change[1])*dir);
		return this.setCritter(c, coord);
	}
	private boolean isAlive(Critter c) {
		return c.mem[4]>0;
	}
	/**
	 * Rotate critter {@code c} 60 degree either to left or to right. 
	 * {@code dir} specifies the direction to rotate: 
	 * a positive integer represents turn right by 60 degree.
	 * a negative integer represents turn left by 60 degree.
	 * zero represents no turn.
	 * @param c critter to be rotated.
	 * @param dir an integer that specifies left or right.
	 * @return true if the rotation is successful.
	 */
	public boolean turn(Critter c, int dir){
		if (dir<0) dir=-1;
		else if (dir>0) dir=1;
		else return false;
		c.mem[4]-=c.mem[3];
		if (!isAlive(c)) {
			destroy(c); 
			return false;
		}
		c.direction=(c.direction+dir+6)%6;
		return true;
	}
	/**
	 * Let critter {@code c} eat the food on the hex ahead of it if available.
	 * @param c critter to eat food
	 * @return true if eating is successful.
	 */
	boolean eat(Critter c){
		c.mem[4]-=c.mem[3];
		if (!isAlive(c)) {
			destroy(c); 
			return false;
		}
		Hex front=c.getNeighbor(0);
		if (!front.isEmpty() && front.content.print().equals("F")){
			if (((Food)front.content).amt+c.mem[4]<=WorldConstants.getConstant("ENERGY_PER_SIZE")*c.mem[3]){
				c.mem[4]+=((Food)front.content).amt;
				front.clear();
				return true;
			}
			else {
				((Food)front.content).amt-=(WorldConstants.getConstant("ENERGY_PER_SIZE")*c.mem[3]-c.mem[4]);
				c.mem[4]=(int) (WorldConstants.getConstant("ENERGY_PER_SIZE")*c.mem[3]);
				return true;
			}
		}
		return false; 
	}
	/**
	 * Let critter {@code c} convert some of its own enerhy into food added to the hex
	 * in front of it, if the hex is either empty or already contains food.
	 * @param c criter to serve
	 * @return true if serve is successful
	 */
	boolean serve(Critter c, int v){
		c.mem[4]-=c.mem[3];
		if (!isAlive(c)) {
			destroy(c); 
			return false;
		}
		Hex front=c.getNeighbor(0);
		int addAmt;
		if (front.isEmpty()){
			addAmt=Math.min(c.mem[4],v);
			front.addFood(addAmt);
			c.mem[4]-=addAmt;
			if (!isAlive(c)) {
				destroy(c); 
			}
			return true;
		}
		else if (front.content.print()=="F"){
			addAmt=Math.min(c.mem[4],v);
			((Food)front.content).amt+=addAmt;
			c.mem[4]-=addAmt;
			if (!isAlive(c)) {
				destroy(c); 
			}
			return true;
		}
		return false;
	}
	/**
	 * Let critter {@code c} attack the critter directly in front of it.
	 * @param c critter to launch attack.
	 * @return true if the attack can happen.
	 */
	boolean attack(Critter c){
		c.mem[4]-=(c.mem[3]*WorldConstants.getConstant("ATTACK_COST"));
		if (!isAlive(c)) {
			destroy(c); 
			return false;
		}
		Hex front=c.getNeighbor(0);
		if (front.content!=null && !front.content.print().equals("#")
				&& !front.content.print().equals("F")){//
			Critter defender=(Critter)front.content;
			int damage=(int) (((double)WorldConstants.getConstant("BASE_DAMAGE")*c.mem[3])
					*(1/(1+Math.exp(-1*WorldConstants.getConstant("DAMAGE_INC")*(double)(c.mem[3]*c.mem[2]-defender.mem[3]*defender.mem[1])))));
			defender.mem[4]-=damage;
			if (!isAlive(defender)){
				destroy(defender);
				return true;
			}
		}
		return false;
	}
	/**
	 * Let critter {@code c} tag the critter in front of it as an enemy or a friend.
	 * @param c critter
	 * @param label label indicating friend or enemy
	 * @return true if the tagging is successful.
	 */
	boolean tag(Critter c, int label){
		c.mem[4]-=c.mem[3];
		if (c.mem[4]<0) {
				destroy(c); 
				return false;				
		}
		else if (c.mem[4]==0) destroy(c);
		Hex front=c.getNeighbor(0);
		if (front.content!=null && !front.content.print().equals("#")
				&& !front.content.print().equals("F")){
			((Critter)front.content).mem[6]=label;
			return true;
		}
		return false;
	}
	/**
	 * Let {@code c} increase size by 1
	 * @param c
	 * @return true if successful
	 */
	boolean grow(Critter c){
		c.mem[4]-=(c.mem[3]*c.complexity()*WorldConstants.getConstant("GROW_COST"));
		if (!isAlive(c)) {
			destroy(c); 
			return false;
		}
		c.mem[3]++;
		System.out.println("grow");
		return true;
	}
	/**
	 * Let critter {@code c} bud
	 * @param c
	 * @return true if successful
	 * @throws SyntaxError
	 */
	boolean bud(Critter c) throws SyntaxError{
		System.out.println("bud");
		c.mem[4]-=(WorldConstants.getConstant("BUD_COST")*c.complexity());
		if (c.mem[4]<0){
			destroy(c);
			return false;
		}
		else {
			ArrayList<int[]> parentMem=new ArrayList<int[]>();
			parentMem.add(c.mem);
			int[] memCopy=inheritanceMemory(parentMem);
			
			ProgramImpl newRuleSet=(ProgramImpl) c.ruleset.copy();
			
			Critter child=new Critter(c.species, memCopy,c.direction, newRuleSet, this);
			
			this.genomeMutation(child);
			
			int[] change=HexCoord.oneMove[(c.direction+3)%6];
			HexCoord newLocation=new HexCoord(c.location.c+change[0],c.location.r+change[1]);
			Hex back=getHex(newLocation);
			if (c.mem[4]==0){
				destroy(c);
			}
			if (back.isEmpty()){
				this.setCritter(child, newLocation);
				return true;
			}
			else{
				return false;
			}
		}
	}
	/**
	 * Establish a memory that inherits from parent memory sets {@code mems} 
	 * @param mems
	 * @return an inherited memory
	 */
	private int[] inheritanceMemory(ArrayList<int[]> mems){
		int[][] memArray=new int[8][mems.size()];
		for (int i=0; i<3; i++){
			for (int j=0; j<mems.size(); j++){
				memArray[i][j]=mems.get(j)[i];
			}
		}
		Random r=new Random();
		int[] childMem;
		if (r.nextInt(mems.size())==0){
			childMem=new int[memArray[0][0]];
		}
		else{
			childMem=new int[memArray[0][1]];
		}
		for (int i=0; i<3; i++){
			if (r.nextInt(mems.size())==0){
				childMem[i]=memArray[i][0];
			}
			else{
				childMem[i]=memArray[i][1];
			}
		}
		childMem[3]=1;
		childMem[4]=(int) WorldConstants.getConstant("INITIAL_ENERGY");
		childMem[5]=0;
		childMem[6]=0;
		childMem[7]=0;
		return childMem;
	}
	/**
	 * Establish an inherited rules from parent rule sets
	 * @param ruleSets
	 * @return an inherited rule set
	 */
	private ArrayList<Rule> inheritanceRules(ArrayList<ArrayList<Node>> ruleSets){
		Random r=new Random();
		int s1=ruleSets.get(0).size();
		int s2=ruleSets.get(1).size();
		int ruleSetSize=ruleSets.get(r.nextInt(ruleSets.size())).size();
		ArrayList<Rule> childRuleSet=new ArrayList<Rule>();
		int temp=Math.min(s1,s2);
		for (int i=0; i<temp; i++){
			childRuleSet.add((Rule) ruleSets.get(r.nextInt()).get(i).copy());
		}
		if (temp==ruleSetSize) return childRuleSet;
		if (s1>temp){
			for (int i=temp; i<s1; i++){
				childRuleSet.add((Rule) ruleSets.get(0).get(i).copy());
			}
		}
		else {
			for (int i=temp; i<s2; i++){
				childRuleSet.add((Rule) ruleSets.get(1).get(i).copy());
			}
		}
		return childRuleSet;
	}
	/**
	 * Mutate genes of {@code c}
	 * @param c
	 * @throws SyntaxError
	 */
	private void genomeMutation(Critter c) throws SyntaxError{
		Random r = new Random(), rAttribute=new Random();
		int temp;
		while ((temp=r.nextInt(8))<=1){
			switch(temp){
			case 0: int choice=rAttribute.nextInt(6);
					switch (choice){
						case 0: c.mem[0]++; break;
						case 1: c.mem[0]=Math.max(8, c.mem[0]-1); break;
						case 2: c.mem[1]++; break;
						case 3: c.mem[1]=Math.max(1, c.mem[1]-1); break;
						case 4: c.mem[2]++; break;
						case 5: c.mem[2]=Math.max(1, c.mem[2]-1); break;
					}
			case 1:c.ruleset.mutate();	
			}	
		}
	}
	
	/**
	 * Critter {@code c} requests mate with the critter right in front of it.
	 * If the critter in front faces toward it and attempts to mate in the same time step, 
	 * mating is successful.
	 * @param c the critter that requests mating.
	 * @return true if the mating is successful.
	 */
	
	boolean mate(Critter c){
		c.mem[4]-=c.mem[3];
		if (c.mem[4]<=0){
			destroy(c);
			return false;
		}
		Hex front=c.getNeighbor(0);
		if (front.content!=null && !front.content.print().equals("#")
				&& !front.content.print().equals("F")){
			Critter mater=(Critter)front.content;
			if (this.toMate.containsKey(mater) && this.toMate.get(mater).equals(c)){
				mater.mem[4]-= (WorldConstants.getConstant("MATE_COST")*mater.complexity());
				c.mem[4]-= (WorldConstants.getConstant("MATE_COST")*c.complexity());
				if (c.mem[4]<0){
					destroy(c);
					return false;
				}
				if (mater.mem[4]<0){
					destroy(mater);
					return false;
				}
				Random r=new Random();
				String ChildSpecies;
				switch(r.nextInt(2)){
				case 0: ChildSpecies=mater.species; break;
				default: ChildSpecies=c.species; break;
				}
				ArrayList<int[]> parentMem=new ArrayList<int[]>();
				parentMem.add(c.mem);
				parentMem.add(mater.mem);
				int[] newMem=inheritanceMemory(parentMem);
				ArrayList<Critter> possibleFollow = new ArrayList<Critter>();
				if (c.getNeighbor(3).isEmpty()){
					possibleFollow.add(c);
				}
				if (mater.getNeighbor(3).isEmpty()){
					possibleFollow.add(mater);
				}
				if (possibleFollow.size()==0) return false;	
				Critter follow=possibleFollow.get(r.nextInt(possibleFollow.size()));
				int[] change=HexCoord.oneMove[(follow.direction+3)%6];
				HexCoord newLocation=new HexCoord(follow.location.c+change[0],follow.location.r+change[1]);
				ArrayList<ArrayList<Node>> parentRuleSet=new ArrayList<ArrayList<Node>>();
				parentRuleSet.add(c.ruleset.getChildren());
				parentRuleSet.add(c.ruleset.getChildren());
				ArrayList<Rule> newRuleSet=inheritanceRules(parentRuleSet);
				ProgramImpl newProgram=new ProgramImpl(newRuleSet);
				Critter child=new Critter(ChildSpecies, newMem, follow.direction,newProgram, this);
				this.setCritter(child, newLocation);
				return true;
			}
			else if (faceToFace(c, mater)){
				this.toMate.put(c, mater);
				return false;
			}
		}
		return false;
	}
	/**
	 * Let Critter {@code c} wait
	 */
	void wait(Critter c){
		c.mem[4]+=WorldConstants.getConstant("SOLAR_FLUX")*c.mem[3];
	}
	@Override
	public HexCoord getCoordinates(Hex h) {
		for (int i=0; i<mapR;i++){
			for (int j=0; j<mapC; j++){
				if (i==this.getCoordinates(h).ConversionFromHexToMatrix().r
					&& j==this.getCoordinates(h).ConversionFromHexToMatrix().c)
					return MatrixCoord.ConversionFromMatrixToHex(i,j);
			}
		}
		return null;
	}
	/**
	 * Remove {@code c} from the world
	 * @param c
	 */
	void destroy(Critter c){
		ordering.Delete(c);;
		if (c.location!=null) {
			Hex cur=getHex(c.location);
			cur.clear();
			cur.addFood((int) (c.mem[3]*WorldConstants.getConstant("FOOD_PER_SIZE")));
		}
	}
	/**
	 * Advance the world with {@code n} steps
	 * @param n
	 */
	public void advance(int n) throws SyntaxError{
		for (int i=0; i<n; i++){
			LListNode temp=ordering.head;
			while (temp!=null){
				Critter c=temp.value;
				Interpreter itp=new InterpreterImpl();
				Outcome o=itp.interpret((ProgramImpl)c.ruleset.copy(),c);
				//System.out.println(c.mem[4]);
				this.carryOut(o, c);
				temp=temp.next;
			}
			this.timeStep++;
			this.toMate.clear();
		}
	}
	/**
	 * Check whether {@code c1} and {@code c2} are face to face
	 * @param c1
	 * @param c2
	 * @return true if face to face
	 */
	public boolean faceToFace(Critter c1, Critter c2){
		return (this.getCoordinates(c1.getNeighbor(0)).c==c2.location.c &&
				this.getCoordinates(c1.getNeighbor(0)).r==c2.location.r &&
				this.getCoordinates(c2.getNeighbor(0)).c==c1.location.c 
				 && this.getCoordinates(c2.getNeighbor(0)).r==c1.location.r);
	}
	/**
	 * Carry out the action specified by outcome {@code o} on critter {@code c}
	 * @param o outcome
	 * @param c critter
	 * @throws SyntaxError
	 */
	void carryOut(Outcome o, Critter c) throws SyntaxError{
		String s=o.getString();
		switch (s){
		case "forward": move(c,1); break;
		case "backward": move(c,-1); break;
		case "left": turn(c, -1); break;
		case "right": turn(c, 1); break;
		case "eat": eat(c); System.out.println(s); break;
		case "serve": serve(c, o.getInt()); break;
		case "attack": attack(c); System.out.println(s);System.out.println(s); break;
		case "tag": tag(c, o.getInt()); break;
		case "grow": grow(c); System.out.println(s);break;
		case "bud": bud(c); System.out.println(s); break;
		case "mate": mate(c);System.out.println(s); break;
		default: wait(c); break;
		}
	}
	/**
	 * Return current time step we're at
	 * @return
	 */
	@Override
	public int currentTimeStep() {
		return this.timeStep;
	}
	/**
	 * @return an array list of available hex
	 */
	public ArrayList<HexCoord> availableHex(){
		ArrayList<HexCoord> available=new ArrayList<HexCoord>();
		for (int i=0; i<mapR;i++){
			for (int j=0; j<mapC; j++){
				if (this.getHexWithMatrixCoord(i, j).isEmpty())
					available.add(MatrixCoord.ConversionFromMatrixToHex(i,j));
			}
		}
		return available;
	}
	/**
	 * As long as there is empty hex in the world, this method ensures success of
	 * randomly adding a rock to the world.
	 * @return false if the world is full.
	 */
	public boolean randomPlaceRock(){
		ArrayList<HexCoord> places=availableHex();
		if (places.size()==0) return false;
		Random r=new Random();
		this.setRock(places.get(r.nextInt(places.size())));
		return true;
	}
	/**
	 * As long as there is empty hex in the world, this method ensures success of
	 * randomly setting critter {@code c} to the world. In the case where
	 *  {@code c} is already placed
	 * in the world, it would be moved to the randomly generated location.
	 * @param c the critter to be randomly placed
	 * @return false if the world is full.
	 */
	public boolean randomPlaceCritter(Critter c){
		ArrayList<HexCoord> places=availableHex();
		if (places.size()==0) return false;
		Random r=new Random();
		this.setCritter(c, places.get(r.nextInt(places.size())));
		return true;
	}
	@Override
	public void worldInfo() {
		StringBuilder sb=new StringBuilder();
		sb.append("Number of time steps elapsed: "+this.currentTimeStep()+";\n");
		sb.append("Number of critters alive: "+this.ordering.Size()+";\n");
		this.printMap(sb);
		System.out.println(sb.toString());
	}
}
