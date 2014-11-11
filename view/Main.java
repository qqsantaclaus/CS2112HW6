package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import console.WorldConstants;
import simulation.MatrixCoord;
import simulation.WorldImpl;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Main extends Application {

	static int width = 1000;
	static int height = 618;
	static int mapwidth = 600;
	static int mapheight = 540;
	static int panewidth=310;
	static int paneheight=540;
	
	private WorldImpl world=null;
	private PrintBoard hexInfoContent;
	private PrintBoard worldInfoContent;
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void start(Stage stage) {
		stage.setTitle("critter world");
		Group outer = new Group();
		Scene scene = new Scene(outer);
		stage.setScene(scene);
		Pane content = new Pane();
		outer.getChildren().add(content);
		scene.getStylesheets().add("view/style.css");

		content.setPrefHeight(height);
		content.setPrefWidth(width);
		content.setId("content");

		// menu
		final MenuBar mb = new MenuBar();
		content.getChildren().add(mb);
		final Menu setup = new Menu("Set Up");
		final Menu advance = new Menu("Advance");
		final Menu helpMenu = new Menu("Help");
		mb.getMenus().addAll(setup, advance, helpMenu);
		final MenuItem newWorld = new MenuItem("New world");
		final MenuItem loadWorld = new MenuItem("Load world file");
		final MenuItem loadCritter = new MenuItem("Load critter file");
		setup.getItems().addAll(newWorld, loadWorld, loadCritter);
		final MenuItem advanceMenu = new MenuItem("Advance one setp");
		final MenuItem advanceRate = new MenuItem("Set advance rate");
		final MenuItem advanceStep = new MenuItem("Set advane time step");
		advance.getItems().addAll(advanceMenu, advanceRate, advanceStep);
		final MenuItem help = new MenuItem("Help");
		final MenuItem print = new MenuItem("Print world information");
		helpMenu.getItems().addAll(help, print);

		mb.setPrefWidth(width);
		//new world
		newWorld.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				System.out.println("new world in clicked");
				try {
					newWorld();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}			
		});

		//load world ifle
		loadWorld.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				System.out.println("load world file is clicked");
				LoadWindow loadWorld=new LoadWindow();
				Stage worldStage=new Stage();
				try {
					loadWorld.start(worldStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

		// map
		final ScrollPane sp = new ScrollPane();
		final Pane map = new Pane();
		content.getChildren().add(sp);
		map.setId("map");
		sp.setPrefSize(mapwidth, mapheight);
		sp.setLayoutX(30);
		sp.setLayoutY(50);
		sp.setContent(map);

		// pane
		Pane pane=new Pane();
		content.getChildren().add(pane);
		pane.setLayoutX(660);
		pane.setLayoutY(50);
		pane.setPrefSize(panewidth,paneheight);

		// world information
		Pane worldInfo = new Pane();
		worldInfo.setId("worldInfo");
		pane.getChildren().add(worldInfo);
		worldInfo.setPrefSize(panewidth, 100);
		worldInfo.setStyle("-fx-border-color:black");
		worldInfo.setLayoutX(0);
		worldInfo.setLayoutY(0);
		worldInfoContent=new PrintBoard();
		worldInfoContent.setAlignment(Pos.CENTER);
		worldInfoContent.setText("Welcome to Critter World");
		worldInfo.getChildren().add(worldInfoContent);
		
		Pane hexInfo = new Pane();
		hexInfo.setId("hexInfo");
		pane.getChildren().add(hexInfo);
		hexInfo.setPrefSize(panewidth, 280);
		hexInfo.setStyle("-fx-border-color:black");
		hexInfo.setLayoutX(0);
		hexInfo.setLayoutY(120);
		hexInfoContent=new PrintBoard();
		hexInfoContent.setAlignment(Pos.CENTER);
		hexInfoContent.setText("Welcome to Critter World!");
		hexInfo.getChildren().add(hexInfoContent);
		
		
		// advance setting
		VBox vb=new VBox();
		vb.setVisible(true);
		HBox hb1=new HBox();
		HBox hb2=new HBox();
		pane.getChildren().add(vb);
		TextField rate = new TextField();
		TextField step = new TextField();
		rate.setPrefWidth(40);
		step.setPrefWidth(40);
		final Label rateLabel=new Label("Enter advance rate (time steps/second):");
		final Label stepLabel=new Label("Enter advance time steps (natural number):");
		final Separator sp2=new Separator();
		sp2.setVisible(false);
		sp2.setPrefHeight(20);
		hb1.getChildren().addAll(rateLabel,rate);
		hb2.getChildren().addAll(stepLabel,step);
		vb.getChildren().addAll(hb1,sp2,hb2);
		vb.setLayoutX(0);
		vb.setLayoutY(420);
		
		// button hbox
		final HBox hb = new HBox();
		pane.getChildren().add(hb);
		hb.setLayoutX(0);
		hb.setLayoutY(513);

		final Button adv = new Button("Advance once");

		final Separator sp1=new Separator();
		sp1.setPrefWidth(50);
		sp1.setVisible(false);
		
		final Button start = new Button("Start/Stop");
		
		hb.getChildren().addAll(adv,sp1,start);

		adv.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent _) {
				System.out.println("advance button is clicked");
			}
		});

		start.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent _) {
				System.out.println("start button is clicked");
			}
		});

		
		outer.setFocusTraversable(true); // seems to be necessary to get key
											// events
		stage.sizeToScene();
		
		this.initializeHexMap(10, 20, map);
		stage.show();
	}
	

	/**
	 * Starts new random world simulation.
	 * 
	 * @throws FileNotFoundException
	 */
	private void newWorld() throws FileNotFoundException {
		loadConstant();
		world = new WorldImpl((int) WorldConstants.getConstant("COLUMNS"),
				(int) WorldConstants.getConstant("ROWS"));
		int times = (new Random().nextInt(this.world.availableHex().size())) / 4;
		for (int i = 0; i < times; i++) {
			this.world.randomPlaceRock();
		}
	}
	
	private void loadConstant() throws FileNotFoundException {
		Scanner scanner = new Scanner(new File("src/console/constant.txt"));
		while (scanner.hasNext()) {
			String s = scanner.nextLine();
			Scanner ss = new Scanner(s);
			String c = ss.next();
			double d = ss.nextDouble();
			WorldConstants.addConstant(c, d);
			ss.close();
		}
		scanner.close();
	}

	public void initializeHexMap(int c, int r, Pane p){
		double verticalShift=Math.sqrt(3)*MapHex.radius;
		double horizontalShift=MapHex.radius*3/2;
		Group g=new Group();
		int mapR=r;
		int mapC=c;
		if(mapR<=(int)mapC/2){
			mapR=mapC/2+1;
		}
		mapR=mapR-(int)(mapC/2);
		Double[] points=new Double[12];
		double startX=100;
		double startY=mapR*Math.sqrt(3)*MapHex.radius+100;
		double[] basicPoints={startX-MapHex.radius,startY,
				 startX-MapHex.radius/2,startY+Math.sqrt(3)/2*MapHex.radius,
				 startX+MapHex.radius/2,startY+Math.sqrt(3)/2*MapHex.radius,
				 startX+MapHex.radius,startY,
				 startX+MapHex.radius/2,startY-Math.sqrt(3)/2*MapHex.radius,
				 startX-MapHex.radius/2,startY-Math.sqrt(3)/2*MapHex.radius};
		
		for (int i=0;i<mapC;i++){
			for (int k=0; k<6; k++){
				points[2*k]=basicPoints[2*k]+horizontalShift*i;
				points[2*k+1]=basicPoints[2*k+1]-(i%2)*verticalShift*1/2;
			}
			for (int j=0;j<mapR-(i%2)*(mapC%2);j++){
				final MapHex hex=new MapHex(MatrixCoord.ConversionFromMatrixToHex(j, i));
				hex.getPoints().addAll(points);
				hex.setOnMouseClicked(new EventHandler<MouseEvent>(){
					@Override
					public void handle(MouseEvent arg0) {
						hex.activate();
						if (world!=null){
						hexInfoContent.setContent(world.printDescription(hex.getColumn(), hex.getRow()));
						}
					}
				});
				g.getChildren().add(hex);
				for (int k=0; k<6; k++){
					points[2*k+1]=points[2*k+1]-verticalShift;
				}
			}
		}
		p.getChildren().add(g);
	}
	
	
}
