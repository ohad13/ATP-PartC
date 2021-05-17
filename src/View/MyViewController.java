package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.SearchableMaze;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;

import java.io.IOException;
import java.nio.file.Files;


public class MyViewController implements IView{
    public MyMazeGenerator generator;
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public  Maze maze;

    public void generateMaze(ActionEvent actionEvent) {
        if(generator == null)
            generator = new MyMazeGenerator();
        int rows = Integer.parseInt(textField_mazeRows.getText());
        int cols = Integer.parseInt(textField_mazeColumns.getText());
        maze = generator.generate(rows, cols);
        mazeDisplayer.drawMaze(maze);
    }

    public void solveMaze(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Solving maze...");
        alert.show();
    }
    public  void CloseB(){
        System.out.println("closessssssss ");
        //TODO smart exit from project
        Platform.exit();
    }
    public void SaveB() throws IOException {
        if(maze != null){
           byte[] bmaze= maze.toByteArray();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Image");
            File file = fileChooser.showSaveDialog(mazeDisplayer.getScene().getWindow());
            if (file != null) {
                Files.write(file.toPath(), bmaze);

                System.out.println("shit");
            }
        }
    }
    public void LoadB(){ //TODO implent this function
       /* FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.showOpenDialog(mazeDisplayer.getScene().getWindow());
        File file = fileChooser.showOpenDialog(mazeDisplayer.getScene().getWindow());
        System.out.println("staart");
        if (file != null) {
            try {
                desktop.open(file);
            } catch (IOException ex) {

            }
            System.out.println("staart");


        }
*/
    }
}