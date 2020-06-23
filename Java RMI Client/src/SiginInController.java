/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileReader;
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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
    void loginButtonAction(ActionEvent event) throws IOException {
//
        String path = "config.json";
        JSONParser parser = new JSONParser();
        JSONObject jsonobject = new JSONObject();
        Object obj = null;
        try {
            obj = parser.parse(new FileReader(path));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        jsonobject = (JSONObject) obj;
        if (Client.server.checkClientCredintials(Client.client, jsonobject.get("name").toString(),jsonobject.get("password").toString() )) {
            Client.ClientName=jsonobject.get("name").toString();
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

    public void RegisterButtonAction(ActionEvent actionEvent) throws IOException {
        Parent root1 = FXMLLoader.load(getClass().getResource("sigin up.fxml"));
        Stage stage1 = new Stage();
        Scene scene1 = new Scene(root1);
        scene1.getStylesheets().add(getClass().getResource("ListViewStyle.css").toExternalForm());
        stage1.setScene(scene1);
        stage1.show();
        connectButton.getScene().getWindow().hide();
    }
}
