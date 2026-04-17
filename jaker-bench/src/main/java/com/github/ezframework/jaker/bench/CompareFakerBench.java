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

import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@Fork(1)
public class CompareFakerBench {

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        @Param({"en-US", "en-GB"})
        public String localeTag;

        public Faker jaker;
        public net.datafaker.Faker dataFaker;
        public com.github.javafaker.Faker javaFaker;
        public Locale locale;

        private final AtomicLong seedCounter = new AtomicLong(1L);

        @Setup(Level.Trial)
        public void setup() {
            locale = Locale.forLanguageTag(localeTag);
            jaker = Jaker.builder().locale(localeTag).seed(12345L).build().faker();
            dataFaker = new net.datafaker.Faker(locale);
            javaFaker = new com.github.javafaker.Faker(locale);
        }

        public Faker buildJaker() {
            return Jaker.builder().locale(localeTag).seed(seedCounter.getAndIncrement()).build().faker();
        }

        public net.datafaker.Faker buildDataFaker() {
            return new net.datafaker.Faker(locale);
        }

        public com.github.javafaker.Faker buildJavaFaker() {
            return new com.github.javafaker.Faker(locale);
        }
    }

    @Benchmark
    public void jakerFirstName(BenchmarkState s, Blackhole bh) {
        bh.consume(s.jaker.name().firstName());
    }

    @Benchmark
    public void dataFakerFirstName(BenchmarkState s, Blackhole bh) {
        bh.consume(s.dataFaker.name().firstName());
    }

    @Benchmark
    public void javaFakerFirstName(BenchmarkState s, Blackhole bh) {
        bh.consume(s.javaFaker.name().firstName());
    }

    @Benchmark
    public void jakerFullPerson(BenchmarkState s, Blackhole bh) {
        bh.consume(s.jaker.person().firstname() + " " + s.jaker.person().lastname());
    }

    @Benchmark
    public void dataFakerFullPerson(BenchmarkState s, Blackhole bh) {
        bh.consume(s.dataFaker.name().firstName() + " " + s.dataFaker.name().lastName());
    }

    @Benchmark
    public void javaFakerFullPerson(BenchmarkState s, Blackhole bh) {
        bh.consume(s.javaFaker.name().firstName() + " " + s.javaFaker.name().lastName());
    }

    @Benchmark
    public void jakerNumberBetween(BenchmarkState s, Blackhole bh) {
        bh.consume(s.jaker.number().numberBetween(1, 1_000_000));
    }

    @Benchmark
    public void dataFakerNumberBetween(BenchmarkState s, Blackhole bh) {
        bh.consume(s.dataFaker.number().numberBetween(1, 1_000_000));
    }

    @Benchmark
    public void javaFakerNumberBetween(BenchmarkState s, Blackhole bh) {
        bh.consume(s.javaFaker.number().numberBetween(1, 1_000_000));
    }

    @Benchmark
    public void jakerCreateFaker(BenchmarkState s, Blackhole bh) {
        bh.consume(s.buildJaker());
    }

    @Benchmark
    public void dataFakerCreateFaker(BenchmarkState s, Blackhole bh) {
        bh.consume(s.buildDataFaker());
    }

    @Benchmark
    public void javaFakerCreateFaker(BenchmarkState s, Blackhole bh) {
        bh.consume(s.buildJavaFaker());
    }
}
