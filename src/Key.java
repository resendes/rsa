import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

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

    private static boolean contains(String[] args, String element) {
        return Arrays.stream(args).anyMatch(element::equals);
    }

    public static void main(String[] args) throws Exception {
        if (contains(args, "generate")) {
            Key.generate(32, "rsa_public.txt", "rsa_private.txt");
        }
        if (contains(args, "encrypt_decrypt")) {
            Rsa.encrypt();
            Rsa.decrypt();
        }
        if (contains(args, "brute_force")) {
            BruteForce.bruteForcePublicKey().stream().forEach(x -> System.out.println("Força bruta:" + x.toString()));
        }
        if (contains(args, "polard_rho")) {
            System.out.println("Pollard Rho: " + PolardRho.factor());
        }

    }


}
