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
            Integer[] keyLengths = {34};
            for (int j = 0; j < keyLengths.length; j++) {
                for (int i = 0; i < 5; i++) {
                    long startingAt = System.currentTimeMillis();
                    Key key = new Key().generate(keyLengths[j]);
                    long executionTime = System.currentTimeMillis() - startingAt;

                    System.out.println("Key generated (N: " + key.getN().bitLength() + " P: " + key.getP().bitLength() + " Q: " + key.getQ().bitLength() + ") in: " + executionTime);
                    writer.newLine();
                    writer.newLine();
                    writer.append("Key generated in (" + key.getN().bitLength() + "): " + executionTime);
                    writer.newLine();
                    writer.append("N: " + key.getN().bitLength() + " P: " + key.getP().bitLength() + " Q: " + key.getQ().bitLength());
                    writer.newLine();
                    writer.append("N: " + key.getN().toString());
                    writer.newLine();
                    writer.append("P: " + key.getP().toString());
                    writer.newLine();
                    writer.append("Q: " + key.getQ().toString());

                    if (contains(args, "encrypt_decrypt")) {
                        Rsa.encrypt(key.getN(), key.getE());
                        Rsa.decrypt(key.getN(), key.getD());
                    }
                    if (contains(args, "brute_force")) {
                        startingAt = System.currentTimeMillis();
                        BruteForce.bruteForcePublicKey(key.getN());
                        executionTime = System.currentTimeMillis() - startingAt;
                        System.out.println("Brute force in (" + key.getN().bitLength() + "): " + executionTime);
                        writer.newLine();
                        writer.newLine();
                        writer.append("Brute force in (" + key.getN().bitLength() + "): " + executionTime);
                        writer.newLine();
                        writer.append("N: " + key.getN().bitLength() + " P: " + key.getP().bitLength() + " Q: " + key.getQ().bitLength());
                        writer.newLine();
                        writer.append("N: " + key.getN().toString());
                        writer.newLine();
                        writer.append("P: " + key.getP().toString());
                        writer.newLine();
                        writer.append("Q: " + key.getQ().toString());
                    }
                    if (contains(args, "pollard_rho")) {
                        startingAt = System.currentTimeMillis();
                        PollardRho.factor(key.getN());
                        executionTime = System.currentTimeMillis() - startingAt;
                        System.out.println("Pollard-Rho (N: " + key.getN().bitLength() + " P: " + key.getP().bitLength() + " Q: " + key.getQ().bitLength() + ") in: " + executionTime);
                        writer.newLine();
                        writer.newLine();
                        writer.append("Pollard-Rho in (" + key.getN().bitLength() + "): " + executionTime);
                        writer.newLine();
                        writer.append("N: " + key.getN().bitLength() + " P: " + key.getP().bitLength() + " Q: " + key.getQ().bitLength());
                        writer.newLine();
                        writer.append("N: " + key.getN().toString());
                        writer.newLine();
                        writer.append("P: " + key.getP().toString());
                        writer.newLine();
                        writer.append("Q: " + key.getQ().toString());
                    }
                }
            }
        } finally {
            writer.close();
        }
    }
}
