package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Philip on 7/14/17.
 */
public class Commit implements Serializable {
    private static String dir = Execution.getDir();
    private String parentID, commitID, description, author;
    private String date;
    private TreeMap<String, String> manyFiles;
    private MySerialization<Commit> cs = new MySerialization<>();
    private Commit parent;
    private ArrayList<String> toBeRemoved = new ArrayList<>();

    public Commit(String des, TreeMap t, String pID) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = dateFormat.format(new Date());
        description = des;
        author = System.getProperty("user.name");
        parentID = pID;
        if (pID == null) {
            // Initialization
            manyFiles = t;
        } else {
            parent = MyFunc.getCommit(pID);
            manyFiles = (TreeMap) parent.manyFiles.clone();
            t.forEach((k, v) -> manyFiles.put((String) k, (String) v));
            parent.toBeRemoved.forEach(k -> manyFiles.remove(k));
            parent.toBeRemoved.clear();
            MyFunc.serializeCommit(parent);
        }


        ArrayList a = new ArrayList();
        a.add(Utils.sha1(manyFiles.values().toArray()));
        a.add(description);
        a.add(date);
        if (parentID != null) {
            a.add(parentID);
        }

        commitID = Utils.sha1(a);

        cs.serialize(this, dir + "/commits/" + commitID);

    }

    public String getParentID() {
        return parentID;
    }

    public String getCommitID() {
        return commitID;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public TreeMap getManyFiles() {
        return manyFiles;
    }

    public String getAuthor() {
        return author;
    }

    public ArrayList<String> getToBeRemoved() {
        return toBeRemoved;
    }

    public boolean remove(String fileName) {
        if (manyFiles.containsKey(fileName)) {
            toBeRemoved.add(fileName);
            File f = new File(fileName);
            f.delete();
            return true;
        }
        return false;
    }

    public void printDate() {
        System.out.println(date);
    }

    public void print() {
        System.out.println("===");
        System.out.println("Commit " + commitID);
        printDate();
        System.out.println(description);
        System.out.println();
    }

    public void printUntrackedFiles() {
        List<String> currentFiles = Utils.plainFilenamesIn(".");
        List<String> untrackedFiles = currentFiles.stream()
               .filter(x -> !manyFiles.containsKey(x))
               .sorted()
               .collect(Collectors.toList());
        for (String s : untrackedFiles) {
            System.out.println(s);
        }

    }

//    public boolean isUntracked(String fileName) {
//        return !manyFiles.containsKey(fileName);
//    }

    public boolean isModified(String fileName) {
        if (!manyFiles.containsKey(fileName)) {
            return false;
        }
        String currentFileID = manyFiles.get(fileName);
        Blob test = new Blob(fileName, false);
        String testFileID = test.getBlobID();
        return !currentFileID.equals(testFileID);
    }

    public boolean isModified(String cID, String fileName) {
        // assuming fileName is present in both commits
        Commit otherCommit = MyFunc.getCommit(cID);
        String otherFileID = otherCommit.getBlobID(fileName);
        return !getBlobID(fileName).equals(otherFileID);
    }

    public boolean isModified(Commit commit, String fileName) {
        // assuming fileName is present in both commits
        String otherFileID = commit.getBlobID(fileName);
        return !getBlobID(fileName).equals(otherFileID);
    }

    public String getBlobID(String fileName) {
        return manyFiles.get(fileName);
    }

    public Set<String> getAllFiles() {
        return manyFiles.keySet();
    }

//    public List<String> deletedFiles() {
//        List currentFiles = Utils.plainFilenamesIn("." );
//
//        return manyFiles.keySet().stream().filter(x -> !currentFiles.contains(x))
//                .collect(Collectors.toList());
//    }

}
