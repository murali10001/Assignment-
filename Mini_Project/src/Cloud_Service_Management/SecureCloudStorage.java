package Cloud_Service_Management;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Base64;
import java.util.Scanner;

public class SecureCloudStorage {

    private static final String CLOUD_FOLDER = "cloud_storage/";

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Secure Cloud Data Storage ===");
        System.out.print("Enter file path to upload: ");
        String filePath = "C:/Users/manis/eclipse-workspace/Mini_Project/src/Cloud_computing/test";

        // Generate AES Key
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        SecretKey secretKey = keyGen.generateKey();

        // Read File
        FileInputStream fis = new FileInputStream(filePath);
        byte[] fileData = fis.readAllBytes();
        fis.close();

        // Encrypt Data
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(fileData);

        // Save to "Cloud"
        File cloudDir = new File(CLOUD_FOLDER);
        if (!cloudDir.exists()) cloudDir.mkdir();

        FileOutputStream fos = new FileOutputStream(CLOUD_FOLDER + "encrypted_file.dat");
        fos.write(encryptedData);
        fos.close();

        // Save Secret Key (for demo purpose, should be protected)
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("File uploaded securely!");
        System.out.println("Your Secret Key (keep safe): " + encodedKey);

        // Decryption demo
        System.out.print("\nDo you want to download & decrypt file? (yes/no): ");
        String choice = sc.nextLine();

        if (choice.equalsIgnoreCase("yes")) {
            System.out.print("Enter secret key: ");
            String keyInput = sc.nextLine();
            byte[] decodedKey = Base64.getDecoder().decode(keyInput);
            SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

            // Read Encrypted File
            FileInputStream fis2 = new FileInputStream(CLOUD_FOLDER + "encrypted_file.dat");
            byte[] encData = fis2.readAllBytes();
            fis2.close();

            Cipher cipher2 = Cipher.getInstance("AES");
            cipher2.init(Cipher.DECRYPT_MODE, originalKey);
            byte[] decryptedData = cipher2.doFinal(encData);

            FileOutputStream fos2 = new FileOutputStream("downloaded_file.txt");
            fos2.write(decryptedData);
            fos2.close();

            System.out.println("File downloaded and decrypted successfully!");
        }

        sc.close();
    }
}
