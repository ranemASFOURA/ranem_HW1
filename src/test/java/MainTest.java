import hw1.q1.Main;
import junit.framework.TestCase;
import java.util.List;

public class MainTest extends TestCase {
    public void testFindPrimesWithThreads() throws Exception {
        // Test multithreaded prime finding with 2 threads
        List<Integer> primes = Main.findPrimesWithThreads(1, 100, 2);
        assertEquals(List.of(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97), primes);

        // Test multithreaded prime finding with 4 threads
        List<Integer> primes4Threads = Main.findPrimesWithThreads(1, 100, 4);
        assertEquals(List.of(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97), primes4Threads);

        // Test with a different range
        List<Integer> primesInRange = Main.findPrimesWithThreads(50, 150, 4);
        assertEquals(List.of(53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149), primesInRange);
    }
}
