package ru.mephi.bakinaa.lab1;

import ru.mephi.bakinaa.lab1.validation.NfsValidator;
import ru.mephi.bakinaa.lab1.validation.RegExValidator;
import ru.mephi.bakinaa.lab1.validation.flex.FlexValidator;
import ru.mephi.bakinaa.lab1.validation.smc.SmcValidator;

import java.io.*;
import java.util.List;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        ArgsParser.Params params;
        try {
            params = new ArgsParser().parseArgs(args);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        if (params.benchmark) {
            benchmark();
            return;
        }

        if (params.fileMode) {
            try(InputStream is = new FileInputStream(params.sourceFileName);
                OutputStream os = new FileOutputStream(params.targetFileName)) {
                new DataAnalyzer(params.validator).analyze(is, os);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Use Ctrl+D to finish input");
            new DataAnalyzer(params.validator).analyze(System.in, System.out);
        }
    }

    private static void benchmark() {
        DataGenerator generator = new DataGenerator();
        List<NfsValidator> validators = List.of(new RegExValidator(), new SmcValidator(), new FlexValidator());

        System.out.println("Valid strings");
        Benchmark validBenchmark = new Benchmark(generator::next);
        // Warmup
        validBenchmark.run(validators, false);

        validBenchmark.run(validators);

        System.out.println("\nInvalid strings 1");
        Benchmark invalidBenchmark1 = new Benchmark(generator::nextInvalid);
        invalidBenchmark1.run(validators);

        System.out.println("\nInvalid strings 2");
        Benchmark invalidBenchmark2 = new Benchmark(Benchmark.DEFAULT_REPS_PER_ITER,
                100, 100, 10000, generator::nextInvalid);
        invalidBenchmark2.run(validators);
    }
}
/*

Benchmark                  Mode  Cnt        Score       Error  Units
ValidatorBenchmark.flex   thrpt   20   262167,570 � 28518,955  ops/s
ValidatorBenchmark.regex  thrpt   20  1314850,817 � 65743,958  ops/s
ValidatorBenchmark.smc    thrpt   20   945162,797 � 21208,259  ops/s




 */