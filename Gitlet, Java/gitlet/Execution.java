package gitlet;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.io.File;
import java.util.Set;
//import java.util.TreeMap;
//import java.util.logging.FileHandler;
//import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Philip on 7/14/17.
 */
public class Execution implements Serializable {
    private static String dir = ".gitlet";
    private static MySerialization<StagingArea> sg = new MySerialization<>();
    private static MySerialization<CommitTree> ctSerializer = new MySerialization<>();
    private static MySerialization<Commit> cs = new MySerialization<>();

    //Dir getter
    public static String getDir() {
        return dir;
    }

    //Commands
    public static void init() {

        //Check if already initialized
        if (new File(dir).exists()) {
            System.out.print("A gitlet version-control system");
            System.out.println(" already exists in the current directory.");
            return;
        }

        // Make folders for future usages
        new File(dir).mkdir();
        new File(dir + "/blobs").mkdir();
        new File(dir + "/commits").mkdir();

        // Create a fresh Staging Area
        StagingArea stagingArea = new StagingArea();


        // Make first commit
        List<String> listOfFiles = Utils.plainFilenamesIn(".");


        for (String d : listOfFiles) {
            stagingArea.firstAdd(d);
        }

        Commit firstCommit = new Commit("initial commit", stagingArea.getFiles(), null);

        // Create a Commit Tree
        CommitTree ct = new CommitTree(firstCommit.getCommitID());
        ct.addBranch("master", ct.getRoot());
        ct.setHead("master");
        // Serialize Commit Tree
        ctSerializer.serialize(ct, dir + "/commitTree.txt");

        // Clear staging area
        stagingArea.clear();

        // Serialize Staging Area
        sg.serialize(stagingArea, dir + "/stage.txt");
    }


    public static void add(String fileName) {
        if (!new File(fileName).exists()) {
            System.out.println("File does not exist.");
            return;
        }

        // De-serialize Staging Area
        StagingArea stagingArea = sg.deserialize(dir + "/stage.txt");

        // Add file to staging area
        stagingArea.add(fileName);

        Commit c = MyFunc.getHeadCommit();
        c.getToBeRemoved().remove(fileName);
        MyFunc.serializeCommit(c);
        // Serialize stage
        sg.serialize(stagingArea, dir + "/stage.txt");

    }

    public static void commit(String description) {
        if (description.equals("")) {
            System.out.println("Please enter a commit message.");
            return;
        }

        // De-serialize Staging Area
        StagingArea stagingArea = sg.deserialize(dir + "/stage.txt");

        Commit headCommit = MyFunc.getHeadCommit();

        if (stagingArea.getFiles().size() == 0 && headCommit.getToBeRemoved().size() == 0) {

            // Serialize Staging Area
            sg.serialize(stagingArea, dir + "/stage.txt");

            System.out.println("No changes added to the commit.");
            return;

        } else {
            // De-serialize Commit Tree
            CommitTree ct = ctSerializer.deserialize(dir + "/commitTree.txt");

            // Make a commit object and add to commit tree
            Commit come = new Commit(description, stagingArea.getFiles(), ct.getHeadNodeID());
            ct.addChild(come.getCommitID());

            // Clear staging area
            stagingArea.clear();

            // Serialize Commit Tree
            ctSerializer.serialize(ct, dir + "/commitTree.txt");

            // Serialize Staging Area
            sg.serialize(stagingArea, dir + "/stage.txt");
        }

    }

    public static void rm(String fileName) {
        // De-serialize Staging Area
        StagingArea stagingArea = sg.deserialize(dir + "/stage.txt");

        // Remove file from Staging Area
        boolean stageRemoved = stagingArea.remove(fileName);
        Commit headCommit = MyFunc.getHeadCommit();
        boolean commitRemoved = headCommit.remove(fileName);
        cs.serialize(headCommit, dir + "/commits/" + headCommit.getCommitID());
        if (!(stageRemoved || commitRemoved)) {
            System.out.println("No reason to remove the file.");
        }
//        if (stagingArea.getFiles().containsKey(fileName)) {
//            stagingArea.remove(fileName);
//        } else {
//            if () {
//
//            } else {
//                System.out.println("No reason to remove the file.");
//
//            }
//        }

        // Serialize Staging Area
        sg.serialize(stagingArea, dir + "/stage.txt");
    }

    public static void log() {
        // De-serialize Commit Tree
        CommitTree ct = ctSerializer.deserialize(dir + "/commitTree.txt");

        ct.printLog();

        // Serialize Commit Tree
        ctSerializer.serialize(ct, dir + "/commitTree.txt");
    }

    public static void globalLog() {
        // De-serialize Commit Tree
        CommitTree ct = ctSerializer.deserialize(dir + "/commitTree.txt");

        ct.printGlobalLog();

        // Serialize Commit Tree
        ctSerializer.serialize(ct, dir + "/commitTree.txt");

    }

    public static void find(String message) {
        // De-serialize Commit Tree
        CommitTree ct = ctSerializer.deserialize(dir + "/commitTree.txt");

        ArrayList<String> ids = ct.findCommitID(message);
        if (ids.size() == 0) {
            System.out.println("Found no commit with that message.");
        } else {
            for (String id : ids) {
                System.out.println(id);
            }
        }

        // Serialize Commit Tree
        ctSerializer.serialize(ct, dir + "/commitTree.txt");
    }

    public static void status() {
        // Get head commit
        Commit headCommit = MyFunc.getHeadCommit();

        // De-serialize Commit Tree
        CommitTree ct = ctSerializer.deserialize(dir + "/commitTree.txt");

        // De-serialize Staging Area
        StagingArea stagingArea = sg.deserialize(dir + "/stage.txt");

        // Print Branches
        System.out.println("=== Branches ===");
        ct.printBranches();
        System.out.println();

        // Print Staged Files
        System.out.println("=== Staged Files ===");
        stagingArea.printStagedFiles();
        System.out.println();

        // Process removed files
        List removedFromStage = stagingArea.getRemovedFile();
        List removedFromCommit = headCommit.getToBeRemoved();
        removedFromStage.addAll(removedFromCommit);
        Set removedFilesSet = (Set) removedFromStage.stream().collect(Collectors.toSet());
        List removedFiles = (List) removedFilesSet.stream()
                .sorted().collect(Collectors.toList());
        // Print romoved files
        System.out.println("=== Removed Files ===");
        for (Object a : removedFromCommit) {
            System.out.println(a);
        }
        System.out.println();

        // Modified
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();

        // Untracked Files
        System.out.println("=== Untracked Files ===");
        System.out.println();


        // Serialize Commit Tree
        ctSerializer.serialize(ct, dir + "/commitTree.txt");

        // Serialize Staging Area
        sg.serialize(stagingArea, dir + "/stage.txt");
    }

    public static void checkout1(String fileName) {
        Commit headCommit = MyFunc.getHeadCommit();
        if (!headCommit.getManyFiles().containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
            return;
        }

        StagingArea stagingArea = MyFunc.getStagingArea();
        stagingArea.getFiles().remove(fileName);
        MyFunc.serializeStage(stagingArea);

        String blobID = (String) headCommit.getManyFiles().get(fileName);
        Blob blob = MyFunc.getBlob(blobID);
        Utils.writeContents(new File(fileName), blob.getMyByte());
    }

    public static void checkout2(String commitID, String fileName) {
        if (commitID.length() < 40) {
            commitID = MyFunc.getFullID(commitID);
        }
        CommitTree commitTree = MyFunc.getCommitTree();

        ArrayList<String> commitIDs = commitTree.getAllCommitID();
        if (!commitIDs.contains(commitID)) {
            System.out.println("No commit with that id exists.");
            return;
        }
        Commit commit = MyFunc.getCommit(commitID);
        if (!commit.getManyFiles().containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
            return;
        }

        StagingArea stagingArea = MyFunc.getStagingArea();
        stagingArea.getFiles().remove(fileName);
        MyFunc.serializeStage(stagingArea);

        String blobID = (String) commit.getManyFiles().get(fileName);
        Blob blob = MyFunc.getBlob(blobID);
        Utils.writeContents(new File(fileName), blob.getMyByte());
    }

    public static void checkout3(String branchName) {
        CommitTree commitTree = MyFunc.getCommitTree();
        if (!commitTree.getBranches().containsKey(branchName)) {
            System.out.println("No such branch exists.");
            return;
        }
        if (branchName.equals(commitTree.getHead())) {
            System.out.println("No need to checkout the current branch.");
            return;
        }

        Commit commit = commitTree.getBranches().get(branchName).getCommit();
        Set fileNames = commit.getManyFiles().keySet();

        Set<String> ufs = untrackedFiles();
        for (String uf : ufs) {
            if (!fileNames.contains(uf) || commit.isModified(uf)) {
                System.out.print("There is an untracked file in the way;");
                System.out.println(" delete it or add it first.");
                return;
            }
        }


        for (Object f: fileNames) {
            String blobID = (String) commit.getManyFiles().get(f);
            Blob blob = MyFunc.getBlob(blobID);
            Utils.writeContents(new File((String) f), blob.getMyByte());
        }
        commitTree.setHead(branchName);
        MyFunc.serializeCommitTree(commitTree);

        List dirFilesToRemove = Utils.plainFilenamesIn(".");
        dirFilesToRemove = (List) dirFilesToRemove.stream()
                .filter(x -> !fileNames.contains(x)).collect(Collectors.toList());
        for (Object o : dirFilesToRemove) {

            new File((String) o).delete();
        }
        StagingArea s = MyFunc.getStagingArea();
        s.clear();
        MyFunc.serializeStage(s);

    }

    public static void branch(String branchName) {
        // De-serialize Commit Tree
        CommitTree ct = ctSerializer.deserialize(dir + "/commitTree.txt");
        if (ct.getBranches().containsKey(branchName)) {
            System.out.println("A branch with that name already exists.");
            return;
        }
        ct.addBranch(branchName);

        // Serialize Commit Tree
        ctSerializer.serialize(ct, dir + "/commitTree.txt");
    }

    public static void rmBranch(String branchName) {
        CommitTree ct = MyFunc.getCommitTree();
        if (!ct.getBranches().containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
        } else if (branchName.equals(ct.getHead())) {
            System.out.println("Cannot remove the current branch.");
        } else {
            ct.removeBranch(branchName);
        }
        MyFunc.serializeCommitTree(ct);
    }

    public static void reset(String commitID) {
        if (commitID.length() < 40) {
            commitID = MyFunc.getFullID(commitID);
        }
        CommitTree ct = MyFunc.getCommitTree();
        ArrayList<String> commitIDs = ct.getAllCommitID();
        if (!commitIDs.contains(commitID)) {
            System.out.println("No commit with that id exists.");
            return;
        }
        List<String> dirFiles = Utils.plainFilenamesIn(".");
        Commit headCommit = MyFunc.getHeadCommit();
        Set<String> headCommitFiles = headCommit.getManyFiles().keySet();
        Set<String> untrackedFiles = untrackedFiles();
        for (String uf : untrackedFiles) {
            if (headCommitFiles.contains(uf) && headCommit.isModified(commitID, uf)) {
                System.out.print("There is an untracked file in the way;");
                System.out.println(" delete it or add it first.");
                return;
            }
        }
        ct.addBranch("dummy", ct.findNode(commitID));
        String tempHead = ct.getHead();
        MyFunc.serializeCommitTree(ct);
        checkout3("dummy");
        ct = MyFunc.getCommitTree();
        ct.setHead(tempHead, MyFunc.getCommit(commitID));
        ct.removeBranch("dummy");
        MyFunc.serializeCommitTree(ct);

    }

    public static void merge(String branchName) {

        CommitTree ct = MyFunc.getCommitTree();
        StagingArea stagingArea = MyFunc.getStagingArea();

        if (!ct.getBranches().containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }

        String currCommitID = ct.getHeadNodeID();
        String splitPointID = ct.findSplitPointID(branchName);
        String givenCommitID = ct.getBranchCommitID(branchName);

        Commit currCommit = MyFunc.getHeadCommit();
        Commit givenCommit = MyFunc.getCommit(givenCommitID);
        Commit splitPointCommit = MyFunc.getCommit(splitPointID);

        if (stagingArea.getFiles().size() != 0 || currCommit.getToBeRemoved().size() != 0) {
            System.out.println("You have uncommitted changes.");
            return;
        }

        if (branchName.equals(ct.getHead())) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }

        Set<String> dirFiles = Utils.plainFilenamesIn(".").stream().collect(Collectors.toSet());
        Set<String> currCommitFiles = currCommit.getAllFiles();
        Set<String> givenBranchFiles = givenCommit.getAllFiles();
        Set<String> splitPointFiles = splitPointCommit.getAllFiles();
        Set<String> allFiles = union(currCommitFiles, union(splitPointFiles, givenBranchFiles));

        if (minus(dirFiles, currCommitFiles).size() != 0) {
            System.out.println("There is an untracked file in the way; delete it or add it first.");
            return;
        }
        if (splitPointID.equals(givenCommitID)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return;
        }
        if (splitPointID.equals(currCommitID)) {
            ct.setHead(branchName);
            System.out.println("Current branch fast-forwarded.");
            MyFunc.serializeCommitTree(ct);
            return;
        }

        boolean encouteredMerge = false;

        for (String f : allFiles) {
            if (mergeHelper1(f, currCommit, givenCommit, splitPointCommit).equals("mc")) {
                if (!encouteredMerge) {
                    System.out.println("Encountered a merge conflict.");
                    encouteredMerge = true;
                }
                byte[] currFileBytes, givenFileBytes;
                if (currCommitFiles.contains(f)) {
                    currFileBytes = MyFunc.getBlob(currCommit.getBlobID(f)).getMyByte();
                } else {
                    currFileBytes = "".getBytes();
                }
                if (givenBranchFiles.contains(f)) {
                    givenFileBytes = MyFunc.getBlob(givenCommit.getBlobID(f)).getMyByte();
                } else {
                    givenFileBytes = "".getBytes();
                }
                String mergedFileContent = "<<<<<<< HEAD\n" + new String(currFileBytes)
                        + "=======\n"
                        + new String(givenFileBytes) + ">>>>>>>\n";
                Utils.writeContents(new File(f), mergedFileContent.getBytes());
            } else if (mergeHelper1(f, currCommit, givenCommit, splitPointCommit).equals("cs")) {
                checkout2(givenCommitID, f);
                stagingArea.add(f);
            } else if (mergeHelper1(f, currCommit, givenCommit, splitPointCommit).equals("rm")) {
                rm(f);
            }
        }
        MyFunc.serializeStage(stagingArea);
        MyFunc.serializeCommitTree(ct);
        if (!encouteredMerge) {
            commit("Merged " + ct.getHead() + " with " + branchName + ".");
        }

    }

    public static Set<String> intersection(Set<String> s1, Set<String> s2) {
        return s1.stream().filter(x -> s2.contains(x)).collect(Collectors.toSet());
    }

    public static Set<String> minus(Set<String> s1, Set<String> s2) {
        return s1.stream().filter(x -> !s2.contains(x)).collect(Collectors.toSet());
    }

    public static Set<String> union(Set<String> s1, Set<String> s2) {
        Set<String> rtn = s1.stream().collect(Collectors.toSet());
        rtn.addAll(s2);
        return rtn;
    }

    public static Set<String> dirFiles() {
        return Utils.plainFilenamesIn(".").stream().collect(Collectors.toSet());
    }

    public static Set<String> untrackedFiles() {
        Commit c = MyFunc.getHeadCommit();
        StagingArea sgA = MyFunc.getStagingArea();
        Set<String> dirFiles = dirFiles();
        Set<String> currCommitFiles = c.getAllFiles();
        Set<String> sgFiles = sgA.getFiles().keySet();
        Set<String> rtn = minus(currCommitFiles, c.getToBeRemoved()
                .stream().collect(Collectors.toSet()));
        rtn = minus(dirFiles, rtn);
        rtn = minus(rtn, sgFiles);
        return rtn;
    }

    public static String mergeHelper1(String fileName, Commit curr, Commit given, Commit split) {

        if (split.getAllFiles().contains(fileName)) {
            if (curr.getAllFiles().contains(fileName)) {
                if (given.getAllFiles().contains(fileName)) {
                    if (isModified(fileName, curr, given) && isModified(fileName, given, split)
                            && isModified(fileName, split, curr)) {
                        return "mc"; // merge conflict
                    }
                    if (isModified(fileName, curr, given) && !isModified(fileName, split, curr)) {
                        return "cs"; // checkout and stage
                    }
                } else {
                    if (isModified(fileName, split, curr)) {
                        return "mc";
                    }
                    if (!isModified(fileName, split, curr)) {
                        return "rm";
                    }
                }
            } else {
                if (given.getAllFiles().contains(fileName)) {
                    if (isModified(fileName, split, given)) {
                        return "mc";
                    }
                }
            }
        } else {
            if (curr.getAllFiles().contains(fileName)) {
                if (given.getAllFiles().contains(fileName)) {
                    if (isModified(fileName, curr, given)) {
                        return "mc";
                    }
                }
            } else {
                if (given.getAllFiles().contains(fileName)) {
                    return "cs";
                }
            }
        }
        return "stay";
    }

    public static boolean isModified(String f, Commit c1, Commit c2) {
        return c1.isModified(c2, f);
    }


    public static void main(String[] args) {
//        init();
//        add("a.txt");
//        status();
//        log();
//        rm("a.txt");
//        add("newfile.txt");
//        commit("added a");
//        add("newfile.txt");
//        rm("p.txt");
//        add("g.txt");
//        add("p.txt");
//        status();
//        commit("rm p.txt");
//        commit("both changed");
//        status();

//        branch("testing");
//        branch("bbb");
//        status();
//        ct.printDescription();

//        globalLog();

//        log();
//        find("change g.txt, unchanged p.txt");
//        status();

//        rm("g.txt");
//        status();
//        Commit k = MyFunc.getHeadCommit();
//        System.out.println(k.getCommitID());
//        System.out.println(k.getManyFiles());

//        new File("/yo").delete();
//        find("added a");

    }


}
