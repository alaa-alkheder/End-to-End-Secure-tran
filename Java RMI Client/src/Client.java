import AES.AES;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * User: Alaa Alkheder
 * Email:alaa-alkheder@outlook.com
 * Github:alaa-alkheder
 */
public class Client extends UnicastRemoteObject implements DriveInterface, Runnable {
    private static final long serialVersionUID = 1L;
    static DriveInterface server;
    static Client client;
    static String ClientName;
    boolean chkExit = true;
    boolean chkLog = false;
    /**
     * server public key variable
     */
    static BigInteger e;
    static BigInteger N;

    static encRSA.RSA rsa = new encRSA.RSA();
    static AES aes = new AES();
    private static String privateKey = "";

    public static String getPrivateKey() {
        return privateKey;
    }


    protected Client(DriveInterface chatinterface) throws RemoteException {

        server = chatinterface;
        client = this;
//        System.out.println(server.registerUser(null));
//        System.out.println(server.registerUser(null));
//        this.ClientName = clientname;
//
//        User u = new User(clientname,password);


//        server.regsterUser(u);
//        server.LoginUser(u);
//        server.forgetPassword(u);
//        server.ChangePassword(u);
//        server.downloadFile();
//        server.UpLoadFile(x);
//        LinkedList<String>ss= (LinkedList<String>) server.showAllFile();
//        System.out.println (String.valueOf(ss.get(0)));
        //        System.out.println(server.downloadFile());
    }


    public void sendFileToServer(String message) throws RemoteException {
        FileDialog dialog = new FileDialog((Frame) null, "Select File to Open");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        String path = dialog.getDirectory() + dialog.getFile();
        System.out.println(path);
        if (dialog.getFile() == null) {
            System.out.println("we dont select any file");
            return;
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
            server.UpLoadFile(f1.getName(), mydata, mylen, ClientName, 0);
            try {
                mylen = in.read(mydata);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String extension = "";
        if (path.contains("."))
            extension = path.substring(path.lastIndexOf("."));
        server.addFileInfo(f1.getName(), fileSize, extension, "default", ClientName);

    }


    @Override
    public boolean shareFile(String fileName, LinkedList<String> name, String me) throws RemoteException {
        return false;
    }


    @Override
    public Boolean registerUser(DriveInterface ci, User user) throws RemoteException {

        return false;
    }

    @Override
    public Boolean testUserName(String user) throws RemoteException {
        return null;
    }

    @Override
    public Boolean forgetPassword(User user) throws RemoteException {
        return null;
    }

    @Override
    public Boolean ChangePassword(User user) throws RemoteException {
        return null;
    }

    @Override
    public String downloadFileInfo(String fileName, String me) throws RemoteException {
        return "";
    }

    @Override
    public void sendFileToClient(String FileName, int type, int downloadType, String me) throws RemoteException {

    }

    @Override
    public void addFileInfo(String filename, int len, String type, String encType, String me) throws RemoteException {

    }

    @Override
    public void UpLoadFile(String filename, byte[] data, int len, String me, int type) throws RemoteException {

    }

    @Override
    public void downloadFile(String filename, byte[] data, int len) throws RemoteException {
        try {

            File f = new File(filename);
            f.createNewFile();
            FileOutputStream out = new FileOutputStream(f, true);
            out.write(data, 0, len);
            out.flush();
            out.close();
            //timer for download
            System.out.println("Done writing data...");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean sendHandKeyToClint(String file) throws RemoteException {
        JSONParser parser = new JSONParser();
        JSONObject jsonobject;
        Object obj = null;
        try {
            obj = parser.parse(file);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        jsonobject = (JSONObject) obj;
        if (String.valueOf(Client.rsa.getPublic_key().getE()).equals(jsonobject.get("e").toString())) {
            jsonobject.replace("handKey", Client.rsa.decryptStringRsa(jsonobject.get("handKey").toString()));
        }
        try (FileWriter file1 = new FileWriter(jsonobject.get("fileName").toString() + ".json")) {
            file1.write(jsonobject.toString());
            file1.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean sendHandKeyToServer(String name, String file) throws RemoteException {
        return false;
    }

    @Override
    public void addFileInfoDirect(String name, String fileNmae, int size, String type, String encType, String me) throws RemoteException {

    }

    @Override
    public Object showAllHandFiles(String me) throws RemoteException {
        return null;
    }


    @Override
    public Object showAllFile(String me) throws RemoteException {
        return null;
    }

    @Override
    public Object showAllUser() throws RemoteException {
        return new String[0];
    }

    @Override
    public Object showAllWorkShop(String me) throws RemoteException {
        return null;
    }

    @Override
    public Object showFileInWorkShop(String workShopName) throws RemoteException {
        return null;
    }

    @Override
    public String showAllFileShareWithMEInfo(String me) throws RemoteException {
        return null;
    }

    @Override
    public Boolean userStatus(String user) throws RemoteException {
        return null;
    }

    @Override
    public Boolean sendMassageChat(String receiver, String massage, String sender) throws RemoteException {
        return null;
    }

    @Override
    public Boolean addWorkShop(String workShopName, String me) throws RemoteException {
        return null;
    }

    @Override
    public Boolean removeFileToWorkShop(String fileName, String workShopName, String me) throws RemoteException {
        return null;
    }

    @Override
    public Boolean addUserToWorkShop(String user, String workShopName, String me) throws RemoteException {
        return null;
    }

    @Override
    public Boolean deleteWorkShop(String fileName, String me) throws RemoteException {
        return null;
    }

    @Override
    public void AddPublicKeyToFile(BigInteger e, BigInteger N, String me) throws RemoteException {

    }

    @Override
    public String returnClientPublicKey(String name) throws RemoteException {
        return null;
    }

    @Override
    public String returnMyPublicKey() throws RemoteException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("e", String.valueOf(rsa.getPublic_key().getE()));
        jsonObject.put("n", String.valueOf(rsa.getPublic_key().getN()));

        return jsonObject.toString();
    }


    @Override
    public void sendPublicKeyToClint(BigInteger e, BigInteger N) throws RemoteException {
        this.e = e;
        this.N = N;
    }

    @Override
    public void sendPublicKeyToServer(BigInteger e, BigInteger N, String me) throws RemoteException {

    }

    @Override
    public void sendPrivateKeyToClint(int type, byte[] bytes) throws RemoteException {
        File file = new File("aesKeyEnc");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            fileOutputStream.close();
            rsa.deccryptrsa("aesKeyEnc", "privateKey", ".txt");
            BufferedReader bufferedReader = new BufferedReader(new FileReader("privateKey.txt"));
            privateKey = bufferedReader.readLine();
            bufferedReader.close();
            Files.delete(Paths.get("aesKeyEnc"));
            Files.delete(Paths.get("privateKey.txt"));
            System.out.println(privateKey);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void sendFileToServerDirect(byte[] byteFile, String fileName, String name, String me) {

    }

    public void broadcastMessage(String clientname, String message) throws RemoteException {
    }

    @Override
    public void sendMessageToClient(int id, String message) throws RemoteException {
        switch (id) {
            //test Connection
            case 0: {
                System.out.println(message);
                break;
            }
            //Send new File to User
            case 1: {
                System.out.println(message);
                break;
            }
            case 2: {
                tempMassage.add(message);
                System.out.println("@@@@@@ Case 2");
                System.out.println("!!!!!!!!!!" + message);
                break;

            }
        }
    }

    public boolean checkClientCredintials(DriveInterface chatinterface, String clientname, String password) throws RemoteException {
        return true;
    }

    public void run() {
    } /*{
        Scanner scanner = new Scanner(System.in);
        String clientName = "";
        String clientPassword = "";

        System.out.println("\n~~ Welcome To RMI Drive Program ~~\n");
        System.out.println("\n~~ Enter 1 to log in ~~\n");
        System.out.println("\n~~ Enter 2 to Sign up ~~\n");

        switch (1) {
            //Login
            case 1: {
                System.out.print("Enter The Name : ");
                clientName = scanner.nextLine();
//                System.out.print("Enter The Password : ");
//                clientPassword = scanner.nextLine();

                try {
                    System.out.println(server.checkClientCredintials(server, clientName, clientPassword));
                    ;
                    LinkedList<String> ss = new LinkedList<String>();
                    ss.add("s");
                    server.shareFile("Compiler.zip", ss);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            }
            //Sigin User
            case 2: {
                System.out.print("Enter The Name : ");
                clientName = scanner.nextLine();
                System.out.print("Enter The Password : ");
                clientPassword = scanner.nextLine();
                Boolean test = null;
                try {
                    //true the user is booked up
                    test = server.testUserName(clientName);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                if (true) {

                    User user = new User();
                    //!!! Add another user information
                    boolean b = false;
                    try {
                        b = server.registerUser(user);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    if (b)
                        System.out.println("done create new account");
                    else
                        System.out.println("we can't create new user");
                }
                break;
            }
        }

        System.out.println("\nConnecting To RMI Server...\n");
//        try {
////            server.sendFileToClient("");
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }

        if (chkLog) {
            System.out.println("Successfully Connected To RMI Server");
            while (chkExit) {


                return;


            }


//            System.out.println("NOTE : Type LOGOUT to Exit From The Service");
//            System.out.println("Now Your Online To Chat\n");
//            String message;
//            while (chkExit) {
//                message = scanner.nextLine();
//                if (message.equals("LOGOUT")) {
//                    chkExit = false;
//                } else {
//                    try {
//                        server.broadcastMessage(ClientName, message);
//                    } catch (RemoteException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            System.out.println("\nSuccessfully Logout From The RMI Chat Program\nThank You For Using...\nDeveloped By Nifal Nizar");
//        } else {
//            System.out.println("\nClient Name or Password Incorrect...");
//            System.out.println("\n");
        }
    }*/

}