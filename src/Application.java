import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;

public class Application {

    private static boolean contains(String[] args, String element) {
        return Arrays.stream(args).anyMatch(element::equals);
    }

    public static void main(String[] args) throws Exception {
        File file = new File("result.txt");
        FileWriter fileWriter = new FileWriter(file, false);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        try {
            Integer[] keyLengths = {32};
            for (int j = 0; j < keyLengths.length; j++) {
                for (int i = 0; i < 1; i++) {
                    long startingAt = System.currentTimeMillis();
                    Key key = new Key().generate(keyLengths[j]);
                    long executionTime = System.currentTimeMillis() - startingAt;

                    writer.append("Key generated (" + key.getN().bitLength() + "): " + executionTime);
                    writer.newLine();

                    if (contains(args, "encrypt_decrypt")) {
                        startingAt = System.currentTimeMillis();
                        Rsa.encrypt(key.getN(), key.getE());
                        Rsa.decrypt(key.getN(), key.getD());
                        executionTime = System.currentTimeMillis() - startingAt;
                        writer.append("Encrypt/Decrypt (" + key.getN().bitLength() + "): " + executionTime);
                        writer.newLine();
                    }
                    if (contains(args, "brute_force")) {
                        startingAt = System.currentTimeMillis();
                        BruteForce.bruteForcePublicKey(key.getN());
                        executionTime = System.currentTimeMillis() - startingAt;
                        writer.append("Brute force (" + key.getN().bitLength() + "): " + executionTime);
                        writer.newLine();
                    }
                    if (contains(args, "pollard_rho")) {
                        startingAt = System.currentTimeMillis();
                        PollardRho.factor(key.getN());
                        executionTime = System.currentTimeMillis() - startingAt;
                        writer.append("Pollard-Rho (" + key.getN().bitLength() + "): " + executionTime);
                        writer.newLine();
                    }
                }
            }
        } finally {
            writer.close();
        }
    }
}
