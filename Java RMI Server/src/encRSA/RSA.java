package encRSA;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    byte n[];
    static private int blocksize = 128;

    public publicKey getPublic_key() {
        return public_key;
    }

    private publicKey public_key;


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

    public RSA(BigInteger e, BigInteger d, BigInteger N) {
        this.e = e;
        this.d = d;
        this.N = N;
    }

    private static String bytesToString(byte[] encrypted) {
        String test = "";
        for (byte b : encrypted) {
            test += Byte.toString(b);
        }
        System.out.println(test);
        return test;
    }

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
        try {
            fileOuputStream = new FileOutputStream(fileDest, true);
            System.out.println(len);
            System.err.println(bFile.length);
            if (len < 1) return;
            fileOuputStream.write(bFile, 0, len);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fileOuputStream.close();
        }
    }

    public void encryptrsa(String path, publicKey public_key) throws IOException {
        base64.ecryptTo64(path);
//        File f = new File(path);
        blockList s = new blockList();
        byte[] mydata = new byte[blocksize];
        FileInputStream in = null;
        int mylen = 0;
        try {
            in = new FileInputStream("temp.txt");
            mylen = in.read(mydata);
            byte[] encrypted = encrypt(mydata, public_key);
            s.ss.addLast(new byteblock(encrypted, mylen));

            while (mylen > 0) {
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
            Files.deleteIfExists(Paths.get("temp.txt"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }

    public void deccryptrsa(String pathFileToDecrypt, String fileName, String filePath) throws IOException, ClassNotFoundException {
        pathFileToDecrypt = getfilename(pathFileToDecrypt) ;
        blockList so = new blockList();
        ObjectInputStream oi = new ObjectInputStream(new FileInputStream(pathFileToDecrypt));
        so = (blockList) oi.readObject();
        for (int i = 0; i < so.ss.size(); i++) {
            writeBytesToFile(decrypt(so.ss.get(i).bytes), "temp", (so.ss.get(i).lenth));
        }
        oi.close();

        base64.decryptVia64("temp", fileName + filePath);
        try {
            Files.deleteIfExists(Paths.get("temp"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }

    public static String getfilename(String pathFile) {
        int pos = pathFile.lastIndexOf(".");
        if (pos == -1)
            return pathFile;
        return pathFile.substring(0, pos);
    }
}