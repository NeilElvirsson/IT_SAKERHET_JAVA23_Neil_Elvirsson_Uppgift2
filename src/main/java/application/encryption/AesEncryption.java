package application.encryption;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class AesEncryption {

    private static final String salt = "fakesalt"; // To do , fix individual salt per user!
    private static final String algorithm = "AES/CBC/PKCS5Padding";

    public static String encrypt (String password, String data) {



        try {
            byte[] iv = new byte[16];


            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, getKeyFromPassword(password), new IvParameterSpec(iv));
           byte[] encryptedData = cipher.doFinal(data.getBytes());
           return Base64.getEncoder().encodeToString(encryptedData);




        } catch (NoSuchAlgorithmException e) {
            System.out.println("Encrypt method" + e.getMessage());
            return null;

        } catch (NoSuchPaddingException e) {
            System.out.println("Encrypt method" + e.getMessage());

            return null;
        } catch (InvalidKeySpecException e) {
            System.out.println("Encrypt method" + e.getMessage());

        } catch (InvalidKeyException e) {
            System.out.println("Encrypt method" + e.getMessage());

        } catch (IllegalBlockSizeException e) {
            System.out.println("Encrypt method" + e.getMessage());

        } catch (BadPaddingException e) {
            System.out.println("Encrypt method" + e.getMessage());

        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public static String decrypt(String password, String data) {

        try {
            byte[] iv = new byte[16];

            byte[] decodedData = Base64.getDecoder().decode(data);


            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, getKeyFromPassword(password),new IvParameterSpec(iv));
            byte[] decryptedData = cipher.doFinal(decodedData);
            return new String(decryptedData);



        } catch (NoSuchAlgorithmException e) {
            System.out.println("Encrypt method" + e.getMessage());
            return null;

        } catch (NoSuchPaddingException e) {
            System.out.println("Encrypt method" + e.getMessage());

            return null;
        } catch (InvalidKeySpecException e) {
            System.out.println("Encrypt method" + e.getMessage());

        } catch (InvalidKeyException e) {
            System.out.println("Encrypt method" + e.getMessage());

        } catch (IllegalBlockSizeException e) {
            System.out.println("Encrypt method" + e.getMessage());

        } catch (BadPaddingException e) {
            System.out.println("Encrypt method" + e.getMessage());

        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }

        return null;


    }
    private static SecretKey getKeyFromPassword(String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec)
                .getEncoded(), "AES");
        return secret;
    }
}
