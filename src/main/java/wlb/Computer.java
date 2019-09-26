package wlb;

public class Computer {
    private int add(int value) {
        return value + 254;
    }

    public int compute(int value) {
        int divisor = 0xdeadbeef;
        return add(value / divisor);
    }

    public static void main(String[] args) {
        System.out.println(new Computer().compute(0xcafebabe));
    }
}
