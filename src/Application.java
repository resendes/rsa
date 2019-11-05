import java.util.Arrays;

public class Application {

    private static boolean contains(String[] args, String element) {
        return Arrays.stream(args).anyMatch(element::equals);
    }

    public static void main(String[] args) throws Exception {
        Integer[] keyLengths = {8, 16, 32, 64};

        for (Integer keyLength : keyLengths) {
            for (int i = 0; i< 20; i++) {
                if (contains(args, "generate")) {
                    long startingAt = System.currentTimeMillis();
                    Key.generate(keyLength, "rsa_public.txt", "rsa_private.txt");
                    System.out.println("Key generated (" + keyLength + ") in: " + (System.currentTimeMillis() - startingAt));
                }
                if (contains(args, "encrypt_decrypt")) {
                    Rsa.encrypt();
                    Rsa.decrypt();
                }
                if (contains(args, "brute_force")) {
                    long startingAt = System.currentTimeMillis();
                    BruteForce.bruteForcePublicKey();
                    System.out.println("Brute force (" + keyLength + ") in: " + (System.currentTimeMillis() - startingAt));
                }
                if (contains(args, "polard_rho")) {
                    long startingAt = System.currentTimeMillis();
                    PolardRho.factor();
                    System.out.println("Pollard Rho (" + keyLength + ") in: " + (System.currentTimeMillis() - startingAt));
                }
            }
        }

    }
}
