package encRSA;

import java.io.Serializable;

public class byteblock implements Serializable {
    byte bytes[];
    int lenth;
    byteblock(byte [] bytes,int len){
        this .bytes = bytes;
        this.lenth=len;
    }
}
