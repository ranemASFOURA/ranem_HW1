import hw1.q1.PrimeCheckerImpl;
import junit.framework.TestCase;
import java.util.List;

public class PrimeCheckerImplTest extends TestCase {

    public void testFindPrimesInRange() {
        // Test case 1: Small range
        PrimeCheckerImpl primeChecker = new PrimeCheckerImpl(1, 10);
        List<Integer> primes = primeChecker.call();
        assertEquals(List.of(2, 3, 5, 7), primes);

        // Test case 2: Large range
        PrimeCheckerImpl largeRangeChecker = new PrimeCheckerImpl(1, 100);
        List<Integer> largePrimes = largeRangeChecker.call();
        assertEquals(List.of(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97), largePrimes);

        // Test case 3: Prime number in a single number range
        PrimeCheckerImpl singlePrimeChecker = new PrimeCheckerImpl(17, 17);
        List<Integer> singlePrime = singlePrimeChecker.call();
        assertEquals(List.of(17), singlePrime);

        // Test case 4: Range without primes
        PrimeCheckerImpl noPrimeChecker = new PrimeCheckerImpl(8, 10);
        List<Integer> noPrimes = noPrimeChecker.call();
        assertEquals(List.of(), noPrimes);
    }

    public void testIsPrime() {
        PrimeCheckerImpl primeChecker = new PrimeCheckerImpl(1, 10);

        // Test prime numbers
        assertTrue(primeChecker.isPrime(2));
        assertTrue(primeChecker.isPrime(3));
        assertTrue(primeChecker.isPrime(5));
        assertTrue(primeChecker.isPrime(7));

        // Test non-prime numbers
        assertFalse(primeChecker.isPrime(1));
        assertFalse(primeChecker.isPrime(4));
        assertFalse(primeChecker.isPrime(6));
        assertFalse(primeChecker.isPrime(9));
    }
}
