// Copyright 2019 Google Inc. All Rights Reserved.

package com.google.re2j.benchmark;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class BenchmarkSubMatch {

  @Param({"RE2J"})
  private Implementations impl;

  @Param({"1000"})
  private int kilobytes;

  @Param({"0", "10"})
  private int percentNoise;

  @Param({"\\d{3}", "9\\d{2}"})
  private String expression;

  private String text;
  private Implementations.Pattern pattern;

  @Setup
  public void setup() {
    text = repeat('a', 100 - percentNoise, '9', percentNoise, kilobytes * 1024) + 909;
    pattern = Implementations.Pattern.compile(impl, expression);
  }

  @Benchmark
  public void findPhoneNumbers(Blackhole bh) {
    Implementations.Matcher matcher = pattern.compile(text);
    while (matcher.find()) {
      bh.consume(matcher.group());
    }
  }

  private static String repeat(char c1, int prob1, char c2, int prob2, int times) {
    StringBuilder sb = new StringBuilder();
    Random r = new Random();
    for (int i = 0; i < times; i++) {
      if (r.nextInt(100) <= prob1) {
        sb.append(c1);
      } else {
        sb.append(c2);
      }

    }
    return sb.toString();
  }
}
