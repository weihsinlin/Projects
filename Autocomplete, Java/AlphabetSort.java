import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * AlphabetSort takes input from stdin and prints to stdout.
 * The first line of input is the alphabet permutation.
 * The the remaining lines are the words to be sorted.
 * 
 * The output should be the sorted words, each on its own line, 
 * printed to std out.
 */
public class AlphabetSort {

    /**
     * Reads input from standard input and prints out the input words in
     * alphabetical order.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        String s;
        Trie t = new Trie();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            String orderString = in.readLine();
            if (orderString == null) {
                throw new IllegalArgumentException("bad");
            }
            TreeSet<Character> test = new TreeSet<>();
            ArrayList<Character> order = new ArrayList<>();
            for (int i = 0; i < orderString.length(); i++) {
                order.add(orderString.charAt(i));
                test.add(orderString.charAt(i));
            }

            if (test.size() != order.size()) {
                throw new IllegalArgumentException("bad");
            }

            s = in.readLine();
            if (s == null) {
                throw new IllegalArgumentException("bad");
            }
            while (s != null) {
                t.insert(s);
                s = in.readLine();
            }
            t.sortedPrint(order);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
