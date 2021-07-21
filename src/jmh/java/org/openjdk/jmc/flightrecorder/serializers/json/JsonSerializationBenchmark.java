package org.openjdk.jmc.flightrecorder.serializers.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.flightrecorder.CouldNotLoadRecordingException;
import org.openjdk.jmc.flightrecorder.JfrLoaderToolkit;
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

@State(Scope.Benchmark)
@Warmup(iterations = 5, time = 10)
@Measurement(iterations = 5, time = 10)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(3)
public class JsonSerializationBenchmark {

  private static InputStream readResource(String path) {
    return JsonSerializationBenchmark.class.getClassLoader().getResourceAsStream(path);
  }

  @Param({
      "007760_latency_before.jfr",
      "012641_allocation_fixed.jfr",
      "020065_fibonacci.jfr",
      "062727_memoryleak_fixed.jfr",
      "047826_guimark.jfr",
      "098734_wldf.jfr",
      "116661_exceptions.jfr",
      "504214_allocation_before.jfr"
  })
  public String recordingName;

  private static IItemCollection events;

  @Setup(Level.Trial)
  public void prepare() throws CouldNotLoadRecordingException, IOException {
    InputStream is = readResource(recordingName);
    if (is == null) {
      throw new IllegalStateException();
    }
    events = JfrLoaderToolkit.loadEvents(is);
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public void fastJson(Blackhole bh) {
    bh.consume(FastJsonIItemCollectionJsonMarshaller.toJsonString(events));
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public void adHoc(Blackhole bh) {
    bh.consume(IItemCollectionJsonSerializer.toJsonString(events));
  }
}
