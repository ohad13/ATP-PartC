package View;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class HelpController implements Initializable {
    public Label instruction;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String str = "Maze Rules:" + '\n'
                + "1. The character can be moved only to empty cells (non-wall cells)." + '\n'
                + "2. In order to solve the maze, you need to reach the goal cell." + '\n'
                + "Please help the disinfectant man get the vaccine!\n" +
                "Game Instructions:" + '\n' + "Use the NumPad numbers to move the character:" + '\n' +
                "UP - 8       DOWN - 2"  + "       RIGHT - 6       LEFT - 4" + '\n'
                + "Diagonal Moves:" + '\n' + "UP-LEFT - 7       DOWN-LEFT - 1"  + "       UP-RIGHT - 9       DOWN-RIGHT - 3" + '\n';
        instruction.setText(str);
    }
}
