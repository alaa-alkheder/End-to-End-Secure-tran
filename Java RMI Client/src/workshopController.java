/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * FXML Controller class
 *
 * @author A.Ibrahim
 */
public class workshopController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private ImageView backgroundImage;

    @FXML
    private PasswordField workshopPasswordTextTield;

    @FXML
    private ScrollPane scrollPaneId;

    @FXML
    public ListView<HBox> userSelectedList1;

    @FXML
    private ScrollPane scrollPaneId1;

    @FXML
    private ListView<HBox> fileSelectedList1;

    @FXML
    private Button addfileButton;

    @FXML
    private Button adduserButton;

    @FXML
    private Button addworkshopButton;
    @FXML
    private Label selectedfileLabel;

    @FXML
    public Label workshopidLabel;

    @FXML
    private TextField textFailedWorkshopName;

    @FXML
    private TextField textFiledWorkshopID;

    @FXML
    private CheckBox checkBoxPrivate;


    @FXML
    public Label workshopnameLabel;

    List<File> fileList = new ArrayList<>();

    @FXML
    void addworkshopButtonAction(ActionEvent event) throws IOException {
        addworkshopButton.getScene().getWindow().hide();
/*//        ObservableList<HBox> topics;
//        topics = userSelectedList1.getSelectionModel().getSelectedItems();
//        System.out.println("-------"+userSelectedList1.getItems().size());
//
//        for (HBox each: topics)
//        {
//
//            System.out.println("gggggggggggggggg"+each.getChildren().get(0));
//        }*/

        if (Client.server.addWorkShop(textFailedWorkshopName.getText(), Client.ClientName)) {
            //todo listuser
            for (int i = 0; i < userSelectedList1.getItems().size(); i++) {
                HBox bb = new HBox();
                bb = (HBox) userSelectedList1.getItems().get(i);
                Label label = new Label();
                label = (Label) bb.getChildren().get(0);
//                System.out.println("////" + label.getText());
                Client.server.addUserToWorkShop(label.getText(), textFailedWorkshopName.getText(), Client.ClientName);
            }
        } else {
            System.out.println("+++++++++++++we can't add this work shop ++++++++++++++++++++");
        }


        for (int i = 0; i < fileList.size(); i++) {
            System.out.println(";;;;;;;;" + fileList.get(i).getName());

            Client.aes.encryption(Client.getPrivateKey(), fileList.get(i).getPath());

            File file = new File(fileList.get(i).getPath());
            File f1 = new File(file.getName() + "Enc");
            int fileSize = 0;
            int timer = 0;
            try {
                fileSize = (int) (Files.size(Paths.get(fileList.get(i).getPath())) / 1024 / 1024) + 1;
                timer = fileSize;
            } catch (IOException e) {
                e.printStackTrace();
            }

            FileInputStream in = null;
            try {
                in = new FileInputStream(f1);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            byte[] mydata = new byte[1024 * 1024];//MB
            int mylen = 0;
            try {
                mylen = in.read(mydata);

            } catch (IOException e) {
                e.printStackTrace();
            }
            while (mylen > 0) {
                //timer for Upload
                System.out.println("Done Upload File Input Stream ..." + --timer);
                Client.server.UpLoadFile(textFailedWorkshopName.getText()+"\\"+f1.getName(), mydata, mylen,Client.ClientName,1    );
                try {
                    mylen = in.read(mydata);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


    }

    @FXML
    void addFileButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("files", "*"));
// todo file list
        List<File> f = fileChooser.showOpenMultipleDialog(null);
        if (f != null)
            for (File file : f) {
                fileList.add(file);
                System.out.println(file.getAbsoluteFile());//give path of file
                file.getName();                                   //give name of file
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
                        fileSelectedList1.getItems().remove(hBox);
                    }
                });
                hBox.setAlignment(Pos.CENTER_LEFT);
                fileSelectedList1.getItems().add(hBox);

            }

    }

public void getFileList(List<String> f)
{ if (f != null)
    for (String file : f) {
     //   fileList.add(file);


        Label label = null;
        try {
            label = new Label("" + file);
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



        ImageView delete = null;
        try {

            delete = new ImageView(new Image(new FileInputStream("../Java RMI Client/src/image/delete.png")));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(mainScreanController.class.getName()).log(Level.SEVERE, null, ex);
        }
        delete.setCursor(Cursor.HAND);
        ImageView download = null;

        try {

            download = new ImageView(new Image(new FileInputStream("../Java RMI Client/src/image/download.png")));

            download.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
                @Override
                public void handle(javafx.scene.input.MouseEvent event) {
                    Label ll = (Label) hBox.getChildren().get(0);
                    String fileName = ll.getText();//print file share name
                        System.out.println(workshopnameLabel.getText()+"+++123456+"+fileName);

                    try {


                        Files.deleteIfExists(Paths.get(fileName + "Enc"));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (FileNotFoundException ex) {
            Logger.getLogger(mainScreanController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Label typeFileLabel = new Label();
      //  typeFileLabel.setText(String.valueOf(typeFile));
        download.setCursor(Cursor.HAND);
        hBox.getChildren().addAll(label,  delete,download);
        //add delete action in fileSelectedList1
        delete.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {

            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                fileSelectedList1.getItems().remove(hBox);
            }
        });
        hBox.setAlignment(Pos.CENTER_LEFT);
        fileSelectedList1.getItems().add(hBox);

    }
}
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}

