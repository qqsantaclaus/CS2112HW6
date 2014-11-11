package simulation;

public abstract class Content {
	/**
	 * @return the symbol of the content, 
	 * where "F" represents food,
	 * "#" represents rock,
	 * and "0-5" represents a critter with corresponding direction.
	 */
	public abstract String print();
	/**
	 * Append the description of the content to a string builder {@code sb}
	 * @param sb a string builder that carries description of content.
	 */
	public abstract void printDescription(StringBuilder sb);
}
