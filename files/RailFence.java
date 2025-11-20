// RailFenceCipher.java
// Simple Rail Fence cipher: encrypt & decrypt (non-menu)

import java.util.*;

public class RailFence {

    public static String railFenceEncrypt(String text, int key) {
        if (key <= 1) return text;

        char[][] rail = new char[key][text.length()];
        for (char[] row : rail) Arrays.fill(row, '\n');

        boolean down = false;
        int row = 0, col = 0;

        for (char c : text.toCharArray()) {
            if (row == 0 || row == key - 1) down = !down;
            rail[row][col++] = c;
            row += down ? 1 : -1;
        }

        StringBuilder result = new StringBuilder();
        for (char[] r : rail)
            for (char c : r)
                if (c != '\n') result.append(c);
        return result.toString();
    }

    public static String railFenceDecrypt(String cipher, int key) {
        if (key <= 1) return cipher;

        char[][] rail = new char[key][cipher.length()];
        for (char[] row : rail) Arrays.fill(row, '\n');

        boolean down = false;
        int row = 0, col = 0;

        for (int i = 0; i < cipher.length(); i++) {
            if (row == 0) down = true;
            if (row == key - 1) down = false;
            rail[row][col++] = '*';
            row += down ? 1 : -1;
        }

        int index = 0;
        for (int i = 0; i < key; i++)
            for (int j = 0; j < cipher.length(); j++)
                if (rail[i][j] == '*' && index < cipher.length())
                    rail[i][j] = cipher.charAt(index++);

        StringBuilder result = new StringBuilder();
        row = 0; col = 0; down = false;
        for (int i = 0; i < cipher.length(); i++) {
            if (row == 0) down = true;
            if (row == key - 1) down = false;
            result.append(rail[row][col++]);
            row += down ? 1 : -1;
        }
        return result.toString();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter plaintext: ");
        String text = sc.nextLine();
        System.out.print("Enter key (rails, integer >= 1): ");
        int rails;
        try {
            rails = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid key. Using 3 rails.");
            rails = 3;
        }

        String enc = railFenceEncrypt(text, rails);
        String dec = railFenceDecrypt(enc, rails);
        System.out.println("Encrypted: " + enc);
        System.out.println("Decrypted: " + dec);
        sc.close();
    }
}
