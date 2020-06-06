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
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author A.Ibrahim
 */
public class SiginUpController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private ImageView backgroundImage;

    @FXML
    private TextArea moreInfo;

    @FXML
    private DatePicker date;

    @FXML
    private RadioButton male;

    @FXML
    private ToggleGroup gender;

    @FXML
    private RadioButton female;

    @FXML
    private ToggleGroup gender1;

    @FXML
    private TextField fullnameTextField;

    @FXML
    private TextField emailTextTield;

    @FXML
    private TextField uniquenameTextTield;

    @FXML
    private Button submit;

    @FXML
    private PasswordField PasswordTextTield;

    @FXML
    private PasswordField ConfirmTextTield;
    @FXML
    private TextField phoneTextTield1;

    @FXML
    void submit(ActionEvent event) throws IOException {
        User user = new User();
        user.setFullName(fullnameTextField.getText());
        user.setPassword(PasswordTextTield.getText().toString());
        user.setUniqueName(uniquenameTextTield.getText());
        user.setEmail(emailTextTield.getText());
        user.setBirthday(date.toString()); //Add birthday
        user.setPhone(phoneTextTield1.getText());
      Boolean  b = Client.server.registerUser(user);
    if (b) System.out.println();

//        Parent root1 = FXMLLoader.load(getClass().getResource("mainScrean.fxml"));
//        Stage stage1 = new Stage();
//        Scene scene1 = new Scene(root1);
//        scene1.getStylesheets().add(getClass().getResource("ListViewStyle.css").toExternalForm());
//        stage1.setScene(scene1);
//        stage1.show();
//        submit.getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
