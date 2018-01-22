package gitlet;


import java.io.File;
import java.io.Serializable;

/**
 * Created by Philip on 7/14/17.
 */
public class Blob implements Serializable {
    private String blobID;
    private byte[] myByte;
    private static MySerialization<Blob> bb = new MySerialization<>();
    private static String dir = Execution.getDir();

    public Blob(File file) {
        myByte = Utils.readContents(file);
        blobID = Utils.sha1(myByte);
    }

    public Blob(File file, boolean se) {
        this(file);
        if (se) {
            bb.serialize(this, dir + "/blobs/" + blobID);
        }
    }

    public Blob(String fileName, boolean se) {
        this(new File(fileName), se);
    }

    public String getBlobID() {
        return blobID;
    }

    public byte[] getMyByte() {
        return myByte;
    }


}
