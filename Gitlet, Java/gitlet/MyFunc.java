package gitlet;

import java.util.ArrayList;

/**
 * Created by Philip on 7/16/17.
 */
public class MyFunc {

    private static MySerialization<CommitTree> ctSerializer = new MySerialization<>();
    private static MySerialization<Commit> cs = new MySerialization<>();
    private static MySerialization<Blob> bb = new MySerialization<>();
    private static MySerialization<StagingArea> sg = new MySerialization<>();
    private static String dir = Execution.getDir();

    public static Commit getHeadCommit() {
        // De-serialize Commit Tree
        CommitTree ct = ctSerializer.deserialize(dir + "/commitTree.txt");
        String headCommitID = ct.getHeadNodeID();

        return getCommit(headCommitID);
    }


    // make sure commitID exists
    public static Commit getCommit(String commitID) {
        return cs.deserialize(dir + "/commits/" + commitID);
    }

    public static Blob getBlob(String blobID) {
        return bb.deserialize(dir + "/blobs/" + blobID);
    }

    public static CommitTree getCommitTree() {
        return ctSerializer.deserialize(dir + "/commitTree.txt");
    }

    public static void serializeCommitTree(CommitTree t) {
        ctSerializer.serialize(t, dir + "/commitTree.txt");
    }

    public static StagingArea getStagingArea() {
        return sg.deserialize(dir + "/stage.txt");
    }

    public static void serializeStage(StagingArea s) {
        sg.serialize(s, dir + "/stage.txt");
    }

    public static void serializeCommit(Commit c) {
        cs.serialize(c, dir + "/commits/" + c.getCommitID());
    }

    public static String getFullID(String abbrev) {
        CommitTree ct = getCommitTree();
        ArrayList<String> allCommitIDs = ct.getAllCommitID();
        for (String cID : allCommitIDs) {
            if (cID.substring(0, abbrev.length()).equals(abbrev)) {
                return cID;
            }
        }
        return null;
    }

}
