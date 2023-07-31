package myJavaLib;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * Utility class that provides static methods for manipulating collections and arrays.
 */
public abstract class Collections_ {

    // Declare a Map to hold Suppliers of different Collection types
    private static final Map<String, Supplier<Collection<Object>>> collectionSuppliers;

    static {
        // Initialize the Map with Suppliers of different Collection types
        collectionSuppliers = new HashMap<>();
        // For each entry, key represents the type of Collection, Value is a Supplier that generates an instance of the Collection
        collectionSuppliers.put("arraylist", ArrayList::new);
        collectionSuppliers.put("linkedlist", LinkedList::new);
        collectionSuppliers.put("hashset", HashSet::new);
        collectionSuppliers.put("treeset", TreeSet::new);
        collectionSuppliers.put("linkedhashset", LinkedHashSet::new);
        collectionSuppliers.put("vector", Vector::new);
        collectionSuppliers.put("stack", Stack::new);
        collectionSuppliers.put("priorityqueue", PriorityQueue::new);
        collectionSuppliers.put("concurrentlinkeddeque", ConcurrentLinkedDeque::new);
        collectionSuppliers.put("concurrentlinkedqueue", ConcurrentLinkedQueue::new);
        collectionSuppliers.put("copyonwritearraylist", CopyOnWriteArrayList::new);
        collectionSuppliers.put("copyonwritearrayset", CopyOnWriteArraySet::new);
        collectionSuppliers.put("priorityblockingqueue", PriorityBlockingQueue::new);
        collectionSuppliers.put("concurrentskipset", ConcurrentSkipListSet::new);
    }

    /**
     * Creates a new collection of the specified type and initializes it with the provided values.
     *
     * @param type   the type of the collection to create,without spaces (arraylist, linkedlist, hashset etc.)
     * @param values the values to add to the collection, as many as you want.
     * @return the created collection.
     * @throws IllegalArgumentException if an invalid collection type is provided
     */
    public static <T> Collection<T> initCollection(String type, T... values) {
        // Obtain the appropriate Collection supplier based on the input type string
        Supplier<Collection<Object>> supplier = collectionSuppliers.get(type.toLowerCase());

        // If no supplier is found for the provided type, throw an exception
        if (supplier == null) {
            throw new IllegalArgumentException("Invalid Collection Type: " + type);
        }

        // Generate a new Collection using the supplier
        Collection<Object> collection = supplier.get();

        // Add all input values to the new Collection
        collection.addAll(Arrays.asList(values));

        // Return the filled collection
        return (Collection<T>) collection;
    }

    /**
     * Sorts the collection in its natural order. Works faster than Java's {@link Collections#sort(List)}
     *
     * @param coll the collection to sort
     * @throws IllegalArgumentException if the collection contains different types of objects or non-Comparable objects
     */

    public static <T> void fastSortCollection(Collection<T> coll) {
        // If the collection contains different types of objects or non-Comparable objects, it throws an exception

        // Check if all elements in the collection are of the same type
        Class<?> clazz = coll.iterator().next().getClass();
        for (Object item : coll) {
            if (!clazz.equals(item.getClass())) {
                throw new IllegalArgumentException("All elements in the collection must be of the same type!");
            }
        }

        // Check if all elements in the collection implement Comparable
        if (coll.iterator().next() == null)
            throw new IllegalArgumentException("All elements in the collection must implement Comparable!");

        // Convert the collection to an array, sort it and then clear and refill the collection
        T[] tmpArr = coll.toArray((T[]) Array.newInstance(coll.iterator().next().getClass(), coll.size()));

        coll.clear();
        Arrays.sort(tmpArr);
        Collections.addAll(coll, tmpArr);
    }

    /**
     * Sorts the collection using the provided comparator. Works faster than Java's {@link Collections#sort(List)}
     *
     * @param coll The collection to sort
     * @param cmp  The comperator (you can leave it empty)
     * @throws IllegalArgumentException if the collection contains different types of objects or non-Comparable objects
     */
    public static <T> void fastSortCollection(Collection<T> coll, Comparator<? super T> cmp) {
        // This function sorts a collection of objects based on a provided Comparator
        // If the collection contains different types of objects, it throws an exception

        // Check if all elements in the collection are of the same type
        Class<?> clazz = coll.iterator().next().getClass();
        for (Object item : coll) {
            if (!clazz.equals(item.getClass())) {
                throw new IllegalArgumentException("All elements in the collection must be of the same type!");
            }
        }

        // Check if all elements in the collection implement Comparable
        if (!(coll.iterator().next() instanceof Comparable))
            throw new IllegalArgumentException("All elements in the collection must implement Comparable!");

        // Convert the collection to an array, sort it with the comparator and then clear and refill the collection
        T[] tmpArr = coll.toArray((T[]) Array.newInstance(coll.iterator().next().getClass(), coll.size()));

        coll.clear();
        Arrays.parallelSort(tmpArr, cmp);
        Collections.addAll(coll, tmpArr);
    }


    /**
     * Compares two collections/arrays element by element. Returns the number of elements that are not equal.
     *
     * @param array1 An array or collection
     * @param array2 An array or collection
     * @return Number of different elements
     * @throws IllegalArgumentException if either array1 or array2 is not an array or collection
     */

    public static int compareSets(Object array1, Object array2) {

        boolean flag = false;
        if (array1 == null || array2 == null)
            throw new IllegalArgumentException("Inputs are null !");


        // Variable initialization
        int len1, len2, counter = 0, select = 0;
        Iterator it1 = null, it2 = null;

        // Check if array1 is a collection, if so, initialize its iterator and length
        if (array1 instanceof AbstractCollection) {
            it1 = ((AbstractCollection<?>) array1).iterator();
            len1 = ((AbstractCollection<?>) array1).size();
            select += 1;
        } else {
            if (!array1.getClass().isArray()) {
                throw new IllegalArgumentException("Array1 is not an array/collection");
            }
            // If not a collection, it's an array - get its length
            len1 = Array.getLength(array1);
        }

        // Do the same for array2
        if (array2 instanceof AbstractCollection) {
            it2 = ((AbstractCollection<?>) array2).iterator();
            len2 = ((AbstractCollection<?>) array2).size();
            select += 2;
        } else {
            if (!array2.getClass().isArray()) {
                throw new IllegalArgumentException("Array1 is not an array/collection");
            }
            len2 = Array.getLength(array2);
        }

        // Get the length of the smallest array or collection
        int len = Math.min(len1, len2);

        // select will be: 0 if both are arrays, 1 if array1 is a collection and array2 is an array,
        // 2 if array1 is an array and array2 is a collection, 3 if both are collections.
        switch (select) {
            case 0:
                // Compare each element in the two arrays, increment counter if they are not equal
                for (int i = 0; i < len; i++)
                    if (!Array.get(array1, i).equals(Array.get(array2, i))) counter++;
                break;
            case 1:
                // Compare each element in the array and collection, increment counter if they are not equal
                for (int i = 0; i < len; i++)
                    if (!Array.get(array2, i).equals(it1.next())) counter++;
                break;
            case 2:
                // Similar to case 1, but array1 is an array and array2 is a collection
                for (int i = 0; i < len; i++)
                    if (!Array.get(array1, i).equals(it2.next())) counter++;
                break;
            case 3:
                // Compare each element in the two collections, increment counter if they are not equal
                for (int i = 0; i < len; i++)
                    if (!it1.next().equals(it2.next())) counter++;
                break;
        }
        // Return the count of unequal pairs
        return counter;
    }
}





