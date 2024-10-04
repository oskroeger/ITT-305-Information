import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        SecureRandom secureRandom = new SecureRandom();
        // List of ANSI color codes
        List<String> colors = Arrays.asList("\u001B[30m", "\u001B[31m", "\u001B[32m", "\u001B[33m", "\u001B[34m", "\u001B[35m", "\u001B[36m", "\u001B[37m");

        // User input
        Scanner scanner = new Scanner(System.in);
        while (true) {
            // black
            System.out.println(colors.get(0) + "Welcome to the RSA encryption and decryption program!");
            System.out.print("Enter your message: ");
            String message = scanner.nextLine();
            int bitLength = getBitLength(scanner);
            BigInteger exponent = getExponent(scanner);
            Date startTime = new Date();
            BigInteger e = exponent;

            // Generate two 1024-bit prime numbers
            BigInteger p = generatePrime(secureRandom, bitLength);
            BigInteger q = generatePrime(secureRandom, bitLength);

            // Calculate modulus n = p * q
            BigInteger n = p.multiply(q);

            // Calculate Euler's Totient Function φ(n) = (p - 1) * (q - 1)
            BigInteger phi = calculatePhi(p, q);

            // Ensure that e is coprime with φ(n)
            if (!isCoprime(e, phi)) {
                System.out.println("The public exponent e is not coprime with φ(n). Please choose a different value for e.");
                continue;
            }

            // Calculate the private exponent d = e^(-1) mod φ(n)
            BigInteger d = e.modInverse(phi);

            // Print values with colors
            printColored("p: " + p, colors.get(0));
            printColored("q: " + q, colors.get(1));
            printColored("n: " + n, colors.get(2));
            printColored("φ(n) = (p - 1) * (q - 1) = " + phi, colors.get(3));
            printColored("e: is the public exponent = " + e, colors.get(4));
            printColored("d: is the private key = " + d, colors.get(5));

            // Encrypt the message
            printColored("Message: " + message, colors.get(0));
            BigInteger encryptedMessage = encryptMessage(message, e, n);
            printColored("Encrypted message as integer: " + encryptedMessage, colors.get(6));

            // Decrypt the message
            BigInteger decryptedMessage = encryptedMessage.modPow(d, n);
            printDecryptedMessage(decryptedMessage, colors);

            Date endTime = new Date();
            long timeElapsed = endTime.getTime() - startTime.getTime();
            printColored("Time elapsed: " + timeElapsed + " milliseconds", colors.get(7));

            // Ask the user if they want to continue
            System.out.print("Do you want to encrypt another message? (yes/no): ");
            String continueChoice = scanner.nextLine();
            if (!continueChoice.equalsIgnoreCase("yes")) {
                break;
            }
        }
    }

    private static boolean isCoprime(BigInteger e, BigInteger phi) {
        return e.gcd(phi).equals(BigInteger.ONE);
    }

    private static BigInteger getExponent(Scanner scanner) {
        // Provide user with multiple choices for the value of e
        List<BigInteger> eChoices = new ArrayList<>();
        for (int i = 2; i < 9; i += 2) {
            // 2 ^ 2 ^ i + 1 = 
            BigInteger e = new BigInteger("2").pow(2).pow(i).add(BigInteger.ONE);
            eChoices.add(e);
        }

        System.out.println("Choose a value for the public exponent e:");
        for (int i = 0; i < eChoices.size(); i++) {
            System.out.println((i + 1) + ": " + eChoices.get(i));
        }

        System.out.print("Enter the number corresponding to your choice: ");
        int eChoiceIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume the newline character left by nextInt()
        return eChoices.get(eChoiceIndex);
    }

    private static int getBitLength(Scanner scanner) {
        int bitLength = 1024;
        System.out.println("What bit length would you like to use for the prime numbers? Longer is more secure (40 - 4096)");
        while (true) {
            System.out.print("Enter a number: ");
            bitLength = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character left by nextInt()
            if (bitLength >= 40 && bitLength <= 4096) {
                break;
            } else {
                System.out.println("Please enter a number between 10 and 4096.");
            }
        }
        return bitLength;
    }

    private static BigInteger generatePrime(SecureRandom secureRandom, int bitLength) {
        return new BigInteger(bitLength, 100, secureRandom);
    }

    private static BigInteger calculatePhi(BigInteger p, BigInteger q) {
        return p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
    }

    private static void printColored(String message, String color) {
        System.out.print(color);
        System.out.println(message);
    }

    private static BigInteger encryptMessage(String message, BigInteger e, BigInteger n) {
        BigInteger messageInt = new BigInteger(message.getBytes());
        return messageInt.modPow(e, n);
    }

    private static void printDecryptedMessage(BigInteger decryptedMessage, List<String> colors) {
        printColored("Decrypted integer value: " + decryptedMessage, colors.get(0));
        printColored("Decrypted message in byte array: " + Arrays.toString(decryptedMessage.toByteArray()), colors.get(1));
        printColored("Decrypted message as an ASCII string: " + new String(decryptedMessage.toByteArray()), colors.get(2));
    }
}