EDIT: This is one my earlier repos, I need to update it to my recent standarts. <br>
You can check SortingBenchmark, a modern Maven library implementin ga part of this library <br>
https://github.com/atahanuz/sorting-benchmark

# myJavaLib

A java library with a lot of useful utility functions !

## Motivation

Java has a big standart library. But as I started to use Java, I found that a lot of functionalities are missing. For example, you can't easily initialize a LinkedList with default values. So I decided to create a library with utility functions to make the life of a Java programmer easier.

The biggest challenge was making the functions generic, make them work with any type of data (arrays of primitivies, arrays of objects, collections etc.). I had to use a lot of reflection which made the code complicated but very easy to use for the end user.

## Usage

The library consists of 6 classes: Arrays_,Benchmark,Collections_,Data,Print,Sorting

Your only interaction with classes are calling their static functions, which makes the library very easy to use. Example
```
myLib.Print.printClassTree(anArrayList);
```
Output:
```
java.util.ArrayList
java.util.AbstractList
java.util.AbstractCollection
java.lang.Object
```
<sub>*(Arrays_ and Collections_ are named with underscore to avoid conflict java.util.Arrays and java.util.Collections)*</sub>
## Installation

The easiest way to import myJavaLib into your project is downloading the JAR and adding as a library in your IDE (in Intellij, you can do it from Project Structure). Or you can manually copy the source files into your project.

After that, you can call the functions like this:
```
myLib.Arrays_.arrayFromAtoB(3,10)
```
Or you can import myLib.* to use the functions without the myLib prefix in the rest of the code:
```
import myLib.*;
...

Arrays_.arrayFromAtoB(3,10)
var newList= Data.deepCopy(oldList);
```

The library has JavaDoc documentation, so you can see the description of each function by hovering over it in your IDE.

![Javadoc](https://i.imgur.com/s9TiaML.png)

## Functions

### Arrays_
- void shuffle(Object array)
- int[] arrayFromAtoB(int a, int b, int interval)
### Collections_
- &lt;T&gt; Collection&lt;T&gt; initCollection
(String type, T... values)
- &lt;T&gt; void fastSortCollection(Collection&lt;T&gt; coll)
- int compareSets(Object array1, Object array2)
### Data
- &lt;T&gt; T deepCopy(T object)
- boolean writeObjectToFile(Object object, String filename, boolean append)
- Object readObjectFromFile(String path)
- ArrayList<Object> file2Arraylist(String path)
### Print
- void printClassTree(Object object)
- void printSet(Object set) 
### Sorting
- void insertionSort(int[] array)
- void bubbleSort(int[] array)
- void selectionSort(int[] array)
- void shellSort(int[] array)
- void quickSort(int[] array)
- void mergeSort(int[] array)
### Benchmark
- void takeSnapshot() 
- double[] getTimes()
 -long[] getMemories() 
- double sortingBenchmark(Consumer<int[]> sortingMethod, int len)


## Notes

- Java has a big ecosystem with thousands of custom libraries. It is very likely that these functionalities were implemented before, so I am not claiming that I am the "inventor" of something new. The point of my library is being a lightweight,small, easy to use solution.


- In the library I have some exception handling, but I didn't focus much on it. Implemeting exception and error handling for everything would take a lot of effort and would result in harder to read, much longer codes. For example, if a function shuffles a given array, I don't think if it is worth adding the boilerplate code to check if the input is an array. In this project I focused on providing functionality in the most concise code possible. Though I am open to feedback and I will add more exception handling if it is requested.




