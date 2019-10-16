package wlb;

import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Fork(3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@OperationsPerInvocation(1024)
public class CacheFriendlinessBenchmark {
    @Param({"1000", "10000", "100000", "1000000"})
    private int size;
    private List<Integer> linkedList;
    private List<Integer> arrayList;
    private int[] array;
    private ArrayList<Integer> values;
    private Iterator<Integer> iterator;
    private int index;

    @Setup
    public void setup() {
        Random r = new Random(-18379);
        values = new ArrayList<>(size);
        linkedList = new LinkedList<>();
        arrayList = new ArrayList<>(size);
        array = new int[size];
        for (int i = 0; i < size; i++) {
            values.add(r.nextInt());
        }
        for (int i = 0; i < size; i++) {
            linkedList.add(values.get(r.nextInt(size)));
            arrayList.add(values.get(r.nextInt(size)));
            array[i] = values.get(r.nextInt(size));
        }
    }

    @Benchmark
    public void linkedList() {
        if (iterator == null || !iterator.hasNext()) {
            iterator = linkedList.iterator();
        }
        for (int i = 0; i < 1024 && iterator.hasNext(); i++) {
            sink(iterator.next());
        }
    }

    @Benchmark
    public void arrayList() {
        if (iterator == null || !iterator.hasNext()) {
            iterator = arrayList.iterator();
        }
        for (int i = 0; i < 1024 && iterator.hasNext(); i++) {
            sink(iterator.next());
        }
    }

    @Benchmark
    public void array() {
        if (index == array.length) {
            index = 0;
        }
        for (int i = 0; i < 1024 && index < array.length; i++) {
            sink(array[index++]);
        }
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    private void sink(int value) {
        // intentionally empty
    }

}
