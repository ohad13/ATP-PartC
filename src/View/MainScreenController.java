package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.SearchableMaze;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;

import java.io.IOException;
import java.nio.file.Files;


public class MainScreenController implements  IView {
    public MyMazeGenerator generator;
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public Maze maze;
    public Stage stage;
    public Scene scene;
    private  Parent root;
    public Pane paneB;
    @FXML
    private Parent MainScreenid ;

    public void generateMaze(ActionEvent actionEvent) {
        if(generator == null)
            generator = new MyMazeGenerator();
        int rows = Integer.parseInt(textField_mazeRows.getText());
        int cols = Integer.parseInt(textField_mazeColumns.getText());
        maze = generator.generate(rows, cols);
        paneB.setStyle("-fx-border-color: #ff0000; -fx-border-width: 5;");

        mazeDisplayer.drawMaze(maze);
    }

    public void Back(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../View/First.fxml"));
        stage  = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



    public void AboutF(ActionEvent actionEvent) throws IOException {
        Stage secondStage = new Stage();

        Parent root1 = FXMLLoader.load(getClass().getResource("../View/About.fxml"));
        Stage stage = (Stage) MainScreenid.getScene().getWindow();
        scene = new Scene(root1);
        secondStage.setScene(scene);
        secondStage.show();
    }
}
