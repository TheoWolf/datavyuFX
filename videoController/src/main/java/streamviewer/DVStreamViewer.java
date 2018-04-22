package streamviewer;

import javafx.beans.property.ReadOnlyObjectWrapper;

import media.DVMedia;
import streamviewer.javafx.JfxStreamViewer;

import util.Clock;
import util.DVStatus;
import util.Identifier;
import util.Rate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A thread safe singleton and Serializable StreamViewer
 */
public enum DVStreamViewer implements StreamViewer {

  INSTANCE;

  private final Identifier identifier = Identifier.createIdentifierZero();
  private DVMedia dvMedia;
  private Clock masterCurrentTime;

  // StreamViewers to be notified
  private Map<Identifier, StreamViewer> streams = new HashMap<>();
  private ReadOnlyObjectWrapper<DVStatus> status ;

  DVStreamViewer() {
    init();
  }

  private void init() {
    dvMedia = DVMedia.INSTANCE;
    masterCurrentTime = Clock.createClock();
    status = new ReadOnlyObjectWrapper<>(DVStatus.READY);// For testing
    streams.put(identifier, this);
  }

  @Override
  public DVMedia getMedia() { return dvMedia.INSTANCE; }

  @Override
  public Identifier getIdentifier() { return identifier; }


  public long getMasterCurrentTime() {
    return this.masterCurrentTime.elapsedTime();
  }

  public void addStream(StreamViewer stream) {
    if(stream == null) { throw new IllegalArgumentException(" An instance of StreamViewer Interface is needed"); }
    streams.put(stream.getIdentifier(), stream);
  }

  public void removeStream(StreamViewer stream){
    if((stream == null)
        || (stream.getIdentifier().asLong() == 0)) {
      throw new IllegalArgumentException(" An instance of StreamViewer Interface is needed");
    }
    stream.close();
    streams.remove(stream);
  }

  public Map<Identifier, StreamViewer> getStreams() {
    return this.streams;
  }

  @Override
  public void play() {
    //Play all StreamViewer
    if(getStatus() != DVStatus.DISPOSED) {
      if(getStatus() == DVStatus.READY){
        streams.entrySet().parallelStream().forEach(e -> {
          if(e.getKey().asLong() == 0) {
            masterCurrentTime.start();
          } else {
            e.getValue().play();
          }
        });
      }
      //TODO: buffer unhandled requests !!
    }

  }

  @Override
  public void pause() {
    //pause all StreamViewer
    if(getStatus() != DVStatus.DISPOSED) {
      if(getStatus() == DVStatus.READY) {
        streams.entrySet().parallelStream().forEach(e -> {
          if(e.getKey().asLong() == 0) {
            masterCurrentTime.stop();
          } else {
            e.getValue().pause();
          }
        });
      }
    }
  }

  @Override
  public void stop() {
    //Stop all StreamViewer
    if(getStatus() != DVStatus.DISPOSED) {
      if(getStatus() == DVStatus.READY) {
        streams.entrySet().parallelStream().forEach(e -> {
          if(e.getKey().asLong() == 0) {
            masterCurrentTime.reset();
          } else {
            e.getValue().stop();
          }
        });
      }
    }
  }

  @Override
  public void shuttleForward() {
    //Shuttle Forward all StreamViewer
    streams.entrySet()
        .parallelStream()
        .forEach(e -> {
          if(e.getKey().asLong() == 0) {
            masterCurrentTime.setClockRate(masterCurrentTime.getRate().next());
          } else {
            e.getValue().shuttleForward();
          }
        });
  }

  @Override
  public void shuttleBackward() {
    //Shuttle Backward all StreamViewer
    streams.entrySet().parallelStream().forEach(e -> {
      if(e.getKey().asLong() == 0) {
        masterCurrentTime.setClockRate(masterCurrentTime.getRate().previous());
      } else {
        e.getValue().shuttleBackward();
      }
    });
  }

  @Override
  public void jogForward() {
    //Jog Forward all StreamViewer
  }

  @Override
  public void jogBackward() {
    //Jog Backward all StreamViewer
  }

  @Override
  public void seek(long timeInMillis) {
    //Seek all StreamViewer
    if(getStatus() == DVStatus.DISPOSED){
      return;
    }
    streams.entrySet().parallelStream().forEach(e -> {
      if(e.getKey().asLong() == 0) {
        masterCurrentTime.setElapsedTime(timeInMillis);
      } else {
        e.getValue().shuttleBackward();
      }
    });
  }

  @Override
  public void back(long timeInMillis) {
    //Jog Backward all StreamViewer
    streams.entrySet().parallelStream().forEach(e -> {
      if(e.getKey().asLong() == 0) {
        masterCurrentTime.setElapsedTime(masterCurrentTime.elapsedTime() - timeInMillis);
      } else {
        e.getValue().back(timeInMillis);
      }
    });
  }

  @Override
  public DVStatus getStatus() {
    return status == null ? DVStatus.UNKNOWN : status.get();
  }

  @Override
  public void setVolume() {
    //Change the Volume of all the StreamViewer
  }

  @Override
  public void getVolume() {

  }

  @Override
  public void visible() {
    //Show all the StreamViewer
  }

  @Override
  public void hide() {
    //Hide all the StreamViewer
  }

  @Override
  public void close() {
    //Close all the StreamViewer and release resources
    streams.entrySet().parallelStream().forEach(e -> {
      if(e.getKey().asLong() == 0) {
        masterCurrentTime.reset();
      } else {
        e.getValue().close();
      }
    });
  }

  public Rate getRate() {
    return masterCurrentTime.getRate();
  }

  public static void main(String[] args) {
    StreamViewer jfxViewer = JfxStreamViewer.createStreamViewer(Identifier.generateIdentifier());
    StreamViewer jfxViewer2 = JfxStreamViewer.createStreamViewer(Identifier.generateIdentifier());
    StreamViewer jfxViewer3 = JfxStreamViewer.createStreamViewer(Identifier.generateIdentifier());
    StreamViewer jfxViewer4 = JfxStreamViewer.createStreamViewer(Identifier.generateIdentifier());
    StreamViewer jfxViewer5 = JfxStreamViewer.createStreamViewer(Identifier.generateIdentifier());

    DVStreamViewer.INSTANCE.addStream(jfxViewer);
    DVStreamViewer.INSTANCE.addStream(jfxViewer2);
    DVStreamViewer.INSTANCE.addStream(jfxViewer3);
    DVStreamViewer.INSTANCE.addStream(jfxViewer4);
    DVStreamViewer.INSTANCE.addStream(jfxViewer5);

    DVStreamViewer.INSTANCE.play();

    try {
      TimeUnit.SECONDS.sleep(10);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    DVStreamViewer.INSTANCE.close();
  }
}
