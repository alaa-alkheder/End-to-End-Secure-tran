import AES.AES;
import encRSA.RSA;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by IntelliJ IDEA.
 * User: Alaa Alkheder
 * Email:alaa-alkheder@outlook.com
 * Github:alaa-alkheder
 */
public class StartServer {
    //to save user profile
    static final String userFile = "user.txt";

    static HashMap<String, User> userProfile = new HashMap<String, User>();
    static HashMap<String, String> user = new HashMap<String, String>();
    static encRSA.RSA rsa = new encRSA.RSA();
    static String privateKey= "1111111111111111111111111111111111111111111111111111111111111111";
    static AES aes=new AES();


    /**
     * Print User info Name&password
     */
    static private void printUserPassword() {

        System.out.println(user.size());
        user.entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + " " + entry.getValue());
        });

    }

    /**
     * read User info (Name&password) and save in the HashMap
     */
    static private void readUserPassword() {
        try {
            File f1 = new File("D:\\user.xLs");
            Workbook workbook = Workbook.getWorkbook(f1.getAbsoluteFile());
            Sheet sheet = workbook.getSheet(0);
            int row = sheet.getRows();
            int col = sheet.getColumns();
//            System.out.println(row);
//            System.out.println(col);
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    Cell name = sheet.getCell(0, i);
                    Cell pass = sheet.getCell(1, i);
                    user.put(name.getContents(), pass.getContents());
                }
            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }


    /**
     * save User info (Name&password) and save  the HashMap in excel file
     */
    static void saveUserPassword() {
        File f1 = new File("D:\\User.xls");

        try {
            f1.delete();
            f1.createNewFile();
            WritableWorkbook workbook = Workbook.createWorkbook(f1);
            WritableSheet writableSheet = workbook.createSheet("temp", 0);
            AtomicInteger i = new AtomicInteger();
            user.entrySet().forEach(entry -> {
                Label name = new Label(0, i.incrementAndGet() - 1, entry.getKey());
                Label pass = new Label(1, i.get() - 1, entry.getValue());
                try {
                    writableSheet.addCell(name);
                    writableSheet.addCell(pass);
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            });

            workbook.write();
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }


    }

    /**
     * save User info (Name&password) and save in the HashMap with Encryption
     */
    static private void saveUserPasswordWithEncryption() {
//        File f1 = new File("D:\\recUser.xls");
//        try {
//            WritableWorkbook workbook = Workbook.createWorkbook(f1);
//            WritableSheet writableSheet = workbook.createSheet("temp", 0);
//            AtomicInteger i = new AtomicInteger();
//            user.entrySet().forEach(entry -> {
//                Label name = new Label(0, i.incrementAndGet() - 1, entry.getKey());
//                Label pass = new Label(1, i.get() - 1, entry.getValue());
//                try {
//                    writableSheet.addCell(name);
//                    writableSheet.addCell(pass);
//                } catch (WriteException e) {
//                    e.printStackTrace();
//                }
//            });
//            workbook.write();
//            workbook.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (RowsExceededException e) {
//            e.printStackTrace();
//        } catch (WriteException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * Check User info Name&password
     */
    static public Boolean CheckUserPassword(String name, String pass) {
        for (HashMap.Entry<String, String> entry : user.entrySet()) {
            if (entry.getKey().equals(name))
                if (entry.getValue().equals(pass))
                    return true;
        }
        return false;
    }

    /**
     * Search for UserName
     */
    static public Boolean SearchUserName(String name) {
        for (HashMap.Entry<String, String> entry : user.entrySet()) {
            if (entry.getKey().equals(name))
                return true;
        }
        return false;
    }

    static void deleteAllUser() {

        try {
            Files.deleteIfExists(Paths.get("D:\\user.xLs"));
            Files.deleteIfExists(Paths.get("..\\Java RMI Server\\userFile"));
            Files.deleteIfExists(Paths.get("..\\Java RMI Server\\user"));
            File f1 = new File("D:\\user.xLs");
            File f2 = new File("..\\Java RMI Server\\userFile");
            File f3 = new File("..\\Java RMI Server\\user");
            f1.createNewFile();
            f2.mkdir();
            f3.createNewFile();
            saveUserObject();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void saveUserObject() {
        try {

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(userFile));
            objectOutputStream.writeObject(userProfile);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] arg) throws RemoteException, MalformedURLException,IOException {
//        deleteAllUser();
        FileWriter fileWriter = new FileWriter("aesKey.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(privateKey);
        bufferedWriter.close();
        fileWriter.close();

        ObjectInputStream objectInputStream = null;
        try {

            objectInputStream = new ObjectInputStream(new FileInputStream(userFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            userProfile = (HashMap<String, User>) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        readUserPassword();
        printUserPassword();
//        saveUserPassword();
        try {
            java.rmi.registry.LocateRegistry.createRegistry(1099);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Naming.rebind("RMIServer", new Server());
    }


}
