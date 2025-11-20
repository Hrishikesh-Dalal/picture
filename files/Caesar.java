import java.util.*;

public class CaesarCipher {

    public static String caesarEncrypt(String text, int key) {
        StringBuilder result = new StringBuilder();
        text = text.toUpperCase();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                result.append((char) ((c - 'A' + key + 26) % 26 + 'A'));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public static String caesarDecrypt(String text, int key) {
        return caesarEncrypt(text, -key);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter plaintext: ");
        String text = sc.nextLine();
        System.out.print("Enter key (shift, integer): ");
        int key;
        try {
            key = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid key. Using 0.");
            key = 0;
        }
        String enc = caesarEncrypt(text, key);
        String dec = caesarDecrypt(enc, key);
        System.out.println("Encrypted: " + enc);
        System.out.println("Decrypted: " + dec);
        sc.close();
    }
}
