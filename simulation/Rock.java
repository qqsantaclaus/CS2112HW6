package simulation;

/**
 * A representation of rock
 * @author apple
 *
 */
public class Rock extends Content{

	@Override
	public String print() {
		return "#";
	}

	@Override
	public void printDescription(StringBuilder sb) {
		sb.append("Rock\n");
	}

}
