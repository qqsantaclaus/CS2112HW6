package simulation;

/**
 * A representation of critter world
 * @author apple
 *
 */
public interface World{
	/**
	 * Print the world map.
	 */
	void printMap(StringBuilder sb);
	/**
	 * Print the description of content of hex at hex coordinate ({@code c}, {@code r})
	 * @param c column of hex coordinate
	 * @param r row of hex coordinate
	 */
	String printDescription(int c,int r);
	/**
	 * Return the hex the given hex coordinates refer to.
	 * @param x column coordinate of hex
	 * @param y row coordinate of Hex
	 * @return the hex represented by (x,y)
	 */
	Hex getHex(int x, int y);
	/**
	 * Get the hex coordinates of a given hex {@code h} if exists.
	 * @param h a hex
	 * @return {@code h}'s hex coordinates in the world; 
	 * return null if the hex is not in the world.
	 */
	HexCoord getCoordinates(Hex h);
	/**
	 * @return the current time step the world is at.
	 */
	int currentTimeStep();
	/**
	 * Print out the information of world, including number of time steps elapsed, 
	 * number of critters alive, and a map of the world.
	 */
	void worldInfo();
}
