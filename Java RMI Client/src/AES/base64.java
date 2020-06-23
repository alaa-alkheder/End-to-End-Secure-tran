package AES;

import java.io.*;
import java.util.Base64;


public class base64 {

    String encodedfile=null;
    FileInputStream fileInputStreamReader;
    StringBuilder bytearray= new StringBuilder();

    public void convert2hex(String path)
    {
        File file = new File(path);
        try {
            fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = Base64.getEncoder().encodeToString(bytes);
            for (int i = 0; i < bytes.length; i++) {
                bytearray.append(String.format("%02X", bytes[i]));
//                bytearray.append(Integer.toHexString(bytes[i] & 0xFF));
            }
            PrintStream out = new PrintStream("temphex");
            out.print(bytearray);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void decode2file(String path,String fileName,String fileType) throws IOException {
        File file = new File(path);
        BufferedReader br = null;
        try {
            String strline,l = "";
            FileInputStream fstream = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(fstream));
            System.out.println("oooooooooooo");
            while((strline=br.readLine())!=null)
                l+=strline;
            System.out.println("dddddddddddddd");
            byte b[]= new byte[l.length()/2];
            for (int i = 0; i < l.length(); i+=2) {
                b[i/2]=(Integer.decode("0x"+l.charAt(i)+l.charAt(i+1))).byteValue();
            }


            FileOutputStream fOut = new FileOutputStream(fileName+fileType);
            fOut.write(b);
            fOut.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




}
