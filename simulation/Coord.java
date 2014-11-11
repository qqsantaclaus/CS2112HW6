package simulation;

public abstract class Coord {
	public int r;
	public int c;

	@Override
	public abstract boolean equals(Object o);
	@Override
	public abstract String toString();
}
