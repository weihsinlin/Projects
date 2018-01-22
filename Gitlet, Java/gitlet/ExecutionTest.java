package gitlet;

import org.junit.Test;

import static org.junit.Assert.*;
import java.io.*;
import java.util.Random;

/**
 * Created by LiwenKo on 7/15/17.
 */


// Delete dir (change to "yo") and new files before running each test
public class ExecutionTest {
    private String dir = Execution.getDir();


    public void write(String name) {
        Utils.writeContents(new File(name), name.getBytes());
    }

    public void write(String name, String file) {
        byte[] content = Utils.readContents(new File("testing/src/" + file));
        Utils.writeContents(new File(name), content);
    }

    public void edit(String fileName) {
        String content = new String(Utils.readContents(new File(fileName)));
        content += "\n" + fileName;
        Utils.writeContents(new File(fileName), content.getBytes());
    }

    public void delete(String fileName) {
        Utils.restrictedDelete(fileName);
    }

    @Test
    public void init() throws Exception {
        Execution.init();
        Execution.init(); // should print "A gitlet version-control system already exists in the current directory."
    }

    @Test
    public void noAddToStage() throws Exception {
        write("a.txt"); // create a new file
        Execution.init();
        Execution.add("a.txt");
        Execution.status();
    }

    @Test
    public void addToStage() throws Exception {
        write("a.txt"); // create a new file
        Execution.init();
        edit("a.txt");
        Execution.add("a.txt");
        Execution.status();
    }

    @Test
    public void nothingToBeAdded() throws Exception {
        write("a.txt"); // create a new file
        Execution.init();
        edit("a.txt");
        Execution.add("b.txt"); // err msg
        Execution.status();
    }

    @Test
    public void nothingToCommit() throws Exception {
        write("a.txt"); // create a new file
        Execution.init();
        Execution.add("a.txt");
        Execution.commit("nothing"); // error msg
        Execution.status();
    }

    @Test
    public void regularCommit() throws Exception {
        write("a.txt"); // create a new file
        Execution.init();
        edit("a.txt");
        Execution.add("a.txt");
        Execution.commit("successful");
        Execution.status(); // stage should be cleared
    }

    @Test
    public void emptyCommit() throws Exception {
        write("a.txt"); // create a new file
        Execution.init();
        edit("a.txt");
        Execution.add("a.txt");
        Execution.commit(""); // err msg here
    }

    @Test
    public void add2rm1() throws Exception {
        Execution.init();
        write("g.txt");
        write("f.txt");
        Execution.add("g.txt");
        Execution.add("f.txt");
        Execution.rm("f.txt");
        Execution.status();
    }

    @Test
    public void rm1() throws Exception {

        // delete yo directory , make sure a.txt and newfile.txt are in dir
//        Execution.init();
//        Execution.rm("a.txt");

        // make changes to newfile.txt before proceeding
//        Execution.add("newfile.txt");
//        Execution.commit("removed a.txt");

        // check if a.txt is still in the head commit
//        Commit k = MyFunc.getHeadCommit();
//        System.out.println(k.getCommitID());
//        System.out.println(k.getManyFiles());



    }

    @Test
    public void rm2() throws Exception {

        // delete yo directory , make sure a.txt and newfile.txt are in dir
//        Execution.init();

        // copy b.txt to dir
//        Execution.add("b.txt");
//        Execution.status();
//        Execution.rm("b.txt");
//        Execution.status();



        // make changes to newfile.txt before proceeding
//        Execution.add("newfile.txt");
//        Execution.commit("added and removed b.txt");

        // check if a.txt is still in the head commit
//        Commit k = MyFunc.getHeadCommit();
//        System.out.println(k.getCommitID());
//        System.out.println(k.getManyFiles());


    }

    @Test
    public void rm3() throws Exception {

        // delete yo directory , make sure a.txt and newfile.txt are in dir
//        Execution.init();
//        Execution.rm("b.txt");

        // Expect the error message

    }

    @Test
    public void log() throws Exception {
    }

    @Test
    public void globalLog() throws Exception {
    }

    @Test
    public void find() throws Exception {
    }

    @Test
    public void statusUntracked() throws Exception {

//        Execution.init();
        // add an outside file
        Execution.status();
    }

    @Test
    public void checkout1_1() throws Exception {
        Execution.init();
        write("a.txt");
        Execution.add("a.txt");
        Execution.commit("added a.txt");
        edit("a.txt");
        Execution.checkout1("a.txt");
        Execution.status();
    }

    @Test
    public void checkout1_2() throws Exception {
        Execution.init();
        write("a.txt");
        Execution.add("a.txt");
        Execution.commit("added a.txt");
        edit("a.txt");
        Execution.add("a.txt");
        Execution.checkout1("a.txt");
        Execution.status();
    }

    @Test
    public void branch() throws Exception {


    }

    @Test
    public void rmBranch() throws Exception {
//        write("a.txt");
//        write("b.txt");
//        Execution.init();
//        edit("a.txt");
//        Execution.add("a.txt");
//        Execution.commit("1 a.txt");
//        Execution.branch("testing");
//        Execution.status();
//        Execution.log();
//        Execution.rmBranch("");
//        Execution.rmBranch("master");
        Execution.checkout3("testing");
//        Execution.rmBranch("testing");
//        edit("a.txt");
//        Execution.add("a.txt");
//        Execution.commit("1 a.txt");
        Execution.status();
    }

    @Test
    public void reset() throws Exception {
    }

    @Test
    public void merge() throws Exception {
        Execution.init();
        write("a.txt");
        write("b.txt");
        write("c.txt");
        Execution.add("a.txt");
        Execution.add("b.txt");
        Execution.add("c.txt");
        Execution.commit("first");
        Execution.branch("test");
        edit("c.txt");
        Execution.add("c.txt");
        edit("a.txt");
        Execution.add("a.txt");
        Execution.commit("m1");
        edit("a.txt");
        Execution.add("a.txt");
        Execution.commit("m2");
        Execution.checkout3("test");
        edit("b.txt");
        Execution.add("b.txt");
        edit("c.txt");
        Execution.add("c.txt");
        Execution.commit("t1");
        edit("b.txt");
        Execution.add("b.txt");
        edit("c.txt");
        Execution.add("c.txt");

        Execution.commit("t2");
        Execution.merge("master");
    }

    @Test
    public void checkout3() throws Exception {
        Execution.init();
        write("a.txt");
        write("b.txt");
        write("c.txt");
        Execution.add("a.txt");
        Execution.add("b.txt");
        Execution.add("c.txt");
        Execution.commit("first");
        Execution.branch("test");
        edit("c.txt");
        Execution.add("c.txt");
        edit("a.txt");
        Execution.add("a.txt");
        Execution.commit("m1");
        write("d.txt");
        Execution.add("d.txt");
        edit("a.txt");
        Execution.add("a.txt");
        Execution.commit("m2");
        edit("d.txt");
        Execution.add("d.txt");
        Execution.status();
        Execution.checkout3("test");
        Execution.status();
    }


    @Test
    public void test28() throws Exception {
        write("f.txt");
        write("g.txt");
        Execution.init();
        Execution.branch("other");
        write("h.txt");
        Execution.add("h.txt");
        Execution.rm("g.txt");
        Execution.commit("addhremoveg");
        Execution.checkout3("other");
        Execution.rm("f.txt");
        write("k.txt");
        Execution.add("k.txt");
        Execution.commit("add k remove f");
        Execution.checkout3("master");
        Execution.merge("other");
        Execution.log();

    }

    @Test
    public void test29() throws Exception {
//        Execution.init();
//        write("f.txt", "wug.txt");
//        write("g.txt", "notwug.txt");
//        Execution.add("f.txt");
//        Execution.add("g.txt");
//        Execution.commit("Two files");
//        Execution.branch("other");
//        write("h.txt", "wug2.txt");
//        Execution.add("h.txt");
//        Execution.rm("g.txt");
//        Execution.commit("add h rm g");
//        Execution.checkout3("other");
//        Execution.rm("f.txt");
//        write("k.txt", "wug3.txt");
//        Execution.add("k.txt");
//        Execution.commit("add k remove f");
//        Execution.log();
//        Execution.checkout3("master");
//        Execution.globalLog();
        Execution.reset("fda81b14fc54cfda20f4fe02b3f6194d7fd8ec20");
//        Execution.status();
//        Execution.log();
//        Execution.checkout3("other");


    }

    @Test
    public void isModifiedsg() throws Exception {
        write("a.txt");
//        edit("g.txt");
//        edit("x.txt");


    }

    @Test
    public void whatever() throws Exception {
    Utils.writeContents(new File("tet.txt"), "".getBytes());

    }


}