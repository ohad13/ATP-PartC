package View;

import Server.Configurations;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class PropertiesController implements Initializable {
    @FXML
    Label generatorLbl;
    @FXML
    Label searcherLbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        searcherLbl.setText( Configurations.getInstance().getP("problemSolver"));
        generatorLbl.setText( Configurations.getInstance().getP("generateMaze"));
    }
}
