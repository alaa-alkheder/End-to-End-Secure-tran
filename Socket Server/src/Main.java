import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) throws NotBoundException{
	// write your code here

        try {
            DriveInterface chatinterface = (DriveInterface) Naming.lookup("rmi://localhost/RMIServer");
            new Thread(new Client(chatinterface));
//             TODO code application logic here
            ServerSocket s = new ServerSocket(1234);
            while(true){
                new ClientHandler(s.accept()).start();
            }
        } catch (IOException ex) {
            System.out.println(";;;;;;;;;;;;;;;;;;;;;;;");
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
