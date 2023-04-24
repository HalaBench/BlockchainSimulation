import com.google.common.hash.Hashing;

import java.security.Signature;
import java.sql.Timestamp;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.*;

public class Transaction implements Serializable {

    private String transactionId; //hash of transaction
    private PublicKey sender; //sender's address
    private PublicKey receiver; //receiver's address
    private Timestamp timestamp;
    private byte[] signature; //signature to prevent anyone else from spending funds in our wallet







    //Constructor
    public Transaction(PublicKey sender, PublicKey receiver,  Timestamp timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
        this.transactionId = calculateHash();
    }


    //inputs and outputs
    public ArrayList<InputTransaction> inputTransaction = new ArrayList<>();
    public ArrayList<TransactionOutput> outputTransaction = new ArrayList<>();


    //addInput
    public void addInput(InputTransaction input) {
        inputTransaction.add(input);
    }

    //addOutput
    public void addOutput(TransactionOutput output) {
        outputTransaction.add(output);
    }


    public Transaction(String id, Timestamp datetime) {

        this.transactionId = id;
        this.timestamp = datetime;
    }


    public String calculateHash() {
        String hash = Hashing.sha256().hashString(transactionId + sender + receiver  + timestamp + signature, StandardCharsets.UTF_8).toString();
        return hash;
    }


    // defining getKeyPair method
    public PrivateKey getPrivetKey(Wallet W2) {
        return W2.getPrivetKey();

    }

    public PublicKey getPublicKey(Wallet W2) {
        return W2.getPublicKey();
    }


    //generateSignature
    public byte[] generateSignature(PrivateKey privateKey) {
        String data = StringUtils.join(Arrays.asList(sender, receiver), "");
        byte[] dataHash = data.getBytes();
        Signature dsa;
        byte[] realSig = null;
        try {
            dsa = Signature.getInstance("SHA256withRSA");
            dsa.initSign(privateKey);
            dsa.update(dataHash);
            realSig = dsa.sign();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return realSig;
    }


    //set receiver
    public void setReceiver(PublicKey receiver) {
        this.receiver = receiver;
    }


    //getInputTransaction
    public ArrayList<InputTransaction> getInputTransaction() {
        return inputTransaction;
    }

    //getOutputTransaction
    public ArrayList<TransactionOutput> getOutputTransaction() {
        return outputTransaction;
    }


    //get transactionId
    public String getTransactionId() {
        return transactionId;
    }

    //set signature
    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    //get signature
    public byte[] getSignature() {
        return signature;
    }


    public String toString() {

        String mes = "Transaction{" + "Id_transaction=" + transactionId + ",time" + timestamp + '}';
        return mes;
    }
}
