import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    static final String userFile = "user.txt";
    static HashMap<String, User> userProfile = new HashMap<String, User>();
    static HashMap<String, String> user = new HashMap<String, String>();

    /**
     * Print User info Name&password
     */
    static private void printUserPassword() {

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
            System.out.println(row);
            System.out.println(col);
            for (int i = 0; i < row; i++) {
//                for (int j = 0; j < col; j++) {
                Cell name = sheet.getCell(0, i);
                Cell pass = sheet.getCell(1, i);
                user.put(name.getContents(), pass.getContents());
//                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }




    /**
     * save User info (Name&password) and save in the HashMap
     */
    static private void saveUserPassword() {
        File f1 = new File("D:\\recUser.xls");
        try {
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
//            System.out.println(entry.getKey() + " : " + entry.getValue());
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
//            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        return false;
    }

    public static void main(String[] arg) throws RemoteException, MalformedURLException {

//        user = (HashMap<String, User>) ois.readObject();
        readUserPassword();
//        printUserPassword();
//        saveUserPassword();
        try {
            java.rmi.registry.LocateRegistry.createRegistry(1099);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Naming.rebind("RMIServer", new Server());
    }
}