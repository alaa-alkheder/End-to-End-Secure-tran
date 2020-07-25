import java.io.Serializable;

/**
 * Created by Dominic on 01-May-16.
 */
public class Userr implements Serializable {

    String userName;
    public Userr(String userName){
        this.userName = userName;
    }
    public  void setUserName(String userName){
        this.userName = userName;
    }

    public String getUserName(){
        return this.userName;
    }
}
