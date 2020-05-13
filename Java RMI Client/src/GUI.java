import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: Alaa Alkheder
 * Email:alaa-alkheder@outlook.com
 * Github:alaa-alkheder
 */
public class GUI {
    //the next function for desc how you can use RMI methods
    void test(){
        try {
            Client.server.testUserName("SOSO");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
