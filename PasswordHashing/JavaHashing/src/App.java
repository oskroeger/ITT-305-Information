import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import org.mindrot.jbcrypt.BCrypt;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter a password: ");
        String password = scanner.nextLine();

        String[] algorithms = {"MD5", "SHA-1", "SHA-256", "SHA-512", "SHA-384", "SHA-224", "SHA3-224", "SHA3-256", "SHA3-384", "SHA3-512", "MD2", "MD4"};
        
        // Hashing with different algorithms
        for (String algorithm : algorithms) {
            String hash = hashWithAlgorithm(password, algorithm);
            printWithColor(algorithm + ": ", hash);
        }

        // Hashing with BCrypt
        String bcryptString = hashWithBCrypt(password);
        printWithColor("BCrypt: ", bcryptString);
        scanner.close();

        // Benchmark for each algorithm (hashing 10,000 times)
        for (String algorithm : algorithms) {
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < 10000; i++) {
                hashWithAlgorithm(password, algorithm);
            }
            long endTime = System.currentTimeMillis();
            long timeTaken = endTime - startTime;
            System.out.println("Time taken to hash " + algorithm + " 10000 times: " + timeTaken + " milliseconds");
        }

        // Benchmark for BCrypt (hashing 10 times)
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            hashWithBCrypt(password);
        }
        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;
        System.out.println("Time taken to hash BCrypt 10 times: " + timeTaken + " milliseconds");
    }

    private static void printWithColor(String string, String string2) {
        // Print first string in red
        System.out.print("\u001B[31m" + string + " ");
        // Print second string in blue
        System.out.println("\u001B[34m" + string2);
        // Reset color
        System.out.print("\u001B[0m");
    }

    private static String hashWithAlgorithm(String password, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return "Algorithm not found: " + algorithm;
        }
    }

    private static String hashWithBCrypt(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
