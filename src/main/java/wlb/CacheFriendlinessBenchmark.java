package wlb;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.max;

@Fork(3)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 5)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class CacheFriendlinessBenchmark {
    @Param({"1024", "16384", "131072", "1048576"})
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
            // Push Integer objects to separate cache lines
            for (int j = 0, end = max(7, 7 + r.nextInt(12)); j < end; j++) {
                bh.consume(Long.valueOf(r.nextLong()));
            }
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
            for (int j = 0, end = max(9, 9 + r.nextInt(16)); j < end; j++) {
                bh.consume(Float.valueOf(r.nextFloat()));
            }
        }
    }

    @Benchmark
    @OperationsPerInvocation(1024)
    public void linkedList(Blackhole bh) {
        Iterator<Integer> it = iterator;
        if (it == null || !it.hasNext()) {
            iterator = it = linkedList.iterator();
        }
        for (int i = 0; i < 1024 && it.hasNext(); i++) {
            bh.consume(it.next().intValue());
        }
    }

    @Benchmark
    @OperationsPerInvocation(1024)
    public void arrayList(Blackhole bh) {
        Iterator<Integer> it = iterator;
        if (it == null || !it.hasNext()) {
            iterator = it = arrayList.iterator();
        }
        for (int i = 0; i < 1024 && it.hasNext(); i++) {
            bh.consume(it.next().intValue());
        }
    }

    @Benchmark
    @OperationsPerInvocation(1024)
    public void array(Blackhole bh) {
        int[] array = this.array;
        int index = this.index;
        if (index == array.length) {
            index = 0;
        }
        for (int i = 0; i < 1024 && index < array.length; i++) {
            bh.consume(array[index++]);
        }
        this.index = index;
    }
}
