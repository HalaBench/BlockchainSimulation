import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import com.google.common.hash.Hashing;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Block implements Serializable {

    public static String index;
    public static int Version;
    public static long timestamp;
    public String HashPrevBlock="0";
    public static int Nonce;
    public static String previousHash; // previous hash in the blockchain so that we can link the blocks together
    public static String Target;
    public String MerkleRoot;
    public String input;
    public int id;
    public String Sender;

    //arraylist of transactions
    ArrayList<Transaction> transactions = new ArrayList<Transaction>();



    public Block(int id, String Sender) {
        this.id = id;
        this.Sender = Sender;

    }


    public Block(long timestamp, String Target) {
        this.index = calculateHash(this);
        this.timestamp = timestamp;
        this.previousHash = "0";
        this.Target = Target;
        Nonce = 0;
        HashPrevBlock = Block.calculateHash(this);
    }


    public String getIndex() {
        return index;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String str() {
        input = index + previousHash + timestamp + Nonce + Version + Target;
        return input;
    }

    public static String calculateHash(Block block) {
        String hash = Hashing.sha256().hashString(block.str(), StandardCharsets.UTF_8).toString();
        return hash;
    }



    public String MekleTree(){
        return Merkletree.merkleTree(transactions, 0, transactions.size()-1);

    }

    //add transaction to block
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }



    public String HashHeaderBlock() {
        return null;
    }


    public String toString() {
        String str = "Block: " + index + "\n";
        str += "Block Hash: " + HashPrevBlock + "\n";
        str += "Block Previous Hash: " + previousHash + "\n";
        str += "Block Timestamp: " + timestamp + "\n";
        str += "Block Nonce: " + Nonce + "\n";
        str += "Block Version: " + Version + "\n";
        str += "Block Target: " + Target + "\n";
        str += "Block MerkleRoot: " + MerkleRoot + "\n";
        str += "Block Transactions: " + transactions + "\n" + "\n" + "\n" + "END OF BLOCK" + "\n" + "\n" + "\n";
        return str;
    }


    //proof of work
    //and is false
    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0"
        while (!HashPrevBlock.substring(0, difficulty).equals(target)) {
            Nonce++;
            HashPrevBlock = calculateHash(this);
        }
        System.out.println("Block Mined!!! : " + HashPrevBlock);
    }
}
