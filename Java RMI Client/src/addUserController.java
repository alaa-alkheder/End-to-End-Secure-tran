/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author A.Ibrahim
 */
public class addUserController implements Initializable {
    public String fileShaingName = "";
    Handler h = new ConsoleHandler();


    /**
     * Initializes the controller class.
     */
    @FXML
    private ImageView backgroundImage;

    @FXML
    private ScrollPane scrollPaneId;

    @FXML
    private ListView<HBox> userlist;

    @FXML
    private TextField usernamefield;

    @FXML
    private MenuButton permission;

    @FXML
    private Button finishButton;

    @FXML
    private MenuItem admin;

    @FXML
    private MenuItem normalUser;
    @FXML
    private Button addButton;


    @FXML
    void setAdminAction(ActionEvent event) {
        permission.setText("Admin");
    }

    @FXML
    void setUserAction(ActionEvent event) {
        permission.setText("Admin");
    }

    List list = new ArrayList();

    @FXML
    void addButtonAction(ActionEvent event) {
        LinkedList<String> Names = new LinkedList<>();
        addButton.getScene().getWindow().hide();
        for (int i = 0; i < list.size(); i++) {
            HBox bb = new HBox();
            bb = (HBox) list.get(i);
            Label label = new Label();
            label = (Label) bb.getChildren().get(0);
            Names.addLast(label.getText().toString());
        }
        try {
            Client.server.shareFile(fileShaingName, Names);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void finishButtonAction(ActionEvent event) throws IOException {
        finishButton.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("workshop.fxml"));
        Parent s = loader.load();
        Scene scene = new Scene(s);
        workshopController controller = loader.getController();
//controller.intiuser(list);
//        controller.intiuser(userlist.getSelectionModel().getSelectedItem());
        for (int i = 0; i < list.size(); i++) {
            Label label = null;
            try {
                HBox bb = new HBox();
                bb = (HBox) list.get(i);
                label = new Label();
                label.setText(((Label) bb.getChildren().get(0)).getText());
                label.setPrefWidth(150);
                label.setStyle("-fx-font:normal bold 14px 'System';");
                label.setGraphic(new ImageView(new Image(new FileInputStream("../Java RMI Client/src/image/user.png"))));

            } catch (FileNotFoundException ex) {
                Logger.getLogger(mainScreanController.class.getName()).log(Level.SEVERE, null, ex);
            }
            HBox h = new HBox();
            h.setPadding(new Insets(12, 10, 12, 10));
            h.setSpacing(60);
            controller.userSelectedList1.setStyle("-fx-Border-color:#c99704; -fx-Border-width:20 ;");
            h.setCursor(Cursor.DEFAULT);

            ImageView info = null;

            try {

                info = new ImageView(new Image(new FileInputStream("../Java RMI Client/src/image/information.png")));
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

            } catch (FileNotFoundException ex) {
                Logger.getLogger(mainScreanController.class.getName()).log(Level.SEVERE, null, ex);
            }
            info.setCursor(Cursor.HAND);

            ImageView delete = null;
            try {

                delete = new ImageView(new Image(new FileInputStream("../Java RMI Client/src/image/delete.png")));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(mainScreanController.class.getName()).log(Level.SEVERE, null, ex);
            }
            delete.setCursor(Cursor.HAND);

            h.getChildren().addAll(label, info, delete);
            //add delete action in userSelectedList1
            delete.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {

                @Override
                public void handle(javafx.scene.input.MouseEvent event) {
                    controller.userSelectedList1.getItems().remove(h);
                }
            });
            h.setAlignment(Pos.CENTER_LEFT);
            controller.userSelectedList1.getItems().add(h);
        }

        // controller.userSelectedList1.getItems().addAll(list);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LinkedList<User> u = new LinkedList<>();
        try {
            u = (LinkedList<User>) Client.server.showAllUser();

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < u.size(); i++) {
            CheckBox checkBox = new CheckBox();

            Label label = null;
            try {
                label = new Label(u.get(i).getUniqueName());
                label.setStyle("-fx-font:normal bold 14px 'System';");
                label.setGraphic(new ImageView(new Image(new FileInputStream("../Java RMI Client/src/image/user.png"))));

            } catch (FileNotFoundException ex) {
                Logger.getLogger(mainScreanController.class.getName()).log(Level.SEVERE, null, ex);
            }
            HBox h = new HBox();
            h.setPadding(new Insets(12, 10, 12, 10));
            h.setSpacing(60);
            userlist.setStyle("-fx-Border-color:#c99704; -fx-Border-width:20 ;");
            h.setCursor(Cursor.DEFAULT);

            ImageView info = null;

            try {

                info = new ImageView(new Image(new FileInputStream("../Java RMI Client/src/image/information.png")));

            } catch (FileNotFoundException ex) {
                Logger.getLogger(mainScreanController.class.getName()).log(Level.SEVERE, null, ex);
            }
            info.setCursor(Cursor.HAND);
            h.getChildren().addAll(label, info, checkBox);
            h.setAlignment(Pos.CENTER_LEFT);
            userlist.getItems().add(h);
            //add click on cell list Action
            checkBox.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {

                @Override
                public void handle(javafx.scene.input.MouseEvent event) {
                    usernamefield.setText(((Label) h.getChildren().get(0)).getText());
                    if (checkBox.isSelected()) {
                        list.add(h);
                    } else {
                        list.remove(h);
                    }
                    System.out.println("" + list.size());
                }
            });
            //add click on info icon Action
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

        }
    }
}
