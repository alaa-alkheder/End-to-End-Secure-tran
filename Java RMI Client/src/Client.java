import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * User: Alaa Alkheder
 * Email:alaa-alkheder@outlook.com
 * Github:alaa-alkheder
 */
public class Client extends UnicastRemoteObject implements ChatInterface, Runnable {
    private static final long serialVersionUID = 1L;
    private ChatInterface server;
    private String ClientName;
    boolean chkExit = true;
    boolean chkLog = false;

    protected Client(ChatInterface chatinterface) throws RemoteException {

        this.server = chatinterface;

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
//        server.showAllFile();
//        System.out.println(server.downloadFile());
    }

    public void sendMessageToServer(String message) throws RemoteException {

        File f1 = new File("tete.xlsx");
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
            server.UpLoadFile(f1.getName(), mydata, mylen);
            try {
                mylen = in.read(mydata);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public void sendMessageToClient(String message) throws RemoteException {
        System.out.println(message);
    }

    @Override
    public Boolean registerUser(User user) throws RemoteException {

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
    public void UpLoadFile(String filename, byte[] data, int len) throws RemoteException {

    }


    @Override
    public Object downloadFile() throws RemoteException {
        return null;
    }

    @Override
    public Object showAllFile() throws RemoteException {
        return null;
    }

    @Override
    public String[] showAllUser() throws RemoteException {
        return new String[0];
    }

    public void broadcastMessage(String clientname, String message) throws RemoteException {
    }

    public boolean checkClientCredintials(ChatInterface chatinterface, String clientname, String password) throws RemoteException {
        return true;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        String clientName = "";
        String clientPassword = "";

        System.out.println("\n~~ Welcome To RMI Drive Program ~~\n");
        System.out.println("\n~~ Enter 1 to log in ~~\n");
        System.out.println("\n~~ Enter 2 to Sign up ~~\n");

        switch (2) {
            case 1: {
                System.out.print("Enter The Name : ");
                clientName = scanner.nextLine();
                System.out.print("Enter The Password : ");
                clientPassword = scanner.nextLine();

                try {
                    chkLog = server.checkClientCredintials(this, clientName, clientPassword);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            }
            case 2: {
                System.out.print("Enter The Name : ");
                clientName = scanner.nextLine();
                System.out.print("Enter The Password : ");
                clientPassword = scanner.nextLine();
                Boolean test = null;
//                try {
//                    //true the user is booked up
//                    test = server.testUserName(clientName);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
                if (true) {

                    User user = new User(clientName, clientPassword);
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


        if (chkLog) {
            System.out.println("Successfully Connected To RMI Server");
            System.out.println("NOTE : Type LOGOUT to Exit From The Service");
            System.out.println("Now Your Online To Chat\n");
            String message;

            while (chkExit) {
                message = scanner.nextLine();
                if (message.equals("LOGOUT")) {
                    chkExit = false;
                } else {
                    try {
                        server.broadcastMessage(ClientName, message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("\nSuccessfully Logout From The RMI Chat Program\nThank You For Using...\nDeveloped By Nifal Nizar");
        } else {
            System.out.println("\nClient Name or Password Incorrect...");
            System.out.println("\n");
        }
    }

}