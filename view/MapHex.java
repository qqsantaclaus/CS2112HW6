package view;

import simulation.HexCoord;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class MapHex extends Polygon {
	private HexCoord location;
	private double StrokeWidth=1;
	static MapHex hasClicked=null;
	static double radius=30;
	
	public MapHex(HexCoord location){
		this.location=location;
		setFill(Color.WHITE);
		setStroke(Color.GREEN);
		setStrokeWidth(StrokeWidth);
	}
	
	public int getColumn(){
		return this.location.c;
	}
	public int getRow(){
		return this.location.r;
	}
	public void activate(){
		if (hasClicked!=null){
			hasClicked.inactivate();
		}
		setStrokeWidth(4);
		hasClicked=this;
	}
	public void inactivate(){
		setStrokeWidth(StrokeWidth);
	}
}
