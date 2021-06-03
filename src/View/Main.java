package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root1 = FXMLLoader.load(getClass().getResource("./First.fxml"));
        primaryStage.setTitle("Best Maze");
        primaryStage.setScene(new Scene(root1, 700, 500));
        Image applicationIcon = new Image(getClass().getResourceAsStream("../resources/Image/maze.png"));
        primaryStage.getIcons().add(applicationIcon);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}