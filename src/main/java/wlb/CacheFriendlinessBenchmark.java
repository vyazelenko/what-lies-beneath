package wlb;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Fork(3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@OperationsPerInvocation(1024)
public class CacheFriendlinessBenchmark {
    @Param({"1000", "10000", "100000", "1000000"})
    private int size;
    private ArrayList<Integer> values;
    private LinkedList<Integer> linkedList;
    private ArrayList<Integer> arrayList;
    private int[] array;
    private Iterator<Integer> iterator;
    private int index;

    @Setup
    public void setup(Blackhole bh) {
        Random r = new Random(-18379);
        values = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            values.add(r.nextInt());
        }
        arrayList = new ArrayList<>(size);
        linkedList = new LinkedList<>();
        array = new int[size];
        for (int i = 0; i < size; i++) {
            Integer value = values.get(r.nextInt(size));
            linkedList.add(value);
            arrayList.add(value);
            array[i] = value;
            // Allocate some garbage to push the next Node to the another cache line
            for (int j = 0; j < 10; j++) {
                bh.consume(Long.valueOf(r.nextLong()));
            }
        }
    }

    @Benchmark
    public void linkedList(Blackhole bh) {
        if (iterator == null || !iterator.hasNext()) {
            iterator = linkedList.iterator();
        }
        for (int i = 0; i < 1024 && iterator.hasNext(); i++) {
            bh.consume(iterator.next().intValue());
        }
    }

    @Benchmark
    public void arrayList(Blackhole bh) {
        if (iterator == null || !iterator.hasNext()) {
            iterator = arrayList.iterator();
        }
        for (int i = 0; i < 1024 && iterator.hasNext(); i++) {
            bh.consume(iterator.next().intValue());
        }
    }

    @Benchmark
    public void array(Blackhole bh) {
        if (index == array.length) {
            index = 0;
        }
        for (int i = 0; i < 1024 && index < array.length; i++) {
            bh.consume(array[index++]);
        }
    }
}
