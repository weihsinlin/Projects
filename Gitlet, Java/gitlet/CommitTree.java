package gitlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Created by LiwenKo on 7/15/17.
 */
public class CommitTree  implements Serializable {
    private Node root;
    private TreeMap<String, Node> branches = new TreeMap();
    private String head;


    public CommitTree() {
        root = null;
    }

    public CommitTree(String commitID) {
        root = new Node(commitID);

    }

    public Node findNode(String commitID) {
        if (root != null) {
            return root.findNode(commitID);
        }
        return null;
    }

    public String findSplitPointID(String branchName) {
        Node branchNode = branches.get(branchName);
        Node headNode = branches.get(head);
        return branchNode.findSplitNode(headNode).commitID;

    }

    public void addBranch(String branchName) {
        if (branches.containsKey(branchName)) {
            System.out.println("A branch with that name already exists.");
        } else {
            Node headNode = branches.get(head);
            branches.put(branchName, headNode);
        }
    }

    public void addBranch(String branchName, Node branch) {
        branches.put(branchName, branch);
    }

    public void addChild(String commitID) {
        Node tmp = new Node(commitID, branches.get(head));
        branches.put(head, tmp);
    }

    public void setHead(String branchName) {
        if (branches.containsKey(branchName)) {
            head = branchName;
        }
    }

    public void setHead(String branchName, Commit c) {
        String commitID = c.getCommitID();
        branches.put(branchName, findNode(commitID));
        head = branchName;
    }

    public void removeBranch(String branchName) {
        if (branches.containsKey(branchName)) {
            branches.remove(branchName);
        }
    }

    public String getHeadNodeID() {
        return branches.get(head).commitID;
    }

    public Node getRoot() {
        return root;
    }

    public TreeMap<String, Node> getBranches() {
        return branches;
    }

    public String getHead() {
        return head;
    }

    public void print() {
        if (root != null) {
            root.print();
        }
    }


    public void printGlobalLog() {
        if (root != null) {
            root.printGlobalLog();
        }
    }

    public void printLog() {
        branches.get(head).printLog();
    }

    public ArrayList<String> findCommitID(String message) {
        if (root != null) {
            return root.findCommitID(message);
        }
        return new ArrayList<>();
    }

    public void printBranches() {
        List l = (List) branches.keySet().stream().sorted().map(x -> helper(x))
                .collect(Collectors.toList());

        for (Object k: l) {
            System.out.println(k);
        }
    }

    public Object helper(Object x) {
        if (head.equals(x)) {
            String result = "*" + x;
            return result;
        }
        return x;
    }

    public ArrayList<String> getAllCommitID() {
        if (root != null) {
            return root.getAllCommitID();
        }
        return null;
    }

    public String getBranchCommitID(String branchName) {
        return branches.get(branchName).commitID;
    }

    public class Node implements Serializable {
        private Node parent;
        private String commitID;
        private ArrayList<Node> children = new ArrayList<>();

        public Node(String commitID) { // initialize
            this.commitID = commitID;
        }

        public Node(String commitID, Node parent) {
            this.commitID = commitID;
            this.parent = parent;
            parent.children.add(this);
        }

        public void print() {
            System.out.println(commitID);
            for (Node n : children) {
                n.print();
            }
        }

        public void printLog() {
            Commit c = this.getCommit();
            c.print();

            if (parent != null) {
                parent.printLog();
            }

        }

        public void printGlobalLog() {
            Commit c = MyFunc.getCommit(commitID);
            c.print();

            for (Node n : children) {
                n.printGlobalLog();
            }
        }


        public ArrayList<String> findCommitID(String message) {
            ArrayList<String> result = new ArrayList<>();
            Commit thisCommit = MyFunc.getCommit(commitID);
            if (thisCommit.getDescription().equals(message)) {
                result.add(commitID);
            }
            for (Node n : children) {
                result.addAll(n.findCommitID(message));
            }

            return result;
        }

        public Commit getCommit() {
            return MyFunc.getCommit(this.commitID);
        }

        public ArrayList<String> getAllCommitID() {
            ArrayList<String> result = new ArrayList<>();
            result.add(commitID);
            for (Node n : children) {
                result.addAll(n.getAllCommitID());
            }
            return result;
        }

        public Node findNode(String cID) {
            if (this.commitID.equals(cID)) {
                return this;
            } else {
                for (Node n : children) {
                    if (n.findNode(cID) != null) {
                        return n.findNode(cID);
                    }
                }
            }
            return null;
        }


        public Node findSplitNode(Node n) {
            if (contains(n)) {
                return this;
            } else {
                return parent.findSplitNode(n);
            }
        }


        public boolean contains(Node n) {
            if (this == n) {
                return true;
            } else {
                boolean rtn = false;
                for (Node cn : children) {
                    rtn = rtn || cn.contains(n);
                }
                return rtn;
            }
        }
//        public boolean findNodeHelper(String commitID) {
//            if (ID.equals(commitID)) {
//                return true;
//            } else {
//                boolean rtn = false;
//                for (Node n : children) {
//                    rtn = rtn || n.findNodeHelper(commitID);
//                }
//                return rtn;
//            }
//        }

    }

    public static void main(String[] args) {
        CommitTree t = new CommitTree("a");
        t.head = "master";
        t.addBranch("master", t.root);
        t.addChild("b");
        t.addBranch("dg");
        t.addChild("c");
        t.addBranch("fh");
        t.addChild("e");
        t.setHead("fh");
        t.addChild("f");
        t.addChild("h");
        t.setHead("dg");
        t.addChild("d");
        t.addChild("g");
        Node a = t.root;
        Node b = t.root.children.get(0);
        Node c = t.root.children.get(0).children.get(0);
        Node d = t.root.children.get(0).children.get(1);
        Node e = c.children.get(0);
        Node f = c.children.get(1);
        Node h = f.children.get(0);
        Node g = d.children.get(0);
//        System.out.println(t.root.children.get(0).children.get(1).ID);
//        System.out.println(a.ID);
//        System.out.println(b.ID);
//        System.out.println(c.ID);
//        System.out.println(d.ID);
//        System.out.println(e.ID);
//        System.out.println(f.ID);
//        System.out.println(g.ID);
//        System.out.println(h.ID);
//        System.out.println(a.findSplitNode(a).ID);
//        System.out.println(a.findSplitNode(b).ID + " " + a.findSplitNode(b).ID);
//        System.out.println(c.findSplitNode(d).ID + " " + d.findSplitNode(c).ID);
//        System.out.println(c.findSplitNode(g).ID + " " + g.findSplitNode(c).ID);
//        System.out.println(h.findSplitNode(c).ID + " " + c.findSplitNode(h).ID);
//        System.out.println(h.findSplitNode(e).ID + " " + e.findSplitNode(h).ID);
//        System.out.println(h.findSplitNode(g).ID + " " + g.findSplitNode(h).ID);
//        System.out.println(h.findSplitNode(h).ID + " " + h.findSplitNode(h).ID);



    }





}
