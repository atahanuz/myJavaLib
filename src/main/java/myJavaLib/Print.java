package myJavaLib;

import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.Collection;

/**
 * A utility class providing static methods for printing various information about objects and classes.
 */

public abstract class Print {

    /**
     * Prints the class hierarchy of the input object. It starts from the class of the object
     * and goes up to the <b>java.lang.Object</b> , printing each class' name.
     *
     * @param obj An object whose class hierarchy needs to be printed. If obj is an instance of Class,
     *            it directly prints the hierarchy of this Class object. Otherwise, it prints the
     *            hierarchy of the class that obj is an instance of.
     * @return The number of classes printed in the hierarchy.
     */

    public static int printClassTree(Object obj) {
        Class<?> tmp;
        int counter = 0;

        // Check if the object is an instance of Class.
        // If so, assign it directly to tmp.
        // Else, get the class of the object and assign it to tmp.
        if (obj.getClass() == Class.class) tmp = (Class<?>) obj;
        else tmp = obj.getClass();

        // Start from the class of the object, print its name and move up the hierarchy,
        // until we reach a null, indicating we've reached the top of the class hierarchy.
        while (tmp != null) {
            System.out.println(tmp);
            tmp = tmp.getSuperclass();
            counter++;
        }
        return counter;
    }

    /**
     * Prints all the elements of a collection or an array in a structured manner.
     *
     * @param set A collection(ArrayList, LinkedList etc.) or an array.
     */

    public static void printSet(Object set) {

        // Check if the input is a collection.
        if (set instanceof AbstractCollection) {
            var it = ((Collection) set).iterator();
            int len = ((Collection) set).size();

            // If the collection is empty, print out a message and return.
            if (len == 0) {
                System.out.println("-Empty " + set.getClass().getSimpleName() + "!");
                return;
            }
            System.out.println("Printing " + set.getClass().getSimpleName() + " with " + len + " elements:");

            // Iterate over the collection and print each element.
            for (int i = 0; i < len; i++)
                System.out.println(i + "| " + it.next());
        }
        // The input is not a collection. So, we treat it as an array.
        else {
            int len = Array.getLength(set);

            // If the array is empty, print out a message and return.
            if (len == 0) {
                System.out.println("-Empty Array !");
                return;
            }
            System.out.println("Printing " + set.getClass().getSimpleName() + " with " + len + " elements:");

            // Iterate over the array and print each element.
            for (int i = 0; i < len; i++)
                System.out.println(i + "| " + Array.get(set, i));
        }
        System.out.println();
    }
}
