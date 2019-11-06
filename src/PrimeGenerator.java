import java.math.BigInteger;
import java.util.Random;

public class PrimeGenerator {

    public static BigInteger getPrimeNumber(int bits) {
        BigInteger n = new BigInteger(bits, new Random());

        while (n.bitLength() < bits) {
            n = new BigInteger(bits, new Random());
        }

        if (!n.testBit(0)) {
            n = n.add(BigInteger.ONE);
        }

        boolean isPrime = false;
        int successTests = 0;

        while (!isPrime && successTests < 30) {
            isPrime = checkPrimeValue(n);
            if (isPrime) {
                successTests++;
            } else {
                successTests = 0;
                n = n.add(BigInteger.TWO);
            }
        }

        return n;

    }

    private static boolean checkPrimeValue(BigInteger n) {
        return nextRandomBigInteger(n)
                .modPow(n.subtract(BigInteger.ONE), n)
                .equals(BigInteger.ONE.mod(n));
    }

    public static BigInteger nextRandomBigInteger(BigInteger n) {
        Random rand = new Random();
        BigInteger result = new BigInteger(n.bitLength(), rand);
        while (result.compareTo(n) >= 0) {
            result = new BigInteger(n.bitLength(), rand);
        }
        return result;
    }
}
