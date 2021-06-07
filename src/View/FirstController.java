package View;

import Model.MyModel;
import ViewModel.MyViewModel;
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
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.*;
import java.net.URL;
import java.util.*;

public class FirstController implements Initializable {
    @FXML
    TextField userNameTextFiled;
    @FXML
    Label topResultTableMainScreen;
    HashMap<Pair<Integer, Integer>, Pair<String, Time>> topResult;

    private MainScreenController mainScreenController;

    public void Startg(javafx.event.ActionEvent actionEvent) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;
        MyModel model;
        String userName = userNameTextFiled.getText();
        if (!isValidName(userName))
            return;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./MainScreen.fxml")); // use to pass user name between 2 scene
        root = loader.load();
        mainScreenController = loader.getController();
        mainScreenController.displayUserName(userName);

        model = new MyModel();
        model.startServers();
        MyViewModel myviewModel = new MyViewModel(model);
        mainScreenController.setMyViewModel(myviewModel);
        myviewModel.addObserver(mainScreenController);

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        scene = new Scene(root);
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> {
            try {
                exit();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        stage.show();
    }

    /**
     * read the hash map from file and call update-Top-Result to print it
     *
     * @throws IOException - if something about the reading file is wrong
     */
    public void displayTopResult() throws IOException {
        topResult = new HashMap<>();
        File file = new File(System.getProperty("java.io.tmpdir"), "hashResult");
        file.createNewFile();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            int row, col;
            String usern;
            String l;
            Time timetemp;
            Pair<Integer, Integer> rowCol;
            Pair<String, Time> playerResult;
            //read from file
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
        updateTopResult();
    }

    /**
     * when the hash table is ready its convert to priority Q and print the top result to the label.
     */
    public void updateTopResult() {
        PriorityQueue<Pair<Integer, String>> pq = new PriorityQueue<>(2, Comparator.comparing(Pair::getKey));
        int row, col;
        String usern, text;
        StringBuilder topRes = new StringBuilder();
        Time t;
        //read the hash map line by line to the Q
        for (Map.Entry<Pair<Integer, Integer>, Pair<String, Time>> entry : topResult.entrySet()) {
            row = entry.getKey().getKey();
            col = entry.getKey().getValue();
            usern = entry.getValue().getKey();
            t = entry.getValue().getValue();
            text = row + "X" + col + " -  Username: " + usern + " Time: " + t.getCurrentTime() + "\n";
            pq.add(new Pair(-row * col, text));
        }

        //chain the result to the string
        while (pq.size() != 0) {
            topRes.append(pq.poll().getValue());
        }
        topResultTableMainScreen.setText(topRes.toString());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            displayTopResult();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * private function to check if the user name is valid
     *
     * @param name - the user name from the GUI.
     * @return T / F if the name is valid.
     */
    private boolean isValidName(String name) {
        if (name.equals("")) {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText("The name can't be empty !");
            a.show();
            return false;
        }
        if (name.contains(",")) {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText("The name can't contains comma (,) !");
            a.show();
            return false;
        }
        return true;
    }

    public void enter(ActionEvent keyEvent) throws IOException {
        Startg(keyEvent);
    } //When click enter go to next page

    private void exit() throws InterruptedException {
        Platform.exit();
        mainScreenController.exit();
    }
}