package myJavaLib;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * A utility class providing static methods of different sorting algorithms.
 * Each of these algorithms can be used for sorting arrays of integers.
 */
public abstract class Sorting {

    /**
     * Inside this class you can find and adjust some parameters related to the QuickSort and MergeSort algorithms.
     */
    static abstract class Parameters {
        /**
         * The array size threshold for switching to InsertionSort in QuickSort algorithm.
         * You can play with different values, though testing has shown 30 to be an optimal choice.
         */
        private static int QUICKSORT_CUTOFF = 30;
        /**
         * The array size threshold for switching to InsertionSort in QuickSort algorithm.
         * You can play with different values, though testing has shown 30 to be an optimal choice.
         */
        private static int MERGESORT_CUTOFF = 30;


        public static int getQuicksortCutoff() {
            return QUICKSORT_CUTOFF;
        }

        public static void setQuicksortCutoff(int quicksortCutoff) {
            if (quicksortCutoff > 0) { // Add your validation rule here.
                QUICKSORT_CUTOFF = quicksortCutoff;
            } else {
                throw new IllegalArgumentException("Threshold must be greater than 0!");
            }
        }

        public static int getMergesortCutoff() {
            return MERGESORT_CUTOFF;
        }

        public static void setMergesortCutoff(int mergeSortCutoff) {
            if (mergeSortCutoff > 0) { // Add your validation rule here.
                MERGESORT_CUTOFF = mergeSortCutoff;
            } else {
                throw new IllegalArgumentException("Threshold must be greater than 0!");
            }
        }
    }


    /**
     * An implementation of the Insertion Sort algorithm.
     * This method sorts the given array in ascending order.
     *
     * @param array the array to be sorted
     */

    public static void insertionSort(int[] array) {
        int len = array.length;
        // We start the loop from the second element to the end
        for (int i, k = 0; ++k < len; ) {
            // We take the current element out and create a hole
            int key = array[i = k];
            // If the current element is smaller than the previous one
            if (key < array[i - 1]) {
                // Keep moving the elements to right until we find the position for the current element
                while (--i >= 0 && key < array[i])
                    array[i + 1] = array[i];
                // Place the current element in its correct position
                array[i + 1] = key;
            }
        }
    }

    /**
     * An implementation of the Bubble Sort algorithm.
     * This method sorts the given array in ascending order.
     *
     * @param array the array to be sorted
     */
    public static void bubbleSort(int[] array) {
        int len = array.length;
        // We start from the beginning of the array
        for (int i, k = 0; ++k < len; ) {
            boolean swapped = false;
            // Go through the array, compare each pair of adjacent elements
            for (i = 0; i < len - k - 1; ++i) {
                // If the current element is greater than the next one
                if (array[i] > array[i + 1]) {
                    // Swap them
                    int temp = array[i];
                    array[i] = array[i + 1];
                    array[i + 1] = temp;
                    swapped = true;
                }
            }
            // If no two elements were swapped by inner loop, then the array is sorted
            if (!swapped) break;
        }
    }

    /**
     * An implementation of the Selection Sort algorithm.
     * This method sorts the given array in ascending order.
     *
     * @param array the array to be sorted
     */
    public static void selectionSort(int[] array) {
        int len = array.length;
        for (int i = 0; i < len - 1; i++) {
            // Find the minimum element in the unsorted sub-array[i..n-1]
            // And swap it with array[i]
            int min = i;
            for (int j = i + 1; j < len; j++) {
                if (array[j] < array[min]) {
                    min = j;
                }
            }
            // Swap the found minimum element with the first element of the unsorted part
            int temp = array[min];
            array[min] = array[i];
            array[i] = temp;
        }
    }

    /**
     * An implementation of the Shell Sort algorithm.
     * This method sorts the given array in ascending order.
     *
     * @param array the array to be sorted
     */

    public static void shellSort(int[] array) {
        int len = array.length;
        // Start with a big gap, then reduce the gap
        for (int gap = len / 2; gap > 0; gap /= 2) {
            // Do a gapped insertion sort for this gap size
            // The first gap elements a[0..gap-1] are already in gapped order
            // Keep adding one more element until the entire array is gap sorted
            for (int i = gap; i < len; i++) {
                // Add a[i] to the elements that have been gap sorted
                // Save a[i] in temp and make a hole at position i
                int temp = array[i];
                // Shift earlier gap-sorted elements up until the correct location for a[i] is found
                int j;
                for (j = i; j >= gap && array[j - gap] > temp; j -= gap) {
                    array[j] = array[j - gap];
                }
                // Put temp (the original a[i]) in its correct location
                array[j] = temp;
            }
        }
    }

    /**
     * Sorts the given array using Multithreaded QuickSort algorithm.
     *
     * @param array the array to be sorted
     */
    public static void quickSort(int[] array) {
        QuickSort.sort(array);
    }

    /**
     * Sorts the given array using Multithreaded MergeSort algorithm.
     *
     * @param array the array to be sorted
     */
    public static void mergeSort(int[] array) {
        MergeSort.sort(array);
    }


    private static class QuickSort extends RecursiveAction {
        // Threshold for problem size below which the problem will be solved directly without more task splitting


        // Array to be sorted
        private final int[] array;
        private final int low;
        private final int high;
        // Shared thread pool
        private static final ForkJoinPool pool = new ForkJoinPool();

        // Constructor to initialize array and indices
        public QuickSort(int[] array, int low, int high) {
            this.array = array;
            this.low = low;
            this.high = high;
        }

        // Main computation performed by this task
        @Override
        protected void compute() {
            // If the problem size is below threshold, solve it directly
            if (high - low <= Parameters.QUICKSORT_CUTOFF) {
                // Simple insertion sort for small arrays
                for (int i, k = low; ++k < high + 1; ) {
                    int key = array[i = k];
                    if (key < array[i - 1]) {
                        while (--i >= low && key < array[i]) {
                            array[i + 1] = array[i];
                        }
                        array[i + 1] = key;
                    }
                }
                return;
            }

            // If problem size is larger, solve it using divide-and-conquer

            // Choosing a pivot element and partitioning the array around the pivot
            // In this case, median of first, middle and last element is chosen as pivot
            int mid = low + (high - low) / 2;
            int lo = Math.min(Math.min(array[low], array[mid]), array[high]);
            int hi = Math.max(Math.max(array[low], array[mid]), array[high]);
            int midVal = array[low] + array[mid] + array[high] - lo - hi;

            int tmp_pivot;
            if (midVal == array[low]) tmp_pivot = low;
            else if (midVal == array[mid]) tmp_pivot = mid;
            else tmp_pivot = high;
            int pivot = array[tmp_pivot];

            // Swapping pivot element to end of array
            array[tmp_pivot] = array[high];
            array[high] = pivot;

            // Partition array around pivot
            int i = low;
            for (int j = low; j < high; j++) {
                if (array[j] < pivot) {
                    int temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                    i++;
                }
            }

            // Swap pivot element to its correct position
            int temp = array[i];
            array[i] = array[high];
            array[high] = temp;
            int pivotIndex = i;

            // Recursively sort elements before and after pivot
            QuickSort leftTask = new QuickSort(array, low, pivotIndex - 1);
            QuickSort rightTask = new QuickSort(array, pivotIndex + 1, high);
            invokeAll(leftTask, rightTask);
        }

        // Method to trigger the parallel quicksort
        public static void sort(int[] array) {
            pool.invoke(new QuickSort(array, 0, array.length - 1));
        }
    }


    private static class MergeSort extends RecursiveAction {
        private final int[] array;
        private final int low;
        private final int high;
        private static final ForkJoinPool pool = new ForkJoinPool();

        public MergeSort(int[] array, int low, int high) {
            this.array = array;
            this.low = low;
            this.high = high;
        }

        @Override
        protected void compute() {
            // If array size is below threshold, sort directly
            if (high - low <= Parameters.MERGESORT_CUTOFF) {
                insertionSort(low, high);
            } else {
                // Else, split the problem and sort each part recursively
                int mid = low + (high - low) / 2;
                MergeSort leftTask = new MergeSort(array, low, mid);
                MergeSort rightTask = new MergeSort(array, mid + 1, high);
                invokeAll(leftTask, rightTask);

                // Merge the sorted parts
                merge(low, mid, high);
            }
        }

        // Insertion sort for small-sized problems
        private void insertionSort(int low, int high) {
            for (int index = low; ++index < high + 1; ) {
                int key = array[index];
                if (key < array[index - 1]) {
                    int cursor = index;
                    while (--cursor >= low && key < array[cursor]) {
                        array[cursor + 1] = array[cursor];
                    }
                    array[cursor + 1] = key;
                }
            }
        }

        // Merge two sorted sections of the array
        private void merge(int low, int mid, int high) {
            // Auxiliary array to hold merged elements
            int[] aux = new int[high - low + 1];
            int leftCursor = low, rightCursor = mid + 1, auxCursor = 0;

            // Merge elements from both sections in order
            while (leftCursor <= mid && rightCursor <= high) {
                if (array[leftCursor] <= array[rightCursor]) aux[auxCursor++] = array[leftCursor++];
                else aux[auxCursor++] = array[rightCursor++];
            }
            // Copy remaining elements from the left section, if any
            while (leftCursor <= mid) aux[auxCursor++] = array[leftCursor++];
            // Copy remaining elements from the right section, if any
            while (rightCursor <= high) aux[auxCursor++] = array[rightCursor++];

            // Copy merged elements back into the original array
            System.arraycopy(aux, 0, array, low, aux.length);
        }

        // Method to trigger the parallel mergesort
        public static void sort(int[] array) {
            pool.invoke(new MergeSort(array, 0, array.length - 1));
        }
    }
}


