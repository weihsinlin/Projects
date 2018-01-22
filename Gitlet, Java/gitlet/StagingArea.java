package gitlet;

import java.io.Serializable;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Philip on 7/14/17.
 */
public class StagingArea implements Serializable {
    private TreeMap files = new TreeMap();
    private String dir = Execution.getDir();
    private ArrayList<String> removedFile = new ArrayList<>();
    private MySerialization<CommitTree> cts = new MySerialization<>();
    private MySerialization<Commit> cs = new MySerialization<>();

    public void firstAdd(String fileName) {
        Blob blob = new Blob(fileName, true);
        files.put(fileName, blob.getBlobID());
    }

    public void add(String fileName) {
        Blob blob = new Blob(fileName, false);

        String oldID = (String) MyFunc.getHeadCommit().getManyFiles().get(fileName);

        if (blob.getBlobID().equals(oldID)) {
            return;
        }
        Blob addedBlob = new Blob(fileName, true);
        files.put(fileName, addedBlob.getBlobID());
        removedFile.remove(fileName);

    }

    public boolean remove(String fileName) {
        if (files.remove(fileName) != null) {
            removedFile.add(fileName);
            return true;
        }
        return false;
    }

    public void clear() {
        files.clear();
        removedFile.clear();
    }

    public void printStagedFiles() {
        List l = (List) files.keySet().stream().sorted().collect(Collectors.toList());

        for (Object k : l) {
            System.out.println(k);
        }
    }

    public void printRemovedFiles() {
        List l = (List) removedFile.stream().sorted().collect(Collectors.toList());

        for (Object k : l) {
            System.out.println(k);
        }
    }

    public boolean isModified(String fileName) {
        if (!files.containsKey(fileName)) {
            return true;
        }

        String currentFileID = (String) files.get(fileName);
        Blob test = new Blob(fileName, false);
        String testFileID = test.getBlobID();
        return !currentFileID.equals(testFileID);
    }


    public TreeMap getFiles() {
        return files;
    }

    public ArrayList<String> getRemovedFile() {
        return removedFile;
    }

    public static void main(String[] args) {

    }

}
