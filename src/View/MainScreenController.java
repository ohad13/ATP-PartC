package View;

import Server.Configurations;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.Solution;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.util.*;

public class MainScreenController implements IView, Initializable, Observer {
    public MazeDisplayer mazeDisplayer;
    public Maze maze;
    public Stage stage;
    public Scene scene;
    public File loadFile;
    public MenuItem SaveL;

    @FXML
    Pane paneB;
    public int rowPlayer;
    public int colPlayer;
    HashMap<Pair<Integer, Integer>, Pair<String, Time>> topResult;
    Time time = new Time("00:00:0");
    @FXML
    TextField textField_mazeRows;
    @FXML
    Button solveBtn;
    @FXML
    Button restB;
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
    private MyViewModel myViewModel;
    private boolean isSolved;

    private MediaPlayer mediaPlayer;
    private Solution solution;
    @FXML
    ChoiceBox algorithmChoiceBox;
    @FXML
    ChoiceBox searchingAlgorithmChoiceBox;

    public void setMyViewModel(MyViewModel myViewModel1) {
        this.myViewModel = myViewModel1;
    }

    public void displayUserName(String username) {
        userLable.setText(username);
    }

    private void readHashmap() throws IOException {
        // read hashtable from file
        if (topResult == null) {
            topResult = new HashMap<>();

            File file = new File(System.getProperty("java.io.tmpdir"), "hashResult"); //C:\Users\adidi\AppData\Local\Temp
            boolean b = file.createNewFile();
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

    public void generateMaze(ActionEvent actionEvent) {
        solveBtn.setVisible(true);
        restB.setVisible(true);
        isSolved = false;
        SaveL.setVisible(true);
        try { //check for valid input
            int rows = Integer.parseInt(textField_mazeRows.getText());
            int cols = Integer.parseInt(textField_mazeColumns.getText());
            myViewModel.generateMaze(rows, cols);
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
        mazeDisplayer.widthProperty().bind(paneB.widthProperty()); // for resizeable maze
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
        //Runtime.getRuntime().exec("taskkill /F /IM <processname>.exe");
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // write to the hash to the file
    public void writeHashToFile() {
        try {
            File file = new File(System.getProperty("java.io.tmpdir"), "hashResult");
            boolean b = file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            ObjectOutputStream outFile = new ObjectOutputStream(new FileOutputStream(file));

            int row, col;
            String usern, test;
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
        if (isSolved)
            return;
        this.myViewModel.movePlayer(keyEvent.getCode());
        mazeDisplayer.setPlayerPos(rowPlayer, colPlayer);
        keyEvent.consume();
    }

    // menu open about
    public void AboutF(ActionEvent actionEvent) throws IOException {
        System.out.println("About-----------------------------------");
        Stage secondStage = new Stage();
        secondStage.setTitle("About");
        Image applicationIcon = new Image(getClass().getResourceAsStream("../resources/Image/wall.png"));
        secondStage.getIcons().add(applicationIcon);
        Parent root1 = FXMLLoader.load(getClass().getResource("../View/About.fxml"));
        scene = new Scene(root1);
        secondStage.setScene(scene);
        secondStage.show();
    }

    //When click enter generate new maze.
    public void generateOnEnter(ActionEvent keyEvent) {
        generateMaze(keyEvent);
    }

    public void mouseClick(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    private void playWinSound() {
        Media sound = new Media(new File("./src/resources/Sound/win.mp3").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    private void playerMoveSound() {
        Media sound = new Media(new File("./src/resources/Sound/move.wav").toURI().toString());
        MediaPlayer mediaPlayer1 = new MediaPlayer(sound);
        mediaPlayer1.play();
    }

    private void playerWrongMoveSound() {
        Media sound = new Media(new File("./src/Resources/Sound/wrongMove.mp3").toURI().toString());
        MediaPlayer mediaPlayer1 = new MediaPlayer(sound);
        mediaPlayer1.play();
    }

    private void errorSound() {
        Media sound = new Media(new File("./src/Resources/Sound/error.MP3").toURI().toString());
        MediaPlayer mediaPlayer1 = new MediaPlayer(sound);
        mediaPlayer1.play();
    }

    private void playBackgroundSound() {
        Media backSound;
        backSound = new Media(new File("./src/Resources/Sound/background.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(backSound);
        mediaPlayer.setVolume(0.25);
        mediaPlayer.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playBackgroundSound();
        try {
            readHashmap();
            Configurations.getInstance();
            Configurations.setP("generateMaze", "MyMazeGenerator");
            Configurations.setP("problemSolver", "DepthFirstSearch");
        } catch (IOException e) {
            e.printStackTrace();
        }
        algorithmChoiceBox.getItems().addAll("EmptyMazeGenerator", "SimpleMazeGenerator", "MyMazeGenerator");
        searchingAlgorithmChoiceBox.getItems().addAll("BreadthFirstSearch", "DepthFirstSearch", "BestFirstSearch");
        algorithmChoiceBox.setValue("MyMazeGenerator");
        searchingAlgorithmChoiceBox.setValue("BreadthFirstSearch");
    }

    public void reset(ActionEvent actionEvent) {
        //playBackgroundSound();
        isSolved = false;
        this.myViewModel.reset();
        mazeDisplayer.setPlayerPos(rowPlayer, colPlayer);
        time.setTime(0, 0, 0);
        timeline.play();
        mazeDisplayer.drawMaze(maze);
        mazeDisplayer.requestFocus();
    }

    public void SaveB() throws IOException {
        System.out.println("Save");
        if (maze != null) {
            int[] tmp = {this.rowPlayer, this.colPlayer};
            ByteBuffer byteBuffer = ByteBuffer.allocate(tmp.length * 4);
            IntBuffer intBuffer = byteBuffer.asIntBuffer();
            intBuffer.put(tmp);
            byte[] array = byteBuffer.array();

            byte[] byteMaze = maze.toByteArray();
            byte[] all = new byte[array.length + byteMaze.length];
            System.arraycopy(array, 0, all, 0, array.length);
            System.arraycopy(byteMaze, 0, all, array.length, byteMaze.length);
            // now the all[] holds the regular toByteArr and the player position.
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save maze");
            File file = fileChooser.showSaveDialog(mazeDisplayer.getScene().getWindow());
            if (file != null) {
                Files.write(file.toPath(), all);
                System.out.println("Saved!");
            }
        }
    }

    public void Load(ActionEvent actionEvent) throws IOException {
        //load an existing maze.
        System.out.println("Load----------------");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load maze");
        loadFile = fileChooser.showOpenDialog(stage);
        myViewModel.setLoadFile(loadFile);
        myViewModel.load();
        mazeDisplayer.drawMaze(maze);
        mazeDisplayer.setPlayerPos(myViewModel.getRowPlayer(), myViewModel.getColPlayer());
        mazeDisplayer.drawPlayer();
        mazeDisplayer.widthProperty().bind(paneB.widthProperty()); // for resizeable maze
        mazeDisplayer.heightProperty().bind(paneB.heightProperty());

        // reset the time after loading
        time.setTime(0, 0, 0);
        timeline.play();

        // if this maze size already exist, show the best result
        Pair<Integer, Integer> rowCol = new Pair(maze.getNumOfRow(), maze.getNumOfCol());
        if (topResult.containsKey(rowCol)) {
            String txt = "The best time for " + maze.getNumOfRow() + "X" + maze.getNumOfCol() + " is: " + topResult.get(rowCol).getValue().getCurrentTime();
            topResultLable.setText(txt);
        } else {
            topResultLable.setText("");
        }
        mazeDisplayer.requestFocus();
        // update the labels of the size.
        textField_mazeRows.setText(String.valueOf(maze.getNumOfRow()));
        textField_mazeColumns.setText(String.valueOf(maze.getNumOfCol()));
    }

    private void mazeIsSolved() {
        playWinSound();
        isSolved = true;
        mediaPlayer.stop(); // stop background music
        Alert a = new Alert(Alert.AlertType.NONE);
        a.setTitle("Congratulations");
        Image applicationIcon = new Image(getClass().getResourceAsStream("../resources/Image/wall.png"));
        ((Stage)a.getDialogPane().getScene().getWindow()).getIcons().add(applicationIcon);
        a.setHeaderText(userLable.getText() + " you are the best!!");
        a.setAlertType(Alert.AlertType.INFORMATION);
        a.setContentText("You finish in: " + time.getCurrentTime());
        a.show();
        timeline.stop(); //stop the time
        Time curTime = new Time(time);
        Pair<Integer, Integer> rowCol = new Pair(maze.getNumOfRow(), maze.getNumOfCol());
        Pair<String, Time> playerResult = new Pair(userLable.getText(), curTime);
        String text;
        if (topResult.containsKey(rowCol)) { //if this current size already exist
            Pair<String, Time> temp = topResult.get(rowCol);
            Time tableTime = temp.getValue(); // get the time from the table
            if (curTime.isGreaterThen(tableTime)) { // if it is the best time - update the map
                topResult.remove(rowCol);
                topResult.put(rowCol, playerResult);
                text = "The best time for " + maze.getNumOfRow() + "X" + maze.getNumOfCol() + " is: " + topResult.get(rowCol).getValue().getCurrentTime();
                topResultLable.setText(text);
            }
        } else { // first time for this size
            topResult.put(rowCol, playerResult);
            // if first time, write my time as best.
            text = "The best time for " + maze.getNumOfRow() + "X" + maze.getNumOfCol() + " is: " + topResult.get(rowCol).getValue().getCurrentTime();
            topResultLable.setText(text);
        }
    }

    public void soundOnOf(ActionEvent actionEvent) {
        mazeDisplayer.requestFocus();//return focus to the maze.
        if (mediaPlayer.getVolume() == 0) {
            mediaPlayer.setVolume(0.25);
            return;
        }
        mediaPlayer.setVolume(0);
    }

    public void solveMaze(ActionEvent actionEvent) {
        try {
            myViewModel.solveMaze();
            mazeDisplayer.drawSol(solution.getSolutionPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mazeDisplayer.requestFocus();
    }

    @Override
    public void update(Observable o, Object arg) {
        if ("generate".equals(arg)) {
            isSolved = false;
            maze = myViewModel.getMaze();
        }
        if ("move".equals(arg)) {
            //player pos.
            rowPlayer = this.myViewModel.getRow();
            colPlayer = this.myViewModel.getCol();
            if (myViewModel.getIsValid() == 1)
                playerWrongMoveSound();
            else
                playerMoveSound();
        }
        if ("solve".equals(arg)) {
            mazeIsSolved();
        }
        if ("reset".equals(arg)) {
            rowPlayer = this.myViewModel.getRow();
            colPlayer = this.myViewModel.getCol();
        }
        if ("getSolve".equals(arg)) {
            this.solution = myViewModel.getSol();
        }
        if ("load".equals(arg)) {
            isSolved = false;
            rowPlayer = this.myViewModel.getRowPlayer();
            colPlayer = this.myViewModel.getColPlayer();
            maze = myViewModel.getMaze();
        }
    }

    public void UpdateClicked(ActionEvent actionEvent) {
        Configurations.setP("generateMaze", algorithmChoiceBox.getValue().toString());
        Configurations.setP("problemSolver", searchingAlgorithmChoiceBox.getValue().toString());
        myViewModel.saveSettings();
        mazeDisplayer.requestFocus();
    }

    public void setOnScroll(ScrollEvent scroll) {
        if (scroll.isControlDown()) {
            double zoom_fac = 1.05;
            if (scroll.getDeltaY() < 0) {
                zoom_fac = 2.0 - zoom_fac;
            }
            Scale newScale = new Scale();
            newScale.setPivotX(scroll.getX());
            newScale.setPivotY(scroll.getY());
            newScale.setX(mazeDisplayer.getScaleX() * zoom_fac);
            newScale.setY(mazeDisplayer.getScaleY() * zoom_fac);
            mazeDisplayer.getTransforms().add(newScale);
            scroll.consume();
        }
    }

    public void exit() throws InterruptedException {
        myViewModel.exit();
    }

    public void Help(ActionEvent actionEvent) throws IOException {
        // open the Help window.
        System.out.println("Help-------------------------------");
        Stage secondStage = new Stage();
        secondStage.setTitle("Help");
        Image applicationIcon = new Image(getClass().getResourceAsStream("../resources/Image/wall.png"));
        secondStage.getIcons().add(applicationIcon);
        Parent root1 = FXMLLoader.load(getClass().getResource("../View/Help.fxml"));
        scene = new Scene(root1);
        secondStage.setScene(scene);
        secondStage.show();
    }

    public void Properties(ActionEvent actionEvent) throws IOException {
        //open window with the game properties.
        System.out.println("Prop----------------");
        Stage secondStage = new Stage();
        secondStage.setTitle("Properties");
        Image applicationIcon = new Image(getClass().getResourceAsStream("../resources/Image/wall.png"));
        secondStage.getIcons().add(applicationIcon);
        Parent root1 = FXMLLoader.load(getClass().getResource("../View/Properties.fxml"));
        scene = new Scene(root1);
        secondStage.setScene(scene);
        secondStage.show();
    }
}
