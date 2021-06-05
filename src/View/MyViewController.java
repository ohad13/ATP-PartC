package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.SearchableMaze;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MyViewController implements IView{
    public MyMazeGenerator generator;
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public  Maze maze;
    public Stage stage;
    public Scene scene;
    private  Parent root;
    private Pane panb;

    /*public void solveMaze(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Solving maze...");
        alert.show();
    }
    public void CloseB(){
        System.out.println("close");
        Platform.exit();
    }
    public void SaveB() throws IOException {
        if(maze != null){
           byte[] bMaze= maze.toByteArray();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Image");
            File file = fileChooser.showSaveDialog(mazeDisplayer.getScene().getWindow());
            if (file != null) {
                Files.write(file.toPath(), bMaze);
                System.out.println("shit");
            }
        }
    }
    public void LoadB(){
       *//* FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.showOpenDialog(mazeDisplayer.getScene().getWindow());
        File file = fileChooser.showOpenDialog(mazeDisplayer.getScene().getWindow());
        System.out.println("start");
        if (file != null) {
            try {
                desktop.open(file);
            } catch (IOException ex) {

            }
            System.out.println("start");
        }
    }*/
}