
import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;

import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;




public class ClientHandler  extends Thread{

    DriveInterface chatinterface ;

//

    Socket socket;
    DataInputStream in;
    DataOutputStream out;
    ObjectOutputStream outp;
    ObjectInputStream inp;
    Userr currentUser;
    static LinkedList<ClientHandler> handlers = new LinkedList<ClientHandler>();
    Client client;
    public ClientHandler(Socket socket) throws IOException,RemoteException {

        try {
            chatinterface = (DriveInterface) Naming.lookup("rmi://localhost/RMIServer");
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
  client=new Client(chatinterface);


        this.socket = socket;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        outp = new ObjectOutputStream(socket.getOutputStream());
        inp = new ObjectInputStream(socket.getInputStream());

    }




    @Override
    public void run() {
        String name = "";
        ArrayList <Object> data= new ArrayList<>();
        try {

            while(true){
                data = (ArrayList<Object>) inp.readObject();
                switch((String) data.get(0)){
                    case "Register":  Register_Request(data); break;
                    case "Connect_user": Connect_user((String)data.get(1)); break;
                    case "Chat": Chat(data); break;
                    case "getAllUser": getAllUser(); break;
                    case "getAllFiles": getAllFiles(); break;
                    case "UploadFile": UploadFile(); break;
                    case "downloadFile": downloadFile((String) data.get(1)); break;
                    case "shareFile": shareFile((String) data.get(1),(String) data.get(2),(String) data.get(3)); break;
                }

            }
        } catch (IOException e) {
            System.out.println("-- Connection to user lost.");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            handlers.remove(this);
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {}
        }
    }

    public void Register_Request(ArrayList user) throws FileNotFoundException, IOException{
        Connect_user((String) user.get(1));
        User user1 =new User();
        user1.setUniqueName((String) user.get(1));
        user1.setPassword("Ssssssss");
        Client.server.registerUser(client,user1);
    }

    public void Connect_user(String userName) throws RemoteException {
        System.out.println(userName);
        this.currentUser = new Userr(userName);
        handlers.add(this);
        Client.server.checkClientCredintials(client,currentUser.userName,"");
        System.out.println("connected : "+currentUser.getUserName());

    }

    public void Chat(ArrayList msg) throws IOException{
        System.out.println(msg);
        Client.server.sendMassageChat((String)msg.get(2),(String)msg.get(3),(String)msg.get(1));
//          String data[] = msg.split(" ");
//          System.out.println(msg);
//          for (int i = 0; i < handlers.size(); i++) {
//              if(handlers.get(i).email.equals((String)msg.get(2))){
//                  handlers.get(i).outp.writeObject(msg);
//                  System.out.println("23232323233");}
//         }
    }

    public void sendmessagetoClient(String sender,String reciever,String message){
        ArrayList <String> datasend= new ArrayList<>();
        datasend.add("message"); datasend.add(sender); datasend.add(reciever); datasend.add(message);
        try {
            outp.writeObject(datasend);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getAllUser() throws FileNotFoundException, IOException{
        // get users from main servre....................................................................

        ArrayList <String> datasend= new ArrayList<>();
        List<User> l = (LinkedList<User>) Client.server.showAllUser();
        for (int i = 0; i < l.size(); i++) {
            datasend.add(l.get(i).getUniqueName());
        }
        datasend.add(0,"getAllUser");
        outp.writeObject(datasend);
    }

    public void getAllFiles() throws RemoteException {
//        List<String> l = (LinkedList<String>) Client.server.showAllFile(currentUser.userName);
        try {
            // get users from main servre....................................................................

            ArrayList <String> datasend= new ArrayList<>();
            List<String> l = (LinkedList<String>) Client.server.showAllFile(currentUser.userName);
            for (int i = 0; i < l.size(); i++) {
                datasend.add(l.get(i));
            }
            datasend.add(0,"getAllFiles");
            outp.writeObject(datasend);
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void downloadFile(String fileName){
        File myFile = new File(fileName);
        ArrayList <Object> data= new ArrayList<>();
        data.add("downloadFile");

        InputStream is = null;
        byte[] mybytearray = new byte[0];
        try {
            is = new FileInputStream(fileName);
            mybytearray = new byte[is.available()];
            is.read(mybytearray);
            outp.writeObject(data);
            out.writeUTF(myFile.getName());
            out.writeLong(mybytearray.length);
            out.write(mybytearray, 0, mybytearray.length);
            out.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void UploadFile() {
        //Upload file to main server

        try {
            int bytesRead;
            DataInputStream clientData = new DataInputStream(socket.getInputStream());
            String fileName = clientData.readUTF();
            System.out.println(fileName);
            OutputStream output = new FileOutputStream(fileName);
            long size = clientData.readLong();
            byte[] buffer = new byte[1024];
            while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                output.write(buffer, 0, bytesRead);
                size -= bytesRead;
                System.out.println("+6");
            }
            output.close();
            new AES.AES().encryption("1111111111111111111111111111111111111111111111111111111111111111", fileName);

            File file = new File(fileName);
            File f1 = new File(file.getName() + "Enc");
            int fileSize = 0;
            int timer = 0;
            try {
                fileSize = (int) (Files.size(Paths.get(fileName)) / 1024 / 1024) + 1;
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
                Client.server.UpLoadFile(f1.getName(), mydata, mylen,currentUser.userName,0);
                try {
                    mylen = in.read(mydata);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String extension = "";
            if (fileName.contains("."))
                extension = fileName.substring(fileName.lastIndexOf("."));
            Client.server.addFileInfo(f1.getName(), fileSize, extension, "default",currentUser.userName);
            /** add file to list */

            try {
                in.close();
                Files.delete(Paths.get(file.getName() + "Enc"));
            } catch (IOException e) {
                e.printStackTrace();
            }

//            clientData.close();

            System.out.println("File "+fileName+" received from client.");
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Client error. Connection closed.");
        }
    }
    public void shareFile(String fileName , String userName,String me) throws RemoteException {
        System.out.println(fileName+"............"+userName);
        LinkedList<String> share = new LinkedList<>();
        share.add(userName);
        System.out.println(fileName.substring(0,fileName.length()-3));
        Client.server.shareFile(fileName.substring(0,fileName.length()-3),share,me);
    }
}



