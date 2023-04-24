import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Wallet {
    KeyPairGenerator keyPairGen;
    KeyPair pair;
    public void Keypairgenertor() throws NoSuchAlgorithmException {
        keyPairGen = KeyPairGenerator.getInstance("RSA");

        //Initializing the KeyPairGenerator
        keyPairGen.initialize(2048);

        //Generating the pair of keys
        pair = keyPairGen.generateKeyPair();
    }
    public PrivateKey getPrivetKey() {
        return pair.getPrivate();

    }
    public PublicKey getPublicKey() {
        return pair.getPublic();
    }
}
