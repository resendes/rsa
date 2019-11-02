import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.Properties;
import java.util.Random;

public class Key {

    int length;
    String publicKey;
    String privateKey;

    public Key(int length, String publicKey, String privateKey) {
        this.length = length;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public boolean generation() throws Exception {
        String s = "a";
        while (s.equals("a")) {
            System.out.println("Comecou!!");
            //Gerar p
            BigInteger p = PrimeGenerator.getPrimeNumber(this.length);

            //Gerar q
            BigInteger q = PrimeGenerator.getPrimeNumber(this.length);

            //Gerar N
            BigInteger N = p.multiply(q);

            //Gerar (p-1)(q-1)
            BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

            //Gerar e
            BigInteger e = relativePrime(phi);

            //Gerar d
            BigInteger d = extendedGcd(e, phi);

            Properties publicKey = new Properties();
            publicKey.setProperty("n", N.toString());
            publicKey.setProperty("e", e.toString());
            FileOutputStream publicKeyFile = new FileOutputStream(this.publicKey);
            publicKey.store(publicKeyFile, null);

            Properties privateKey = new Properties();
            privateKey.setProperty("n", N.toString());
            privateKey.setProperty("d", d.toString());
            FileOutputStream privateKeyFile = new FileOutputStream(this.privateKey);
            publicKey.store(privateKeyFile, null);


            BigInteger x = new BigInteger("a".getBytes());
            BigInteger y = x.modPow(e, N);
            BigInteger m = y.modPow(d, N);
            s = new String(m.toByteArray());
            System.out.println(s);
        }

        return true;

    }

    BigInteger extendedGcd(BigInteger a, BigInteger b) {
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

    private BigInteger relativePrime(BigInteger phi) {
        BigInteger e = new BigInteger(phi.bitLength(), new Random());
        while (!gcd(e, phi).equals(BigInteger.ONE)) {
            e = new BigInteger(phi.bitLength(), new Random());
        }
        return e;
    }

    public BigInteger gcd(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO))
            return a;
        return gcd(b, a.mod(b));
    }

    public static void main(String[] args) throws Exception {
        Key key = new Key(1024,"rsa_public.txt", "rsa_private.txt");
        key.generation();
    }


}
