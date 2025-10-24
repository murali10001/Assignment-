package Cryptography_project;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Scanner;

public class TextSteganography {

    // AES encryption
    public static String encrypt(String data, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // safe padding
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // AES decryption
    public static String decrypt(String encryptedData, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }

    // Encode: insert encrypted message characters into cover text using interval
    public static String encode(String coverText, String secret, int interval) {
        if (coverText.length() < secret.length() * interval) {
            throw new IllegalArgumentException("Cover text is too short to hide the secret message.");
        }

        StringBuilder encoded = new StringBuilder();
        int secretIndex = 0;
        for (int i = 0; i < coverText.length(); i++) {
            encoded.append(coverText.charAt(i));
            if ((i + 1) % interval == 0 && secretIndex < secret.length()) {
                encoded.append(secret.charAt(secretIndex));
                secretIndex++;
            }
        }
        return encoded.toString();
    }

    // Decode: extract exactly secretLength characters by using the interval positions
    // (the same interval used during encoding). This avoids accidentally selecting
    // cover-text characters that look like Base64.
    public static String decode(String encodedText, int interval, int secretLength) {
        StringBuilder hidden = new StringBuilder();
        int coverCount = 0; // counts cover characters since last inserted secret char
        int found = 0;

        for (int i = 0; i < encodedText.length() && found < secretLength; i++) {
            char ch = encodedText.charAt(i);

            if (coverCount == interval) {
                // this char is an inserted secret char
                hidden.append(ch);
                found++;
                coverCount = 0;
            } else {
                coverCount++;
            }
        }

        return hidden.toString();
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("==== TEXT STEGANOGRAPHY WITH ENCRYPTION ====");
            System.out.print("Enter your message: ");
            String message = sc.nextLine();

            System.out.print("Enter 16-char secret key (AES): ");
            String key = sc.nextLine();

            if (key.length() != 16) {
                System.out.println("Error: Key must be exactly 16 characters!");
                return;
            }

            System.out.print("Enter cover text: ");
            String cover = sc.nextLine();

            // Encrypt the message
            String encrypted = encrypt(message, key);
            System.out.println("\nEncrypted Message: " + encrypted);

            // Calculate interval automatically
            int interval = cover.length() / encrypted.length();
            if (interval < 1) interval = 1;

            // Encode
            String encodedText = encode(cover, encrypted, interval);
            System.out.println("\nEncoded Text: \n" + encodedText);

            // Decode using the interval we computed during encoding
            String hiddenData = decode(encodedText, interval, encrypted.length());
            String decrypted = decrypt(hiddenData, key);

            System.out.println("\nDecoded & Decrypted Message: " + decrypted);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
