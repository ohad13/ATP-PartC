package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.SearchableMaze;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class MyViewController implements IView{
    public MyMazeGenerator generator;
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;

    public void generateMaze(ActionEvent actionEvent) {
        if(generator == null)
            generator = new MyMazeGenerator();
        int rows = Integer.parseInt(textField_mazeRows.getText());
        int cols = Integer.parseInt(textField_mazeColumns.getText());
        Maze maze = generator.generate(rows, cols);
        mazeDisplayer.drawMaze(maze);
    }

    public void solveMaze(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Solving maze...");
        alert.show();
    }
}