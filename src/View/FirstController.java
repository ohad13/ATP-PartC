package View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class FirstController {
    private Stage stage;
    private Scene scene;
    private  Parent root;
    @FXML
    TextField userNameTextFiled;
    @FXML
    Label topResultTableMainScreen;


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
    public void  displayTopResult(String topres){
        topResultTableMainScreen.setText(topres);
    }
}
