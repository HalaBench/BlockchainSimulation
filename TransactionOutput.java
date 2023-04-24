
import com.google.common.hash.Hashing;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;


public class TransactionOutput implements Serializable {
    
      private String id;
    private PublicKey reciepient; //new owner of these coins.
    private float value; //the amount of coins they own
    private String parentTransactionId; //the id of the transaction this output was created in

    //Constructor
    public TransactionOutput(PublicKey reciepient, float value, String parentTransactionId) {
        this.reciepient = reciepient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = calculateHash();
    }

    //Calculate the transaction hash (which will be used as its Id)
    
      private String calculateHash(){
         
        return Hashing.sha256().hashString(parentTransactionId+reciepient+value, StandardCharsets.UTF_8).toString();
        
    }
    
     

    //Check if coin belongs to you
    public boolean isMine(PublicKey publicKey) {
        return (publicKey == reciepient);
    }

    //Getters
    public String getId() {
        return id;
    }

    public PublicKey getReciepient() {
        return reciepient;
    }

    public float getValue() {
        return value;
    }

    public String getParentTransactionId() {
        return parentTransactionId;
    }
    
}
