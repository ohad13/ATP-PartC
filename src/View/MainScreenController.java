package View;

import Server.Configurations;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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
    public int rowPlayer;
    public int colPlayer;
    public BorderPane MainScreenid;
    private double mouseX, mouseY;
    private MyViewModel myViewModel;
    private boolean isSolved;
    private MediaPlayer mediaPlayer;
    private Solution solution;
    HashMap<Pair<Integer, Integer>, Pair<String, Time>> topResult;
    Time time = new Time("00:00:0");

    @FXML
    Button soundOn;
    @FXML
    Button soundOff;
    @FXML
    AnchorPane paneB;
    @FXML
    ScrollPane scrollPaneContainer;
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

    /**
     * set the view model so the main screen will know him as well.
     *
     * @param myViewModel1 - the current View Model
     */
    public void setMyViewModel(MyViewModel myViewModel1) {
        this.myViewModel = myViewModel1;
    }

    /**
     * show on this screen the name player.
     *
     * @param username - the name of the user from the 'first' scene.
     */
    public void displayUserName(String username) {
        userLable.setText(username);
    }

    /**
     * read from the file that hold our Hash map and save it locally.
     */
    private void readHashMap() throws IOException {
        // read hashtable from file
        if (topResult == null) {// if there is no hash map at all, create one.
            topResult = new HashMap<>();
            // read from the tmp dir in the computer.
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
                // read each line and add it to the hash map.
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

    /**
     * generate new maze according to the size in the GUI.
     * also check if already been solved, if so display the best time for this size.
     */
    public void generateMaze(ActionEvent actionEvent) {
        isSolved = false;
        try { //check for valid input
            mediaPlayer.play(); // play background music
            int rows = Integer.parseInt(textField_mazeRows.getText());
            int cols = Integer.parseInt(textField_mazeColumns.getText());
            if (rows < 2 || cols < 2)
                throw new Exception("");
            myViewModel.generateMaze(rows, cols);
            solveBtn.setVisible(true);
            restB.setVisible(true);
            SaveL.setVisible(true);
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

    /**
     * sent you to first page
     */
    public void Back(ActionEvent actionEvent) throws IOException {
        if (topResult != null) {
            writeHashToFile();
        }
        Parent root = FXMLLoader.load(getClass().getResource("../View/First.fxml"));
        //Runtime.getRuntime().exec("taskkill /F /IM <processname>.exe");
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        mediaPlayer.setVolume(0);
        stage.show();
    }

    /**
     * write to the hash to the file
     */
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

    /**
     * move the player.
     *
     * @param keyEvent - when click on number/numPad to make the player move.
     */
    public void movePlayer(KeyEvent keyEvent) {
        if (isSolved || maze == null)
            return;
        this.myViewModel.movePlayer(keyEvent.getCode());
        mazeDisplayer.setPlayerPos(rowPlayer, colPlayer);
        keyEvent.consume();
    }

    /**
     * menu open about window.
     */
    public void AboutF(ActionEvent actionEvent) throws IOException {
        Stage secondStage = new Stage();
        secondStage.setTitle("About");
        Image applicationIcon = new Image(getClass().getResourceAsStream("../resources/Image/maze.png"));
        secondStage.getIcons().add(applicationIcon);
        Parent root1 = FXMLLoader.load(getClass().getResource("../View/About.fxml"));
        scene = new Scene(root1);
        secondStage.setScene(scene);
        secondStage.show();
    }

    /**
     * When click 'enter' - generate new maze.
     */
    public void generateOnEnter(ActionEvent keyEvent) {
        generateMaze(keyEvent);
    }

    /**
     * when click with the mouse on the maze, focus on the maze.
     */
    public void mouseClick(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    /**
     * play win music - when the player hit the end point
     */
    private void playWinSound() {
        Media sound = new Media(new File("./src/resources/Sound/win.mp3").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    /**
     * play move music - when the player move somewhere
     */
    private void playerMoveSound() {
        Media sound = new Media(new File("./src/resources/Sound/move.wav").toURI().toString());
        MediaPlayer mediaPlayer1 = new MediaPlayer(sound);
        mediaPlayer1.play();
    }

    /**
     * play wrong move music - when hit the wall..
     */
    private void playerWrongMoveSound() {
        Media sound = new Media(new File("./src/Resources/Sound/wrongMove.mp3").toURI().toString());
        MediaPlayer mediaPlayer1 = new MediaPlayer(sound);
        mediaPlayer1.play();
    }

    /**
     * play error music.
     */
    private void errorSound() {
        Media sound = new Media(new File("./src/Resources/Sound/error.MP3").toURI().toString());
        MediaPlayer mediaPlayer1 = new MediaPlayer(sound);
        mediaPlayer1.play();
    }

    /**
     * play background music
     */
    private void playBackgroundSound() {
        Media backSound;
        backSound = new Media(new File("./src/Resources/Sound/background.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(backSound);
        mediaPlayer.setVolume(0.25);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);//play the music forever.
        mediaPlayer.play();
    }

    /**
     * reset the game, make the player at the start point again, reset also the time.
     *
     * @param actionEvent - on click on reset button.
     */
    public void reset(ActionEvent actionEvent) {
        mediaPlayer.play(); // play background music
        isSolved = false;
        this.myViewModel.reset();
        mazeDisplayer.setPlayerPos(rowPlayer, colPlayer);
        time.setTime(0, 0, 0);
        timeline.play();
        mazeDisplayer.drawMaze(maze);
        mazeDisplayer.requestFocus();
    }

    /**
     * saved the current maze to a file.
     *
     * @throws IOException - if save is failed.
     */
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

    /**
     * load an existing maze and draw it.
     *
     * @param actionEvent - on click
     */
    public void Load(ActionEvent actionEvent) {
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

    /**
     * if the view is notify that the maze is solved, open an alert and make win sound.
     * also check if this is the best time and if so update the label and the hash.
     */
    private void mazeIsSolved() {
        playWinSound();
        isSolved = true;
        mediaPlayer.stop(); // stop background music
        Alert a = new Alert(Alert.AlertType.NONE);
        a.setTitle("Congratulations");
        Image applicationIcon = new Image(getClass().getResourceAsStream("../resources/Image/maze.png"));
        ((Stage) a.getDialogPane().getScene().getWindow()).getIcons().add(applicationIcon);
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

    /**
     * turn on/off the sound of the game.
     *
     * @param actionEvent - turn off/on the music.
     */
    public void soundOnOf(ActionEvent actionEvent) {
        mazeDisplayer.requestFocus();//return focus to the maze.
        if (mediaPlayer.getVolume() == 0) {
            mediaPlayer.setVolume(0.25);
            soundOn.setVisible(true);
            soundOff.setVisible(false);
            //ohad here - make all sound off and on together
            return;
        }
        mediaPlayer.setVolume(0);
        soundOn.setVisible(false);
        soundOff.setVisible(true);
    }

    /**
     * when the client ask to solve his maze, ask for the solution and draw it.
     *
     * @param actionEvent - when the client ask to solve the maze.
     */
    public void solveMaze(ActionEvent actionEvent) {
        try {
            myViewModel.solveMaze();
            mazeDisplayer.drawSol(solution.getSolutionPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mazeDisplayer.requestFocus();
    }

    /**
     * update the properties when the prop window closed
     */
    public void UpdateClicked() {
        String gen = PropertiesController.getGenerator();
        String ser = PropertiesController.getSearcher();
        int nThreads = PropertiesController.getNThreads();
        Configurations.setP("generateMaze", gen);
        Configurations.setP("problemSolver", ser);
        Configurations.setP("threadPoolSize", String.valueOf(nThreads));
        myViewModel.saveSettings(gen, ser, nThreads);
        mazeDisplayer.requestFocus();
    }

    /**
     * if the client scroll the mouse roll, than zoom in/out.
     *
     * @param scroll - when scroll the wheel-zoom in/out.
     */
    public void setOnScroll(ScrollEvent scroll) {
        scrollPaneContainer.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPaneContainer.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        if (scroll.isControlDown()) {
            double zoom_fac = 1.05;
            if (scroll.getDeltaY() < 0) {
                zoom_fac = 2.0 - zoom_fac;
            }
            if (mazeDisplayer.computeAreaInScreen() - 200 <= scrollPaneContainer.computeAreaInScreen() && zoom_fac == 0.95)
                return;
            Scale newScale = new Scale();
            newScale.setX(mazeDisplayer.getScaleX() * zoom_fac);
            newScale.setY(mazeDisplayer.getScaleY() * zoom_fac);
            Group contentGroup = new Group();
            Group zoomGroup = new Group();
            contentGroup.getChildren().add(zoomGroup);
            zoomGroup.getChildren().add(paneB);
            scrollPaneContainer.setContent(contentGroup);
            Scale scaleTransform = new Scale(zoom_fac, zoom_fac, 0, 0);
            zoomGroup.getTransforms().add(scaleTransform);
            mazeDisplayer.getTransforms().add(newScale);
            scroll.consume();
        }
    }

    /**
     * called whan closed the main window and close all, includes he servers.
     *
     * @throws InterruptedException - if the exit isn't going so well..
     */
    public void exit() throws InterruptedException {
        myViewModel.exit();
    }

    /**
     * open the Help window.
     *
     * @param actionEvent - when clicked on the Help button.
     * @throws IOException - if loading the FXML gone wrong.
     */
    public void Help(ActionEvent actionEvent) throws IOException {
        Stage secondStage = new Stage();
        secondStage.setTitle("Help");
        Image applicationIcon = new Image(getClass().getResourceAsStream("../resources/Image/maze.png"));
        secondStage.getIcons().add(applicationIcon);
        Parent root1 = FXMLLoader.load(getClass().getResource("../View/Help.fxml"));
        scene = new Scene(root1);
        secondStage.setScene(scene);
        secondStage.show();
    }

    /**
     * open the properties window.
     */
    public void Properties(ActionEvent actionEvent) throws IOException {
        Stage secondStage = new Stage();
        secondStage.setTitle("Properties");
        Image applicationIcon = new Image(getClass().getResourceAsStream("../resources/Image/maze.png"));
        secondStage.getIcons().add(applicationIcon);
        Parent root1 = FXMLLoader.load(getClass().getResource("../View/Properties.fxml"));
        scene = new Scene(root1);

        secondStage.setScene(scene);
        secondStage.show();
        secondStage.setOnCloseRequest(e -> {
            UpdateClicked();
        });
    }

    /**
     * if the mouse get dragged so calculate to which direction and how much and move the player if possible.
     */
    public void mouseDragged(MouseEvent mouseEvent) {
        if (myViewModel.getMaze() == null || isSolved)
            return;
        //Cell Size
        double cellHeight = mazeDisplayer.getHeight() / myViewModel.getMaze().getNumOfRow();
        double cellWidth = mazeDisplayer.getWidth() / myViewModel.getMaze().getNumOfCol();
        double addPlayerRow_s = mouseEvent.getX() - mouseX;
        double addPlayerCol_s = mouseEvent.getY() - mouseY;
        if (Math.abs(addPlayerRow_s) < cellHeight && Math.abs(addPlayerCol_s) < cellWidth) {
            return;
        }
        int countRows = (int) (addPlayerRow_s / cellHeight);
        int countCols = (int) (addPlayerCol_s / cellWidth);
        while (countCols != 0 || countRows != 0) {
            if (countCols < 0) {
                myViewModel.movePlayer(KeyCode.UP);
                countCols++;
            }
            if (countCols > 0) {
                myViewModel.movePlayer(KeyCode.DOWN);
                countCols--;
            }
            if (countRows < 0) {
                myViewModel.movePlayer(KeyCode.LEFT);
                countRows++;
            }
            if (countRows > 0) {
                myViewModel.movePlayer(KeyCode.RIGHT);
                countRows--;
            }
            mazeDisplayer.setPlayerPos(rowPlayer, colPlayer);
        }
    }

    /**
     * keep the View updated for the x,y of the mouse in the screen.
     */
    public void mouseMove(MouseEvent mouseEvent) {
        mouseX = mouseEvent.getX();
        mouseY = mouseEvent.getY();
    }

    @Override
    public void update(Observable o, Object arg) {
        // this function call after the observers get the 'notify' message.
        // check what message we got and act as needed.
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
            else if (myViewModel.getIsValid() == 0)
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // when the Controller creates this function called.
        // set the Pane scrollable.
        scrollPaneContainer.setContent(paneB);
        scrollPaneContainer.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPaneContainer.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        // bind the maze Displayer and the scrollPane.
        mazeDisplayer.widthProperty().bind(this.scrollPaneContainer.widthProperty());
        mazeDisplayer.heightProperty().bind(this.scrollPaneContainer.heightProperty());
        //play music
        playBackgroundSound();
        try {
            readHashMap();//try and read the Hash map for all the records.
            // set default settings to the config.
            Configurations.getInstance();
            Configurations.setP("generateMaze", "MyMazeGenerator");
            Configurations.setP("problemSolver", "DepthFirstSearch");
            Configurations.setP("threadPoolSize", "4");//new
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}