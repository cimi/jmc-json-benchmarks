package org.openjdk.jmc.flightrecorder.json;

import java.io.IOException;
import java.io.InputStream;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.flightrecorder.CouldNotLoadRecordingException;
import org.openjdk.jmc.flightrecorder.JfrLoaderToolkit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
public class JsonSerializationBenchmark {

  private static InputStream readResource(String path) {
    InputStream is = JsonSerializationBenchmark.class.getClassLoader().getResourceAsStream(path);
    return JsonSerializationBenchmark.class.getClassLoader().getResourceAsStream(path);
  }

  private static IItemCollection events;

  @Setup(Level.Trial)
  public void doSetup() throws CouldNotLoadRecordingException, IOException {
    InputStream is = readResource("real-recording.jfr");
    if (is == null) {
      throw new IllegalStateException();
    }
    events = JfrLoaderToolkit.loadEvents(is);
  }

  @Benchmark
  public void fastJson(Blackhole bh) {
    bh.consume(FastJsonIItemCollectionJsonMarshaller.toJsonString(events));
  }

  @Benchmark
  public void adHoc(Blackhole bh) {
    bh.consume(AdHocIItemCollectionJsonMarshaller.toJsonString(events));
  }
}
