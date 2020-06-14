package AES;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException {
	// write your code here

        String path = "C:\\Users\\ABOOD\\Desktop\\cry.rar";
        String key = getRandomKey(64);
//        key = key.toUpperCase();
        key = "0000000000000000000000000000000000000000000000000000000000000000";
        File f=new File(path);
        new AES().encryption(key,f.getPath());
        String pathOfEncryptedFile = "tempenc",fileType = ".rar";
        new AES().decryption(key,pathOfEncryptedFile,f.getName(),fileType);
        System.out.println(key);
//        System.out.println(getRandomKey(32));
//        System.out.println(getRandomKey(48));
//        System.out.println(getRandomKey(64));
    }

    static public String getRandomKey(int length){
        Random rand = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length()<length)
            sb.append(Integer.toHexString(rand.nextInt()));
        return sb.toString().substring(0,length);
    }
}
