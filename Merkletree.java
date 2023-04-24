
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

import com.google.common.hash.Hashing;

public class Merkletree {
    static List<Transaction> data = new ArrayList<>() ;
    public static String calculateHash(String blo) {
        String hash = Hashing.sha256().hashString(blo, StandardCharsets.UTF_8).toString();
        return hash;
    }
    public static String merkleTree(List<Transaction> file , int low , int high) {
        if(low==high) {
            String content= String.valueOf(file.get(low));
            String hashcode=calculateHash(content);
            return hashcode;
        }
        if (data.size() % 2 != 0) {
            Transaction content = data.get(data.size() - 1);
            data.add(data.size(), content);
        }
        int mid=low+(high-low)/2;
        String leftHashCode=merkleTree(file, low ,mid);
        String rightHashCode=merkleTree(file,mid+1 , high);

        return calculateHash(leftHashCode+""+rightHashCode);

    }

    public static void main(String[] args) {


        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        data.add(new Transaction("0",timestamp));
        data.add(new Transaction("1",timestamp));
        data.add(new Transaction("2",timestamp));
        data.add(new Transaction("3",timestamp));
        data.add(new Transaction("4",timestamp));

        String rootMerkle=merkleTree(data,0, data.size()-1);
        System.out.println(rootMerkle);

    }
}