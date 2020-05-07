import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Alaa Alkheder
 * Email:alaa-alkheder@outlook.com
 * Github:alaa-alkheder
 */

public class Server extends UnicastRemoteObject implements DriveInterface {

    private static final long serialVersionUID = 1L;
    private ArrayList<DriveInterface> clientList;
    private String path="";
    DriveInterface sssss;

    protected Server() throws RemoteException {
        clientList = new ArrayList<DriveInterface>();

    }

    public synchronized boolean checkClientCredintials(DriveInterface chatinterface,String clientname,String password) throws RemoteException {
        if (StartServer.CheckUserPassword(clientname,password)){
            this.clientList.add(chatinterface);
            sssss=chatinterface;
            sssss.sendMessageToClient("asddsaasd");
//            sssss.sendFileToClient("");
            //!!!! un comment next line after create register
//            path=StartServer.userProfile.get(clientname).getPath();
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
        }
        else {
            //!!!!! Create random path
        }

        //this file for save a temp object when we receive a notification and the user is offline
        File file=new File(path+"\\"+"temp.txt");
        //this file save the uploaded file in the server
        File Userfiles=new File(path+"\\"+"UserFiles.xls");
        try {
            file.createNewFile();
            Userfiles.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StartServer.userProfile.put(user.getUniqueName(),user);




        return true;
    }


    /**
     *
     */
    public void sendFileToClient(String FileName) throws RemoteException {
        String path="qwqw.pdf";//path for user + file name
        File f1 = new File(path);
        int fileSize=0;
        int timer=0;
        try {
            fileSize= (int) (Files.size(Paths.get(path))/1024/1024)+1;
            timer=fileSize;
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileInputStream in = null;
        try {
            in = new FileInputStream(f1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] mydata = new byte[1024 * 1024];
        int mylen = 0;
        try {
            mylen = in.read(mydata);

        } catch (IOException e) {
            e.printStackTrace();
        }
        while (mylen > 0) {
            //timer for Upload
            System.out.println("Done Upload File Input Stream ..."+ --timer);
           sssss.downloadFile  (f1.getName(), mydata, mylen);
            try {
                mylen = in.read(mydata);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
    int i=0;//test delete after the finish code
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
            //timer for download
            System.out.println("Done writing data..."+ i++);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void addFileInfo(String filename, int len, String type) throws RemoteException {
        System.out.println(filename);
        System.out.println(len +"MB");
        System.out.println(type);
        //add file upload info to UserFiles.xls
    }

    @Override
    public Object downloadFile(String filename, byte[] data, int len) throws RemoteException {
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