package sample;

import View.FirstController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    /*public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("../View/MyView.fxml"));
        Parent root1 = FXMLLoader.load(getClass().getResource("C:\\Users\\adidi\\IdeaProjects\\ATP-PartCa\\src\\View\\First.fxml"));
        primaryStage.setTitle("Hello World");
        //primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.setScene(new Scene(root1, 900, 500));

        primaryStage.show();
    }*/
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("../View/First.fxml").openStream());
        primaryStage.setTitle("MAZES & DRAGONS");
        Scene scene = new Scene(root, 590, 402);
        scene.getStylesheets().add(getClass().getResource("/View/ViewStyle.css").toExternalForm());
        primaryStage.setScene(scene);
        //FirstController.currentStage = primaryStage;
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}