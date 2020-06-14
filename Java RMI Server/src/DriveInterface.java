

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Alaa Alkheder
 * Email:alaa-alkheder@outlook.com
 * Github:alaa-alkheder
 */

public interface DriveInterface extends java.rmi.Remote {

    boolean checkClientCredintials(DriveInterface ci, String name, String pass) throws RemoteException;

    void broadcastMessage(String name, String message) throws RemoteException;

    void sendMessageToClient(int id,String message) throws RemoteException;



    boolean shareFile(String fileName, LinkedList<String> name)throws RemoteException;

    Boolean registerUser(User user) throws RemoteException;

    Boolean testUserName(String user) throws RemoteException;

    Boolean forgetPassword(User user) throws RemoteException;

    Boolean ChangePassword(User user) throws RemoteException;

    String downloadFileInfo() throws RemoteException;

    public void sendFileToClient(String FileName) throws RemoteException;

    void UpLoadFile(String filename, byte[] data, int len) throws RemoteException;

    void addFileInfo(String filename, int len, String type) throws RemoteException;

    void downloadFile(String filename, byte[] data, int len) throws RemoteException;

    Object showAllFile() throws RemoteException;

    Object showAllUser() throws RemoteException;


    /**
     * cryptography RSA methods
     */

    void sendPublicKeyToClint(BigInteger e,BigInteger N) throws RemoteException;
    void sendPublicKeyToServer(BigInteger e,BigInteger N) throws RemoteException;
    /**
     * cryptography AES methods
     */

    void sendPrivateKeyToClint(int type,byte[] bytes) throws RemoteException;

}
