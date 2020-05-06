import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Alaa Alkheder
 * Email:alaa-alkheder@outlook.com
 * Github:alaa-alkheder
 */
public class User implements Serializable {
    private   String FullName;
    private   String uniqueName;
    private   String password;
    private   String email;
//  private   String Name;
//  private   String Name;
private   String path;
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public User(String uniqueName, String password) {
        this.uniqueName = uniqueName;
        this.password = password;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
