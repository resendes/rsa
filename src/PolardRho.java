import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Properties;
import java.util.Random;

public class PolardRho {

    public static BigInteger factor() throws IOException {
        FileInputStream publicKeyFile = new FileInputStream("rsa_public.txt");
        Properties publicKey = new Properties();
        publicKey.load(publicKeyFile);

        BigInteger n = new BigInteger(publicKey.getProperty("n"));

        BigInteger x = new BigInteger(n.bitLength(), new Random()), y = new BigInteger(n.bitLength(), new Random()), divisor = BigInteger.ONE;
        BigInteger maxIterations = n.sqrt();
        BigInteger count = BigInteger.ZERO;
        while (divisor.compareTo(BigInteger.ONE) == 0) {
            x = g(x, n);
            y = g(g(y, n), n);
            divisor = gcd(x.subtract(y).abs(), n);

            if (count.compareTo(maxIterations) >  0) {
                x = new BigInteger(n.bitLength(), new Random());
                y = new BigInteger(n.bitLength(), new Random());
                divisor = BigInteger.ONE;
                count = BigInteger.ZERO;
            } else {
                count = count.add(BigInteger.ONE);
            }
        }
        if (divisor.equals(n))
            return null;
        return divisor;

    }

    private static  BigInteger g(BigInteger x, BigInteger n) {
        return x.multiply(x).add(BigInteger.ONE).mod(n);
    }

    private static BigInteger gcd(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO))
            return a;
        return gcd(b, a.mod(b));
    }
}
