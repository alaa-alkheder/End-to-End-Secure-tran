import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
/**
 * This utility compresses a list of files to standard ZIP format file.
 * It is able to compress all sub files and sub directories, recursively.
 * @author www.codejava.net
 *
 */


/**
 * Created by IntelliJ IDEA.
 * User: Alaa Alkheder
 * Email:alaa-alkheder@outlook.com
 * Github:alaa-alkheder
 */
public class ZipUtility {
    /**
     * A constants for buffer size used to read/write data
     */
    private static final int BUFFER_SIZE = 4096;
    /**
     * Compresses a list of files to a destination zip file
     * @param listFiles A collection of files and directories
     * @param destZipFile The path of the destination zip file
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void zip(File[] listFiles, String destZipFile) throws FileNotFoundException,
            IOException {
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destZipFile));
        for (File file : listFiles) {
            if (file.isDirectory()) {
                zipDirectory(file, file.getName(), zos);
            } else {
                zipFile(file, zos);
            }
        }
        zos.flush();
        zos.close();
    }
    /**
     * Compresses files represented in an array of paths
     * @param files a String array containing file paths
     * @param destZipFile The path of the destination zip file
     * @throws FileNotFoundException
     * @throws IOException
     */
//    public void zip(String[] files, String destZipFile) throws FileNotFoundException, IOException {
//        List<File> listFiles = new ArrayList<File>();
//        for (int i = 0; i < files.length; i++) {
//            listFiles.add(new File(files[i]));
//        }
//        zip(listFiles, destZipFile);
//    }
    /**
     * Adds a directory to the current zip output stream
     * @param folder the directory to be  added
     * @param parentFolder the path of parent directory
     * @param zos the current zip output stream
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void zipDirectory(File folder, String parentFolder,
                              ZipOutputStream zos) throws FileNotFoundException, IOException {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                zipDirectory(file, parentFolder + "/" + file.getName(), zos);
                continue;
            }
            zos.putNextEntry(new ZipEntry(parentFolder + "/" + file.getName()));
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(file));
            long bytesRead = 0;
            byte[] bytesIn = new byte[BUFFER_SIZE];
            int read = 0;
            while ((read = bis.read(bytesIn)) != -1) {
                zos.write(bytesIn, 0, read);
                bytesRead += read;
            }
            zos.closeEntry();
        }
    }
    /**
     * Adds a file to the current zip output stream
     * @param file the file to be added
     * @param zos the current zip output stream
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void zipFile(File file, ZipOutputStream zos)
            throws FileNotFoundException, IOException {
        zos.putNextEntry(new ZipEntry(file.getName()));
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
                file));
        long bytesRead = 0;
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = bis.read(bytesIn)) != -1) {
            zos.write(bytesIn, 0, read);
            bytesRead += read;
        }
        zos.closeEntry();
    }

    /**
     * A console application that tests the ZipUtility class
     * @author www.codejava.net
     *
     */

    static public String Convert() {
            File myFiles =new File("C:\\Users\\Eng Alaa Alkheder\\Desktop\\SRS File Sharing") ;
            File[] f=myFiles.listFiles();
            String zipFile = "temp.zip";
            ZipUtility zipUtil = new ZipUtility();
            try {
                zipUtil.zip(f, zipFile);
            } catch (Exception ex) {
                // some errors occurred
                ex.printStackTrace();
            }
return zipFile;
    }

    public void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
//            if (fileEntry.isDirectory()) {
//                listFilesForFolder(fileEntry);
//            } else {
                System.out.println(fileEntry.getName());
//            }
        }
    }

    final File folder = new File("/home/you/Desktop");
//    listFilesForFolder(folder);


}