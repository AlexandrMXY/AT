package ru.mephi.bakinaa.lab1;

import ru.mephi.bakinaa.lab1.validation.NfsValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Benchmark {
    public static final int DEFAULT_REPS_PER_ITER = 10000;

    private final int repeatsPerIter;
    private final int step;
    private final int first;
    private final int last;
    private final Function<Integer, String> supplier;

    private final String[] data;

    public Benchmark(int repeatsPerIter, int step, int first, int last, Function<Integer, String> supplier) {
        this.repeatsPerIter = repeatsPerIter;
        this.step = step;
        this.first = first;
        this.last = last;
        this.supplier = supplier;
        data = new String[repeatsPerIter];
    }

    public Benchmark(Function<Integer, String> supplier) {
        this(10000, 1, 9, NfsValidator.MAX_LENGTH_INCLUDE_HEADER, supplier);
    }

    public void run(List<NfsValidator> validators) {
        run(validators, true);
    }

    public void run(List<NfsValidator> validators, boolean print) {
        List<Long> results = validators.stream().map((v) -> 0L).collect(Collectors.toCollection(ArrayList::new));

        for (int i = first; i < last; i += step) {
            initTestData(i);

            for (int val = 0; val < validators.size(); val++) {
                NfsValidator validator = validators.get(val);

                long begin = System.currentTimeMillis();
                for (int j = 0; j < repeatsPerIter; j++)
                    validator.isValid(data[j]);
                long time = System.currentTimeMillis() - begin;

                results.set(val, time);
            }

            if (!print)
                continue;

            System.out.print(i);
            for (long time : results)
                System.out.print(" " + time);
            System.out.println();
        }
    }

    private void initTestData(int len) {
        for (int i = 0; i < data.length; i++)
            data[i] = supplier.apply(len);
    }
}
