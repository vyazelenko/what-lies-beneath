package wlb;

public class ComputerBenchmarkWrong {
    private static final int ITERATIONS = 1_000_000_000;

    public static void main(String[] args) {
        Computer computer = new Computer();
        long start = System.nanoTime();
        int result = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            result = computer.compute(0xcafebabe);
        }
        long end = System.nanoTime();
        System.out.printf("%f ns/op, result=%d%n", (double) (end - start) / ITERATIONS, result);
    }
}
