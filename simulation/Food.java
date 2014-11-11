package simulation;

/**
 * Representation of food
 *
 */
public class Food extends Content {
	int amt;
	
	public Food(int amt){
		this.amt=amt;
	}

	@Override
	public String print() {
		return "F";
	}

	@Override
	public void printDescription(StringBuilder sb) {
		sb.append(this.amt+"\n");
	}

}
