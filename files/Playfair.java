// PlayfairCipher.java
// Playfair cipher (5x5 matrix, J replaced by I)

import java.util.*;

public class Playfair {

    private static char[][] generatePlayfairMatrix(String key) {
        key = key.toUpperCase().replace("J", "I");
        StringBuilder sb = new StringBuilder();
        for (char c : key.toCharArray()) {
            if (Character.isLetter(c) && sb.indexOf(String.valueOf(c)) == -1) {
                sb.append(c);
            }
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            if (c == 'J') continue;
            if (sb.indexOf(String.valueOf(c)) == -1) sb.append(c);
        }
        char[][] matrix = new char[5][5];
        for (int i = 0; i < 25; i++) {
            matrix[i / 5][i % 5] = sb.charAt(i);
        }
        return matrix;
    }

    private static String processPlayfairText(String text) {
        text = text.toUpperCase().replace("J", "I").replaceAll("[^A-Z]", "");
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < text.length()) {
            char a = text.charAt(i);
            char b = (i + 1 < text.length()) ? text.charAt(i + 1) : 'X';
            if (a == b) {
                sb.append(a).append('X');
                i++;
            } else {
                sb.append(a).append(b);
                i += 2;
            }
        }
        if (sb.length() % 2 != 0) sb.append('X');
        return sb.toString();
    }

    private static int[] findPos(char[][] matrix, char c) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (matrix[i][j] == c) return new int[]{i, j};
            }
        }
        return null;
    }

    public static String playfairEncrypt(String text, String key) {
        char[][] matrix = generatePlayfairMatrix(key);
        text = processPlayfairText(text);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i += 2) {
            char a = text.charAt(i), b = text.charAt(i + 1);
            int[] p1 = findPos(matrix, a), p2 = findPos(matrix, b);
            if (p1[0] == p2[0]) {
                result.append(matrix[p1[0]][(p1[1] + 1) % 5]);
                result.append(matrix[p2[0]][(p2[1] + 1) % 5]);
            } else if (p1[1] == p2[1]) {
                result.append(matrix[(p1[0] + 1) % 5][p1[1]]);
                result.append(matrix[(p2[0] + 1) % 5][p2[1]]);
            } else {
                result.append(matrix[p1[0]][p2[1]]);
                result.append(matrix[p2[0]][p1[1]]);
            }
        }
        return result.toString();
    }

    public static String playfairDecrypt(String text, String key) {
        char[][] matrix = generatePlayfairMatrix(key);
        StringBuilder result = new StringBuilder();
        text = text.toUpperCase().replaceAll("[^A-Z]", "");
        // ensure even length
        if (text.length() % 2 != 0) text += "X";
        for (int i = 0; i < text.length(); i += 2) {
            char a = text.charAt(i), b = text.charAt(i + 1);
            int[] p1 = findPos(matrix, a), p2 = findPos(matrix, b);
            if (p1[0] == p2[0]) {
                result.append(matrix[p1[0]][(p1[1] + 4) % 5]);
                result.append(matrix[p2[0]][(p2[1] + 4) % 5]);
            } else if (p1[1] == p2[1]) {
                result.append(matrix[(p1[0] + 4) % 5][p1[1]]);
                result.append(matrix[(p2[0] + 4) % 5][p2[1]]);
            } else {
                result.append(matrix[p1[0]][p2[1]]);
                result.append(matrix[p2[0]][p1[1]]);
            }
        }
        return result.toString();
    }

    // helper to print the matrix (for debugging/learning)
    private static void printMatrix(char[][] m) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) System.out.print(m[i][j] + " ");
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter plaintext: ");
        String text = sc.nextLine();
        System.out.print("Enter keyword: ");
        String key = sc.nextLine();

        // show matrix (optional)
        // System.out.println("Playfair matrix:");
        // printMatrix(generatePlayfairMatrix(key));

        String enc = playfairEncrypt(text, key);
        String dec = playfairDecrypt(enc, key);
        System.out.println("Encrypted: " + enc);
        System.out.println("Decrypted (raw, may include padding X): " + dec);
        sc.close();
    }
}
