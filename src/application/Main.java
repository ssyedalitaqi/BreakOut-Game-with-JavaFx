package application;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.animation.*;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.util.Duration;
import java.util.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;


public class Main extends Application {
   static int score=0;
    @Override
    public void start(Stage primaryStage) {
    	
        //creates main layout
    	Label text = new Label();
    	text.setText("Game Over\nScore "+score);
    	text.setTextFill(Color.WHITE);
    	text.setFont(new Font("Arial",60));
    	Pane stackPane = new Pane(text);
    	stackPane.setTranslateX(150);
    	stackPane.setTranslateY(80);
    	Button btn=new Button("Play Again");
    	Button btn1 = new Button("Quit");
    	HBox hbox = new HBox();
    	hbox.getChildren().addAll(btn,btn1);
    	hbox.setSpacing(10);
    	hbox.setTranslateX(250);
    	hbox.setTranslateY(200);
    	VBox VBox1 = new VBox();
    	VBox1.getChildren().addAll(stackPane,hbox);
    	VBox1.setStyle("-fx-background-color: black");
    	Scene overScene=new Scene(VBox1,610,440);
        Stage overStage = new Stage();
    	overStage.setScene(overScene);
    	btn.setOnAction(event->{
    		overStage.close();
    		start(primaryStage);
    	});
    	btn1.setOnAction(event->{
    		overStage.close();
    		
    	});
    	//uPane
    	Pane uPane = new Pane();
    	Label label = new Label();
    	label.setText(0+"");
    	label.setTranslateX(300);
    	label.setTranslateY(10);
    	uPane.getChildren().add(label);
    	uPane.setPadding(new Insets(30,0,0,0));
        Pane pane= new Pane();
        pane.setStyle("-fx-background-color: black");
        VBox vbox = new VBox();
        vbox.getChildren().addAll(uPane,pane);
        Scene scene= new Scene(vbox,610,440);
    
        //creates components and adds them to the main layout
    
        ArrayList<Rectangle> allBricks = new ArrayList<>();
        for(int x=0; x<10; x++) {
            for(int y=0; y<8; y++) {
                Rectangle brick=new Rectangle(60,15);
                if(y==0) {
                	brick.setFill(Color.SANDYBROWN);
                }
                if(y==1) {
                	brick.setFill(Color.BISQUE);
                }
                if(y==2) {
                	brick.setFill(Color.DARKCYAN);
                }
                if(y==3) {
                	brick.setFill(Color.CORNFLOWERBLUE);
                }
                if(y==4) {
                	brick.setFill(Color.LIGHTPINK);
                }
                if(y==5) {
                	brick.setFill(Color.LIGHTGREEN);
                }
                if(y==6) {
                	brick.setFill(Color.PEACHPUFF);
                }
                if(y==7) {
                	brick.setFill(Color.PLUM);
                }
                brick.setLayoutX(x*61);
                brick.setLayoutY((16*y)+35);
                pane.getChildren().add(brick);
                allBricks.add(brick);
            }
        }
        Rectangle ball= new Rectangle(15,15, Color.RED);
        ball.relocate(50, 200);
    
        Rectangle paddle= new Rectangle(90,7, Color.WHITE);
        paddle.relocate(275, 390);
     
        pane.getChildren().addAll(paddle, ball);
     
        //controls paddle movement
      class KeyHandler implements EventHandler<ActionEvent> {

			@Override
			public void handle(ActionEvent event) {
				
				
			}
    	  
      }
       int movement = 25;
    
       scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                if(paddle.getLayoutX() < 0) {
                    return;
                }
                paddle.setLayoutX(paddle.getLayoutX()-movement);
            }

            if (event.getCode() == KeyCode.RIGHT) {
                if(paddle.getLayoutX() > 510) {
                     paddle.setLayoutX(510);
                } 
                paddle.setLayoutX(paddle.getLayoutX()+movement);
            }
         });
    
        //creates an indefinite bouncing ball
       class eventHandler implements EventHandler<ActionEvent>{
    	  
               int count=0;
               double dx = 5; 
               double dy =  3;   
           
              @Override
               public void handle(ActionEvent t) {
                   //ball movement
                   ball.setLayoutX(ball.getLayoutX() + dx);
                   ball.setLayoutY(ball.getLayoutY() + dy);
               
                   boolean leftWall = ball.getLayoutX() <= 0; 
                   boolean topWall = ball.getLayoutY() < 0;
                   boolean rightWall = ball.getLayoutX() >= 590;
                   boolean bottomWall = ball.getLayoutY() >= 410;
               
                   // If the top wall has been touched, the ball reverses direction.
                   if (topWall) {
                      dy = dy * -1;
                   }
               
                   // If the left or right wall has been touched, the ball reverses direction.
                   if (leftWall || rightWall) {
                       dx = dx * -1;
                   }
                   if(bottomWall) {
                       dy = 3;
                       dx=5;
                       ball.relocate(50, 200);
                       count++;
                       if(count==3) {
                    	   text.setText("Game Over\n   Score "+score);
                    	   primaryStage.close();
                    	   overStage.show();
                       }
                   }
               
                   //if ball collides with paddle
                   if (collide(paddle)) {
                   dy = -dy;
                   }
                            
                   //if ball and brick collides, remove brick
               
                   Rectangle temp=null;
                   for(Rectangle brick:allBricks) { 
                       if(collide(brick)) {
                    	   score+=2;
                    	   label.setText((Integer.parseInt(label.getText())+2)+"");
                           temp=brick;
                           pane.getChildren().remove(brick);
                           dy=-dy;
                       }
                   }
                   allBricks.remove(temp);
                   temp=null;
               }
               public boolean collide(Rectangle paddle) {
                   Shape collisionArea = Shape.intersect(ball, paddle);
                   return collisionArea.getBoundsInLocal().getWidth() != -1;
               }
          
    	   
       }
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(25),new eventHandler()));
   
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
 
        primaryStage.setTitle("Breakout Game!");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
