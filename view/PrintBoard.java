package view;



import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class PrintBoard extends Label{
	private Font f;
	
	public PrintBoard(){
		super();
		f=new Font(16);
	}
	
	public void setContent(String s){
		this.setText(s);
		this.setFont(f);
		
	}

}
