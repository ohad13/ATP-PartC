package View;

import Server.Configurations;
import ViewModel.MyViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;


public class SettingsController implements Initializable {
    @FXML
    ChoiceBox algorithmChoiceBox;
    @FXML
    ChoiceBox searchingAlgorithmChoiceBox;
    //@FXML
    //Button setSettingsB;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        algorithmChoiceBox.getItems().addAll("EmptyMazeGenerator", "SimpleMazeGenerator", "MyMazeGenerator");
        searchingAlgorithmChoiceBox.getItems().addAll("BreadthFirstSearch", "DepthFirstSearch", "BestFirstSearch");
        algorithmChoiceBox.setValue("MyMazeGenerator");
        searchingAlgorithmChoiceBox.setValue("BFS");

        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("resources/config.properties"));

            String a1 = properties.getProperty("problemSolver");
            String a2 = properties.getProperty("generateMaze");
            switch (a1) {
                case "BestFirstSearch":
                    searchingAlgorithmChoiceBox.setValue("BestFirstSearch");
                    break;
                case "DepthFirstSearch":
                    searchingAlgorithmChoiceBox.setValue("DepthFirstSearch");
                    break;
                case "BreadthFirstSearch":
                    searchingAlgorithmChoiceBox.setValue("BreadthFirstSearch");
                    break;
            }
            switch (a2) {
                case "MyMazeGenerator":
                    algorithmChoiceBox.setValue("MyMazeGenerator");
                    break;
                case "SimpleMazeGenerator":
                    algorithmChoiceBox.setValue("SimpleMazeGenerator");
                    break;
                case "EmptyMazeGenerator":
                    algorithmChoiceBox.setValue("EmptyMazeGenerator");
                    break;
            }
        } catch (Exception e) {
        }
    }

    public void UpdateClicked() throws IOException {// save settings button.
        // update the config with the new settings.
        Configurations.setP("generateMaze", algorithmChoiceBox.getValue().toString());
        Configurations.setP("problemSolver", searchingAlgorithmChoiceBox.getValue().toString());

        //FXMLLoader loader2 =  FXMLLoader(getClass().getResource("./MainScreen.fxml")); // use to pass user name between 2 scene

        //MainScreenController mainScreenController = loader.getController();
        //mainScreenController.saveSettings();

        //Stage stage = (Stage) setSettingsB.getScene().getWindow();
        //stage.close();// close the window.
    }
}