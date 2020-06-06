import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sigin in.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("ListViewStyle.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {

        DriveInterface chatinterface = (DriveInterface) Naming.lookup("rmi://localhost/RMIServer");
        new Thread(new Client(chatinterface));
        launch(args);
    }
}
