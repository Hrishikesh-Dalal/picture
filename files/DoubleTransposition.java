// DoubleTransposition.java
// Double transposition cipher using two columnar passes (non-menu)

import java.util.*;

public class DoubleTransposition {

    // reuse columnar functions (copy/paste or keep same file)
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

    public static String doubleEncrypt(String text, String key1, String key2) {
        return columnarEncrypt(columnarEncrypt(text, key1), key2);
    }

    public static String doubleDecrypt(String cipher, String key1, String key2) {
        return columnarDecrypt(columnarDecrypt(cipher, key2), key1);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter plaintext: ");
        String text = sc.nextLine();
        System.out.print("Enter first key: ");
        String key1 = sc.nextLine().trim();
        System.out.print("Enter second key: ");
        String key2 = sc.nextLine().trim();

        if (key1.isEmpty() || key2.isEmpty()) {
            System.out.println("Keys cannot be empty. Using defaults \"KEY1\" and \"KEY2\".");
            if (key1.isEmpty()) key1 = "KEY1";
            if (key2.isEmpty()) key2 = "KEY2";
        }

        String enc = doubleEncrypt(text, key1, key2);
        String dec = doubleDecrypt(enc, key1, key2);
        System.out.println("Encrypted: " + enc);
        System.out.println("Decrypted: " + dec);
        sc.close();
    }
}
