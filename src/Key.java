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
        System.out.println("Comecou!!");
        //Gerar p
        BigInteger p = PrimeGenerator.getPrimeNumber(this.length);

        //Gerar q
        BigInteger q = PrimeGenerator.getPrimeNumber(this.length);

        //Gerar N
        BigInteger N = p.multiply(q);
        //Gerar e
        BigInteger totient = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        BigInteger e =  relativePrime(totient, this.length*2);

        System.out.println(e);

        Properties publicKey = new Properties();
        publicKey.setProperty("n", N.toString());
        publicKey.setProperty("e", e.toString());
        FileOutputStream publicKeyFile = new FileOutputStream(this.publicKey);
        publicKey.store(publicKeyFile, null);

        return true;

    }

    private BigInteger relativePrime(BigInteger totient, int bits) {
        BigInteger e = new BigInteger(bits, new Random());
        while (e.gcd(totient).compareTo(BigInteger.ONE) != 0) {
            e = e.add(BigInteger.ONE);
        }
        return e;
    }

    public static void main(String[] args) throws Exception {
        Key key = new Key(1024,"teste.txt", "");
        key.generation();
    }


}
