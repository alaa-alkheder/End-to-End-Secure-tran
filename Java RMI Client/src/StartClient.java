import java.io.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * User: Alaa Alkheder
 * Email:alaa-alkheder@outlook.com
 * Github:alaa-alkheder
 */
public class StartClient {
    public static void main(String[] args) throws IOException, NotBoundException, NotBoundException {


//
        ChatInterface chatinterface = (ChatInterface) Naming.lookup("rmi://localhost/RMIServer");
        new Thread(new Client(chatinterface)).start();
    }

}
