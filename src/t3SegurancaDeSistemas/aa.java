package t3SegurancaDeSistemas;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class aa {

    public static void main(String[] args) throws Exception {
        String key = "teste";
        String clean = "teste";

        byte[] encrypted = encrypt(clean, key);      
		System.out.println("TEXTO DESCIFRADO");
		System.out.println("mensagem em texto: ");
		System.out.println(new String(encrypted, StandardCharsets.UTF_8));
		System.out.println("mensagem descriptografada em Base64: ");
		System.out.println(Base64.getEncoder().encodeToString(encrypted));
		System.out.println("mensagem descriptografada em hex:");
		System.out.println(byteArrayToHexString(encrypted));
		System.out.println("byte -> hex -> texto plano: ");
		System.out.println(fromHexString(byteArrayToHexString(encrypted)));
        String decrypted = decrypt(encrypted, key);
        System.out.println(decrypted);
    }

    public static byte[] encrypt(String plainText, String key) throws Exception {
        byte[] clean = plainText.getBytes();

        // Generating IV.
        int ivSize = 16;
        byte[] iv = new byte[ivSize];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        // Hashing key.
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(key.getBytes("UTF-8"));
        byte[] keyBytes = new byte[16];
        System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);
        System.out.println("KEY  DESCIFRADO");
		System.out.println("mensagem em texto: ");
		System.out.println(new String(keyBytes, StandardCharsets.UTF_8));
		System.out.println("mensagem descriptografada em Base64: ");
		System.out.println(Base64.getEncoder().encodeToString(keyBytes));
		System.out.println("mensagem descriptografada em hex:");
		System.out.println(byteArrayToHexString(keyBytes));
		System.out.println("byte -> hex -> texto plano: ");
		System.out.println(fromHexString(byteArrayToHexString(keyBytes)));
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        // Encrypt.
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(clean);

        // Combine IV and encrypted part.
        byte[] encryptedIVAndText = new byte[ivSize + encrypted.length];
        System.arraycopy(iv, 0, encryptedIVAndText, 0, ivSize);
        System.arraycopy(encrypted, 0, encryptedIVAndText, ivSize, encrypted.length);

        return encryptedIVAndText;
    }

    public static String decrypt(byte[] encryptedIvTextBytes, String key) throws Exception {
        int ivSize = 16;
        int keySize = 16;

        // Extract IV.
        byte[] iv = new byte[ivSize];
        System.arraycopy(encryptedIvTextBytes, 0, iv, 0, iv.length);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        // Extract encrypted part.
        int encryptedSize = encryptedIvTextBytes.length - ivSize;
        byte[] encryptedBytes = new byte[encryptedSize];
        System.arraycopy(encryptedIvTextBytes, ivSize, encryptedBytes, 0, encryptedSize);

        // Hash key.
        byte[] keyBytes = new byte[keySize];
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(key.getBytes());
        System.arraycopy(md.digest(), 0, keyBytes, 0, keyBytes.length);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        // Decrypt.
        Cipher cipherDecrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decrypted = cipherDecrypt.doFinal(encryptedBytes);

        return new String(decrypted);
    }
    
    public static String byteArrayToHexString(byte[] encrypted) {
		final char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] hexChars = new char[encrypted.length * 2]; // Each byte has two hex characters (nibbles)
		int v;
		for (int j = 0; j < encrypted.length; j++) {
			v = encrypted[j] & 0xFF; // Cast bytes[j] to int, treating as unsigned value
			hexChars[j * 2] = hexArray[v >>> 4]; // Select hex character from upper nibble
			hexChars[j * 2 + 1] = hexArray[v & 0x0F]; // Select hex character from lower nibble
		}
		return new String(hexChars);
	}
    
    public static String fromHexString(String hex) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < hex.length(); i += 2) {
			str.append((char) Integer.parseInt(hex.substring(i, i + 2), 16));
		}
		return str.toString();
	}
}
