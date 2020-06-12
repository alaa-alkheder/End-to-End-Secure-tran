import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Alaa Alkheder
 * Email:alaa-alkheder@outlook.com
 * Github:alaa-alkheder
 */
public class shareFile implements Serializable {
    String fileName;
    String Owner;
    String paht;

    public shareFile(String fileName, String owner, String paht) {
        this.fileName = fileName;
        Owner = owner;
        this.paht = paht;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    public String getPaht() {
        return paht;
    }

    public void setPaht(String paht) {
        this.paht = paht;
    }
}
