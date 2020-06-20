/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;

/**
 * FXML Controller class
 *
 * @author A.Ibrahim
 */
public class directshaingController implements Initializable {

    @FXML
    private ImageView backgroundImage;

    @FXML
    private ScrollPane scrollPaneId;

    @FXML
    private ListView<HBox> userSelectedList;

    @FXML
    private ScrollPane scrollPaneId1;

    @FXML
    private ListView<HBox> fileSelectedList;

    @FXML
    private Button browsButton;

    @FXML
    private Button sendButton;

    @FXML
    private Label selectedfileLabel;

    @FXML

    private Label selectedfileLabel11;

    @FXML
    private TextArea bodyTextFeild;

    @FXML
    private Label selectedfileLabel1;

    @FXML
    private TextField textFiledWorkshopID;
    File test;
    String nameTest;

    @FXML
    void browsButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("files", "*"));

        List<File> f = fileChooser.showOpenMultipleDialog(null);

        for (File file : f) {
            System.out.println(file.getAbsoluteFile());//give path of file
            test = file;                               //give name of file
            Label label = null;
            try {
                label = new Label("" + file.getName());
                label.setPrefWidth(150);
                label.setStyle("-fx-font:normal bold 14px 'System';");
                label.setGraphic(new ImageView(new Image(new FileInputStream("../Java RMI Client/src/image/approval.png"))));

            } catch (FileNotFoundException ex) {
                Logger.getLogger(mainScreanController.class.getName()).log(Level.SEVERE, null, ex);
            }
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(12, 10, 12, 10));
            hBox.setSpacing(50);
            // fileSelectedList1.setStyle("-fx-Border-color:#c99704;   ");
            hBox.setCursor(Cursor.DEFAULT);

            ImageView info = null;

            try {

                info = new ImageView(new Image(new FileInputStream("../Java RMI Client/src/image/information.png")));
                info.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {

                    @Override
                    public void handle(javafx.scene.input.MouseEvent event) {
                        Parent root;
                        try {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("fileinfo.fxml"));
                            Parent s = loader.load();
                            Scene scene = new Scene(s);
                            FileinfoController controller = loader.getController();
                            controller.fileName.setText("" + file.getName());
                            controller.fileName.setStyle("-fx-font:normal bold 12px 'System';");
                            controller.path.setText("" + file.getAbsoluteFile());
                            controller.path.setStyle("-fx-font:normal bold 12px 'System';");
                            File f = new File(file.getAbsolutePath());
                            Double ss = f.length() / (1024 * 1024 * 1.0);

                            controller.size.setText(ss + " MB");
                            Stage stage = new Stage();

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

            hBox.getChildren().addAll(label, info, delete);
            //add delete action in fileSelectedList1
            delete.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {

                @Override
                public void handle(javafx.scene.input.MouseEvent event) {
                    fileSelectedList.getItems().remove(hBox);
                }
            });
            hBox.setAlignment(Pos.CENTER_LEFT);
            fileSelectedList.getItems().add(hBox);

        }

    }

    @FXML
    void sendButtonAction(ActionEvent event) throws IOException, ParseException {


        nameTest = "r1";
        String aesKey = Client.aes.getRandomKey(64);
        System.out.println(aesKey);
//        Client.aes.encryption(aesKey, test.getPath());
//        String st = Client.server.returnClientPublicKey(nameTest);
//        JSONParser parser = new JSONParser();
//        JSONObject jsonobject;
//        Object obj = null;
//        jsonobject = (JSONObject) parser.parse(st);
//        String encKey = Client.rsa.encryptStringRsa(aesKey, new BigInteger(jsonobject.get("e").toString()), new BigInteger(jsonobject.get("n").toString()));
//        jsonobject.put("handKey", encKey);
//        jsonobject.put("fileName", test.getName());
//        Client.server.sendHandKeyToServer(nameTest, jsonobject.toString());
        Client.aes.encryption(aesKey, test.getPath());
        File file = new File(test.getName() + "Enc");
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] mydata = new byte[(int) Files.size(Paths.get(file.getPath()))];
        int mylen = 0;
        try {
            in.read(mydata);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Client.server.sendFileToServerDirect(mydata,file.getName(),nameTest);


    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LinkedList<User> u = new LinkedList<>();
        try {
            u = (LinkedList<User>) Client.server.showAllUser();

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        ToggleGroup toggleGroup = new ToggleGroup();
        for (int i = 0; i < u.size(); i++) {
            RadioButton radioButton = new RadioButton();

            radioButton.setToggleGroup(toggleGroup);
            Label label = null;
            try {
                label = new Label(u.get(i).getUniqueName());
                label.setStyle("-fx-font:normal bold 14px 'System';");
                label.setGraphic(new ImageView(new Image(new FileInputStream("../Java RMI Client/src/image/user.png"))));
                label.setPrefWidth(100.0);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(mainScreanController.class.getName()).log(Level.SEVERE, null, ex);
            }
            HBox h = new HBox();
            h.setPadding(new Insets(12, 10, 12, 10));
            h.setSpacing(60);

            h.setCursor(Cursor.DEFAULT);

            ImageView info = null;

            try {

                info = new ImageView(new Image(new FileInputStream("../Java RMI Client/src/image/information.png")));

            } catch (FileNotFoundException ex) {
                Logger.getLogger(mainScreanController.class.getName()).log(Level.SEVERE, null, ex);
            }
            info.setCursor(Cursor.HAND);
            h.getChildren().addAll(label, info, radioButton);
            h.setAlignment(Pos.CENTER_LEFT);
            userSelectedList.getItems().add(h);

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
