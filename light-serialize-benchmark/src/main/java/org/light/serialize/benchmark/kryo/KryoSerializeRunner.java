package org.light.serialize.benchmark.kryo;

import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

/**
 * @date 2021/05/16
 */
public class KryoSerializeRunner {

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include("org.light.serialize.benchmark.kryo.*")
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
