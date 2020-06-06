/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author A.Ibrahim
 */
public class FileinfoController implements Initializable {

    @FXML
    private ImageView backgroundImage;

    @FXML
    public Label fileName;

    @FXML
    public Label path;

    @FXML
    private ListView<HBox> usersSharing;

    @FXML
    private Label UniqueNameLabel1;

    @FXML
    public Label size;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//         fileName.setText("file1");
//         path.setText("file1\\file");
//         size.setText("20MB");
        for (int i = 0; i < 20; i++) {
            Label label = null;
            Label permissionLAbel = null;
            try {
                label = new Label("user " + i);
                permissionLAbel = new Label("Normal USer ");
                label.setStyle("-fx-font:normal bold 14px 'System';");
                label.setGraphic(new ImageView(new Image(new FileInputStream("../untitled2/src/image/user.png"))));

            } catch (FileNotFoundException ex) {
                Logger.getLogger(mainScreanController.class.getName()).log(Level.SEVERE, null, ex);
            }

            ImageView info = null;

            try {

                info = new ImageView(new Image(new FileInputStream("../untitled2/src/image/information.png")));

            } catch (FileNotFoundException ex) {
                Logger.getLogger(mainScreanController.class.getName()).log(Level.SEVERE, null, ex);
            }
            info.setCursor(Cursor.HAND);

            //click on info icon action in user tab
            info.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {

                @Override
                public void handle(javafx.scene.input.MouseEvent event) {
                    Parent root;
                    try {
                        root = FXMLLoader.load(getClass().getResource("UserInfo.fxml"));
                        Stage stage = new Stage();
                        Scene scene = new Scene(root);
                        scene.getStylesheets().add(getClass().getResource("ListViewStyle.css").toExternalForm());
                        stage.setScene(scene);
                        stage.show();

                    } catch (IOException ex) {
                        Logger.getLogger(mainScreanController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });
            HBox h = new HBox();
            h.setPadding(new Insets(12, 10, 12, 10));
            h.setSpacing(60);
            usersSharing.setStyle("-fx-Border-color:#c99704; -fx-Border-width:20 ;");
            h.setCursor(Cursor.DEFAULT);
            h.setAlignment(Pos.CENTER_LEFT);

            h.getChildren().addAll(label, info, permissionLAbel);
            usersSharing.getItems().add(h);
        }

    }


}