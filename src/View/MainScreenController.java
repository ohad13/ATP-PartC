package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainScreenController implements IView, Initializable {
    public MyMazeGenerator generator;
    private  boolean isSolved;
    public MazeDisplayer mazeDisplayer;
    public Maze maze;
    public Stage stage;
    public Scene scene;
    public Pane paneB;

    HashMap<Pair<Integer, Integer>, Pair<String, Time>> topResult;
    Time time = new Time("00:00:0");
    @FXML
    TextField textField_mazeRows;
    @FXML
    TextField textField_mazeColumns;
    @FXML
    Label userLable;
    @FXML
    Label topResultLable;
    @FXML
    Text timer;
    Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(0.01), e -> {
                time.oneSecondPassed();
                timer.setText(time.getCurrentTime());
            }));
    private Media backsound;
    private MediaPlayer mediaPlayer;
    @FXML
    private Parent MainScreenid;

    public void displayUserName(String username) {
        userLable.setText(username);
    }

    private void readHashmap() throws IOException {
        // read hashtable from file
        if (topResult == null) {
            topResult = new HashMap<>();

            File file = new File(System.getProperty("java.io.tmpdir"), "hashResult"); //C:\Users\adidi\AppData\Local\Temp
            file.createNewFile();
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                int row, col;
                String usern;
                String l;
                Time timetemp;
                Pair<Integer, Integer> rowCol;
                Pair<String, Time> playerResult;

                while ((l = br.readLine()) != null) {
                    String[] args = l.split(",", 5);
                    if (args.length == 5) {
                        row = Integer.parseInt(args[1]);
                        col = Integer.parseInt(args[2]);
                        usern = args[3];
                        timetemp = new Time(args[4]);

                        rowCol = new Pair(row, col);
                        playerResult = new Pair(usern, timetemp);
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
        readHashmap();

        isSolved=false;
        try { //check for valid input
            if (generator == null)
                generator = new MyMazeGenerator();

            int rows = Integer.parseInt(textField_mazeRows.getText());
            int cols = Integer.parseInt(textField_mazeColumns.getText());
            maze = generator.generate(rows, cols);

        } catch (Exception e) {
            errorSound();
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText("Wrong Parameters, Please insert 2 numbers bigger then 2");

            //clean the canvas
            mazeDisplayer.cleanCanvas();
            paneB.setStyle("-fx-border-color: #eeeeee; -fx-border-width: 0;");

            a.show();
            return;
        }

        mazeDisplayer.drawMaze(maze);
        mazeDisplayer.widthProperty().bind(paneB.widthProperty());
        mazeDisplayer.heightProperty().bind(paneB.heightProperty());

        // if this maze size already exist, show the best result
        Pair<Integer, Integer> rowCol = new Pair(maze.getNumOfRow(), maze.getNumOfCol());
        if (topResult.containsKey(rowCol)) {
            String txt = "The best time for " + maze.getNumOfRow() + "X" + maze.getNumOfCol() + " is: " + topResult.get(rowCol).getValue().getCurrentTime();
            topResultLable.setText(txt);
        } else {
            topResultLable.setText("");
        }
        mazeDisplayer.requestFocus();

        time.setTime(0, 0, 0);
        timer.setText(time.getCurrentTime());
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    //sent you to first page
    public void Back(ActionEvent actionEvent) throws IOException {
        if (topResult != null) {
            writeHashToFile();
        }
        Parent root = FXMLLoader.load(getClass().getResource("../View/First.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // write to the hash to the file
    public void writeHashToFile() {
        try {
            File file = new File(System.getProperty("java.io.tmpdir"), "hashResult");
            file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            ObjectOutputStream outFile = new ObjectOutputStream(new FileOutputStream(file));

            int row, col;
            String usern, test = "";
            Time t;
            for (Map.Entry<Pair<Integer, Integer>, Pair<String, Time>> entry : topResult.entrySet()) {
                row = entry.getKey().getKey();
                col = entry.getKey().getValue();
                usern = entry.getValue().getKey();
                t = entry.getValue().getValue();
                test = "," + row + "," + col + "," + usern + "," + t.getCurrentTime() + "\n";
                outFile.writeObject(test);
            }
            bw.flush();
            bw.close();
        } catch (IOException var22) {
            var22.printStackTrace();
        }
    }

    public void movePlayer(KeyEvent keyEvent) {
        if(isSolved)
            return;
        int player_row_pos = mazeDisplayer.getRow_player();
        int player_col_pos = mazeDisplayer.getCol_player();
        switch (keyEvent.getCode()) {
            case UP:
            case NUMPAD8:
                if (maze.possibleToGo(player_row_pos - 1, player_col_pos)) {
                    player_row_pos -= 1;
                    playerMoveSound();
                } else playerWorngMoveSound();

                break;
            case DOWN:
            case NUMPAD2:
                if (maze.possibleToGo(player_row_pos + 1, player_col_pos)) {
                    player_row_pos += 1;
                    playerMoveSound();
                } else playerWorngMoveSound();

                break;
            case LEFT:
            case NUMPAD4:
                if (maze.possibleToGo(player_row_pos, player_col_pos - 1)) {
                    player_col_pos -= 1;
                    playerMoveSound();
                } else playerWorngMoveSound();

                break;
            case RIGHT:
            case NUMPAD6:
                if (maze.possibleToGo(player_row_pos, player_col_pos + 1)) {
                    player_col_pos += 1;
                    playerMoveSound();
                } else playerWorngMoveSound();
                break;
            case NUMPAD7:
                if (maze.possibleToGo(player_row_pos-1, player_col_pos - 1)) {
                    player_col_pos += -1;
                    player_row_pos += -1;
                    playerMoveSound();
                } else playerWorngMoveSound();
                break;
            case NUMPAD9:
                if (maze.possibleToGo(player_row_pos-1, player_col_pos + 1)) {
                    player_col_pos += 1;
                    player_row_pos += -1;
                    playerMoveSound();
                } else playerWorngMoveSound();
                break;
            case NUMPAD3:
                if (maze.possibleToGo(player_row_pos+1, player_col_pos + 1)) {
                    player_col_pos += 1;
                    player_row_pos += 1;
                    playerMoveSound();
                } else playerWorngMoveSound();
                break;
            case NUMPAD1:
                if (maze.possibleToGo(player_row_pos+1, player_col_pos - 1)) {
                    player_col_pos += -1;
                    player_row_pos += 1;
                    playerMoveSound();
                } else playerWorngMoveSound();
                break;
            default:
                playerWorngMoveSound();
                mazeDisplayer.setPlayerPos(player_row_pos, player_col_pos);
        }

        mazeDisplayer.setPlayerPos(player_row_pos, player_col_pos);

        // when maze is solved

        if (mazeDisplayer.getRow_player() == maze.getGoalPosition().getRowIndex() && mazeDisplayer.getCol_player() == maze.getGoalPosition().getColumnIndex()) {
            mazeIsSolved();
        }
        keyEvent.consume();
    }

    // menu open about
    public void AboutF(ActionEvent actionEvent) throws IOException {
        Stage secondStage = new Stage();

        Parent root1 = FXMLLoader.load(getClass().getResource("../View/About.fxml"));
        Stage stage = (Stage) MainScreenid.getScene().getWindow();
        scene = new Scene(root1);
        secondStage.setScene(scene);
        secondStage.show();
    }

    //When click enter go to next page
    public void gentext(ActionEvent keyEvent) throws IOException {
        generateMaze(keyEvent);
    }

    public void mouseClick(MouseEvent mouseEvent) {

        mazeDisplayer.requestFocus();
    }

    private void playWinSound() {
        Media sound = new Media(new File("./src/Resources/Sound/win.mp3").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    private void playerMoveSound() {
        Media sound = new Media(new File("./src/Resources/Sound/move.wav").toURI().toString());
        MediaPlayer mediaPlayer1 = new MediaPlayer(sound);
        mediaPlayer1.play();
    }

    private void playerWorngMoveSound() {
        Media sound = new Media(new File("./src/Resources/Sound/worngMove.mp3").toURI().toString());
        MediaPlayer mediaPlayer1 = new MediaPlayer(sound);
        mediaPlayer1.play();
    }

    private void errorSound() {
        Media sound = new Media(new File("./src/Resources/Sound/error.MP3").toURI().toString());
        MediaPlayer mediaPlayer1 = new MediaPlayer(sound);
        mediaPlayer1.play();
    }

    private void playBackgorundSound() {
        backsound = new Media(new File("./src/Resources/Sound/background.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(backsound);
        mediaPlayer.setVolume(0.25);
        mediaPlayer.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playBackgorundSound();

    }

    public void rest(ActionEvent actionEvent) {
        isSolved=false;
        mazeDisplayer.setPlayerPos(maze.getStartPosition().getRowIndex(), maze.getStartPosition().getColumnIndex());
        time.setTime(0, 0, 0);
        timeline.play();
        playBackgorundSound();
        mazeDisplayer.requestFocus();
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
    private void mazeIsSolved(){
        playWinSound();
        isSolved=true;
        mediaPlayer.stop(); // stop background music
        Alert a = new Alert(Alert.AlertType.NONE);
        a.setAlertType(Alert.AlertType.INFORMATION);
        a.setContentText(userLable.getText() + " you are the best!! \n you finsh withn: " + time.getCurrentTime());
        a.show();
        timeline.stop(); //stop the time

        Time curTime = new Time(time);
        Pair<Integer, Integer> rowCol = new Pair(maze.getNumOfRow(), maze.getNumOfCol());
        Pair<String, Time> playerResult = new Pair(userLable.getText(), curTime);


        if (topResult.containsKey(rowCol)) { //if this current size alredy exist
            Pair<String, Time> temp = topResult.get(rowCol);
            Time talbeTime = temp.getValue(); // get the time from the table
            if (curTime.isgreaterThen(talbeTime)) { // if its the best time update the map
                topResult.remove(rowCol);
                topResult.put(rowCol, playerResult);
                String text = maze.getNumOfRow() + "X" + maze.getNumOfCol() + " -  Username: " + userLable.getText() + " Time: " + curTime.getCurrentTime() ;

                topResultLable.setText(text);
            }
        } else { // first time for this size
            topResult.put(rowCol, playerResult);
        }

    }

    public void soundOnOf(ActionEvent actionEvent) {
        if(mediaPlayer.getVolume()==0){
            mediaPlayer.setVolume(0.25);
            return;
        }
        mediaPlayer.setVolume(0);


    }
}