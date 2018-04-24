package streamviewer;

import media.DVMedia;
import org.junit.Test;
import streamviewer.javafx.JfxStreamViewer;
import util.Identifier;

import static org.junit.Assert.*;

public class DVStreamViewerTest {

  @Test
  public void getMedia() {
    assertTrue(DVStreamViewer.INSTANCE.getMedia() instanceof DVMedia);
  }

  @Test
  public void getIdentifier() {
    assertEquals(DVStreamViewer.INSTANCE.getIdentifier().asLong(), 0L);
  }

  @Test
  public void getMasterCurrentTime() {

  }

  @Test
  public void play() {
  }

  @Test
  public void pause() {
  }

  @Test
  public void addStream() {
    //There is always one stream in the streams HashMap, the DVStreamViewer is added
    //to the streams Map when created, and we have to consider it when checking the size of
    //streams
    int streamNumber = 3;

    for(int i = 0; i < streamNumber; i++) {
      StreamViewer stream = JfxStreamViewer.createStreamViewer(Identifier.generateIdentifier(), null);
      DVStreamViewer.INSTANCE.addStream(stream);
    }
    assertEquals(4, DVStreamViewer.INSTANCE.getStreams().size());
  }

  @Test
  public void removeStream() {
    //There is always one stream in the streams HashMap, the DVStreamViewer is added
    //to the streams Map when created, and we have to consider it when checking the size of
    //streams
    StreamViewer stream1 = JfxStreamViewer.createStreamViewer(Identifier.generateIdentifier(), null);
    StreamViewer stream2 = JfxStreamViewer.createStreamViewer(Identifier.generateIdentifier(), null);
    DVStreamViewer.INSTANCE.addStream(stream1);
    DVStreamViewer.INSTANCE.addStream(stream2);
    assertEquals(3, DVStreamViewer.INSTANCE.getStreams().size());
    DVStreamViewer.INSTANCE.removeStream(stream1);
    assertEquals(2, DVStreamViewer.INSTANCE.getStreams().size());
  }


  @Test
  public void stop() {
    //Stop all StreamViewer
  }

  @Test
  public void shuttleForward() {
    //Shuttle Forward all StreamViewer
  }

  @Test
  public void shuttleBackward() {
    //Shuttle Backward all StreamViewer
  }
}