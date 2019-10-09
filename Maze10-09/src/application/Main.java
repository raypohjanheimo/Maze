package application;
	

import java.util.HashMap;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;



public class Main extends Application {
	
	
	int GameState; 	// The state or screen of the game we're in: 
					// 0=start, 1=maze selection screen, 2=character selection, 3=game, 4=death, 5=victory
	
	//maps a keycode to a boolean, says if key is pressed or not
	private HashMap<KeyCode,Boolean> keys = new HashMap<KeyCode,Boolean>();
	//list of mazelevels, iterates over mazemodel levels.
    private ArrayList<Node> walls = new ArrayList<Node>();

	
	private Pane root = new Pane();
	private Pane gamePlatform = new Pane();
	
	private Node hero;
	private int levelWidth;

	private void initGui() {
		
		
		GameState = 0;
		
		Text t = new Text(100,100,"This is a text sample");
		
		
		Button btn = new Button("Start Game");
		btn.setOnMouseClicked(e ->{
			
			GameState = 1;

			System.out.println("Detected " + e.getClass().getName() +
					" on a "+ e.getSource().getClass().getName());
		});
		
		//gradient background
		LinearGradient lg0 = new LinearGradient(
				    0, // start X 
	                0, // start Y
	                0, // end X
	                1, // end Y
	                true, // proportional
	                CycleMethod.NO_CYCLE, // cycle colors
	                // stops
	                new Stop(0.1f, Color.web("#000000",1.0)),
	                new Stop(1.0f, Color.web("#c471ed",0.8)));

		//create background
		Rectangle startscreen = new Rectangle(800,800);
		startscreen.setFill(lg0);
		
		
		//takes array from mazemodel, mupliplies length of array by 50px, allowing 
		//each item  except hero to take up 50px space 		
		

	      root.getChildren().addAll(startscreen, gamePlatform, btn, t);
			
					
					
		/*
	    if (isPressed(null))) {
	        GameState=1;
	      }
	    
	    */
	    
	    
	    
	    if (GameState == 1) {
	    	
	    	
	   
		//gradient background
		LinearGradient lg1 = new LinearGradient(
				    0, // start X 
	                0, // start Y
	                0, // end X
	                1, // end Y
	                true, // proportional
	                CycleMethod.NO_CYCLE, // cycle colors
	                // stops
	                new Stop(0.1f, Color.web("#12c2e9",1.0)),
	                new Stop(1.0f, Color.web("#c471ed",0.8)));
		//gradient tiles
		LinearGradient lg2 = new LinearGradient(
			    0, // start X 
                0, // start Y
                0, // end X
                1, // end Y
                true, // proportional
                CycleMethod.NO_CYCLE, // cycle colors
                // stops
                new Stop(0.1f, Color.web("#D3CCE3",1.0)),
                new Stop(1.0f, Color.web("#E9E4F0",1.0)));
	
		//create background
		Rectangle background = new Rectangle(800,800);
		background.setFill(lg1);
		//takes array from mazemodel, mupliplies length of array by 50px, allowing 
		//each item  except hero to take up 50px space 
		levelWidth = MazeModel.LEVEL1[0].length() * 50;

		
		for (int i = 0; i < MazeModel.LEVEL1.length; i++) {
            //obtain each row from 0 to final length of array from model
			String row = MazeModel.LEVEL1[i];
			//go through each character within that row
            for (int j = 0; j < row.length(); j++) {
                //parse each character to do something depending on the value
            	switch (row.charAt(j)) {
                   //if 0 do nothing
            		case '0':
                        break;
                    //if 1 create an entity which will be in the x and y position
                    case '1':
                    	//define x, y, width and height of mazewall
                        Node wall = create(j*50, i*50, 50, 50);
                        ((Shape) wall).setFill(lg2);
                        walls.add(wall);
                        break;
                }
            }
		}
		  hero = create(0, 100, 20, 20);
		  ((Shape) hero).setFill(lg2);
		  
	      root.getChildren().addAll(background, gamePlatform);
	      
	      
	      
	      // Enemy
			Circle ball = new Circle(10, Color.DARKSLATEBLUE);
		    ball.relocate(50,50);

	        root.getChildren().add(ball);

	        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20), new EventHandler<ActionEvent>() {
	        	double dx = 6; //Step on x or velocity
	        	double dy = 4; //Step on y
	        	
	            @Override
	            public void handle(ActionEvent t) {
	            	//move the ball
	            	ball.setLayoutX(ball.getLayoutX() + dx);
	            	ball.setLayoutY(ball.getLayoutY() + dy);

	                Bounds bounds = root.getBoundsInLocal();
	                
	                //If the ball reaches the left or right border make the step negative
	                if(ball.getLayoutX() <= (bounds.getMinX() + ball.getRadius()) || 
	                        ball.getLayoutX() >= (bounds.getMaxX() - ball.getRadius()) ){

	                	dx = -dx;

	                }

	                //If the ball reaches the bottom or top border make the step negative
	                if((ball.getLayoutY() >= (bounds.getMaxY() - ball.getRadius())) || 
	                        (ball.getLayoutY() <= (bounds.getMinY() + ball.getRadius()))){

	                	dy = -dy;

	                }
	                
	            }
	        }));
	        
	        timeline.setCycleCount(Timeline.INDEFINITE);
	        timeline.play();
	      
	}
	}
	
	//game logic, when keys are pressed call vertical or horizontal move methods. This method is looped through with animation timer.
	private void update() {
		 if (isPressed(KeyCode.W)) {
			    movePlayerVertical(-5);
	        }

	        if (isPressed(KeyCode.A)) {
	            movePlayerHorizontal(-5);
	        }

	        if (isPressed(KeyCode.D)) {
	            movePlayerHorizontal(5);
	        }

	        if(isPressed(KeyCode.S)) {
			    movePlayerVertical(5);
	        }
	}
	//, must fix, verrryy buggy right now
	//move hero 1 unit at a time by the value of x
	private void movePlayerHorizontal(int x) {
		
		 boolean movingRight = x > 0;
		 	//since x can be negative, we get the absolute value of x
	        for (int i = 0; i < Math.abs(x); i++) {
	        	//for all walls
	            for (Node wall : walls) {
	            	//get bounds of player and wall, if they intersect there is a collision
	                if (hero.getBoundsInParent().intersects(wall.getBoundsInParent())) {	                	
	                    if (movingRight) {
	                    	//checks if right side of plater is colliding with wall
	                    	//get hero's position, add 50 for its width and detect if it equals position of wall
	                        if (hero.getTranslateX() + 20 == wall.getTranslateX()) {
	                        	//cannot move
	                            hero.setTranslateX(hero.getTranslateX() - 1);

	                        	System.out.println(hero.getTranslateX());
	                        	System.out.println("colision-right");
	                            return;
	                        }
	                    }
	                    else {
	                    	//move left
	                    	//get left side position of player, check if it equals right of wall
	                        if (hero.getTranslateX() == wall.getTranslateX() + 50) {
	                            hero.setTranslateX(hero.getTranslateX() + 1);

	                        	System.out.println("colision-left");
	                            return;
	                        }
	                    }
	                }
	            }
	            
	            hero.setTranslateX(hero.getTranslateX() + (movingRight ? 1 : -1));
	        }
		
	}
	
	private void movePlayerVertical(int y) {
		 boolean movingDown = y > 0;

	        for (int i = 0; i < Math.abs(y); i++) {
	            for (Node wall : walls) {
	                if (hero.getBoundsInParent().intersects(wall.getBoundsInParent())) {
	                    if (movingDown) {
	                        if (hero.getTranslateY() + 20 == wall.getTranslateY()) {
	                            hero.setTranslateY(hero.getTranslateY() - 1);
	                            System.out.println(hero.getTranslateY());
	                            System.out.println("colisionbottom");
	                            return;
	                        }
	                    }
	                    else {
	                    	//move up
	                        if (hero.getTranslateY() == wall.getTranslateY() + 50) {
	                            hero.setTranslateY(hero.getTranslateY() + 1);
	                            System.out.println(hero.getTranslateY());
	                            System.out.println("colisiontop");

	                            return;
	                        }
	                    }
	                }
	            }
	            hero.setTranslateY(hero.getTranslateY() + (movingDown ? 1 : -1));
	        }
	}

	private Node create(int x, int y, int w, int h) {
		 Rectangle entity = new Rectangle(w, h);
	        entity.setTranslateX(x);
	        entity.setTranslateY(y);
	        entity.getProperties().put("alive", true);

	        gamePlatform.getChildren().add(entity);
	        return entity;
	}
	//get keycode from map defined (key), if not found return false by default. 
	//If found return the value of that key
	private boolean isPressed(KeyCode key) {
		return keys.getOrDefault(key, false);
	}
	

	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		
		initGui();			
		Scene scene = new Scene(root);
		
		//what key was pressed, set that in map of keys, set value to true or false
		scene.setOnKeyPressed(event -> 
			keys.put(event.getCode(), true)
		);
		scene.setOnKeyReleased(event -> 
			keys.put(event.getCode(), false)
		);
//		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setTitle("Maze it up");
		primaryStage.setScene(scene);
		primaryStage.show();
		// a loop that calls update method 60 times a second (60fps)
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				update();
			}
			
		};
		timer.start();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
