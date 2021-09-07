package org.light.serialize.benchmark.light;

import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

/**
 * @author alex
 */
public class LightSerializeRunner {

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include("org.light.serialize.benchmark.light.*")
                .warmupIterations(2)
                .warmupBatchSize(10)
                .warmupTime(TimeValue.seconds(2))
                .measurementIterations(3)
                .measurementBatchSize(10)
                .measurementTime(TimeValue.seconds(1))
                .mode(Mode.Throughput)
                .forks(1)
                .threads(1)
                .build();

        new Runner(opt).run();

    }
}
