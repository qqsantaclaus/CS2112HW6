package interpret;

/**
 * An implementation for representing an outcome of interpreting a critter
 * program. The outcome contains a string and an integer, which together
 * represents the action to be carried out.
 */
public class OutcomeImpl implements Outcome {
	private String s;
	private int i;

	OutcomeImpl(String s, int i) {
		this.s = s;
		this.i = i;
	}

	/**
	 * get the string representation of the outcome
	 * 
	 * @return a string representation of the outcome
	 */

	public String getString() {
		return s;
	}

	/**
	 * get the integer needed for the action, return -1 is the action does not
	 * need the integer
	 * 
	 * @return an integer that is the expression contained in the action return
	 *         -1 if the action is not tag or serve
	 */
	public int getInt() {
		return i;
	}

}
