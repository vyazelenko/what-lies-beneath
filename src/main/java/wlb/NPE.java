package wlb;

public class NPE {
    public static void main(String[] args) {
        new Thread(() -> {
            foo();
        }, "¯\\_(ツ)_/¯").start();
    }

    private static void foo() {
        throw new NullPointerException("💩💩💩");
    }
}
