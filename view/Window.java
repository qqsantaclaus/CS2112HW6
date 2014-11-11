package view;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Window extends Application{

	static int width=500;
	static int height=400;
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("print world information");
		Group outer = new Group();
		Scene scene = new Scene(outer);
		stage.setScene(scene);
		Pane content = new Pane();
		outer.getChildren().add(content);
		scene.getStylesheets().add("view/style.css");
		
		content.setPrefSize(width, height);
		content.setId("content");
		
		Label label=new Label("world name:");
		content.getChildren().add(label);
		label.setLayoutX(40);
		label.setLayoutY(40);
		
		
		outer.setFocusTraversable(true); // seems to be necessary to get key
		// events
		stage.sizeToScene();
		stage.show();
	}
	
}
