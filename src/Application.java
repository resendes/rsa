import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Application {

    private static boolean contains(String[] args, String element) {
        return Arrays.stream(args).anyMatch(element::equals);
    }

    public static void main(String[] args) throws Exception {
        Integer[] keyLengths = {42};
        Map<Integer, List<Long>> pollardExecutions = new HashMap<>();
        Map<Integer, List<Long>> generateKeysExecutions = new HashMap<>();
        Map<Integer, List<Long>> bruteForceExecutions = new HashMap<>();

        for (int j = 0; j < keyLengths.length; j++) {
            generateKeysExecutions.put(keyLengths[j], new ArrayList<>());
            pollardExecutions.put(keyLengths[j], new ArrayList<>());
            bruteForceExecutions.put(keyLengths[j], new ArrayList<>());
            for (int i = 0; i < 10; i++) {
                if (contains(args, "generate")) {
                    long startingAt = System.currentTimeMillis();
                    Key.generate(keyLengths[j], "rsa_public.txt", "rsa_private.txt");
                    long executionTime = System.currentTimeMillis() - startingAt;
                    generateKeysExecutions.get(keyLengths[j]).add(executionTime);
                    System.out.println("Key generated (" + keyLengths[j] + ") in: " + executionTime);
                }
                if (contains(args, "encrypt_decrypt")) {
                    Rsa.encrypt();
                    Rsa.decrypt();
                }
                if (contains(args, "brute_force")) {
                    long startingAt = System.currentTimeMillis();
                    BruteForce.bruteForcePublicKey();
                    long executionTime = System.currentTimeMillis() - startingAt;
                    bruteForceExecutions.get(keyLengths[j]).add(executionTime);
                    System.out.println("Brute force (" + keyLengths[j] + ") in: " + (executionTime));
                }
                if (contains(args, "pollard_rho")) {
                    long startingAt = System.currentTimeMillis();
                    PollardRho.factor();
                    long executionTime = System.currentTimeMillis() - startingAt;
                    pollardExecutions.get(keyLengths[j]).add(executionTime);
                    System.out.println("Pollard Rho (" + keyLengths[j] + ") in: " + (executionTime));
                }
            }
        }

        File file = new File("result.txt");
        FileWriter fileWriter = new FileWriter(file, false);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        // Add some more data.
        //Write data into another file.

        generateKeysExecutions.entrySet().stream().forEach(x -> {
            try {
                writer.append("Key generation (" + x.getKey() + ")");
                writer.newLine();
                writer.append(String.join(", ", x.getValue().stream().map(aLong -> aLong.toString()).collect(Collectors.toList())));
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        pollardExecutions.entrySet().stream().forEach(x -> {
            try {
                writer.append("Pollard Rho (" + x.getKey() + ")");
                writer.newLine();
                writer.append(String.join(", ", x.getValue().stream().map(aLong -> aLong.toString()).collect(Collectors.toList())));
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        bruteForceExecutions.entrySet().stream().forEach(x -> {
            try {
                writer.append("Brute Force (" + x.getKey() + ")");
                writer.newLine();
                writer.append(String.join(", ", x.getValue().stream().map(aLong -> aLong.toString()).collect(Collectors.toList())));
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        writer.close();

    }
}
