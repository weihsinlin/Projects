
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.List;


/**
 * Implements autocomplete on prefixes for a given dictionary of terms and weights.
 *
 * @author
 */
public class Autocomplete {
    /**
     * Initializes required data structures from parallel arrays.
     *
     * @param terms Array of terms.
     * @param weights Array of weights.
     */
    Trie dict = new Trie();
    public Autocomplete(String[] terms, double[] weights) {
        /* YOUR CODE HERE. */

        if (terms.length != weights.length) {
            throw new IllegalArgumentException("Yo! Two arrays must have same length. ");
        }

        Set<String> termsSet = new HashSet<>(Arrays.asList(terms));
        if (terms.length != termsSet.size()) {
            throw new IllegalArgumentException("Yo! Duplicated input. ");
        }

        for (int i = 0; i < terms.length; i++) {
            if (weights[i] < 0) {
                throw new IllegalArgumentException("Yo! You got a negative weight. ");
            }
            dict.insert(terms[i], weights[i]);
        }
    }

    /**
     * Find the weight of a given term. If it is not in the dictionary, return 0.0
     *
     * @param term
     * @return
     */
    public double weightOf(String term) {
        /* YOUR CODE HERE. */
        return dict.findWeigit(term);
    }

    /**
     * Return the top match for given prefix, or null if there is no matching term.
     * @param prefix Input prefix to match against.
     * @return Best (highest weight) matching string in the dictionary.
     */
    public String topMatch(String prefix) {

//        Node prefixNode = dict.findPrefixNode(prefix);
//
//        if (prefixNode == null) {
//            return null;
//        }
//
//        Node currNode = prefixNode;
//        double targetWeight = currNode.maxWeight;
//        char c;
//        String rtn = prefix;
//        while (!(currNode.weight == targetWeight && currNode.largestWeight() != targetWeight)) {
//            c = currNode.heaviestChar();
//            rtn += c;
//            currNode = currNode.getLinks().get(c);
//        }
//        return rtn;

        ArrayList<String> rtn = (ArrayList<String>) topMatches(prefix, 5);
        if (rtn.size() == 0) {
            return null;
        }
        double targetWeight = weightOf(rtn.get(0));
        List<String> matches = rtn.stream().filter(x -> weightOf(x) == targetWeight)
                .sorted().collect(Collectors.toList());
        return matches.get(matches.size() / 2);
    }

    /**
     * Returns the top k matching terms (in descending order of weight) as an iterable.
     * If there are less than k matches, return all the matching terms.
     *
     * @param prefix
     * @param k
     * @return
     */
    public Iterable<String> topMatches(String prefix, int k) {
        if (k <= 0) {
            throw new IllegalArgumentException("Yo! top k must be a positive integer. ");
        }

        Node prefixNode = dict.findPrefixNode(prefix);
        ArrayList<String> words = new ArrayList<>();

        if (prefixNode == null) {
            return words;
        }
        SearchTree st = new SearchTree(prefixNode);
        for (int i = 0; i < k; i++) {
            String match = nextTopMatch(prefix, st);
            if (match == null) {
                return words;
            }
            words.add(match);
        }

        return words;
    }

    private String nextTopMatch(String prefix, SearchTree st) {
        Node currNode = st.root.n;
        STNode currSTNode = st.root;
//        st.print();
        double targetWeight = currSTNode.maxW;
        if (targetWeight == -1) {
            return null;
        }
        char c;
        String rtn = prefix;
        while (currNode.weight != targetWeight || currSTNode.visited) {
            c = currSTNode.pq.peek();
            rtn += c;
            currNode = currNode.getLinks().get(c);

            STNode temp = currSTNode.getChild(c);
            if (temp == null) {
                temp = new STNode(currNode);
                currSTNode.addChild(temp);
            }

            currSTNode = temp;
        }

        // Update maxweights back
        currSTNode.visited = true;
        currSTNode.maxW = -1;
        currSTNode.updateMaxW();
        while (currSTNode.parent != null) {
            currSTNode = currSTNode.parent;
            currSTNode.updateMaxW();
            c = currSTNode.pq.poll();
            currSTNode.pq.add(c);
        }



        return rtn;
    }

    /**
     * Test client. Reads the data from the file, then repeatedly reads autocomplete
     * queries from standard input and prints out the top k matching terms.
     *
     * @param args takes the name of an input file and an integer k as
     *             command-line arguments
     */
    public static void main(String[] args) {
        // initialize autocomplete data structure
        In in = new In(args[0]);
        int N = in.readInt();
        String[] terms = new String[N];
        double[] weights = new double[N];
        for (int i = 0; i < N; i++) {
            weights[i] = in.readDouble();   // read the next weight
            in.readChar();                  // scan past the tab
            terms[i] = in.readLine();       // read the next term
        }

        Autocomplete autocomplete = new Autocomplete(terms, weights);


        // process queries from standard input
        int k = Integer.parseInt(args[1]);
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            for (String term : autocomplete.topMatches(prefix, k)) {
                StdOut.printf("%14.1f  %s\n", autocomplete.weightOf(term), term);

            }
            System.out.println(autocomplete.topMatch(prefix));
        }
    }
}
