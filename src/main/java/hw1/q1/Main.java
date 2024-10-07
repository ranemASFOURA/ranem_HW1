package hw1.q1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws Exception {
        int totalStart = 1;
        int totalEnd = 100000;  // Test with a large range

        // List of different thread counts to test
        List<Integer> threadCounts = List.of(1, 2, 4, 8, 16, 32);

        // List of different domain sizes (i.e., sub-range lengths)
        List<Integer> domainSizes = List.of(1000, 50000, 80000, totalEnd);

        // Map to store the best performance (time and thread count) for each domain size
        Map<Integer, PerformanceResult> bestPerformanceForDomains = new HashMap<>();

        System.out.println("Testing performance for different domain sizes and thread counts...\n");

        // Iterate through each domain size
        for (int domainSize : domainSizes) {
            double bestTimeForDomain = Double.MAX_VALUE;  // Track the best time for this domain size
            int bestThreadCountForDomain = 1;

            System.out.println("Testing Domain Size: " + domainSize);

            // Variable to track previous time for calculating improvements
            double prevTime = Double.MAX_VALUE;

            // Iterate through each thread count
            for (int numThreads : threadCounts) {
                long startTime = System.nanoTime();

                // Adjust totalEnd for each domain size
                int adjustedEnd = Math.min(domainSize, totalEnd);
                List<Integer> primes = findPrimesWithThreads(totalStart, adjustedEnd, numThreads);

                long endTime = System.nanoTime();
                double elapsedTimeInSeconds = (endTime - startTime) / 1_000_000_000.0;

                // Calculate performance improvement (compared to previous run)
                double improvement = prevTime == Double.MAX_VALUE ? 0 : ((prevTime - elapsedTimeInSeconds) / prevTime) * 100;

                System.out.println("Threads: " + numThreads + ", Domain Size: " + domainSize
                        + ", Time: " + elapsedTimeInSeconds + " seconds, Primes found: " + primes.size()
                        + ", Improvement: " + String.format("%.2f", improvement) + "%");

                // Check if this is the best time for the current domain size
                if (elapsedTimeInSeconds < bestTimeForDomain) {
                    bestTimeForDomain = elapsedTimeInSeconds;
                    bestThreadCountForDomain = numThreads;
                }

                // Check for saturation point: when improvement is less than 5%
                if (improvement < 5 && numThreads > 1) {
                    System.out.println("Saturation point reached at " + numThreads + " threads for domain size: " + domainSize);
                    break;  // Exit loop for this domain size, as increasing threads won't help
                }

                // Update previous time for the next iteration
                prevTime = elapsedTimeInSeconds;
            }

            // Store the best performance for this domain size
            bestPerformanceForDomains.put(domainSize, new PerformanceResult(bestThreadCountForDomain, bestTimeForDomain));
            System.out.println();
        }

        // Output the best performance for each domain size
        System.out.println("Best performance for each domain size:");
        for (int domainSize : bestPerformanceForDomains.keySet()) {
            PerformanceResult result = bestPerformanceForDomains.get(domainSize);
            System.out.println("Domain Size: " + domainSize + ", Best Thread Count: " + result.getThreadCount()
                    + ", Best Time: " + result.getTime() + " seconds");
        }
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

    // Class to store the performance result for each domain size
    static class PerformanceResult {
        private final int threadCount;
        private final double time;

        public PerformanceResult(int threadCount, double time) {
            this.threadCount = threadCount;
            this.time = time;
        }

        public int getThreadCount() {
            return threadCount;
        }

        public double getTime() {
            return time;
        }
    }
}
