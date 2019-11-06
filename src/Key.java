import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Properties;
import java.util.Random;

public class Key {

    private BigInteger n;
    private BigInteger d;
    private BigInteger e;
    private BigInteger p;
    private BigInteger q;

    public Key generate(int length) throws Exception {
        //Gerar p
        this.p = PrimeGenerator.getPrimeNumber(length);
        //Gerar q
        this.q = PrimeGenerator.getPrimeNumber(length);
        //Gerar N
        BigInteger N = this.p.multiply(this.q);
        //Gerar (p-1)(q-1)
        BigInteger phi = this.p.subtract(BigInteger.ONE).multiply(this.q.subtract(BigInteger.ONE));
        //Gerar e
        BigInteger e = relativePrime(phi);
        //Gerar d
        BigInteger d = extendedGcd(e, phi);

        this.n = N;
        this.e = e;
        this.d = d;

        storeKeys();

        return this;
    }

    private void storeKeys() throws IOException {
        Properties publicKeyProp = new Properties();
        publicKeyProp.setProperty("n", this.n.toString());
        publicKeyProp.setProperty("e", this.e.toString());
        FileOutputStream publicKeyFile = new FileOutputStream("rsa_public.txt");
        publicKeyProp.store(publicKeyFile, null);

        Properties privateKeyProp = new Properties();
        privateKeyProp.setProperty("n", this.n.toString());
        privateKeyProp.setProperty("d", this.d.toString());
        FileOutputStream privateKeyFile = new FileOutputStream("rsa_private.txt");
        privateKeyProp.store(privateKeyFile, null);
    }

    private static BigInteger extendedGcd(BigInteger a, BigInteger b) {
        BigInteger s = BigInteger.ZERO;
        BigInteger t = BigInteger.ONE;
        BigInteger r = b;
        BigInteger oldS = BigInteger.ONE;
        BigInteger oldT = BigInteger.ZERO;
        BigInteger oldR = a;

        while (!r.equals(BigInteger.ZERO)) {
            BigInteger quotient = oldR.divide(r);
            BigInteger calcOldR = oldR.subtract(quotient.multiply(r));
            BigInteger calcOldS = oldS.subtract(quotient.multiply(s));
            BigInteger calcOldT = oldT.subtract(quotient.multiply(t));

            oldR = r;
            oldS = s;
            oldT = t;

            r = calcOldR;
            s = calcOldS;
            t = calcOldT;
        }

        return oldS;

    }

    private static BigInteger relativePrime(BigInteger phi) {
        BigInteger e = new BigInteger(phi.bitLength(), new Random());
        while (!gcd(e, phi).equals(BigInteger.ONE)) {
            e = new BigInteger(phi.bitLength(), new Random());
        }
        return e;
    }

    private static BigInteger gcd(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO))
            return a;
        return gcd(b, a.mod(b));
    }

    public BigInteger getP() {
        return p;
    }

    public Key setP(BigInteger p) {
        this.p = p;
        return this;
    }

    public BigInteger getQ() {
        return q;
    }

    public Key setQ(BigInteger q) {
        this.q = q;
        return this;
    }

    public BigInteger getN() {
        return n;
    }

    public Key setN(BigInteger n) {
        this.n = n;
        return this;
    }

    public BigInteger getD() {
        return d;
    }

    public Key setD(BigInteger d) {
        this.d = d;
        return this;
    }

    public BigInteger getE() {
        return e;
    }

    public Key setE(BigInteger e) {
        this.e = e;
        return this;
    }
}
