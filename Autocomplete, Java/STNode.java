import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Created by Philip on 8/2/17.
 */
public class STNode {
    Node n;
    PriorityQueue<Character> pq;
    double maxW;
    ArrayList<STNode> children = new ArrayList<>();
    STNode parent;
    boolean visited;
    HashMap<Character, Double> charToMaxW = new HashMap<>();

    STNode(Node n) {
        this.n = n;
        this.maxW = n.maxWeight;
        for (char c : n.getLinks().keySet()) {
            charToMaxW.put(c, n.getLinks().get(c).maxWeight);
        }
        this.pq = new PriorityQueue<>((o1, o2) ->
                (int) Math.signum(charToMaxW.get(o2) - charToMaxW.get(o1)));
        for (char c : n.getLinks().keySet()) {
            pq.add(c);
        }
    }

    void addChild(STNode stn) {
        children.add(stn);
        stn.parent = this;
    }

    public STNode getChild(char c) {
        for (STNode child : children) {
            if (child.n.me == c) {
                return child;
            }
        }
        return null;
    }


    public STNode getParent() {
        return parent;
    }


    // change maxW to max(maxW of charToMaxW)
    public void updateMaxW() {
        double w = -1;
        for (char c : charToMaxW.keySet()) {
            if (charToMaxW.get(c) > w) {
                w = charToMaxW.get(c);
            }
        }

        if (visited) {
            maxW = w;
        } else {
            maxW = Math.max(w, n.weight);
        }
        if (parent != null) {
            parent.charToMaxW.put(n.me, maxW);
        }
    }


    public void print(int k) {
        for (int i = 0; i < k; i++) {
            System.out.print("   ");
        }
        System.out.println("Char=" + n.me + " SearchMax=" + maxW);

        for (STNode c : children) {

//            System.out.println(key + " Max Weight = "
//                    + links.get(key).maxWeight + " Weight = " + links.get(key).weight);
//                System.out.println(key);
            c.print(k + 1);
        }
    }
}
