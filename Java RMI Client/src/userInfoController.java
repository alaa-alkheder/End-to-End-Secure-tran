/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author A.Ibrahim
 */
public class userInfoController implements Initializable {

    /**
     * Initializes the controller class.
     */

    @FXML
    private ImageView backgroundImage;

    @FXML
    public Label fullNameLabel;

    @FXML
    public Label UniqueNameLabel;

    @FXML
    public Label PhoneLabel;

    @FXML
    public Label EmailLabel;

    @FXML
    public Label BirthdayLabel;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
