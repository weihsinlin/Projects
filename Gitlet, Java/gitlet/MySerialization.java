package gitlet;

import java.io.FileOutputStream;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileInputStream;

/**
 * Created by Philip on 7/15/17.
 */
public class MySerialization<T>  implements  Serializable {

    public void serialize(T item, String dir) {
        try {
            FileOutputStream fileOut = new FileOutputStream(dir);
            ObjectOutputStream out  = new ObjectOutputStream(fileOut);
            out.writeObject(item);
            out.close();
            fileOut.close();

        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public T deserialize(String dir) {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(dir));
            T item = (T) in.readObject();
            return item;

        } catch (IOException i) {
            System.out.print(i);
        } catch (ClassNotFoundException e) {
            System.out.print(e);
        }
        return null;
    }

}
