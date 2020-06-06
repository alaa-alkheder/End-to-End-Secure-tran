/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author A.Ibrahim
 */
public class SiginInController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private ImageView backgroundImage;

    @FXML
    private TextField usernameTextfield;

    @FXML
    private PasswordField passwordtextfield;

    @FXML
    private Button connectButton;

    @FXML
    private ImageView Img;

    @FXML
    private Label error;

    @FXML
    private Label registerLabel;

    @FXML
    private Button RegisterButton;


    @FXML
    void RegisterButtonAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("sigin up.fxml"));
        Stage stage=new Stage();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("ListViewStyle.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
        connectButton.getScene().getWindow().hide();
    }

    @FXML
    void loginButtonAction(ActionEvent event) throws IOException {
        if (Client.server.checkClientCredintials(Client.server, usernameTextfield.getText(), passwordtextfield.getText().toString() )) {
            Parent root1 = FXMLLoader.load(getClass().getResource("mainScrean.fxml"));
            Stage stage1 = new Stage();
            Scene scene1 = new Scene(root1);
            scene1.getStylesheets().add(getClass().getResource("ListViewStyle.css").toExternalForm());
            stage1.setScene(scene1);
            stage1.show();
            connectButton.getScene().getWindow().hide();
        }else {
            error.setText("the user name or password is error");
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
