package console;

import java.util.HashMap;

public class WorldConstants {
	
	private static HashMap<String, Double> constant=new HashMap<String,Double>();
	
	public static void addConstant(String name, double value){
		constant.put(name, value);;
	}
	public static double getConstant(String name){
		return constant.get(name);
	}
}
