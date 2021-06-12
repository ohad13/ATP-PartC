package View;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutController implements Initializable {
    public Label aboutText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String str = "Adi and Ohad looking for jobs, follow as on Linkdin!";
        // https://github.com/ohad13/ATP-PartC.git
        aboutText.setText(str);
    }
}
