import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class BruteForce {

    public static List<BigInteger> bruteForcePublicKey() throws IOException {
        FileInputStream publicKeyFile = new FileInputStream("rsa_public.txt");
        Properties publicKey = new Properties();
        publicKey.load(publicKeyFile);

        BigInteger n = new BigInteger(publicKey.getProperty("n"));

        List<BigInteger> factors = new LinkedList<>();

        while(n.mod(BigInteger.TWO).compareTo(BigInteger.ZERO) == 0){
            factors.add(BigInteger.TWO);
            n = n.divide(BigInteger.TWO);
        }

        BigInteger d = BigInteger.valueOf(3); // Possíveis fatores
        BigInteger d2 = BigInteger.valueOf(9); // d2 = d * d
        while(d2.compareTo(n) <= 0){
            // Se d é fator, faz a divisão e armazena o fator
            if(n.mod(d).equals(BigInteger.ZERO)) {
                factors.add(d);
                n = n.divide(d);
            }else{
                // Se d não é fator, verifica o próximo
                d = d.add(BigInteger.TWO);
                d2 = d.multiply(d); // d2 = d*d
            }
        }
        // Essa condição é necessária quando n for primo
        if(n.compareTo(BigInteger.ONE) > 1){
            factors.add(n);
        }
        return factors;
    }

}
