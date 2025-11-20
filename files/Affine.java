public class Affine {

    // Function to find modular inverse
    public static int modInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1)
                return x;
        }
        return -1; // No modular inverse
    }

    // Encryption: C = (a * P + b) mod 26
    public static String encrypt(String text, int a, int b) {
        StringBuilder result = new StringBuilder();

        for (char ch : text.toUpperCase().toCharArray()) {
            if (Character.isLetter(ch)) {
                int x = ch - 'A';
                int encrypted = (a * x + b) % 26;
                result.append((char)(encrypted + 'A'));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    // Decryption: P = a_inv * (C - b) mod 26
    public static String decrypt(String cipher, int a, int b) {
        StringBuilder result = new StringBuilder();
        int a_inv = modInverse(a, 26);

        for (char ch : cipher.toUpperCase().toCharArray()) {
            if (Character.isLetter(ch)) {
                int y = ch - 'A';
                int decrypted = (a_inv * (y - b + 26)) % 26;
                result.append((char)(decrypted + 'A'));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    // Driver
    public static void main(String[] args) {
        String text = "HELLO";
        int a = 5, b = 8;   // a must be coprime with 26

        String encrypted = encrypt(text, a, b);
        String decrypted = decrypt(encrypted, a, b);

        System.out.println("Original : " + text);
        System.out.println("Encrypted: " + encrypted);
        System.out.println("Decrypted: " + decrypted);
    }
}
