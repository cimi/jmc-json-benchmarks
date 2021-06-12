package org.openjdk.jmc.flightrecorder.json.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IType;
import org.openjdk.jmc.flightrecorder.CouldNotLoadRecordingException;
import org.openjdk.jmc.flightrecorder.JfrLoaderToolkit;
import org.openjdk.jmc.flightrecorder.json.AdHocIItemCollectionJsonMarshaller;
import org.skyscreamer.jsonassert.JSONAssert;

@RunWith(Parameterized.class)
public class AdHocItemCollectionJsonMarshallerTest {

  @Parameters
  public static Object[] data() {
    return new Object[] { "latency_before", "wldf" };
  }

  private final String recordingName;

  public AdHocItemCollectionJsonMarshallerTest(String recordingName) {
    this.recordingName = recordingName;
  }

  @Test
  public void testAdHocJsonMarshallerJsonStructure()
      throws IOException, CouldNotLoadRecordingException, JSONException {
    IItemCollection recording = JfrLoaderToolkit.loadEvents(Utils.readResource(recordingName + ".jfr"));
    List<IItem> events = new ArrayList<>();
    Set<IType<IItem>> eventTypes = new HashSet<>();
    for (IItemIterable eventGroup : recording) {
      if (!eventTypes.contains(eventGroup.getType())) {
        // we pick two events from each type
        eventGroup.stream().limit(2).forEach(events::add);
        eventTypes.add(eventGroup.getType());
      }
    }
    String jsonEvents = AdHocIItemCollectionJsonMarshaller.toJsonString(events);
    String expected = Utils.readResourceAsString(recordingName + ".json");
    JSONAssert.assertEquals(expected, jsonEvents, true);
  }
}
