package encRSA;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Random;

public class RSA {
    private BigInteger p;
    private BigInteger q;
    private BigInteger N;
    private BigInteger phi;
    private BigInteger e;
    private BigInteger d;
    private int bitlength = 512;
    private Random r;
    static private int blocksize = 128;//block size =bit length/4
    private publicKey public_key;
    byte n[];


    /**
     *
     * @return
     */
    public publicKey getPublic_key() {
        return public_key;
    }

    /**
     * default constrictor
     */
    public RSA() {
        r = new Random();
        p = BigInteger.probablePrime(bitlength, r);
        q = BigInteger.probablePrime(bitlength, r);
        N = p.multiply(q);
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        e = BigInteger.probablePrime(bitlength / 2, r);
        while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0) {
            e.add(BigInteger.ONE);
        }
        d = e.modInverse(phi);
        public_key = new publicKey(this.e, this.N);

    }
    /**
     * values constrictor
     */
    public RSA(BigInteger e, BigInteger d, BigInteger N) {
        this.e = e;
        this.d = d;
        this.N = N;
    }
//not important
    /*private static String bytesToString(byte[] encrypted) {
        String test = "";
        for (byte b : encrypted) {
            test += Byte.toString(b);
        }
        System.out.println(test);
        return test;
    }*/

    // Encrypt message
    private byte[] encrypt(byte[] message, publicKey public_key) {
        return (new BigInteger(message)).modPow(public_key.e, public_key.N).toByteArray();
    }

    // Decrypt message
    private byte[] decrypt(byte[] message) {
        return (new BigInteger(message)).modPow(d, N).toByteArray();
    }

    // Write Bytes Array to File

    private static void writeBytesToFile(byte[] bFile, String fileDest, int len) throws IOException {
        FileOutputStream fileOuputStream = null;
        try {//append is true because every time we add block to file
            fileOuputStream = new FileOutputStream(fileDest, true);
            //len is a offset
            if (len < 1) return;
            fileOuputStream.write(bFile, 0, len);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fileOuputStream.close();
        }
    }

    public void encryptrsa(String path, publicKey public_key) throws IOException {
        //convert file to string
        base64.ecryptTo64(path);
        blockList s = new blockList(); //to save the blocks of file after encryption
        byte[] mydata = new byte[blocksize];//block size id 128 bit
        FileInputStream in = null;
        int mylen = 0;
        try {
            //temp file has data after convert to string in base64
            in = new FileInputStream("temp.txt");
            mylen = in.read(mydata);
            byte[] encrypted = encrypt(mydata, public_key);
            s.ss.addLast(new byteblock(encrypted, mylen));//add the encrypted block
            while (mylen > 0) {//repeat for all file
                mylen = in.read(mydata);
                encrypted = encrypt(mydata, public_key);
                s.ss.addLast(new byteblock(encrypted, mylen));
            }
            in.close();
            ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(getfilename(path) + "Enc"));
            o.writeObject(s);
            o.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            //delete base64 temp after we use
            Files.deleteIfExists(Paths.get("temp.txt"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }

    public void deccryptrsa(String pathFileToDecrypt, String fileName, String filePath) throws IOException, ClassNotFoundException {
        pathFileToDecrypt = getfilename(pathFileToDecrypt) ;//get file name without extension
        blockList so = new blockList();
        ObjectInputStream oi = new ObjectInputStream(new FileInputStream(pathFileToDecrypt));
        so = (blockList) oi.readObject();//read encrypted block list
        for (int i = 0; i < so.ss.size(); i++) {//decrypt the list and save in file
            writeBytesToFile(decrypt(so.ss.get(i).bytes), "temp", (so.ss.get(i).lenth));
        }
        oi.close();
        //convet file from string to file via base64
        base64.decryptVia64("temp", fileName + filePath);
        try {
            //delete decrypt file
            Files.deleteIfExists(Paths.get("temp"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }



    public String encryptStringRsa(String message, BigInteger e, BigInteger N) {
        byte b[] = message.getBytes();
        return Base64.getEncoder().encodeToString(encrypt(b, new publicKey(e, N)));
    }

    public String decryptStringRsa(String message) {

        byte b[] = Base64.getDecoder().decode(message);
        String temp = new String(decrypt(b));
        return temp;
    }

    private byte[] decryptWithD(byte[] message, BigInteger N, BigInteger d) {
        return (new BigInteger(message)).modPow(d, N).toByteArray();
    }

    public String decryptStringRsaWithD(String message, BigInteger N, BigInteger d) {
        byte b[] = message.getBytes();
        String temp = new String(decryptWithD(b, N, d));
        return temp;
    }


    /**
     * get file name without extension
     * @param pathFile
     * @return
     */
    public static String getfilename(String pathFile) {
        int pos = pathFile.lastIndexOf(".");
        if (pos == -1)
            return pathFile;
        return pathFile.substring(0, pos);
    }
}