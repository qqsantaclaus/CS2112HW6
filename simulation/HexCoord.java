package simulation;

/**
 * Hex coordinates
 *
 */
public class HexCoord extends Coord{
	
	static final int[][] oneMove={{0, 1}, 
								  {1, 1}, 
	                              {1, 0}, 
	                              {0, -1}, 
	                              {-1,-1}, 
	                              {-1, 0}};
	
	public HexCoord(int c, int r){
		this.r=r;
		this.c=c;
	}
	/**
	 * Convert hex coordinate to matrix coordinate
	 * @return matrix coordinate
	 */
	public MatrixCoord ConversionFromHexToMatrix(){
			return ConversionFromHexToMatrix(this.c, this.r);
	}
	/**
	 * Convert hex coordinate to matrix coordinate
	 * @return matrix coordinate
	 */
	public static MatrixCoord ConversionFromHexToMatrix(int c, int r){
		int col=c;
		int row=(r-(int)(c+1)/2);
		return new MatrixCoord(row, col);
	}
	@Override
	public boolean equals(Object o) {
		if (o instanceof HexCoord){
			HexCoord temp=(HexCoord)o;
			return (temp.c==this.c && temp.r==this.r);
		}
		return false;
	}
	@Override
	public String toString() {
		return "("+c+","+r+")";
	}
}
