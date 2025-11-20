// ColumnarTransposition.java
// Columnar transposition cipher: encrypt & decrypt (non-menu)

import java.util.*;

public class ColumnarTransposition {

    public static String columnarEncrypt(String text, String key) {
        int cols = key.length();
        int rows = (int) Math.ceil((double) text.length() / cols);
        char[][] grid = new char[rows][cols];
        int k = 0;

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                grid[i][j] = (k < text.length()) ? text.charAt(k++) : 'X';

        Integer[] order = new Integer[cols];
        for (int i = 0; i < cols; i++) order[i] = i;
        Arrays.sort(order, Comparator.comparingInt(key::charAt));

        StringBuilder cipher = new StringBuilder();
        for (int col : order)
            for (int i = 0; i < rows; i++)
                cipher.append(grid[i][col]);

        return cipher.toString();
    }

    public static String columnarDecrypt(String cipher, String key) {
        int cols = key.length();
        int rows = (int) Math.ceil((double) cipher.length() / cols);
        char[][] grid = new char[rows][cols];

        Integer[] order = new Integer[cols];
        for (int i = 0; i < cols; i++) order[i] = i;
        Arrays.sort(order, Comparator.comparingInt(key::charAt));

        int k = 0;
        for (int col : order)
            for (int i = 0; i < rows; i++)
                grid[i][col] = (k < cipher.length()) ? cipher.charAt(k++) : 'X';

        StringBuilder plain = new StringBuilder();
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                plain.append(grid[i][j]);

        return plain.toString().replaceAll("X+$", "");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter plaintext: ");
        String text = sc.nextLine();
        System.out.print("Enter key (word): ");
        String key = sc.nextLine().trim();
        if (key.isEmpty()) {
            System.out.println("Empty key not allowed. Using default key \"KEY\".");
            key = "KEY";
        }

        String enc = columnarEncrypt(text, key);
        String dec = columnarDecrypt(enc, key);
        System.out.println("Encrypted: " + enc);
        System.out.println("Decrypted: " + dec);
        sc.close();
    }
}
