import java.io.Serializable;

public class InputTransaction  implements Serializable {
    
     private String transactionId; //hash of transaction
    private TransactionOutput UTXO; //contains the unspent transaction output

     //constructor
    public InputTransaction(TransactionOutput UTXO, String transactionId) {
        this.UTXO = UTXO;
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public TransactionOutput getUTXO() {
        return UTXO;
    }

    public void setUTXO(TransactionOutput UTXO) {
        this.UTXO = UTXO;
    }
    
}
