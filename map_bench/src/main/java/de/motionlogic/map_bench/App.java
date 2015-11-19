package de.motionlogic.map_bench;

import java.util.*;
import org.mapdb.*;

/**
 * The actual benchmark
 *
 */
public class App 
{
	private static String loremIpsum = "Lorem ipsum dolor sit amet, \n" +
				"consectetur adipiscing elit. Vivamus id est aliquam, \n" +
				"posuere nisl non, eleifend nisl. Nam blandit sem lacus, \n" +
                "eget accumsan ligula elementum vel. Vestibulum commodo \n" +
                "nulla a tortor facilisis porta. Suspendisse et imperdiet \n" +
                "lacus, eu faucibus lectus. Vestibulum egestas, dolor \n" +
                "eget auctor tincidunt, sapien leo porttitor dolor, ut \n" +
                "vulputate velit nibh non ex. Vestibulum aliquet, velit \n" +
                "sit amet vehicula rutrum, sem est porttitor tortor, \n" +
                "efficitur scelerisque erat enim consectetur arcu. \n" +
                "Proin ex tortor, rutrum a imperdiet sed, auctor \n" +
                "molestie mauris. Sed eget mollis urna. Aenean molestie \n" +
                "tempus nulla, ac tristique ipsum tincidunt ac. Vivamus \n" +
                "nulla erat, gravida sit amet efficitur nec, interdum in urna.";

    // 20 million KV tuples
    private static int N = 2000000;

    private static void printResults(String coll, int n, long elapsedTime, double occupiedMemory) {
        double n_millions = n / Math.pow(10,6);
        double occupiedMemGB = occupiedMemory / Math.pow(10,9);

        System.out.println(coll + " inserting " + Double.toString(n_millions) + " million elements in " + Long.toString(elapsedTime) + "ms");
        System.out.println("Consumed memory: " + Double.toString(Math.abs(occupiedMemGB)) + "GB");
    }

    public static void main( String[] args )
    {
        /*
         * Run the tests for java.util.Map
         */
        Map<String, String> jMap = new HashMap();


        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemoryBefore = totalMemory - Runtime.getRuntime().freeMemory();
        long startTime = System.currentTimeMillis();

        for(long counter = 0; counter < N; counter += 1) {
            jMap.put(Long.toString(counter), loremIpsum);
        }
        long stopTime = System.currentTimeMillis();
        long freeMemoryAfter = totalMemory - Runtime.getRuntime().freeMemory();

        printResults("java.util.HashMap", N, stopTime - startTime, freeMemoryBefore - freeMemoryAfter);


        /*
         * Run the tests for org.mapdb.HTreeMap
         */
        DB db = DBMaker
            .newMemoryDirectDB()
            .transactionDisable()
            .allocateRecidReuseEnable()
            .asyncWriteEnable()
            .make();

        HTreeMap treeMap = db
            .hashMapCreate("test")
            .keySerializer(Serializer.STRING)
            .valueSerializer(Serializer.STRING)
            .make();


        freeMemoryBefore = totalMemory - Runtime.getRuntime().freeMemory();
        startTime = System.currentTimeMillis();

        for(long counter = 0; counter < N; counter += 1) {
            treeMap.put(Long.toString(counter), loremIpsum);
        }
        stopTime = System.currentTimeMillis();
        freeMemoryAfter = totalMemory - Runtime.getRuntime().freeMemory();

        printResults("org.mapdb.HashMap", N, stopTime - startTime, freeMemoryBefore - freeMemoryAfter);

        /*
         * Run the tests for org.mapdb.HTreeMap
         */
        Map map = db
            .treeMapCreate("test2")
            .keySerializer(Serializer.STRING)
            .valueSerializer(Serializer.STRING)
            .make();


        freeMemoryBefore = totalMemory - Runtime.getRuntime().freeMemory();
        startTime = System.currentTimeMillis();

        for(long counter = 0; counter < N; counter += 1) {
            map.put(Long.toString(counter), loremIpsum);
        }
        stopTime = System.currentTimeMillis();
        freeMemoryAfter = totalMemory - Runtime.getRuntime().freeMemory();

        printResults("org.mapdb.TreeMap", N, stopTime - startTime, freeMemoryBefore - freeMemoryAfter);
    }
}
