package View;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutController implements Initializable {
    public Label text2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String str2 = "This game was created as a project in the course\n" +
                "'Advanced Topics in Programming, taken in 'Ben Gurion University' of Israel,\n" +
                "as part of the 'Software and Information Systems Engineering' program.\n" +
                "Created on our second year of studying on 2021.\n\n" +
                "The Creators:\n" +
                "   Adi Marom\n" +
                "   Ohad Miller\n\n" +
                "Algorithms used in the game:\n\n" +
                "   Maze Generating:\n" +
                "       - Prim algorithm for generating mazes\n" +
                "       - Simple algorithm\n\n" +
                "   Solving Mazes:\n" +
                "       - BreadthFirstSearch\n" +
                "       - DepthFirstSearch\n" +
                "       - BestFirstSearch\n\n";
        // https://github.com/ohad13/ATP-PartC.git
        text2.setText(str2);
    }
}
