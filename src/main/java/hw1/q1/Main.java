package hw1.q1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws Exception {
        int totalStart = 1;
        int totalEnd = 1000000;  // You can adjust this to test on larger ranges

        // List of different thread counts to test
        List<Integer> threadCounts = List.of(1, 2, 4, 8, 16, 32,64);  // Vary this as needed

        // Variable to track the best performance
        double bestTime = Double.MAX_VALUE;
        int bestThreadCount = 1;
        double performanceThreshold = 1.05;  // Threshold for performance improvement

        System.out.println("Testing performance for different thread counts...");

        // Iterate through each thread count and measure the performance
        for (int numThreads : threadCounts) {
            long startTime = System.nanoTime();
            List<Integer> primes = findPrimesWithThreads(totalStart, totalEnd, numThreads);
            long endTime = System.nanoTime();

            double elapsedTimeInSeconds = (endTime - startTime) / 1_000_000_000.0;
            System.out.println("Threads: " + numThreads + ", Time: " + elapsedTimeInSeconds + " seconds, Primes found: " + primes.size());

            // Check if the current configuration has the best time
            if (elapsedTimeInSeconds < bestTime) {
                bestTime = elapsedTimeInSeconds;
                bestThreadCount = numThreads;
            }

            // Stop testing more threads if the performance improvement becomes negligible
            if (bestTime / elapsedTimeInSeconds > performanceThreshold) {
                System.out.println("Performance improvement has plateaued at " + numThreads + " threads.");
                break;
            }
        }

        // Output the best thread count and its time
        System.out.println("\nOptimal number of threads: " + bestThreadCount + ", Best time: " + bestTime + " seconds");
    }

    public static List<Integer> findPrimesWithThreads(int totalStart, int totalEnd, int numThreads) throws Exception {
        int rangeSize = (totalEnd - totalStart + 1) / numThreads;

        // ExecutorService for multithreading
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<List<Integer>>> futures = new ArrayList<>();

        // Submit tasks to executor for each subrange
        for (int i = 0; i < numThreads; i++) {
            int start = totalStart + i * rangeSize;
            int end = (i == numThreads - 1) ? totalEnd : (start + rangeSize - 1);  // Adjust last range

            PrimeCheckerImpl primeChecker = new PrimeCheckerImpl(start, end);
            futures.add(executor.submit(primeChecker));
        }

        // Collect results from each thread
        List<Integer> allPrimes = new ArrayList<>();
        for (Future<List<Integer>> future : futures) {
            allPrimes.addAll(future.get());
        }

        executor.shutdown();  // Shut down the executor service

        return allPrimes;
    }
}
