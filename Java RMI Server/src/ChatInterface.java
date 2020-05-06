

import java.io.File;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: Alaa Alkheder
 * Email:alaa-alkheder@outlook.com
 * Github:alaa-alkheder
 */

public interface ChatInterface extends java.rmi.Remote {
    boolean checkClientCredintials(ChatInterface ci, String name, String pass) throws RemoteException;
    void broadcastMessage(String name, String message) throws RemoteException;
    void sendMessageToClient(String message) throws RemoteException;


    Boolean registerUser(User user) throws RemoteException;
    Boolean testUserName(String user)  throws RemoteException;
    Boolean forgetPassword(User user)  throws RemoteException;
    Boolean ChangePassword(User user)  throws RemoteException;


    void UpLoadFile(String filename, byte[] data, int len) throws RemoteException;
    Object downloadFile()throws RemoteException;
    Object showAllFile()throws RemoteException;
    String[] showAllUser() throws RemoteException;

}
