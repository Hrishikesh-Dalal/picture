// HillCipher.java
// Hill cipher 2x2 (encrypt & decrypt) using modular inverse arithmetic

import java.util.*;

public class HillCipher {

    private static int modInverse(int a, int m) {
        a = (a % m + m) % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) return x;
        }
        return -1;
    }

    public static String hillEncrypt(String text, int[][] key) {
        text = text.toUpperCase().replaceAll("[^A-Z]", "");
        if (text.length() % 2 != 0) text += "X";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i += 2) {
            int a = text.charAt(i) - 'A';
            int b = text.charAt(i + 1) - 'A';
            int c1 = (key[0][0] * a + key[0][1] * b) % 26;
            int c2 = (key[1][0] * a + key[1][1] * b) % 26;
            result.append((char) (c1 + 'A')).append((char) (c2 + 'A'));
        }
        return result.toString();
    }

    public static String hillDecrypt(String text, int[][] key) {
        int det = key[0][0] * key[1][1] - key[0][1] * key[1][0];
        det = (det % 26 + 26) % 26;
        int detInv = modInverse(det, 26);
        if (detInv == -1) return null; // not invertible

        int[][] invKey = new int[2][2];
        invKey[0][0] = key[1][1];
        invKey[1][1] = key[0][0];
        invKey[0][1] = -key[0][1];
        invKey[1][0] = -key[1][0];

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                invKey[i][j] = ((invKey[i][j] * detInv) % 26 + 26) % 26;
            }
        }

        StringBuilder result = new StringBuilder();
        text = text.toUpperCase().replaceAll("[^A-Z]", "");
        if (text.length() % 2 != 0) text += "X";
        for (int i = 0; i < text.length(); i += 2) {
            int a = text.charAt(i) - 'A';
            int b = text.charAt(i + 1) - 'A';
            int p1 = (invKey[0][0] * a + invKey[0][1] * b) % 26;
            int p2 = (invKey[1][0] * a + invKey[1][1] * b) % 26;
            result.append((char) (p1 + 'A')).append((char) (p2 + 'A'));
        }
        return result.toString();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter plaintext: ");
        String text = sc.nextLine();

        int[][] key = new int[2][2];
        System.out.println("Enter 2x2 key matrix (integers 0..25 preferred):");
        for (int i = 0; i < 2; i++) {
            System.out.print("Row " + (i+1) + " (two integers separated by space): ");
            String[] parts = sc.nextLine().trim().split("\\s+");
            while (parts.length < 2) {
                System.out.print("Please enter two integers: ");
                parts = sc.nextLine().trim().split("\\s+");
            }
            key[i][0] = Integer.parseInt(parts[0]);
            key[i][1] = Integer.parseInt(parts[1]);
        }

        // Compute determinant and check invertibility
        int det = key[0][0] * key[1][1] - key[0][1] * key[1][0];
        det = (det % 26 + 26) % 26;
        int detInv = modInverse(det, 26);
        if (detInv == -1) {
            System.out.println("The key matrix is not invertible modulo 26 (det = " + det + ").");
            System.out.println("Decryption is not possible with this key. Choose another key.");
            sc.close();
            return;
        }

        String enc = hillEncrypt(text, key);
        String dec = hillDecrypt(enc, key);
        System.out.println("Encrypted: " + enc);
        if (dec == null) {
            System.out.println("Decryption failed (non-invertible key).");
        } else {
            System.out.println("Decrypted: " + dec);
        }
        sc.close();
    }
}
