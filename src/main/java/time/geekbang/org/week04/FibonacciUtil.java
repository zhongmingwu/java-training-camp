package time.geekbang.org.week04;

public class FibonacciUtil {

    // 0 - 0
    // 1 - 1
    // 2 - 1
    // 3 - 2
    // 4 - 3
    // 5 - 5
    public static long fibonacci(int n) {
        int N = n;
        if (n < 0) {
            throw new IllegalArgumentException("n must greater than or equal to 0");
        }

        int a = 0, b = 1;
        while (n-- > 0) {
            a = (b += a) - a;
        }
        System.out.printf("worker_thread=[%s], fibonacci(%s)=%s, ", Thread.currentThread().getName(), N, a);
        return a;
    }
}
