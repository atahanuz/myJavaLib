package myJavaLib;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Utility class for benchmarking memory usage, code execution time and sorting algorithm performance.
 * <p><p><p>{@link Benchmark#takeSnapshot()},{@link Benchmark#getTimes()} and {@link Benchmark#getMemories()} methods are being used
 * together.
 * <p> <p><p>
 * {@link Benchmark#sortingBenchmark(Consumer, int)} is a seperate module.
 */

public abstract class Benchmark {

    /**
     * Holds a list of system times at the point of snapshot.
     */
    static private ArrayList<Long> times;

    /**
     * Holds a list of used memory at the point of snapshot.
     */
    static private ArrayList<Long> memories;

    /**
     * Takes a snapshot of the current system time and used memory.
     * You can access the results with {@link Benchmark#getTimes} and {@link Benchmark#getMemories} methods.
     */
    public static void takeSnapshot() {
        // If times ArrayList is null, instantiate it
        if (times == null) times = new ArrayList<Long>();

        // If memories ArrayList is null, instantiate it
        if (memories == null) memories = new ArrayList<Long>();

        // Add current system time in nanoseconds to the times list
        times.add(System.nanoTime());

        // Add current used memory in bytes to the memories list
        memories.add(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
    }

    /**
     * Calculates and prints the time intervals between snapshots in seconds. It also returns the results in an array.
     *
     * @return array of time intervals in seconds between subsequent snapshots. If no snapshots have been taken, returns null.
     */

    public static double[] getTimes() {
        // If no snapshot has been taken, return null
        if (times == null) return null;

        // Create an array to hold time intervals
        double[] array = new double[times.size() - 1];

        // Iterate over the times list and calculate intervals
        for (int i = 0; i < times.size() - 1; i++) {
            // Calculate interval and convert to seconds
            array[i] = (double) (times.get(i + 1) - times.get(i)) / 1000000000;

            // Print the calculated interval
            System.out.println("Interval " + i + "= " + array[i] + " sec");
        }

        // Return the array of intervals
        return array;
    }


    /**
     * Calculates and returns the memory usage intervals between snapshots in kilobytes.
     *
     * @return array of memory usage intervals in KB between subsequent snapshots. If no snapshots have been taken, returns null.
     */
    public static long[] getMemories() {
        // If no snapshot has been taken, return null
        if (memories == null) return null;

        // Create an array to hold memory usage intervals
        long[] array = new long[memories.size() - 1];

        // Iterate over the memories list and calculate intervals
        for (int i = 0; i < memories.size() - 1; i++) {
            // Calculate memory usage interval and convert to KB
            array[i] = (memories.get(i + 1) - memories.get(i)) / 1000;

            // Print the calculated memory usage interval
            System.out.println("Interval " + i + "= " + array[i] + " KB");
        }

        // Return the array of memory usage intervals
        return array;
    }

    /**
     * Performs a benchmark on a given sorting method using a randomly generated array. Prints the time it took to sort.
     *
     * @param sortingMethod Method reference or lambda to the sorting method (i.e.  Arrays::parallelSort);
     * @param len           length of the array to sort.
     * @return returns the time printed to the console.
     */

    public static double sortingBenchmark(Consumer<int[]> sortingMethod, int len) {
        // Generate random array
        int[] array = new Random(0).ints(len).toArray();
        return SortingBenchmark.sortingBenchmark(sortingMethod, array);
    }

    /**
     * Performs a benchmark on a given sorting method using a provided array. Prints the time it took to sort.
     *
     * @param sortingMethod Method reference or lambda to the sorting method (i.e.Arrays::parallelSort);
     * @param array         the array to be sorted
     * @return returns the time printed to the console.
     */

    public  double sortingBenchmark(Consumer<int[]> sortingMethod, int[] array) {
        return SortingBenchmark.sortingBenchmark(sortingMethod, array);
    }


    private static class SortingBenchmark {

        private static double sortingBenchmark(Consumer<int[]> sortingMethod, int len) {
            // Generate random array
            int[] array = new Random(0).ints(len).toArray();
            return sortAndBenchmark(sortingMethod, array);
        }

        private static double sortingBenchmark(Consumer<int[]> sortingMethod, int[] array) {
            return sortAndBenchmark(sortingMethod, array);
        }

        private static double sortAndBenchmark(Consumer<int[]> sortingMethod, int[] array) {
            // Get the start time
            long startTime = System.nanoTime();

            // Perform the sort
            sortingMethod.accept(array);

            //Get the end time
            long endTime = System.nanoTime();


            // Check if array is sorted
            for (int i = 0; i < array.length - 1; i++) {
                if (array[i] > array[i + 1]) {
                    System.out.println("NOT SORTED !!");
                    break;
                }
            }
            array = null;
            System.gc();


            // Calculate elapsed time in seconds
            double elapsedTime = (endTime - startTime) / 1e9;

            // Prints the time to the console
            System.out.printf("Time= %.9f seconds%n", elapsedTime);

            // Returns the printed time
            return elapsedTime;
        }

    }

}

