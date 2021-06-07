package View;

import Server.Configurations;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class PropertiesController implements Initializable {
    @FXML
    Label generatorLbl;
    @FXML
    Label searcherLbl;
    private static String generator;
    private static String searcher;
    private static int cnt = 0;
    @FXML
    ChoiceBox algorithmChoiceBox;
    @FXML
    ChoiceBox searchingAlgorithmChoiceBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        algorithmChoiceBox.getItems().addAll("EmptyMazeGenerator", "SimpleMazeGenerator", "MyMazeGenerator");
        searchingAlgorithmChoiceBox.getItems().addAll("BreadthFirstSearch", "DepthFirstSearch", "BestFirstSearch");
        if (cnt == 0) {
            algorithmChoiceBox.setValue("MyMazeGenerator");
            searchingAlgorithmChoiceBox.setValue("BreadthFirstSearch");
            cnt++;
        } else {
            algorithmChoiceBox.setValue(Configurations.getInstance().getP("generateMaze"));
            searchingAlgorithmChoiceBox.setValue(Configurations.getInstance().getP("problemSolver"));
        }
        searcherLbl.setText(Configurations.getInstance().getP("problemSolver"));
        generatorLbl.setText(Configurations.getInstance().getP("generateMaze"));
        generator = generatorLbl.getText();
        searcher = searcherLbl.getText();
    }

    public static String getGenerator() {
        return generator;
    }

    public static String getSearcher() {
        return searcher;
    }

    public void updateProp(ActionEvent actionEvent) {
        generator = algorithmChoiceBox.getValue().toString();
        searcher = searchingAlgorithmChoiceBox.getValue().toString();
    }
}
