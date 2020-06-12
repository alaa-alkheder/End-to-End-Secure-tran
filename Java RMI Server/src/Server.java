import com.sun.scenario.animation.shared.ClipInterpolator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Alaa Alkheder
 * Email:alaa-alkheder@outlook.com
 * Github:alaa-alkheder
 */

public class Server extends UnicastRemoteObject implements DriveInterface {

    //    private static final long serialVersionUID = 1L;
    static private ArrayList<DriveInterface> clientList;
    static HashMap<String, DriveInterface> userClient = new HashMap<String, DriveInterface>();
    private User user;
    private String name;
    DriveInterface userInterface;

    protected Server() throws RemoteException {
        clientList = new ArrayList<DriveInterface>();

    }

    public synchronized boolean checkClientCredintials(DriveInterface chatinterface, String clientname, String password) throws RemoteException {
        System.out.println(chatinterface);
        if (StartServer.CheckUserPassword(clientname, password)) {
            this.clientList.add(chatinterface);
            userInterface = chatinterface;
            name = clientname;
            userClient.put(name, chatinterface);

            chatinterface.sendMessageToClient(0, "Test Connection ");
////            sssss.sendFileToClient("");
//            //!!!! un comment next line after create register
//            user = StartServer.userProfile.get(clientname);
//            System.out.println(user);
            System.out.println("55555555555555");
            return true;
        }
        return false;
    }

    public synchronized void broadcastMessage(String clientname, String message) throws RemoteException {
//        for (int i = 0; i < clientList.size(); i++) {
//            clientList.get(i).sendMessageToClient(clientname.toUpperCase() + " : " + message);
//        }
    }

    @Override
    public synchronized void sendMessageToClient(int id, String message) throws RemoteException {
    }

    @Override
    public boolean shareFile(String fileName, LinkedList<String> names) throws RemoteException {

        System.out.println("File share");
        System.out.println(fileName);
        for (int i = 0; i < names.size(); i++) {
            System.out.println(i + ":" + names.get(i));


            //1-for any user we add File_path to his/her fileShareMe
//            try {
            String path = "..\\Java RMI Server\\userFile\\" + names.get(i) + "\\fileShareMe";
            LinkedList<shareFile> listFileInfo = new LinkedList<>();
//                FileInputStream fileInputStream = new FileInputStream(path);
//                ObjectInputStream ois = new ObjectInputStream(fileInputStream);
//                listFileInfo = (LinkedList<shareFile>) ois.readObject();
//                ois.close();
//                fileInputStream.close();
//                listFileInfo.add(new shareFile(fileName, name, user.getPath() + "\\" + fileName));
////                Files.deleteIfExists(Paths.get(path));
//                File file=new File(path);
//                file.delete();
//                FileOutputStream fileOutputStream = new FileOutputStream(path);
//                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
//                objectOutputStream.writeObject(listFileInfo);
//                objectOutputStream.close();
//                fileOutputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
            System.out.println("done:for any user we add File_path to his/her fileShareMe ");
//           //2- send notification if the user is online
            userClient.entrySet().forEach(entry -> {
                try {
                    System.out.println("/*/*/*/*/*/*/*/*/*" + entry.getKey().toString());
//                    if (entry.getKey().);
                    entry.getValue().sendMessageToClient(1, "your received file" + "from : " + name + " => File Name is : " + fileName);
//                    else{ //!!!!!  save notification in temp file
//                }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }


            });

            //!!!! 3-add user_list to User File

        }
        return false;
    }


    /**
     * this function for create new account
     *
     * @param user
     * @return
     * @throws RemoteException
     */
    @Override
    public Boolean registerUser(User user) throws RemoteException {
        //check the user if exits
        if (StartServer.SearchUserName(user.getUniqueName()))
            return false;
        //make folder for new user
        String path1 = "userFile\\" + user.getUniqueName();
        name = user.getUniqueName();
        File f = new File(path1);
        boolean b = f.mkdir();
        if (b) {
            user.setPath(path1);
            this.user = user;
        } else {
            //!!!!! Create random path
//            user.setPath(path1);
//            this.user = user;
        }
        //this file for save a temp object when we receive a notification and the user is offline
        File file = new File(this.user.getPath() + "\\" + "temp.txt");
        //this file save the uploaded file in the server
//        String path = "C:\\Users\\Eng Alaa Alkheder\\Desktop\\"+ "UserFiles";
//        File Userfiles = new File(path);
        File Userfiles = new File(this.user.getPath() + "\\" + "UserFiles");
        //
        File fileShare = new File(this.user.getPath() + "\\" + "fileShare");
        //
        File fileShareMe = new File(this.user.getPath() + "\\" + "fileShareMe");
        fileShare.mkdir();

        try {
            fileShareMe.createNewFile();
            file.createNewFile();
            System.out.println("register user   create file is :" + Userfiles.createNewFile());
            ;
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(this.user.getPath() + "\\" + "UserFiles");
            ObjectOutputStream oob = new ObjectOutputStream(fileOutputStream);
            oob.writeObject(new ArrayList<fileInfo>());
            oob.close();
            fileOutputStream.close();
            FileOutputStream outputStream = new FileOutputStream(this.user.getPath() + "\\" + "fileShareMe");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(new LinkedList<shareFile>());
            objectOutputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Save user object
        StartServer.userProfile.put(user.getUniqueName(), user);
        StartServer.user.put(user.getUniqueName(), user.getPassword());
        StartServer.saveUserPassword();
        StartServer.saveUserObject();
        return true;
    }

    /**
     *
     */
    public void sendFileToClient(String FileName) throws RemoteException {
//        String path = "qwqw.pdf";//path for user + file name

        String path = "..\\Java RMI Server\\userFile\\" + name + "\\" + FileName;
        File f1 = new File(path);
        int fileSize = 0;
        int timer = 0;
        try {
            fileSize = (int) (Files.size(Paths.get(path)) / 1024 / 1024) + 1;
            timer = fileSize;
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
            System.out.println("Done Upload File Input Stream ..." + --timer);
            userInterface.downloadFile(f1.getName(), mydata, mylen);
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
     */
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
    public String downloadFileInfo() throws RemoteException {

        return "sssssss";
    }

    int i = 0;//test delete after the finish code

    @Override
    public void UpLoadFile(String filename, byte[] data, int len) throws RemoteException {
        try {
//            System.out.println(this.user.getPath());
            String path = "..\\Java RMI Server\\userFile\\" + name + "\\" + filename;
            File f = new File(path);
            f.createNewFile();
            FileOutputStream out = new FileOutputStream(f, true);
            out.write(data, 0, len);
            out.flush();
            out.close();
            //timer for download
            System.out.println("Done writing data..." + i++);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addFileInfo(String filename, int len, String type) throws RemoteException {
//        String path = "C:\\Users\\Eng Alaa Alkheder\\Desktop\\"+ "UserFiles";
        String path = "..\\Java RMI Server\\userFile\\" + name + "\\UserFiles";

        List<fileInfo> listFileInfo = new ArrayList<fileInfo>();
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fileInputStream);
            listFileInfo = (ArrayList<fileInfo>) ois.readObject();
            ois.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("FILE INFO " + filename);
        System.out.println("@@@" + listFileInfo.size());
        for (int j = 0; j < listFileInfo.size(); j++) {
            System.out.println(j + " : " + listFileInfo.get(i).getName());
        }
        fileInfo fileInfo = new fileInfo(filename, len, type);
        listFileInfo.add(fileInfo);
        System.out.println("********1");
//        for (int j = 0; j < listFileInfo.size(); j++) {
//            System.out.println(j + " : " + listFileInfo.get(i).getName());
//        }

        try {
//            File f=new File(path);
//            f.delete();
//            Files.deleteIfExists(Paths.get(path));
            File file = new File(path);
            System.out.println(file.delete());
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("********12");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            ObjectOutputStream oob = new ObjectOutputStream(fileOutputStream);
            oob.writeObject(listFileInfo);
            oob.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("********13");
    }

    @Override
    public void downloadFile(String filename, byte[] data, int len) throws RemoteException {
    }

    @Override
    public Object showAllFile() throws RemoteException {
/*

  //Creating a File object for directory
      File directoryPath = new File("D:\\ExampleDirectory");
      //List of all files and directories
      File filesList[] = directoryPath.listFiles();
* */
//        String path = "C:\\Users\\Eng Alaa Alkheder\\Desktop\\"+ "UserFiles";
        String path = "..\\Java RMI Server\\userFile\\" + name + "\\" + "UserFiles";
        List<fileInfo> listFileInfo = new ArrayList<>();
        List<String> StringlistFileInfo = new LinkedList<>();
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
            listFileInfo = (ArrayList<fileInfo>) ois.readObject();
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        for (int j = 0; j < listFileInfo.size(); j++) {
            StringlistFileInfo.add(listFileInfo.get(i).getName());
            System.out.println(listFileInfo.get(i).getName());
        }


        return StringlistFileInfo;
    }

    @Override
    public Object showAllUser() throws RemoteException {
        LinkedList<User> user = new LinkedList<>();
        for (HashMap.Entry<String, User> entry : StartServer.userProfile.entrySet()) {
            User A = entry.getValue();
            A.setPassword("*****");
            user.addLast(A);
        }
        return user;
    }


}