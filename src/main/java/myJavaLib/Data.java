package myJavaLib;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Utility class containing static methods for deep copying objects and writing/reading objects to files.
 *
 * @implNote For these methods to work correctly, the classes of the objects being
 * operated upon must implement the {@link Serializable} interface.
 */

public abstract class Data {

    /**
     * Makes a deep copy of the given object using serialization.
     *
     * @param object the object to copy
     * @return a deep copy of the object, or null if an error occurred during the copy
     */

    public static <T> T deepCopy(T object) {

        T copiedObject = null;
        try {
            // Create a ByteArrayOutputStream
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            // Create an ObjectOutputStream to write the input object to the ByteArrayOutputStream
            ObjectOutputStream oos = new ObjectOutputStream(bout);
            oos.writeObject(object);
            oos.close();

            // Create a ByteArrayInputStream using the bytes from the ByteArrayOutputStream
            ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
            // Create an ObjectInputStream to read an object from the ByteArrayInputStream
            ObjectInputStream ois = new ObjectInputStream(bin);
            // Read the copied object from the ObjectInputStream and cast it to type T
            copiedObject = (T) ois.readObject();
            ois.close();
        } catch (IOException e) {
            System.out.println("IO error during deep copy operation: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found during deep copy operation: " + e.getMessage());
        }
        // Return the copied object
        return copiedObject;
    }

    /**
     * Writes an object to a file, with the option to append to the file if it already exists.
     * With appending you can store multiple objects on the file and read them by <b>file2Arraylist</b> method
     *
     * @param object   the object to write
     * @param filename the name of the file
     * @param append   if true and the file already exists, the object will be appended to the end of file.
     *                 If false or the file does not exist, a new file will be created
     * @return true if the writing process is successful, false otherwise
     */
   public static boolean writeObjectToFile(Object object, String filename, boolean append) {
        // Make append false if the file doesn't exist
        if (!Files.exists(Path.of(filename))) append = false;

        ObjectOutputStream outputStream = null;
        try {
            // If the file already exists and append is true, create an ObjectOutputStream that does not write a new header
            if (append && Files.size(Path.of(filename)) > 0) {
                outputStream = new ObjectOutputStream(new FileOutputStream(filename, true)) {
                    protected void writeStreamHeader() throws IOException {
                        reset();
                    }
                };
            } else {
                // Else, create a normal ObjectOutputStream
                outputStream = new ObjectOutputStream(new FileOutputStream(filename));
            }
            // Write the object to the ObjectOutputStream
            outputStream.writeObject(object);
            // If the writing process is successful, return true
            return true;
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            // Close the ObjectOutputStream in a finally block to ensure it gets executed
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    System.out.println("Error when closing the stream: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Reads a single object from a file.
     *
     * @param path the path to the file
     * @return the object read from the file, or null if an error occurred during the read
     */
    public static Object readObjectFromFile(String path) {
        ObjectInputStream inputStream = null;
        try {
            // Create an ObjectInputStream from a file
            inputStream = new ObjectInputStream(new FileInputStream(path));
            // Return the read object
            return inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            // Close the ObjectInputStream in a finally block to ensure it gets executed
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println("Error when closing the stream: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Reads all objects from a file and returns an ArrayList containing all of them.
     *
     * @param path the path to the file
     * @return the object read from the file, or null if an error occurred during the read
     */

    public static ArrayList<Object> file2Arraylist(String path) {

        if (!Files.exists(Paths.get(path))) {
            try {
                throw new FileNotFoundException("File " + path + " does not exist");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


        ArrayList<Object> list = new ArrayList<>();
        try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(path))) {
            while (true) {
                try {
                    // Keep adding objects to the list until an EOFException is thrown
                    list.add(stream.readObject());
                } catch (EOFException e) {
                    // End of file reached, break the loop
                    break;
                } catch (ClassNotFoundException e) {
                    System.out.println("Error: Cannot recognize the object's class: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        // Return the list of objects
        return list;
    }
}


