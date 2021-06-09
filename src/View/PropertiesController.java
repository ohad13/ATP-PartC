package View;

import Server.Configurations;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class PropertiesController implements Initializable {
    private static String generator;
    private static int NThreads;
    private static String searcher;
    private static int cnt = 0;
    @FXML
    Label generatorLbl;
    @FXML
    Label searcherLbl;
    @FXML
    ChoiceBox algorithmChoiceBox;
    @FXML
    ChoiceBox searchingAlgorithmChoiceBox;
    @FXML
    TextField textField_Nthreads;

    public static String getGenerator() {
        return generator;
    }

    public static String getSearcher() {
        return searcher;
    }

    public static int getNThreads() {
        return NThreads;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        algorithmChoiceBox.getItems().addAll("EmptyMazeGenerator", "SimpleMazeGenerator", "MyMazeGenerator");
        searchingAlgorithmChoiceBox.getItems().addAll("BreadthFirstSearch", "DepthFirstSearch", "BestFirstSearch");

        if (cnt == 0) {
            algorithmChoiceBox.setValue("MyMazeGenerator");
            searchingAlgorithmChoiceBox.setValue("BreadthFirstSearch");
            textField_Nthreads.setText("3");
            cnt++;
        } else {
            algorithmChoiceBox.setValue(Configurations.getInstance().getP("generateMaze"));
            searchingAlgorithmChoiceBox.setValue(Configurations.getInstance().getP("problemSolver"));
            textField_Nthreads.setText(Configurations.getInstance().getP("threadPoolSize"));
        }
        searcherLbl.setText(Configurations.getInstance().getP("problemSolver"));
        generatorLbl.setText(Configurations.getInstance().getP("generateMaze"));
        generator = generatorLbl.getText();
        searcher = searcherLbl.getText();
        NThreads = Integer.parseInt(Configurations.getInstance().getP("threadPoolSize"));
    }

    public void updateProp(ActionEvent actionEvent) {
        try{
            NThreads = Integer.parseInt(textField_Nthreads.getText());
            generator = algorithmChoiceBox.getValue().toString();
            searcher = searchingAlgorithmChoiceBox.getValue().toString();
        } catch (NumberFormatException e) {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText("Wrong Parameters, Please insert number only at the Thread number..");
            a.show();
        }
    }
}