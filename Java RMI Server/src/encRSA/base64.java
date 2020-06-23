package encRSA;

import java.io.*;
import java.util.Base64;

/**
 * Created by IntelliJ IDEA.
 * User: Alaa Alkheder
 * Email:alaa-alkheder@outlook.com
 * Github:alaa-alkheder
 */
public class base64 {

    public static String ecryptTo64(String path) throws IOException {

        File file = new File(path);
        String dist = "temp.txt";
        String encodedfile = null;
        FileInputStream fileInputStreamReader = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        fileInputStreamReader.read(bytes);
        encodedfile = Base64.getEncoder().encodeToString(bytes);
        BufferedWriter writer = new BufferedWriter(new FileWriter(dist));
        writer.write(encodedfile);
        writer.close();
        System.out.println("Done encrypt in base64");
        return dist;
    }

    public static String decryptVia64(String path, String dist) throws IOException {

        BufferedReader bufferedReader=new BufferedReader(new FileReader(path));
     String encodedfile=bufferedReader.readLine();
        byte[] actualByte = Base64.getDecoder()
                .decode(encodedfile);
        FileOutputStream fOut = new FileOutputStream(dist);
        fOut.write(actualByte);
        fOut.close();
        bufferedReader.close();
        System.out.println("Done ");
        return dist;
    }
    public static String getfilename(String pathFile){
        int pos = pathFile.lastIndexOf(".");
        if(pos == -1)
            return pathFile;
        return pathFile.substring(0,pos);
    }

}
