package simulation;
/**
 * Matrix coordinates
 *
 */
public class MatrixCoord extends Coord {
	public MatrixCoord(int row, int col) {
		this.r=row;
		this.c=col;
	}

	public static HexCoord ConversionFromMatrixToHex(int r, int c){
		return new HexCoord(c, (r + 1 + (int) ((c + 1) / 2)) - 1);
	}

	public HexCoord ConversionFromMatrixToHex() {
		return ConversionFromMatrixToHex(this.r, this.c);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof MatrixCoord) {
			MatrixCoord temp = (MatrixCoord) o;
			return (temp.c == this.c && temp.r == this.r);
		}
		return false;
	}

	@Override
	public String toString() {
		return "("+r+","+c+")";
	}

}
