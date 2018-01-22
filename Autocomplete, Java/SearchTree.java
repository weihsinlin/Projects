
/**
 * Created by Philip on 8/2/17.
 */
public class SearchTree {

    STNode root;


    public SearchTree(Node prefixNode) {
        root = new STNode(prefixNode);
    }

    public void print() {
        root.print(0);
    }



}
