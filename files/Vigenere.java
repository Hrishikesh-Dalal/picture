public class Vigenere {

    // Function to repeat key to match text length
    public static String generateKey(String text, String key) {
        key = key.toUpperCase();
        StringBuilder newKey = new StringBuilder(key);

        while (newKey.length() < text.length()) {
            newKey.append(key);
        }
        return newKey.substring(0, text.length());
    }

    // Encryption
    public static String encrypt(String text, String key) {
        text = text.toUpperCase();
        key = key.toUpperCase();
        StringBuilder cipher = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char t = text.charAt(i);
            if (Character.isLetter(t)) {
                int p = t - 'A';
                int k = key.charAt(i) - 'A';
                int c = (p + k) % 26;
                cipher.append((char)(c + 'A'));
            } else {
                cipher.append(t);
            }
        }
        return cipher.toString();
    }

    // Decryption
    public static String decrypt(String cipher, String key) {
        key = key.toUpperCase();
        cipher = cipher.toUpperCase();
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < cipher.length(); i++) {
            char c = cipher.charAt(i);
            if (Character.isLetter(c)) {
                int ci = c - 'A';
                int ki = key.charAt(i) - 'A';
                int p = (ci - ki + 26) % 26;
                text.append((char)(p + 'A'));
            } else {
                text.append(c);
            }
        }
        return text.toString();
    }

    // Driver
    public static void main(String[] args) {
        String text = "HELLO";
        String key = "KEY";

        String newKey = generateKey(text, key);
        String encrypted = encrypt(text, newKey);
        String decrypted = decrypt(encrypted, newKey);

        System.out.println("Original : " + text);
        System.out.println("Key       : " + newKey);
        System.out.println("Encrypted : " + encrypted);
        System.out.println("Decrypted : " + decrypted);
    }
}
