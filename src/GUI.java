import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;  
import javafx.scene.control.Label;  
import javafx.scene.layout.*;  
import javafx.stage.Stage;  
public class GUI extends Application {  
  
    @Override  
    public void start(Stage primaryStage) throws Exception {  
        BorderPane BPane = new BorderPane();
        Label btmLabel = new Label("test");
        Label test = new Label("Center");
        BPane.setTop(new Label("This will be at the top"));  
        BPane.setLeft(new Label("This will be at the left"));  
        BPane.setRight(new Label("This will be at the Right"));  
        BPane.setCenter(test);  
        BPane.setBottom(btmLabel);  
        BorderPane.setAlignment(test, Pos.CENTER_LEFT);
        BorderPane.setAlignment(btmLabel, Pos.CENTER);
        Scene scene = new Scene(BPane,600,400);  
        primaryStage.setScene(scene);  
        primaryStage.show();  
    }  
    public static void main(String[] args) {  
        launch(args);  
    }  
      
}  