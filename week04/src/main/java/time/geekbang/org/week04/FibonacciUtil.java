package time.geekbang.org.week04;

public class FibonacciUtil {

    public static long fibonacci(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must greater than or equal to 0");
        }

        int a = 0, b = 1;
        while (n-- > 0) {
            a = (b += a) - a;
        }
        System.out.printf("thread=[%s], ", Thread.currentThread().getName());
        return a;
    }
}
