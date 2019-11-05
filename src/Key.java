import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.Properties;
import java.util.Random;

public class Key {

    public static void generate(int length, String publicKey, String privateKey) throws Exception {
        //Gerar p
        BigInteger p = PrimeGenerator.getPrimeNumber(length);

        //Gerar q
        BigInteger q = PrimeGenerator.getPrimeNumber(length);

        //Gerar N
        BigInteger N = p.multiply(q);

        //Gerar (p-1)(q-1)
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        //Gerar e
        BigInteger e = relativePrime(phi);

        //Gerar d
        BigInteger d = extendedGcd(e, phi);

        Properties publicKeyProp = new Properties();
        publicKeyProp.setProperty("n", N.toString());
        publicKeyProp.setProperty("e", e.toString());
        FileOutputStream publicKeyFile = new FileOutputStream(publicKey);
        publicKeyProp.store(publicKeyFile, null);

        Properties privateKeyProp = new Properties();
        privateKeyProp.setProperty("n", N.toString());
        privateKeyProp.setProperty("d", d.toString());
        FileOutputStream privateKeyFile = new FileOutputStream(privateKey);
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

}
