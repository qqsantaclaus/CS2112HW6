package interpret;

/**
 * An interface for representing an outcome of interpreting a critter program.
 * The outcome contains a string and an integer, which together represents the
 * action to be carried out.
 */
public interface Outcome {
	/**
	 * get the string representation of the outcome
	 * 
	 * @return a string representation of the outcome
	 */
	public String getString();

	/**
	 * get the integer needed for the action, return -1 is the action does not
	 * need the integer
	 * 
	 * @return an integer that is the expression contained in the action return
	 *         -1 if the action is not tag or serve
	 */
	public int getInt();
}
