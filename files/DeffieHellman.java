package Exp4;
import java.util.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Scanner;

public class DeffieHellman {
    private static final SecureRandom random = new SecureRandom();

    // Generate random prime of given bit length
    private static BigInteger generatePrime(int bits) {
        return BigInteger.probablePrime(bits, random);
    }

    // SHA-256 hashing for Key Derivation
    private static String sha256(BigInteger key) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(key.toByteArray());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append("0");
            hexString.append(hex);
        }
        return hexString.toString();
    }

    // Simple XOR encryption/decryption demo
    private static String xorEncryptDecrypt(String text, byte[] key) {
        byte[] input = text.getBytes();
        byte[] output = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = (byte) (input[i] ^ key[i % key.length]);
        }
        return new String(output);
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        BigInteger p = null, g = null;
        BigInteger a = null, A = null, b = null, B = null, K_A = null, K_B = null;

        while (true) {
            System.out.println("\n--- Diffie-Hellman Key Exchange ---");
            System.out.println("1) Select / Generate Public Parameters (p, g)");
            System.out.println("2) Generate Keys for Alice and Bob");
            System.out.println("3) Compute Shared Secret");
            System.out.println("4) Derive Symmetric Key & Encrypt/Decrypt Demo");
            System.out.println("5) Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Enter bit length for prime p (e.g., 512 for demo): ");
                    int bits = sc.nextInt();
                    p = generatePrime(bits);
                    g = new BigInteger("2"); // simple generator
                    System.out.println("Public Parameters:");
                    System.out.println("p = " + p);
                    System.out.println("g = " + g);
                    break;

                case 2:
                    if (p == null || g == null) {
                        System.out.println("Please generate (p, g) first.");
                        break;
                    }
                    a = new BigInteger(p.bitLength() - 2, random);
                    A = g.modPow(a, p);
                    b = new BigInteger(p.bitLength() - 2, random);
                    B = g.modPow(b, p);

                    System.out.println("Alice: Private Key (a) = hidden, Public Key (A) = " + A);
                    System.out.println("Bob: Private Key (b) = hidden, Public Key (B) = " + B);
                    break;

                case 3:
                    if (A == null || B == null) {
                        System.out.println("Generate keys first.");
                        break;
                    }
                    K_A = B.modPow(a, p);
                    K_B = A.modPow(b, p);
                    System.out.println("Alice computes shared secret K_A = " + K_A);
                    System.out.println("Bob computes shared secret K_B = " + K_B);
                    System.out.println("Shared Secret Match: " + K_A.equals(K_B));
                    break;

                case 4:
                    if (K_A == null || K_B == null) {
                        System.out.println("Compute shared secret first.");
                        break;
                    }
                    String symmetricKeyHex = sha256(K_A);
                    byte[] symmetricKey = symmetricKeyHex.getBytes();
                    System.out.println("Derived Symmetric Key (SHA-256): " + symmetricKeyHex);

                    sc.nextLine(); // flush buffer
                    System.out.print("Enter message to encrypt: ");
                    String message = sc.nextLine();

                    String ciphertext = xorEncryptDecrypt(message, symmetricKey);
                    String decrypted = xorEncryptDecrypt(ciphertext, symmetricKey);

                    System.out.println("Ciphertext: " + ciphertext);
                    System.out.println("Decrypted: " + decrypted);
                    break;

                case 5:
                    System.out.println("Exiting...");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
