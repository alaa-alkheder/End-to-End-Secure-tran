/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author A.Ibrahim
 */
public class mainScreanController implements Initializable {

    @FXML
    private TabPane tabpane;

    @FXML
    private Tab filetab;

    @FXML
    private Label internalLabel;

    @FXML
    private ScrollPane scrollPaneId;

    @FXML
    private Label fileShaingLable;

    @FXML
    private ImageView fileShaingIcon;

    @FXML
    private Button UploadButton;

    @FXML
    private ImageView UploadIcon;

    @FXML
    private Button DownloadButton;

    @FXML
    private ImageView slackLogoImage;

    @FXML
    private ImageView downloadIcon;

    @FXML
    private Label driveLabel;
    @FXML
    private ListView<HBox> list;

    @FXML
    private ListView<HBox> userlist;

    @FXML
    private Button chatButton;

    @FXML
    private Label usernameFiledLabel;

    @FXML
    private Label emailFiledLable;

    @FXML
    private Label phoneFiledLable;

    @FXML
    private Label permissoinFiledLable;

    @FXML
    private Label userLabel;

    @FXML
    private ImageView userIcon;

    @FXML
    private Label infoLabel;

    @FXML
    private ImageView infoIcon;

    @FXML
    private Label workshopLabel;

    @FXML
    private Label createWorkshopLabel;

    @FXML
    private ImageView addWorkShopLogo;

    @FXML
    private Button addWorkhopButton;

    @FXML
    public ListView<HBox> workshoplist;

    @FXML
    private Button logoutButton;

    @FXML
    void DownloadButtonAction(ActionEvent event) {

    }

    @FXML
    void UploadButtonAction(ActionEvent event) {

    }

    @FXML
    void addWorkhopButtonAction(ActionEvent event) throws IOException {
        Stage stage1 = new Stage();
        Parent root1 = FXMLLoader.load(getClass().getResource("addUser.fxml"));

        Scene scene1 = new Scene(root1);
        scene1.getStylesheets().add(getClass().getResource("ListViewStyle.css").toExternalForm());
        stage1.setScene(scene1);
        stage1.show();
    }

    @FXML
    void chatButtonAction(ActionEvent event) {

    }

    @FXML
    void logoutButtonAction(ActionEvent event) {
        Platform.exit();

    }

    /////////////////////////////////chat parameters
    @FXML
    private BorderPane borderPane;

    @FXML
    private ListView<HBox> chatPaneList;

    @FXML
    private TextArea messageTextArea;

    @FXML
    private Button sendButton;

    @FXML
    private Button recordButton;

    @FXML
    private ImageView microphoneImageView;

    @FXML
    private HBox onlineUsersHbox;

    @FXML
    private Label onlineCountLabel;

    @FXML
    private ListView<HBox> userList;

    @FXML
    private ImageView userImageView;

    @FXML
    private ImageView statusLumb;

    @FXML
    private Label usernameLabel;

    @FXML
    private MenuButton statusComboBox;

    @FXML
    private MenuItem busy;

    @FXML
    private MenuItem away;
    @FXML
    private MenuItem online;
    @FXML
    private Label lumbLabel;

    @FXML
    void infoIconAction(ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("Userinfo.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("ListViewStyle.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(mainScreanController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    void AwayAction(ActionEvent event) throws FileNotFoundException {
        statusComboBox.setText("Away");
        lumbLabel.setGraphic(new ImageView(new Image(new FileInputStream("../Java RMI Client/src/image/away.png"))));
    }

    @FXML
    void busyAction(ActionEvent event) throws FileNotFoundException {
        statusComboBox.setText("Busy");
        lumbLabel.setGraphic(new ImageView(new Image(new FileInputStream("../Java RMI Client/src/image/busy.png"))));

    }

    @FXML
    void onlineAction(ActionEvent event) throws FileNotFoundException {
        statusComboBox.setText("Online");
        lumbLabel.setGraphic(new ImageView(new Image(new FileInputStream("../Java RMI Client/src/image/online.png"))));
    }

    @FXML
    void sendButtonAction(ActionEvent event) {
        String massageTesxt = messageTextArea.getText();
        String[] lines = massageTesxt.split("\n");
        int massageLine = lines.length;
        System.out.println("" + massageLine);
        int massasgeLength = messageTextArea.getText().length();
        int linenumber = 0;
        if (massasgeLength > 0) {
            System.out.println("masss" + massageLine);
            HBox box = new HBox();
            if (massasgeLength > 60 && massageLine > 1) {
                box.setPrefHeight((massasgeLength / 60 * 1.0 + massageLine) * 20);
            } else if (massasgeLength > 60 && massageLine == 1) {
                box.setPrefHeight((massasgeLength / 60 * 1.0 + 1) * 30);
            } else {
                box.setPrefHeight(30);
            }
            box.setId("massage");
            Label massageLabel = new Label("" + messageTextArea.getText());
            messageTextArea.setText("");
            // box.setPrefWidth(500.0);
            massageLabel.setWrapText(true);

            massageLabel.setPrefWidth(400);

            DateFormat  dateFormat = new SimpleDateFormat("HH:mm");
            Calendar calendar = Calendar.getInstance();
            Label timeLabel = new Label("" + dateFormat.format(calendar.getTime()));
            timeLabel.setPrefWidth(40.0);
            box.setFillHeight(true);
            box.setSpacing(10);
            box.setAlignment(Pos.CENTER_RIGHT);
            box.getChildren().addAll(massageLabel, timeLabel);
            chatPaneList.getItems().add(box);
            box.setPadding(new Insets(5, 5, 5, 5));
            receveButtonAction(event);
//            if (chatPaneList.isHover()) {
//Background.
//            }
        }

    }

    @FXML
    void receveButtonAction(ActionEvent event) {
        String massageTesxt = "helllllllllrecereceveButtonActionreceveButtonActionreceveButtonActionveButtonActionreceveButtonActionreceveButtonAction";
        String[] lines = massageTesxt.split("\n");
        int massageLine = lines.length;
        System.out.println("" + massageLine);
        int massasgeLength = massageTesxt.length();
        int linenumber = 0;
        if (massasgeLength > 0) {

            HBox box = new HBox();
            if (massasgeLength > 60 && massageLine > 1) {
                box.setPrefHeight((massasgeLength / 60 * 1.0 + massageLine) * 20);
            } else if (massasgeLength > 60 && massageLine == 1) {
                box.setPrefHeight((massasgeLength / 60 * 1.0 + 1) * 30);
            } else {
                box.setPrefHeight(30);
            }
            box.setId("mas");
            Label massageLabel = new Label("" + massageTesxt);
            messageTextArea.setText("");
            box.setPrefWidth(500.0);
            massageLabel.setWrapText(true);
            massageLabel.setPrefWidth(400.0);
            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            Calendar calendar = Calendar.getInstance();
            Label timeLabel = new Label("" + dateFormat.format(calendar.getTime()));
            timeLabel.setPrefWidth(40.0);
            box.setFillHeight(true);
            box.setSpacing(10);
            box.setAlignment(Pos.CENTER_LEFT);
            box.getChildren().addAll(timeLabel, massageLabel);
            chatPaneList.getItems().add(box);
            box.setPadding(new Insets(5, 5, 5, 5));

//            if (chatPaneList.isHover()) {
//Background.
//            }
        }

    }

    @FXML
    void sendMethod(KeyEvent event) {

    }

    @FXML
    void RecodingNow(javafx.scene.input.MouseEvent event) throws FileNotFoundException {
        recordButton.setGraphic(new ImageView(new Image(new FileInputStream("../Java RMI Client/src/image/greenMic.png"))));
    }

    @FXML
    void StopRecording(javafx.scene.input.MouseEvent event) throws FileNotFoundException {
        recordButton.setGraphic(new ImageView(new Image(new FileInputStream("../Java RMI Client/src/image/222.png"))));
    }

    @FXML
    private Label internalLabel1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        file tab controller

        for (int i = 0; i < 20; i++) {
            Label label = null;
            try {
                label = new Label("  file" + i);
                label.setStyle("-fx-font:normal bold 14px 'System';");
                label.setGraphic(new ImageView(new Image(new FileInputStream("../Java RMI Client/src/image/folder (4).png"))));

            } catch (FileNotFoundException ex) {
                Logger.getLogger(mainScreanController.class.getName()).log(Level.SEVERE, null, ex);
            }
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(12, 10, 12, 10));
            hBox.setSpacing(50);
            list.setStyle("-fx-border-color: #c99704;-fx-border-width: 20;");
            hBox.setCursor(Cursor.DEFAULT);

            ImageView info = null;

            try {

                info = new ImageView(new Image(new FileInputStream("../Java RMI Client/src/image/information.png")));
                //add action to info icon
                info.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {

                    @Override
                    public void handle(javafx.scene.input.MouseEvent event) {
                        Parent root;
                        try {
                            root = FXMLLoader.load(getClass().getResource("fileinfo.fxml"));
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

            ImageView share = null;
            try {

                share = new ImageView(new Image(new FileInputStream("../Java RMI Client/src/image/share.png")));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(mainScreanController.class.getName()).log(Level.SEVERE, null, ex);
            }
            share.setCursor(Cursor.HAND);
            ImageView delete = null;
            try {

                delete = new ImageView(new Image(new FileInputStream("../Java RMI Client/src/image/delete.png")));

            } catch (FileNotFoundException ex) {
                Logger.getLogger(mainScreanController.class.getName()).log(Level.SEVERE, null, ex);
            }
            delete.setCursor(Cursor.HAND);

            hBox.getChildren().addAll(label, info, share, delete);
            //add delete action in file tab
            delete.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {

                @Override
                public void handle(javafx.scene.input.MouseEvent event) {
                    list.getItems().remove(hBox);
                }
            });

            hBox.setAlignment(Pos.CENTER_LEFT);
            list.getItems().add(hBox);

        }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// user tab controller
        for (int i = 0; i < 20; i++) {
            Label label = null;
            try {
                label = new Label("user " + i);
                label.setStyle("-fx-font:normal bold 14px 'System';");
                label.setGraphic(new ImageView(new Image(new FileInputStream("../Java RMI Client/src/image/user.png"))));

            } catch (FileNotFoundException ex) {
                Logger.getLogger(mainScreanController.class.getName()).log(Level.SEVERE, null, ex);
            }
            HBox h = new HBox();
            h.setPadding(new Insets(12, 10, 12, 10));
            h.setSpacing(60);
            userlist.setStyle("-fx-font:normal bold 14px 'System';");
            h.setCursor(Cursor.DEFAULT);

            ImageView info = null;

            try {

                info = new ImageView(new Image(new FileInputStream("../Java RMI Client/src/image/information.png")));

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
            //click on cell list action in user tab
            h.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {

                @Override
                public void handle(javafx.scene.input.MouseEvent event) {
                    Label ll = (Label) h.getChildren().get(0);
                    String userName = ll.getText();
                    ////call search about username in bove and bush iuserinfo in user tab in info field
                    usernameFiledLabel.setText(userName);
                    emailFiledLable.setPadding(new Insets(3, 3, 3, 3));
                    emailFiledLable.setText("ahmad@gmail.com");//add email witch u search about
                    phoneFiledLable.setPadding(new Insets(3, 3, 3, 3));
                    phoneFiledLable.setText("+963998654456");//add phone witch u search about
                    permissoinFiledLable.setPadding(new Insets(3, 3, 3, 3));
                    permissoinFiledLable.setText("Admin");//add permission witch u search about

                }
            });
            //click on info icon action in user tab
            info.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {

                @Override
                public void handle(javafx.scene.input.MouseEvent event) {
                    Parent root;
                    try {
                        root = FXMLLoader.load(getClass().getResource("Userinfo.fxml"));
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

            //add delete action in user tab
            delete.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {

                @Override
                public void handle(javafx.scene.input.MouseEvent event) {
                    userlist.getItems().remove(h);
                }
            });
            h.setAlignment(Pos.CENTER_LEFT);
            userlist.getItems().add(h);
        }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// workshop tab controller
        for (int i = 0; i < 20; i++) {
            Label label = null;
            try {
                label = new Label("  workshop " + i);
                label.setStyle("-fx-font:normal bold 14px 'System';");
                label.setGraphic(new ImageView(new Image(new FileInputStream("../Java RMI Client/src/image/people (1).png"))));

            } catch (FileNotFoundException ex) {
                Logger.getLogger(mainScreanController.class.getName()).log(Level.SEVERE, null, ex);
            }
            HBox hh = new HBox();
            hh.setPadding(new Insets(12, 10, 12, 10));
            hh.setSpacing(40);
            workshoplist.setStyle("-fx-font:normal bold 13px 'System';");
            hh.setCursor(Cursor.DEFAULT);

            ImageView info = null;

            try {

                info = new ImageView(new Image(new FileInputStream("../Java RMI Client/src/image/information.png")));

            } catch (FileNotFoundException ex) {
                Logger.getLogger(mainScreanController.class.getName()).log(Level.SEVERE, null, ex);
            }
            info.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {

                @Override
                public void handle(javafx.scene.input.MouseEvent event) {
                    Parent root;
                    try {
                        root = FXMLLoader.load(getClass().getResource("workShopInfo.fxml"));
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
            info.setCursor(Cursor.HAND);

            ImageView adduser = null;

            try {

                adduser = new ImageView(new Image(new FileInputStream("../Java RMI Client/src/image/adduser.png")));

            } catch (FileNotFoundException ex) {
                Logger.getLogger(mainScreanController.class.getName()).log(Level.SEVERE, null, ex);
            }
            adduser.setCursor(Cursor.HAND);
            adduser.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {

                @Override
                public void handle(javafx.scene.input.MouseEvent event) {
                    Parent root;
                    try {
                        root = FXMLLoader.load(getClass().getResource("addUser.fxml"));
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

            ImageView delete = null;
            try {

                delete = new ImageView(new Image(new FileInputStream("../Java RMI Client/src/image/delete.png")));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(mainScreanController.class.getName()).log(Level.SEVERE, null, ex);
            }
            delete.setCursor(Cursor.HAND);

            hh.getChildren().addAll(label, info, adduser, delete);
            //add delete action in workshop tab
            delete.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {

                @Override
                public void handle(javafx.scene.input.MouseEvent event) {
                    workshoplist.getItems().remove(hh);
                }
            });
            hh.setAlignment(Pos.CENTER_LEFT);
            workshoplist.getItems().add(hh);

        }
//        chat controller/////////////////////////////////////////////////////////////////////////////////
        for (int i = 0; i < 20; i++) {
            Label label = null;
            HBox h = new HBox();
            h.setId("USer");
            label = new Label("user " + i);
            label.setStyle("-fx-font:normal bold 14px 'System';");

            try {
                label.setGraphic(new ImageView(new Image(new FileInputStream("../Java RMI Client/src/image/default1.png"))));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(mainScreanController.class.getName()).log(Level.SEVERE, null, ex);
            }

            h.setPadding(new Insets(12, 10, 12, 10));
            h.setSpacing(20);
            userList.setStyle("-fx-font:normal bold 13px 'System';");;
            h.setCursor(Cursor.DEFAULT);

            Label online = new Label("");
            try {
                online.setGraphic(new ImageView(new Image(new FileInputStream("../Java RMI Client/src/image/online.png"))));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(mainScreanController.class.getName()).log(Level.SEVERE, null, ex);
            }

            h.getChildren().addAll(online, label);
            h.setAlignment(Pos.CENTER_LEFT);
            userList.getItems().add(h);

        }
    }

}
