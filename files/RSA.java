// Implements: Key Generation, Encryption, Decryption
// Note: Uses BigInteger for large integers
// Menu: 1) Generate Keys  2) Encrypt  3) Decrypt  4) Quit

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

public class RSA {

    private static BigInteger n, d, e;
    private static int blockSize;
    private static final SecureRandom random = new SecureRandom();

    // Generate probable prime of given bit length
    private static BigInteger generatePrime(int bits) {
        return BigInteger.probablePrime(bits, random);
    }

    // Generate RSA key pair
    private static void generateKeys(int bits) {
        BigInteger p = generatePrime(bits / 2);
        BigInteger q = generatePrime(bits / 2);
        n = p.multiply(q);
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        e = BigInteger.valueOf(65537); // common choice
        if (!phi.gcd(e).equals(BigInteger.ONE)) {
            e = BigInteger.valueOf(3);
            while (!phi.gcd(e).equals(BigInteger.ONE)) {
                e = e.add(BigInteger.TWO);
            }
        }
        d = e.modInverse(phi);
        blockSize = (n.bitLength() - 1) / 8;

        System.out.println("\n[Key Generation Complete]");
        System.out.println("Public key (e, n):");
        System.out.println(" e = " + e);
        System.out.println(" n = " + n);
        System.out.println("Private key (d, n):");
        System.out.println(" d = " + d);
        System.out.println(" n = " + n);
        System.out.println("Max plaintext block length: " + blockSize + " bytes");
    }

    // Encode bytes to BigInteger
    private static BigInteger bytesToInt(byte[] data) {
        return new BigInteger(1, data);
    }

    // Decode BigInteger back to bytes of given length
    private static byte[] intToBytes(BigInteger x, int length) {
        byte[] arr = x.toByteArray();
        if (arr.length == length) return arr;
        byte[] result = new byte[length];
        // copy from the end
        int start = Math.max(0, arr.length - length);
        int destPos = length - (arr.length - start);
        System.arraycopy(arr, start, result, destPos, arr.length - start);
        return result;
    }

    // Encryption
    private static List<BigInteger> encrypt(String message) {
        byte[] data = message.getBytes();
        List<BigInteger> cipherBlocks = new ArrayList<>();
        for (int i = 0; i < data.length; i += blockSize) {
            int end = Math.min(i + blockSize, data.length);
            byte[] block = Arrays.copyOfRange(data, i, end);
            BigInteger m = bytesToInt(block);
            BigInteger c = m.modPow(e, n);
            cipherBlocks.add(c);
        }
        return cipherBlocks;
    }

    // Decryption
    private static String decrypt(List<BigInteger> cipherBlocks) {
        byte[] recovered = new byte[0];
        for (BigInteger c : cipherBlocks) {
            BigInteger m = c.modPow(d, n);
            byte[] block = intToBytes(m, blockSize);
            // trim trailing zeros only at the end
            recovered = concat(recovered, block);
        }
        return new String(recovered).trim();
    }

    private static byte[] concat(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    // Menu-driven program
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\nMenu:");
            System.out.println(" 1) Generate RSA Keys");
            System.out.println(" 2) Encrypt message");
            System.out.println(" 3) Decrypt message");
            System.out.println(" 4) Quit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.print("Enter key size in bits (e.g., 512, 1024, 2048): ");
                int bits = sc.nextInt();
                sc.nextLine();
                generateKeys(bits);

            } else if (choice == 2) {
                if (n == null) {
                    System.out.println("Generate keys first!");
                    continue;
                }
                System.out.print("Enter plaintext: ");
                String msg = sc.nextLine();
                List<BigInteger> ciphertext = encrypt(msg);
                System.out.println("Ciphertext blocks:");
                for (BigInteger c : ciphertext) {
                    System.out.print(c.toString() + " ");
                }
                System.out.println();

            } else if (choice == 3) {
                if (n == null) {
                    System.out.println("Generate keys first!");
                    continue;
                }
                System.out.println("Enter ciphertext blocks (space-separated integers):");
                String line = sc.nextLine();
                String[] parts = line.trim().split("\\s+");
                List<BigInteger> blocks = new ArrayList<>();
                for (String s : parts) {
                    blocks.add(new BigInteger(s));
                }
                String recovered = decrypt(blocks);
                System.out.println("Recovered plaintext: " + recovered);

            } else if (choice == 4) {
                System.out.println("Bye!");
                break;
            } else {
                System.out.println("Invalid choice!");
            }
        }
        sc.close();
    }
}
