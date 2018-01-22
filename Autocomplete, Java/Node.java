import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.PriorityQueue;

/**
 * Created by Philip on 8/2/17.
 */
public class Node {
    char me;
    boolean exists;
    Map<Character, Node> links = new HashMap<>();
    double weight = -1, maxWeight = -1;
//    PriorityQueue<Character> orderQ
//            = new PriorityQueue<>((o1, o2) ->
//            (int) Math.signum(links.get(o2).maxWeight - links.get(o1).maxWeight));


    public void print(int k) {
        for (Character key : links.keySet()) {
            for (int i = 0; i < k; i++) {
                System.out.print("   ");
            }
            System.out.println(key + " Max Weight = "
                    + links.get(key).maxWeight + " Weight = " + links.get(key).weight);
//                System.out.println(key);
            links.get(key).print(k + 1);
        }
    }

    public void sortedPrint(String prev, List<Character> order) {
        if (exists) {
            System.out.println(prev);
        }
        for (Character c : order) {
            if (links.get(c) != null) {
                links.get(c).sortedPrint(prev + c, order);
            }
        }
    }

    public Character heaviestChar() {
        double w = -1;
        Character rtn = null;
        for (Character c : links.keySet()) {
            if (links.get(c).maxWeight > w) {
                w = links.get(c).maxWeight;
                rtn = c;
            }
        }
        return rtn;
    }

    public double largestWeight() {
        double w = 0;
        for (Character c : links.keySet()) {
            if (links.get(c).maxWeight > w) {
                w = links.get(c).maxWeight;
            }
        }
        return w;
    }

    public Map<Character, Node> getLinks() {
        return links;
    }

    //        private void addToQueue(char c) {
//            orderQ.add(c);
//        }

    public void setMe(char me) {
        this.me = me;
    }
}
