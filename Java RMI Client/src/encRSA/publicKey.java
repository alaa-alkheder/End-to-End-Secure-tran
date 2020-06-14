package encRSA;

import java.io.Serializable;
import java.math.BigInteger;

public class publicKey implements Serializable {
    BigInteger e;
    BigInteger N;

    public publicKey(BigInteger e, BigInteger n) {
        this.e = e;
        N = n;
    }

    public BigInteger getE() {
        return e;
    }

    public BigInteger getN() {
        return N;
    }

    public void setE(BigInteger e) {
        this.e = e;
    }

    public void setN(BigInteger n) {
        N = n;
    }
}
