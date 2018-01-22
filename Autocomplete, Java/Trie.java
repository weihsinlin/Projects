
//import java.util.HashMap;
//import java.util.Map;
//import java.util.PriorityQueue;
import java.util.List;
/**
 * Prefix-Trie. Supports linear time find() and insert().
 * Should support determining whether a word is a full word in the
 * Trie or a prefix.
 *
 * @author
 */
public class Trie {


    private Node root = new Node();

    public Node getRoot() {
        return root;
    }

//    private class Node {
//        boolean exists;
//        Map<Character, Node> links = new HashMap<>();
//        double weight, maxWeight;
////        PriorityQueue<Character> orderQ
////                = new PriorityQueue<>((o1, o2) ->
////                (int) Math.signum(links.get(o2).maxWeight - links.get(o1).maxWeight));
//
//
//        public void print(int k) {
//            for (Character key : links.keySet()) {
//                for (int i = 0; i < k; i++) {
//                    System.out.print("   ");
//                }
//                System.out.println(key + " Max Weight = "
//                        + links.get(key).maxWeight + " Weight = " + links.get(key).weight);
////                System.out.println(key);
//                links.get(key).print(k + 1);
//            }
//        }
//
//        public void sortedPrint(String prev, List<Character> order) {
//            if (exists) {
//                System.out.println(prev);
//            }
//            for (Character c : order) {
//                if (links.get(c) != null) {
//                    links.get(c).sortedPrint(prev + c, order);
//                }
//            }
//        }
//
////        private void addToQueue(char c) {
////            orderQ.add(c);
////        }
//    }

    public void sortedPrint(List<Character> order) {
        root.sortedPrint("", order);
    }

    public void print() {
        root.print(0);
    }


    public boolean find(String s, boolean isFullWord) {
        return findHelper(s, isFullWord, 0, root);
    }

    private boolean findHelper(String s, boolean isFullWord, int index, Node n) {

        if (index == s.length()) {
            if (isFullWord) {
                return n.exists;
            } else {
                return true;
            }
        }
        char c = s.charAt(index);
        if (n.links.containsKey(c)) {
            return findHelper(s, isFullWord, index + 1, n.links.get(c));
        } else {
            return false;
        }
    }

    private double findWeightHelper(String s, int index, Node n) {

        if (index == s.length()) {
            if (n.exists) {
                return n.weight;
            } else {
                return 0.0;
            }
        }
        char c = s.charAt(index);
        if (n.links.containsKey(c)) {
            return findWeightHelper(s, index + 1, n.links.get(c));
        } else {
            return 0.0;
        }
    }

    public double findWeigit(String s) {
        return findWeightHelper(s, 0, root);
    }

    public void insert(String s) {
        if (s.isEmpty() || s == null) {
            throw new IllegalArgumentException("yo, bad arg");
        }

        insertHelper(root, s, 0, 0);
    }

//    private void insertHelper(Node n, String key, int k) {
//
//        if (k == key.length()) {
//            n.exists = true;
//            return;
//        }
//        char c = key.charAt(k);
//        Node child;
//        if (!n.links.containsKey(c)) {
//            child = new Node();
//            n.addToQueue(c);
//        } else {
//            child = n.links.get(c);
//        }
//        n.links.put(c, child);
//        insertHelper(child, key, k + 1);
//    }

    public void insert(String s, double weight) {
        if (s.isEmpty() || s == null) {
            throw new IllegalArgumentException("yo, bad arg");
        }
        insertHelper(root, s, 0, weight);
    }

    private void insertHelper(Node n, String key, int k, double weight) {
        boolean addOrNot = false;
        if (k == key.length()) {
            n.exists = true;
            n.weight = weight;
            n.maxWeight = Math.max(weight, n.maxWeight);
            return;
        }
        char c = key.charAt(k);
        Node child;
        if (!n.links.containsKey(c)) {
            child = new Node();
            child.setMe(c);
            addOrNot = true;
        } else {
            child = n.links.get(c);
        }
        n.maxWeight = Math.max(weight, n.maxWeight);
        n.links.put(c, child);
//        if (addOrNot) {
//            n.addToQueue(c);
//        }
        insertHelper(child, key, k + 1, weight);
    }

    public Node findPrefixNode(String prefix) {
        char[] p = prefix.toCharArray();
        Node curr = root;
        for (int i = 0; i < p.length; i++) {
            if (curr != null && curr.getLinks().containsKey(p[i])) {
                curr = curr.getLinks().get(p[i]);
            } else {
                return null;
            }
        }
        return curr;
    }
}
