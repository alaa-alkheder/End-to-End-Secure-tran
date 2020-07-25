

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.LinkedList;



public interface DriveInterface extends java.rmi.Remote {

    boolean checkClientCredintials(DriveInterface ci, String name, String pass) throws RemoteException;

    void broadcastMessage(String name, String message) throws RemoteException;

    void sendMessageToClient(int id, String message) throws RemoteException;


    boolean shareFile(String fileName, LinkedList<String> name ,String me) throws RemoteException;

    Boolean registerUser(DriveInterface ci,User user) throws RemoteException;

    Boolean testUserName(String user) throws RemoteException;

    Boolean forgetPassword(User user) throws RemoteException;

    Boolean ChangePassword(User user) throws RemoteException;

    String downloadFileInfo(String fileName ,String me) throws RemoteException;

    public void sendFileToClient(String FileName, int type,int downloadType,String me) throws RemoteException;

    void UpLoadFile(String filename, byte[] data, int len, String me,int type) throws RemoteException;

    void addFileInfo(String filename, int len, String type, String encType ,String me) throws RemoteException;

    void downloadFile(String filename, byte[] data, int len ) throws RemoteException;

    Object showAllFile(String me) throws RemoteException;

    Object showAllUser() throws RemoteException;

    String showAllFileShareWithMEInfo(String me) throws RemoteException;

    Boolean userStatus(String user) throws RemoteException;

    Boolean sendMassageChat(String receiver, String massage, String sender) throws RemoteException;


    /**
     * cryptography RSA methods
     */
    void AddPublicKeyToFile(BigInteger e, BigInteger N,String me) throws RemoteException;


    /**
     * cryptography AES methods
     */

    void sendPrivateKeyToClint(int type, byte[] bytes) throws RemoteException;


    /**
     * cryptography hand methods
     */
    void sendFileToServerDirect(byte[] byteFile, String fileName, String name,String me) throws RemoteException;

    void sendPublicKeyToClint(BigInteger e, BigInteger N) throws RemoteException;

    void sendPublicKeyToServer(BigInteger e, BigInteger N,String me) throws RemoteException;

    String returnClientPublicKey(String name) throws RemoteException;

    String returnMyPublicKey( ) throws RemoteException;

    boolean sendHandKeyToClint(String file) throws RemoteException;

    boolean sendHandKeyToServer(String name, String file) throws RemoteException;

    void addFileInfoDirect(String name,String fileName, int size, String type, String encType,String me) throws RemoteException;

    Object  showAllHandFiles(String me) throws RemoteException;


}
