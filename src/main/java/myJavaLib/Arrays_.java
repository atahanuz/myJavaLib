package myJavaLib;

import java.lang.reflect.Array;
import java.util.Random;

/**
 * This utility class contains static methods to manipulate arrays in various ways.
 */
public abstract class Arrays_ {

    /**
     * This method takes an array and randomly shuffles its elements.
     *
     * @param array The array to be shuffled. It can be of any type.
     */
    public static void shuffle(Object array) {
        // Getting the length of the input array
        int len = Array.getLength(array);
        // Creating a new Random object
        Random rand = new Random();
        // Iterate through the array from beginning to end
        for (int i = 0; i < len; i++) {
            // Generating a random index in the range [i, len)
            int randomIndex = rand.nextInt(len - i) + i;
            // If the random index is different from the current index
            if (i != randomIndex) {
                // Swap the elements at the current index and the random index
                Object temp = Array.get(array, i);
                Array.set(array, i, Array.get(array, randomIndex));
                Array.set(array, randomIndex, temp);
            }
        }
    }

    /**
     * This method generates an array of integers from 'a' to 'b' (inclusive).
     *
     * @param a The starting integer of the array.
     * @param b The ending integer of the array.
     * @return An array of integers from 'a' to 'b'.
     */
    public static int[] arrayFromAtoB(int a, int b) {
        // Calculate the size of the array
        int size = Math.abs(b - a) + 1;
        // Create an array with the calculated size
        int[] arr = new int[size];
        // Determine the step for each iteration: +1 if a < b, -1 otherwise
        int increment = (a < b) ? 1 : -1;
        // Iterate through the array, filling it with values from a to b
        for (int i = 0; i < size; i++, a += increment) {
            arr[i] = a;
        }
        return arr;
    }

    /**
     * This method generates an array of integers from 'a' to 'b' with a given interval.
     *
     * @param a        The starting integer of the array.
     * @param b        The ending integer of the array.
     * @param interval The interval between each two adjacent integers in the array.
     * @return An array of integers from 'a' to 'b' with a given interval.
     * @throws IllegalArgumentException if a < b and interval is non-positive, or if a > b and interval is non-negative.
     */
    public  static int[] arrayFromAtoB(int a, int b, int interval) {
        // If a is equal to b, return an array containing only a
        if (a == b) {
            return new int[]{a};
        }

        // If a < b and interval is non-positive, or if a > b and interval is non-negative, throw an exception
        if (a < b && interval <= 0) {
            throw new IllegalArgumentException("Invalid input: non-positive interval for a < b");
        } else if (a > b && interval >= 0) {
            throw new IllegalArgumentException("Invalid input: non-negative interval for a > b");
        }

        // Calculate the length of the array
        int length = Math.abs((b - a) / interval) + 1;
        // Create an array with the calculated length
        int[] result = new int[length];

        // Iterate through the array, filling it with values from a to b with the specified interval
        for (int i = 0; i < length; i++) {
            result[i] = a + i * interval;
        }

        return result;
    }
}

