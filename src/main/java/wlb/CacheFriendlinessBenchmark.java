package wlb;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.shuffle;

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
            // Allocate 4K of random data to force TLB miss as well as cache miss
            byte[] bytes = new byte[4096];
            r.nextBytes(bytes);
            bh.consume(bytes);
        }
        shuffle(values);
        arrayList = new ArrayList<>(size);
        linkedList = new LinkedList<>();
        array = new int[size];
        for (int i = 0; i < size; i++) {
            Integer value = values.get(i);
            linkedList.add(value);
            arrayList.add(value);
            array[i] = value;
            // Allocate 4K of random data to force TLB miss as well as a cache miss
            byte[] bytes = new byte[4096];
            r.nextBytes(bytes);
            bh.consume(bytes);
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
