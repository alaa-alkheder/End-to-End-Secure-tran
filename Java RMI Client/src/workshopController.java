/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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

    @FXML
    void addworkshopButtonAction(ActionEvent event) throws IOException {
        addworkshopButton.getScene().getWindow().hide();

    }

    @FXML
    void addFileButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
  //      List isFile = new ArrayList<>();
//        isFile.add("*.doc");
//        isFile.add("*.docx");
//        isFile.add("*.Doc");
//        isFile.add("*.Docx");
//        isFile.add("*.pdf");
//        isFile.add("*.Xls");
//        isFile.add("*.cpp");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("files", "*"));
//        DirectoryChooser directoryChooser=new DirectoryChooser();
//File selectfolder=directoryChooser.showDialog(null);
        List<File> f = fileChooser.showOpenMultipleDialog(null);

        for (File file : f) {
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
            fileSelectedList1.setStyle("-fx-Border-color:#c99704; -fx-Border-width:20 ;");
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
                            controller.fileName.setText(""+ file.getName());
                            controller.fileName.setStyle("-fx-font:normal bold 12px 'System';");
                            controller.path.setText(""+file.getAbsoluteFile());
                            controller.path.setStyle("-fx-font:normal bold 12px 'System';");
                            File f=new File(file.getAbsolutePath());
                            Double ss=f.length()/(1024*1024*1.0);

                            controller.size.setText(ss+" MB");
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}

