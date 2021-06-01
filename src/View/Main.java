package View;

import Model.IModel;
import Model.MyModel;
import View.FirstController;
import View.MainScreenController;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileInputStream;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root1 = FXMLLoader.load(getClass().getResource("./First.fxml"));
        primaryStage.setTitle("Best Maze");
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setScene(new Scene(root1, 700, 500));


       /* FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("./MainScreen.fxml"));
        //fxmlLoader.setLocation(getClass().getResource("./MainScreen.fxml"));
        fxmlLoader.load();
*/





/*      Image applicationIcon = new Image(getClass().getResourceAsStream("../Resources/Image/wall.png"));
        primaryStage.getIcons().add(applicationIcon);//todo*/
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}