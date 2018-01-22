import ucb.junit.textui;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/** The suite of all JUnit tests for the Autocomplete class.
 *  @author
 */
public class TestAutocomplete {

    /** A dummy test to avoid complaint. */


    @Test
    public void topMatch() {
        String[] ss = {"a"};
        double[] dd = {100};
        Autocomplete a = new Autocomplete(ss, dd);
        assertEquals(a.topMatch("a"), "a");
        assertEquals(a.topMatch(""), "a");
        assertNull(a.topMatch("h"));
    }

    @Test
    public void topMatch0() {
        String[] ss = {"abc"};
        double[] dd = {100};
        Autocomplete a = new Autocomplete(ss, dd);
        assertEquals(a.topMatch("a"), "abc");
        assertEquals(a.topMatch(""), "abc");
        assertEquals(a.topMatch("abc"), "abc");
        assertNull(a.topMatch("h"));
        assertNull(a.topMatch("abcde"));
    }

    @Test
    public void topMatch1() {
        String[] ss = {"a", "ab", "abc"};
        double[] dd = {100, 200, 50};
        Autocomplete a = new Autocomplete(ss, dd);
        assertEquals(a.topMatch(""), "ab");
        assertEquals(a.topMatch("a"), "ab");
        assertEquals(a.topMatch("abc"), "abc");
        assertNull(a.topMatch("b"));
        assertNull(a.topMatch("c"));
        assertNull(a.topMatch("abcd"));
    }

    @Test
    public void topMatch2() {
        String[] ss = {"a", "b", "c", "ax"};
        double[] dd = {0.4, 0.2, 0.3, 0.4};
        Autocomplete a = new Autocomplete(ss, dd);
        assertEquals(a.topMatch(""), "ax");
        assertEquals(a.topMatch("a"), "ax");
        assertEquals(a.topMatch("b"), "b");
        assertEquals(a.topMatch("c"), "c");
        assertEquals(a.topMatch("ax"), "ax");
        assertNull(a.topMatch("abc"));
        assertNull(a.topMatch("abcd"));
    }

    @Test
    public void topMatch3() {
        String[] ss = {"a", "b", "c", "ax"};
        double[] dd = {0, 1, 1, 0};
        Autocomplete a = new Autocomplete(ss, dd);

        assertEquals(a.topMatch(""), "c");
        assertEquals(a.topMatch("a"), "ax");
        assertEquals(a.topMatch("ax"), "ax");
        assertEquals(a.topMatch("c"), "c");

        assertNull(a.topMatch("e"));
        assertNull(a.topMatch("abcd"));
    }

    @Test
    public void topMatch4() {
        String[] ss = {"a", "ab", "abc", "abcdg", "abef"};
        double[] dd = {3, 2, 0, 5, 0};
        Autocomplete a = new Autocomplete(ss, dd);

        assertEquals(a.topMatch(""), "abcdg");
        assertEquals(a.topMatch("abe"), "abef");
        assertEquals(a.topMatch("abcd"), "abcdg");

        assertNull(a.topMatch("e"));

    }

    @Test
    public void topMatch5() throws Exception {
        String[] ss = {};
        double[] dd = {};
        Autocomplete a = new Autocomplete(ss, dd);

        assertNull(a.topMatch("a"));
//        assertNull(a.topMatch(""));

    }

    @Test
    public void topMatch6() throws Exception {
        String[] ss = {"ab", "cd", "ef", "c"};
        double[] dd = {2, 3, 4, 3.5};
        Autocomplete a = new Autocomplete(ss, dd);

        assertEquals(a.topMatch(""), "ef");
        assertEquals(a.topMatch("a"), "ab");
        assertEquals(a.topMatch("cd"), "cd");
        assertEquals(a.topMatch("c"), "c");
    }

    @Test
    public void topMatch7() throws Exception {
        String[] ss = {"a", "ab", "abxc", "abxcd", "abxcde", "abxcdef", "abxcdefg", "abxcyz"};
        double[] dd = {1, 8, 2, 6, 3, 5, 4, 7};
        Autocomplete a = new Autocomplete(ss, dd);

        assertEquals(a.topMatch(""), "ab");
        assertEquals(a.topMatch("abxc"), "abxcyz");
        assertEquals(a.topMatch("abxcde"), "abxcdef");
        assertEquals(a.topMatch("abxcdefg"), "abxcdefg");
        assertEquals(a.topMatch("abxcy"), "abxcyz");
        assertEquals(a.topMatch(""), "ab");
        assertEquals(a.topMatch("abxc"), "abxcyz");
        assertEquals(a.topMatch("abxcde"), "abxcdef");
        assertEquals(a.topMatch("abxcdefg"), "abxcdefg");
        assertEquals(a.topMatch("abxcy"), "abxcyz");
    }

    @Test
    public void checkExceptions1() throws Exception {
        String[] ss = {"a", "b"};
        double[] dd = {1, 2, 3};
        try {
            Autocomplete a = new Autocomplete(ss, dd);
        } catch (IllegalArgumentException e) {
            return;
        }
        assertFalse(true);
    }

    @Test
    public void checkExceptions2() throws Exception {
        String[] ss = {"a", "b", "b"};
        double[] dd = {1, 2, 3};
        try {
            Autocomplete a = new Autocomplete(ss, dd);
        } catch (IllegalArgumentException e) {
            return;
        }
        assertFalse(true);
    }

    @Test
    public void checkExceptions3() throws Exception {
        String[] ss = {"a", "b", "b"};
        double[] dd = {-1, 2, 3};
        try {
            Autocomplete a = new Autocomplete(ss, dd);
        } catch (IllegalArgumentException e) {
            return;
        }
        assertFalse(true);
    }

    @Test
    public void checkExceptions4() throws Exception {
        String[] ss = {"a", "b", "c"};
        double[] dd = {1, 2, 3};
        try {
            Autocomplete a = new Autocomplete(ss, dd);
            a.topMatches("c", 0);
        } catch (IllegalArgumentException e) {
            return;
        }
        assertFalse(true);
    }

    @Test
    public void topMatches() throws Exception {
        String[] ss = {"a"};
        double[] dd = {100};
        Autocomplete a = new Autocomplete(ss, dd);
        ArrayList<String> result1 = new ArrayList<>();
        assertTrue(a.topMatches("h", 9).equals(result1));
        ArrayList<String> result2 = new ArrayList<>();
        result2.add("a");
        assertTrue(a.topMatches("a", 1).equals(result2));
        assertTrue(a.topMatches("a", 5).equals(result2));

    }

    @Test
    public void topMatches0() {
        String[] ss = {"a", "ab", "abc"};
        double[] dd = {100, 200, 50};
        Autocomplete a = new Autocomplete(ss, dd);
        ArrayList<String> resultEmpty = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();
        String[] ordered = {"ab", "a", "abc"};
        for (String s : ordered) {
            result.add(s);
        }
        assertEquals(a.topMatches("", 3), result);
        assertEquals(a.topMatches("", 100), result);
        assertEquals(a.topMatches("a", 3), result);
        assertEquals(a.topMatches("a", 5), result);

        result.remove("abc");
        assertEquals(a.topMatches("", 2), result);
        assertEquals(a.topMatches("a", 2), result);

        assertEquals(a.topMatches("b", 2), resultEmpty);
        assertEquals(a.topMatches("bc", 2), resultEmpty);
        assertEquals(a.topMatches("abcd", 2), resultEmpty);
        assertEquals(a.topMatches("d", 2), resultEmpty);

    }

    @Test
    public void topMatches1() throws Exception {
        String[] ss = {"hi", "hit", "hello", "he", "hey", "good"};
        double[] dd = {100, 20, 50.01, 500, 49, 80};
        Autocomplete a = new Autocomplete(ss, dd);
        ArrayList<String> resultEmpty = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();


    }

    @Test
    public void topMatches2() throws Exception {
        String[] ss = {"a", "ab", "abc", "abcdg", "abef"};
        double[] dd = {3, 2, 0, 5, 0};
        Autocomplete a = new Autocomplete(ss, dd);

        ArrayList<String> resultEmpty = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();
        String[] ordered = {"abcdg", "abc"};
        for (String s : ordered) {
            result.add(s);
        }
        assertEquals(result, a.topMatches("abc", 2));
        result.clear();
        String[] ordered1 = {"abcdg", "ab", "abef", "abc"};
        for (String s : ordered1) {
            result.add(s);
        }
        assertEquals(result, a.topMatches("ab", 6));

    }




    @Test
    public void weightOf() throws Exception {
        String[] ss = {"hi", "hit", "hello", "he", "hey", "good"};
        double[] dd = {100, 99, 50.01, 50, 49, 80};
        Autocomplete a = new Autocomplete(ss, dd);
        assertTrue(a.weightOf("h") == 0.0);
        assertTrue(a.weightOf("hi") == 100.0);
        assertTrue(a.weightOf("hello") == 50.01);
        assertTrue(a.weightOf("goodddddd") == 0);

    }

    /** Run the JUnit tests above. */
    public static void main(String[] ignored) {
        textui.runClasses(TestAutocomplete.class);
    }
}
