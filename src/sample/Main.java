package sample;

import View.FirstController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("../View/MyView.fxml"));
        Parent root1 = FXMLLoader.load(getClass().getResource("../View/First.fxml"));
        primaryStage.setTitle("Best Maze");

        primaryStage.initStyle(StageStyle.UTILITY);
        //primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.setScene(new Scene(root1, 700, 500));

        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}