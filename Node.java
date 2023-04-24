import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.sql.Timestamp;
import java.util.ArrayList;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Node extends Agent {

    Gui gui;
    Wallet wallet;
    Timestamp timestamp;
    ArrayList node = new ArrayList();
    Map<String, PublicKey> publicKeys = new HashMap<String, PublicKey>();

    //block for each node
    Block block;

    int diff=1;

    //blockchain
    static ArrayList<Block> blockchain = new ArrayList<Block>();


    public void setup() {

        System.out.println("I'am the agent " + getLocalName());
        gui = new Gui(getLocalName(), this);
        gui.setVisible(true);


        Object[] args = getArguments();
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                node.add(args[i].toString());
            }

        }

        //create a wallet
        wallet = new Wallet();
        try {
            //generate key pair
            wallet.Keypairgenertor();

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }

        //send public key to all nodes
        for (int i = 0; i < node.size(); i++) {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(new AID(node.get(i).toString(), AID.ISLOCALNAME));
            msg.setLanguage("English");
            msg.setOntology("Wallet-ontology");
            try {
                msg.setContentObject(wallet.getPublicKey());
            } catch (IOException ex) {
                Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
            }
            send(msg);
        }

        addBehaviour(new ReceiveMsg());

    }



    public Block createBlock() {

        timestamp = new Timestamp(System.currentTimeMillis());
        block = new Block(timestamp.getTime(), "0000");
        System.out.println(block.toString());
        return block;
    }


    public void sendGenesisBlock() {


        for (int i = 0; i < node.size(); i++) {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(new AID(node.get(i).toString(), AID.ISLOCALNAME));
            try {
                msg.setContentObject(block);
            } catch (IOException ex) {
                System.err.print(ex);
            }
            send(msg);
        }
    }


    //send block to each node
    public void EnvoiBlock() throws IOException {

        for (int i = 0; i < node.size(); i++) {

            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(new AID(node.get(i).toString(), AID.ISLOCALNAME));
            msg.setContentObject((Serializable) block);
            send(msg);

        }

    }


    //send transaction to each node with putting input and output in the Transaction
    public void EnvoiTransaction() throws IOException {



        timestamp = new Timestamp(System.currentTimeMillis());
        Transaction transaction = new Transaction(wallet.getPublicKey(), null, timestamp);

        //TransactionOutput
        TransactionOutput transactionOutput1 = new TransactionOutput(wallet.getPublicKey(), 100, transaction.getTransactionId());
        TransactionOutput transactionOutput2 = new TransactionOutput(wallet.getPublicKey(), 25, transaction.getTransactionId());
        TransactionOutput transactionOutput3 = new TransactionOutput(wallet.getPublicKey(), 10, transaction.getTransactionId());

        //inputTransaction
        InputTransaction inputTransaction1 = new InputTransaction( transactionOutput1, transaction.getTransactionId());
        InputTransaction inputTransaction2 = new InputTransaction( transactionOutput2, transaction.getTransactionId());
        InputTransaction inputTransaction3 = new InputTransaction( transactionOutput3, transaction.getTransactionId());

        //add input and output to the transaction
        transaction.addInput(inputTransaction1);
        transaction.addInput(inputTransaction2);
        transaction.addInput(inputTransaction3);
        transaction.addOutput(transactionOutput1);
        transaction.addOutput(transactionOutput2);
        transaction.addOutput(transactionOutput3);


        System.out.println("Transaction input: " + transaction.getInputTransaction().toString());
        System.out.println("Transaction output: " + transaction.getOutputTransaction().toString());


        //sign the transaction
        transaction.setSignature(transaction.generateSignature(wallet.getPrivetKey()));

        //display the signature
        System.out.println("Signature: " + transaction.getSignature());


        //add transaction to the block
        this.block.addTransaction(transaction);
        System.out.println(block.transactions.size());

        // if the block is full mine it
        if (this.block.transactions.size() == 10) {
            System.out.println("block is full");
            this.block.HashPrevBlock = Block.calculateHash(this.block);
            //par rapport a la blockchain
            Block.previousHash = this.block.HashPrevBlock;
            block.MerkleRoot = Merkletree.merkleTree(block.transactions, 0, block.transactions.size() - 1);
            this.block.mineBlock(diff);
            blockchain.add(this.block);
            System.out.println("block is mined");
            System.out.println(block.toString());
        }








        for (int i = 0; i < node.size(); i++) {

            //encrypt transaction with public key of each node from the hashmap publicKeys
            //String encryptedTransaction = RSA.encrypt(transaction, publicKeys.get(node.get(i).toString()));
            //System.out.println(encryptedTransaction + "\n\n");

            transaction.setReceiver(publicKeys.get(node.get(i).toString()));
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(new AID(node.get(i).toString(), AID.ISLOCALNAME));
            msg.setContentObject((Serializable) transaction);
            send(msg);



        }

    }




    //listening
    public class ReceiveMsg extends CyclicBehaviour {

        @Override
        public void action() {

            ACLMessage msgRecu;
            msgRecu = receive();
            if (msgRecu != null) {

                try {
                    if (((ACLMessage) msgRecu).getContentObject() instanceof Block) {
                        Block b1;
                        b1 = (Block) ((ACLMessage) msgRecu).getContentObject();

                        String mess = "\n" + getLocalName() + " : i've received the message " + b1 + " from the agent " + msgRecu.getSender().getLocalName();
                        gui.jTextArea3.append(mess);
                        gui.jTextArea1.append(blockchain.toString() + "\n");
                        //System.out.println(blockchain);
                        diff++;
                    }
                    if (((ACLMessage) msgRecu).getContentObject() instanceof Transaction) {
                        Transaction T1;
                        T1 = (Transaction) ((ACLMessage) msgRecu).getContentObject();

                        String mess = "\n" + getLocalName() + " : i've received the message  " + T1 + " from the agent " + msgRecu.getSender().getLocalName();

                        gui.jTextArea4.append(mess);
                    }
                    if (((ACLMessage) msgRecu).getContentObject() instanceof PublicKey) {
                        PublicKey p1;
                        p1 = (PublicKey) ((ACLMessage) msgRecu).getContentObject();
                        String agentName = msgRecu.getSender().getLocalName();
                        publicKeys.put(agentName, p1);
                        String mess = "\n" + getLocalName() + " : i've received the message  " + p1 + " from the agent " + msgRecu.getSender().getLocalName();

                        gui.jTextArea2.append(mess);
                    }
                } catch (UnreadableException e) {

                    e.printStackTrace();
                }

            }
        }
    }

}
