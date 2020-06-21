import com.sun.scenario.animation.shared.ClipInterpolator;
import com.sun.xml.internal.fastinfoset.tools.FI_DOM_Or_XML_DOM_SAX_SAXEvent;
import encRSA.RSA;
import encRSA.publicKey;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Alaa Alkheder
 * Email:alaa-alkheder@outlook.com
 * Github:alaa-alkheder
 */

public class Server extends UnicastRemoteObject implements DriveInterface {
    /**
     * user files name
     */
    //this file for save a temp object when we receive a notification and the user is offline
    public final String tempFile = "temp.json";
    // //this directory save the uploaded file in the server
    public final String userFilesDirectory = "files";
    // //this directory save the file  info in the server
    public final String filesInfoDirectory = "fileInfo";
    //this file saves files path that have been shared with me
    public final String fileShareWithMe = "fileShareWithMe.json";
    //this file saves files ...............
    public final String DirectShareFiles = "DirectShareFiles";
    public final String DirectShareFilesInfo = "DirectShareFilesInfo";
    //
    public final String jsonExtension = ".json";

    /**
     * user files path
     */
    public String tempPath;
    public String userFilesDirectoryPath;
    public String filesInfoDirectoryPath;
    public String fileShareWithMePath;
    public String DirectShareFilesPath;
    public String DirectShareFilesInfoPath;


    /**
     * clint public key variable
     */
    BigInteger e;
    BigInteger N;


    //    private static final long serialVersionUID = 1L;
    static private ArrayList<DriveInterface> clientList;
    static HashMap<String, DriveInterface> userClient = new HashMap<String, DriveInterface>();
    private User user;
    private String name;
    DriveInterface userInterface;

    /**
     * default constrictor
     *
     * @throws RemoteException
     */
    protected Server() throws RemoteException {
        clientList = new ArrayList<DriveInterface>();

    }

    /**
     * check Client Credentials method
     *
     * @param chatinterface
     * @param clientname
     * @param password
     * @return true | false
     * @throws RemoteException this function responsible for check User name and password
     *                         1-if the user name and password are correct define the interface , save client data and return true.
     *                         2-return false otherwise
     */
    public synchronized boolean checkClientCredintials(DriveInterface chatinterface, String clientname, String password) throws RemoteException {
        System.out.println(chatinterface);
        if (StartServer.CheckUserPassword(clientname, password)) {
            this.clientList.add(chatinterface);
            userInterface = chatinterface;
            name = clientname;
            userClient.put(name, chatinterface);
            //send massage to test the connection =>for programmer delete in product version
            chatinterface.sendMessageToClient(0, "Test Connection ");
            defineUserPath(StartServer.userProfile.get(clientname));
            chatinterface.sendPublicKeyToClint(StartServer.rsa.getPublic_key().getE(), StartServer.rsa.getPublic_key().getN());
            System.out.println(StartServer.rsa.getPublic_key().getE() + "----" + StartServer.rsa.getPublic_key().getN());
            return true;//if you login done
        }
        return false;//if happen any problem
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

            String temp = names.get(i).toString();
            //1-for any user we add File_path to his/her fileShareMe

            JSONParser parser = new JSONParser();
            JSONObject jsonobject;
            Object obj = null;
            Object obj1 = null;
            String path = "..\\Java RMI Server\\userFile\\" + temp + "\\" + fileShareWithMe;

            try {
                obj = parser.parse(new FileReader(path));
                obj1 = parser.parse(new FileReader(filesInfoDirectoryPath + "\\" + fileName + "Enc" + jsonExtension));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            jsonobject = (JSONObject) obj;
            JSONObject jsontemp = (JSONObject) obj1;
            JSONObject jo = new JSONObject();
            jo.put("fileName", fileName);
            jo.put("from", name);
            jo.put("type", (String) jsontemp.get("type"));
            System.out.println(jsontemp.get("len"));
            jo.put("len", jsontemp.get("len"));
            jo.put("path", userFilesDirectoryPath + "\\" + fileName);
            String n = ((String) jsonobject.get("n"));
            int x = Integer.parseInt(n);
            x++;
            n = String.valueOf(x);
            jsonobject.replace("n", n);
            jsonobject.put("file" + x, jo.toJSONString());

            try (FileWriter file = new FileWriter(path)) {
                file.write(jsonobject.toString());
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("done:for any user we add File_path to his/her fileShareMe ");
//           //2- send notification if the user is online

            userClient.entrySet().forEach(entry -> {
                try {

                    if (entry.getKey().toString() == temp)
                        entry.getValue().sendMessageToClient(1, "your received file" + "from : " + name + " => File Name is : " + fileName);
                    else { //!!!!!  save notification in temp file
                        System.out.println("user :" + entry.getKey().toString() + " is offline and received file " + fileName);
                    }
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
    public Boolean registerUser(DriveInterface chatinterface,User user) throws RemoteException {
        //check the user if exits
        if (StartServer.SearchUserName(user.getUniqueName()))
            return false;
        //make folder for new user
        String path1 = "userFile\\" + user.getUniqueName();
        name = user.getUniqueName();
        File f = new File(path1);
        boolean b = f.mkdir();
        this.clientList.add(chatinterface);
        userInterface = chatinterface;
        userClient.put(name, chatinterface);
        //send massage to test the connection =>for programmer delete in product version
        chatinterface.sendMessageToClient(0, "Test Connection ");
        if (b) {
            user.setPath(path1);

        } else {
            //!!!!! Create random path
//            user.setPath(path1);
//            this.user = user;
        }

        defineUserPath(user);
        //this file for save a temp object when we receive a notification and the user is offline
        File tempfile = new File(tempPath);
        //this directory save the uploaded file in the server
        File Userfiles = new File(userFilesDirectoryPath);
        //
        File fileInfo = new File(filesInfoDirectoryPath);

        File fileDirectShare = new File(DirectShareFilesPath);
        File DirectShareFilesInfo = new File(DirectShareFilesInfoPath);
//        //create new Files
        try {
            tempfile.createNewFile();
            Userfiles.mkdir();
            fileInfo.mkdir();
            fileDirectShare.mkdir();
            DirectShareFilesInfo.mkdir();
            //
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("n", "0");
            FileWriter file = new FileWriter(fileShareWithMePath);
            file.write(jsonObject.toString());
            file.flush();

            createHandShaking(user.getUniqueName());
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        //Save user object in the
        StartServer.userProfile.put(user.getUniqueName(), user);
        StartServer.user.put(user.getUniqueName(), user.getPassword());
        StartServer.saveUserPassword();
        StartServer.saveUserObject();
        return true;
    }


    public void sendFileToClient(String FileName, int type,int downloadType) throws RemoteException {
        String path="";
        if(downloadType == 0){//0 is share with me file without hand key
              path = userFilesDirectoryPath + "\\" + FileName;
        }
        System.err.println(FileName);

        if (downloadType == 1) {//use when we click download in file share with me view

            path = DirectShareFilesPath+"\\"+FileName;
            System.out.println("**************"+path);
        }
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
    public String downloadFileInfo(String fileName) throws RemoteException {
        JSONParser parser = new JSONParser();
        JSONObject jsonobject;
        Object obj = null;
        try {
            obj = parser.parse(new FileReader(DirectShareFilesInfoPath + "\\" + fileName  + jsonExtension));
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        jsonobject = (JSONObject) obj;
        return jsonobject.toString();
    }

    @Override
    public boolean sendHandKeyToClint(String file) throws RemoteException {
        return false;
    }

    @Override
    public boolean sendHandKeyToServer(String name, String file) throws RemoteException {
        for (HashMap.Entry<String, DriveInterface> entry : userClient.entrySet()) {
            if (entry.getKey().equals(name)) {//the user is online
                entry.getValue().sendHandKeyToClint(file);
                System.out.println("user " + entry.getKey() + "is online");
            } else {//user is offline

            }


        }
        return false;
    }

    @Override
    public void addFileInfoDirect(String name, String fileName, int size, String type, String encType) throws RemoteException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("filename", fileName);
        jsonObject.put("size", size);
        jsonObject.put("type", type);
        jsonObject.put("keyType", encType);
        jsonObject.put("owner", this.name);
        try (FileWriter file = new FileWriter("..\\Java RMI Server\\userFile\\" + name + "\\" + DirectShareFiles + "\\" + DirectShareFilesInfo +"\\"+ fileName + jsonExtension)) {
            file.write(jsonObject.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    int i = 0;//test delete after the finish code

    @Override
    public void UpLoadFile(String filename, byte[] data, int len) throws RemoteException {
        try {
            //!!!!check if the file uploaded use searchFile();

            File f = new File(userFilesDirectoryPath + "\\" + filename);
            f.createNewFile();
            FileOutputStream out = new FileOutputStream(f, true);
            out.write(data, 0, len);
            out.flush();
            out.close();
            //timer for download
//            System.out.println("Done writing data..." + i++);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Done upload file name is :  " + filename);
    }

    @Override
    public void addFileInfo(String filename, int len, String type, String encType) throws RemoteException {
        //!!!!check if the file uploaded use searchFile();
        System.out.println(" ADD FILE INFO method  --- file info" + filename);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("filename", filename);
        jsonObject.put("size", len);
        jsonObject.put("type", type);
        jsonObject.put("keyType", encType);
        try (FileWriter file = new FileWriter(filesInfoDirectoryPath + "\\" + filename + jsonExtension)) {
            file.write(jsonObject.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println(filename);
//        try {
//            StartServer.rsa.deccryptrsa(userFilesDirectoryPath+"\\"+filename,filename.substring(0,filename.length()-3),type);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (ClassNotFoundException ex) {
//            ex.printStackTrace();
//        }
        StartServer.aes.decryption(StartServer.privateKey, userFilesDirectoryPath + "\\" + filename, "alaa", type);


    }

    @Override
    public void downloadFile(String filename, byte[] data, int len) throws RemoteException {
    }

    @Override
    public Object  showAllHandFiles() throws RemoteException {
        System.err.println(name+"::::::::::::::: "+DirectShareFilesPath);
        List<String> StringlistFileInfo = new LinkedList<>();
        File[] files = new File(DirectShareFilesPath).listFiles();
        //If this pathname does not denote a directory, then listFiles() returns null.
        for (File file : files) {

            if (file.isFile()) {
                StringlistFileInfo.add(file.getName());
                System.out.println(file.getName());
            }
        }
        return StringlistFileInfo;
    }


    /**
     * show All File method
     *
     * @return Linked list from String Type have files name
     * @throws RemoteException
     */
    @Override
    public Object showAllFile() throws RemoteException {
        List<String> StringlistFileInfo = new LinkedList<>();
        File[] files = new File(userFilesDirectoryPath).listFiles();
//If this pathname does not denote a directory, then listFiles() returns null.

        for (File file : files) {
            if (file.isFile()) {
                StringlistFileInfo.add(file.getName());
                System.out.println(file.getName());
            }
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

    @Override
    public String showAllFileShareWithMEInfo() throws RemoteException {

        JSONParser parser = new JSONParser();
        JSONObject jsonobject;
        Object obj = null;

        try {
            obj = parser.parse(new FileReader(fileShareWithMePath));
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        jsonobject = (JSONObject) obj;
        return jsonobject.toString();
    }

    @Override
    public Boolean userStatus(String user) throws RemoteException {
        for (Map.Entry<String, DriveInterface> entry : userClient.entrySet()) {
            if (user.equals(entry.getKey())) ;
            return true;
        }
        return false;
    }

    @Override
    public void AddPublicKeyToFile(BigInteger EE, BigInteger NN) throws RemoteException {
        String path = "publicKey.json";
        JSONParser parser = new JSONParser();
        JSONObject jsonobject = new JSONObject();
        Object obj = null;
        try {
            obj = parser.parse(new FileReader(path));
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        jsonobject = (JSONObject) obj;
        int i = Integer.parseInt(String.valueOf(jsonobject.get("i")));
        i++;
        JSONObject js = new JSONObject();
        js.put("user", name);
        js.put("e", EE);
        js.put("n", NN);
        jsonobject.put("i" + i, js.toJSONString());
        jsonobject.replace("i", String.valueOf(i));
        try (FileWriter file = new FileWriter(path)) {
            file.write(jsonobject.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String returnClientPublicKey(String name) throws RemoteException {

        for (Map.Entry<String, DriveInterface> entry : userClient.entrySet()) {
            if (name.equals(entry.getKey())) {
                System.out.println(name + "*-**********" + entry.getKey());


                return entry.getValue().returnMyPublicKey();
            }
        }     //user is offline
        JSONParser parser = new JSONParser();
        JSONParser parser1 = new JSONParser();
        JSONObject jsonobject = new JSONObject();
        Object obj = null;
        try {
            obj = parser.parse(new FileReader("publicKey.json"));

            jsonobject = (JSONObject) obj;
            System.out.println("jsonobject.get(\"i\")" + jsonobject.get("i"));
            for (int i = 1; i <= Integer.parseInt(String.valueOf(jsonobject.get("i"))); i++) {
                JSONObject jsonObject1 = (JSONObject) parser1.parse(jsonobject.get("i" + i).toString());
                if (jsonObject1.get("user").toString().equals(name)) {
                    System.out.println("jsonobject.get(\"name\")" + jsonObject1.get("user"));
                    return jsonObject1.toString();
                }
            }
        } catch (ParseException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    @Override
    public String returnMyPublicKey() throws RemoteException {
        return null;
    }


    @Override
    public void sendPublicKeyToClint(BigInteger e, BigInteger N) throws RemoteException {

    }

    @Override
    public void sendPublicKeyToServer(BigInteger e, BigInteger N) throws RemoteException {
        this.e = e;
        this.N = N;
        try {
            System.out.println("////" + e + "*****" + N);
            StartServer.rsa.encryptrsa("aesKey.txt", new publicKey(e, N));
            userInterface.sendPrivateKeyToClint(1, Files.readAllBytes(Paths.get("aesKeyEnc")));
//            Files.delete(Paths.get("aesKeyEnc"));
//            Files.delete(Paths.get("aesKey.txt"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }

    @Override
    public void sendPrivateKeyToClint(int type, byte[] bytes) throws RemoteException {

    }

    @Override
    public void sendFileToServerDirect(byte[] byteFile, String fileName, String name) {
        try {
            FileOutputStream out = new FileOutputStream("userFile\\" + name + "\\" + DirectShareFiles + "\\" + fileName);
            out.write(byteFile);
            out.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }


    /**
     * private method
     */


    private boolean searchFile(String name) {
        //Creating a File object for directory
        File directoryPath = new File(filesInfoDirectoryPath);
        //List of all files and directories
        File filesList[] = directoryPath.listFiles();
        for (int j = 0; j < filesList.length; j++) {
            if (filesList[i].getName() == name)
                return true;
        }
        return false;
    }

    private void defineUserPath(User user) {
        //define User path
        this.user = user;
        tempPath = this.user.getPath() + "\\" + tempFile;
        userFilesDirectoryPath = this.user.getPath() + "\\" + userFilesDirectory;
        filesInfoDirectoryPath = this.user.getPath() + "\\" + filesInfoDirectory;
        fileShareWithMePath = this.user.getPath() + "\\" + fileShareWithMe;
        DirectShareFilesPath = this.user.getPath() + "\\" + DirectShareFiles;
        DirectShareFilesInfoPath = this.user.getPath() + "\\" + DirectShareFiles + "\\" + DirectShareFilesInfo;
    }

    private void createHandShaking(String name) throws IOException, ParseException {
        String path = "handShakingKey.json";
        JSONParser parser = new JSONParser();
        JSONObject jsonobject = new JSONObject();
        Object obj = null;
        obj = parser.parse(new FileReader(path));
        jsonobject = (JSONObject) obj;
        int i = Integer.parseInt(String.valueOf(jsonobject.get("i")));
        i++;
        for (Map.Entry<String, String> entry : StartServer.user.entrySet()) {
            JSONObject js = new JSONObject();
            js.put("user1", entry.getKey());
            js.put("user2", name);
            js.put("key265", getRandomKey(64));
            js.put("key192", getRandomKey(48));
            js.put("key128", getRandomKey(32));
            jsonobject.put("i" + i, js.toJSONString());
            i++;
        }
        jsonobject.replace("i", String.valueOf(i));
        try (FileWriter file = new FileWriter(path)) {
            file.write(jsonobject.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private String getRandomKey(int length) {
        Random rand = new Random();
        StringBuffer sb = new StringBuffer();
        while (sb.length() < length)
            sb.append(Integer.toHexString(rand.nextInt()));
        return sb.toString().substring(0, length);
    }
}