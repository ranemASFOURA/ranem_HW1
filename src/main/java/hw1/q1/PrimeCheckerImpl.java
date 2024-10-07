package hw1.q1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


public class PrimeCheckerImpl implements PrimeChecker, Callable<List<Integer>>{
    private final int start;
    private final int end;

    // Constructor to initialize the range
    public PrimeCheckerImpl(int start, int end) {
        this.start = start;
        this.end = end;
}
    @Override
    public List<Integer> findPrimesInRange(int start, int end) {
        List<Integer> primes = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            if (isPrime(i)) {
                primes.add(i);
            }
        }
        return primes;
    }

    @Override
    public List<Integer> call() throws Exception {
        return findPrimesInRange(start, end);
    }

    // Helper method to check if a number is prime
    private boolean isPrime(int num) {
        if (num <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }
}

