package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.SearchableMaze;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.scene.control.TextField;
import javafx.util.Pair;

import java.io.File;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;



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
    HashMap<Pair<Integer, Integer>, Pair<String,Time>> topResult;

    @FXML
    private Parent MainScreenid ;

    @FXML
    private Text timer;
    @FXML
    Label userLable;
    @FXML
    Label topResultLable;



    Time time = new Time("00:00:0");
    Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1),
                    e -> {

                        time.oneSecondPassed();
                        timer.setText(time.getCurrentTime());
                    }));

    public  void displayUserName(String username){
        userLable.setText(username);
    }
    public void generateMaze(ActionEvent actionEvent) {
        if(topResult== null){
            topResult= new HashMap<>();
        }
        time.setTime(0,0,0);
        timer.setText(time.getCurrentTime());

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
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

    public void movePlayer(KeyEvent keyEvent) {
        int player_row_pos = mazeDisplayer.getRow_player();
        int player_col_pos = mazeDisplayer.getCol_player();
        switch (keyEvent.getCode()){
            case UP :
                if(maze.possibleToGo(player_row_pos -1,player_col_pos))
                    player_row_pos -=1;
                break;
            case DOWN:
                if(maze.possibleToGo(player_row_pos +1,player_col_pos))
                    player_row_pos+=1;
                break;
            case LEFT:
                if(maze.possibleToGo(player_row_pos ,player_col_pos-1))
                    player_col_pos-=1;
                break;
            case RIGHT:
                if(maze.possibleToGo(player_row_pos ,player_col_pos+1))
                    player_col_pos+=1;
                break;
            default:
                mazeDisplayer.setPlayerPos(player_row_pos ,player_col_pos);
        }

        mazeDisplayer.setPlayerPos(player_row_pos ,player_col_pos);

        // when maze is solved
        if(mazeDisplayer.getRow_player()==maze.getGoalPosition().getRowIndex() && mazeDisplayer.getCol_player()== maze.getGoalPosition().getColumnIndex()){
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setContentText( userLable.getText() + " are the best!! \n you finsh withn: " + time.getCurrentTime());
            a.show();
            timeline.stop();

            Time curTime = new Time(time);
            Pair<Integer, Integer> rowCol = new Pair(maze.getNumOfRow(),maze.getNumOfCol());
            Pair<String,Time> playerResult = new Pair(userLable.getText(),curTime);


            if(topResult.containsKey(rowCol)){ //if this current size alredy exist 
                Pair<String,Time> temp = topResult.get(rowCol);
                Time talbeTime = temp.getValue(); // get the time from the table
                if(curTime.isgreaterThen(talbeTime)){
                    topResult.remove(rowCol);
                    topResult.put(rowCol,playerResult);
                }
            }
            else {
                topResult.put(rowCol, playerResult);
            }
            updateTopResult();
        }
        keyEvent.consume();
    }

    public void mouseClick(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }
    public void updateTopResult(){
        int row,col;
        String usern,test="";
        Time t;
        for (Map.Entry<Pair<Integer, Integer>,  Pair<String,Time>> entry : topResult.entrySet()) {
            row = entry.getKey().getKey();
            col = entry.getKey().getValue();
            usern = entry.getValue().getKey();
            t = entry.getValue().getValue();
            test += "row = " + row + " col = " +col +  " username= " + usern + " time= " + t.getCurrentTime() + "\n";
        }
        topResultLable.setText(test);
    }
}
