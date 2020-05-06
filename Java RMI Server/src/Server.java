import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Alaa Alkheder
 * Email:alaa-alkheder@outlook.com
 * Github:alaa-alkheder
 */

public class Server extends UnicastRemoteObject implements ChatInterface {

    private static final long serialVersionUID = 1L;
//    private static String clientName [] = {"alaa","Dinesh","Asjad","Clarry","Tahar"};
//    private static String clientPass [] = {"12345","123456","12345","123456","12345"};
    private ArrayList<ChatInterface> clientList;

    protected Server() throws RemoteException {
        clientList = new ArrayList<ChatInterface>();
    }

    public synchronized boolean checkClientCredintials(ChatInterface chatinterface,String clientname,String password) throws RemoteException {
        if (StartServer.CheckUserPassword(clientname,password)){
            this.clientList.add(chatinterface);
            return true;
        }
        return false;
    }

    public synchronized void broadcastMessage(String clientname , String message) throws RemoteException {
        for(int i=0; i<clientList.size(); i++) {
            clientList.get(i).sendMessageToClient(clientname.toUpperCase() + " : "+ message);
        }
    }

    public synchronized void sendMessageToClient(String message) throws RemoteException{}

    /**
     * this function for create new account
     *
     * @param user
     * @return
     * @throws RemoteException
     */
    @Override
    public Boolean registerUser(User user) throws RemoteException {
        String path="userFile\\" + user.getUniqueName();
        File f = new File(path);
        boolean b = f.mkdir();
        if (b) {
            user.setPath(path);
//            System.out.println("userFile\\" + user.getUniqueName());
        }
        else {
            //!!!!! Create random path
        }
        //this file for save a temp object when we receive a notification and the user is offline
        File file=new File(path+"\\"+"temp.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StartServer.userProfile.put(user.getUniqueName(),user);




        return true;
    }

/**
 * this function when we create a new account ,we test a unique Name is booked up
 * if the test result true this mean the name is booked up
 * */
    @Override
    public Boolean testUserName(String user) throws RemoteException {
        return StartServer.SearchUserName(user);
    }

    @Override
    public Boolean forgetPassword(User user) throws RemoteException {
        System.out.println("User Forget Password");

        return null;
    }

    @Override
    public Boolean ChangePassword(User user) throws RemoteException {
        System.out.println("User change Password");
        return null;
    }

    @Override
    public void UpLoadFile(String filename, byte[] data, int len) throws RemoteException
    {
        try{
            File f=new File(filename);
            f.createNewFile();
            FileOutputStream out=new FileOutputStream(f,true);
            out.write(data,0,len);
            out.flush();
            out.close();
            System.out.println("Done writing data...");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Object downloadFile() throws RemoteException {
        System.out.println("Download file");
        return null;
    }

    @Override
    public Object showAllFile() throws RemoteException {
        System.out.println("Show all File");
        return null;
    }

    @Override
    public String[] showAllUser() throws RemoteException {
//        return clientName.clone();
        return null;
    }


}