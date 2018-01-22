import ucb.junit.textui;
import org.junit.Test;


import static org.junit.Assert.*;

/** The suite of all JUnit tests for the Trie class.
 *  @author
 */
public class TestTrie {

    @Test
    public void insert1() {
        Trie t = new Trie();
        t.insert("hi", 100);
        t.print();
    }

    @Test
    public void insert2() {
        Trie t = new Trie();
        t.insert("hi", 100);
        t.insert("hit", 20);
        t.print();
    }

    @Test
    public void insert3() {
        Trie t = new Trie();
        t.insert("hi", 100);
        t.insert("hit", 200);
        t.print();
    }

    @Test
    public void insert4() {
        Trie t = new Trie();
        t.insert("hi", 100);
        t.insert("he", 80);
        t.insert("hello", 50.01);
        t.insert("hey", 49);
        t.insert("goodbye", 20);
        t.print();
    }

    @Test
    public void find() throws Exception {
        Trie t = new Trie();
        t.insert("hello");
        t.insert("hey");
        t.insert("goodbye");
        assertTrue(t.find("hell", false));
        assertTrue(t.find("hello", true));
        assertTrue(t.find("good", false));
        assertFalse(t.find("bye", false));
        assertFalse(t.find("heyy", false));
        assertFalse(t.find("hell", true));
    }

    @Test
    public void findPrefixNode() throws Exception {
        Trie t = new Trie();
        t.insert("hello");
        t.insert("hey");
        t.insert("goodbye");
        t.findPrefixNode("he").print(0);

    }

    /** Run the JUnit tests above. */
    public static void main(String[] ignored) {
        textui.runClasses(TestTrie.class);
    }
}
