import java.io.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;

/**
 * Created by IntelliJ IDEA.
 * User: Alaa Alkheder
 * Email:alaa-alkheder@outlook.com
 * Github:alaa-alkheder
 */
public class StartClient {
    public static void main(String[] args) throws IOException, NotBoundException {
        DriveInterface chatinterface = (DriveInterface) Naming.lookup("rmi://localhost/RMIServer");
        new Thread(new Client(chatinterface)).start();
    }

}
