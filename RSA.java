
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import javax.crypto.Cipher;

public class RSA {
   public static String encrypt(Block block, PublicKey publicKey) {
        String encryptedBlock = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encryptedBlock = Base64.getEncoder().encodeToString(cipher.doFinal(block.toString().getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedBlock;
    }
   
   
      public static String encrypt(Transaction transaction, PublicKey publicKey) {
        String encryptedBlock = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encryptedBlock = Base64.getEncoder().encodeToString(cipher.doFinal(transaction.toString().getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedBlock;
    }
     
     

   public static String decrypt(String blockCrypt, PrivateKey privateKey) {
        String decryptedBlock = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            decryptedBlock = new String(cipher.doFinal(Base64.getDecoder().decode(blockCrypt)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedBlock;
    }

}