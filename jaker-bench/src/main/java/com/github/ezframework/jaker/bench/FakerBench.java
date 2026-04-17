package com.github.ezframework.jaker.bench;

import com.github.ezframework.jaker.Faker;
import com.github.ezframework.jaker.Jaker;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@Fork(2)
public class FakerBench {

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        @Param({"en-US", "en-GB"})
        public String locale;

        public Faker faker;

        private final AtomicLong seedCounter = new AtomicLong(1L);

        @Setup(Level.Trial)
        public void setup() {
            faker = Jaker.builder().locale(locale).seed(12345L).build().faker();
        }

        public Faker buildFaker() {
            return Jaker.builder().locale(locale).seed(seedCounter.getAndIncrement()).build().faker();
        }
    }

    @Benchmark
    public void firstName(BenchmarkState s, Blackhole bh) {
        bh.consume(s.faker.name().firstName());
    }

    @Benchmark
    public void fullPerson(BenchmarkState s, Blackhole bh) {
        bh.consume(s.faker.person().firstname() + " " + s.faker.person().lastname());
    }

    @Benchmark
    public void numberBetween(BenchmarkState s, Blackhole bh) {
        bh.consume(s.faker.number().numberBetween(1, 1_000_000));
    }

    @Benchmark
    public void createFaker(BenchmarkState s, Blackhole bh) {
        bh.consume(s.buildFaker());
    }
}
