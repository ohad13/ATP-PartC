package View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.awt.event.ActionEvent;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class FirstController implements Initializable {
    private Stage stage;
    private Scene scene;
    private  Parent root;
    @FXML
    TextField userNameTextFiled;
    @FXML
    Label topResultTableMainScreen;
    HashMap<Pair<Integer, Integer>, Pair<String,Time>> topResult;

    public void Startg(javafx.event.ActionEvent actionEvent) throws IOException {
        String userName = userNameTextFiled.getText();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/MainScreen.fxml")); // use to pass user name between 2 sence
        root = loader.load();
        MainScreenController mainScreenController = loader.getController();
        mainScreenController.displayUserName(userName);

        //Parent root = FXMLLoader.load(getClass().getResource("../View/MainScreen.fxml"));
        stage  = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    public void  displayTopResult() throws IOException {

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
        updateTopResult();

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
            test += + row + "X" +col +  " -  username: " + usern + " time: " + t.getCurrentTime() + "\n";
        }
        topResultTableMainScreen.setText(test);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            displayTopResult();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
