package wlb;

public class Computer {
    private int add(int value) {
        return value + 254;
    }

    public int compute(int value) {
        return add(value / 0xdeadbeef);
    }

    public static void main(String[] args) {
        System.out.println(new Computer().compute(0xcafebabe));
    }
}
