package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.SearchableMaze;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
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
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.scene.control.TextField;
import javafx.util.Pair;

import java.io.*;

import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import static javafx.scene.media.MediaPlayer.INDEFINITE;


public class MainScreenController implements  IView ,Initializable {
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
    private  Media sound;
    private MediaPlayer mediaPlayer;
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
    private  void readHashmap() throws IOException {
        // read hashtable from file
        if(topResult== null){
            topResult= new HashMap<>();

            File file = new File(System.getProperty("java.io.tmpdir"), "hashResult");
            file.createNewFile();
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                int row,col;
                String usern;
                String l;
                Time timetemp;
                Pair<Integer, Integer> rowCol ;
                Pair<String,Time> playerResult ;

                while((l = br.readLine()) != null) {
                    String[] args = l.split(",", 5);
                    if (args.length == 5) {
                        row = Integer.parseInt(args[1]);
                        col = Integer.parseInt(args[2]);
                        usern = args[3];
                        timetemp = new Time(args[4]); // TODO need to be time and not string

                        rowCol = new Pair(row,col);
                        playerResult = new Pair(usern,timetemp);
                        topResult.put(rowCol, playerResult);
                    }
                }

                br.close();
            } catch (IOException var24) {
                var24.printStackTrace();
            }
        }

    }
    public void generateMaze(ActionEvent actionEvent) throws IOException {
        playBackgorund();
        readHashmap();

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

        Pair<Integer, Integer> rowCol = new Pair(maze.getNumOfRow(),maze.getNumOfCol()); // if this maze size alredy exist, show the best result
        if(topResult.containsKey(rowCol)) {
            String txt = "The best time for " + maze.getNumOfRow() + "X" + maze.getNumOfCol() + " is: "+ topResult.get(rowCol).getValue().getCurrentTime();
            topResultLable.setText(txt);
        }else {
            topResultLable.setText("");
        }
        mazeDisplayer.requestFocus();
    }

    public void Back(ActionEvent actionEvent) throws IOException {
        if(topResult!=null) {
            writeHash();
        }
        Parent root = FXMLLoader.load(getClass().getResource("../View/First.fxml"));
        stage  = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void writeHash(){
        try {
            File file = new File(System.getProperty("java.io.tmpdir"), "hashResult");
            file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            ObjectOutputStream outFile = new ObjectOutputStream(new FileOutputStream(file));


            int row,col;
            String usern,test="";
            Time t;
            for (Map.Entry<Pair<Integer, Integer>,  Pair<String,Time>> entry : topResult.entrySet()) {
                row = entry.getKey().getKey();
                col = entry.getKey().getValue();
                usern = entry.getValue().getKey();
                t = entry.getValue().getValue();
                test = "," + row + "," +col +  "," + usern + "," + t.getCurrentTime() +"\n";
                outFile.writeObject(test);
            }
            bw.flush();
            bw.close();
        } catch (IOException var22) {
            var22.printStackTrace();

        }
    } //write hashtable to file
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
        playerMove();
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
            playWin();
            mediaPlayer.stop();
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setContentText( userLable.getText() + " you are the best!! \n you finsh withn: " + time.getCurrentTime());
            a.show();
            timeline.stop(); //stop the time

            Time curTime = new Time(time);
            Pair<Integer, Integer> rowCol = new Pair(maze.getNumOfRow(),maze.getNumOfCol());
            Pair<String,Time> playerResult = new Pair(userLable.getText(),curTime);


            if(topResult.containsKey(rowCol)){ //if this current size alredy exist 
                Pair<String,Time> temp = topResult.get(rowCol);
                Time talbeTime = temp.getValue(); // get the time from the table
                if(curTime.isgreaterThen(talbeTime)){ // if its the best time update the map
                    topResult.remove(rowCol);
                    topResult.put(rowCol,playerResult);
                }
            }
            else { // first time for this size
                topResult.put(rowCol, playerResult);
            }
        }
        keyEvent.consume();
    }

    public void mouseClick(MouseEvent mouseEvent) {

        mazeDisplayer.requestFocus();
    }
    private  void  playWin(){
        Media sound=new Media(new File("./src/Resources/Sound/win.mp3").toURI().toString());
        MediaPlayer mediaPlayer=new MediaPlayer(sound);
        mediaPlayer.play();
    }
    private  void  playerMove(){
        Media sound=new Media(new File("./src/Resources/Sound/move.wav").toURI().toString());
        MediaPlayer mediaPlayer=new MediaPlayer(sound);
        mediaPlayer.play();
    }
    private  void  playBackgorund(){
        sound=new Media(new File("./src/Resources/Sound/background.mp3").toURI().toString());
        mediaPlayer=new MediaPlayer(sound);
        mediaPlayer.setVolume(0.5);
        mediaPlayer.play();




    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
