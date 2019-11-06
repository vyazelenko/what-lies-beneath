package wlb;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.shuffle;

@Fork(3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@OperationsPerInvocation(1024)
public class CacheFriendlinessBenchmark {
    @Param({"1000", "10000", "100000", "1000000"})
    private int size;
    private ArrayList<Integer> values;
    private List<Integer> linkedList;
    private List<Integer> arrayList;
    private int[] array;
    private Iterator<Integer> iterator;
    private int index;

    @Setup
    public void setup() {
        Random r = new Random(-18379);
        values = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            values.add(r.nextInt());
        }
        arrayList = new ArrayList<>(values);
        shuffle(arrayList, r);
        linkedList = new ArrayList<>(values);
        shuffle(linkedList, r);
        array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = values.get(r.nextInt(size));
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
