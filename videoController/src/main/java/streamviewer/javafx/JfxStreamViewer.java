package streamviewer.javafx;

import media.DVMedia;
import streamviewer.DVStreamViewer;
import streamviewer.StreamViewer;
import util.Clock;
import util.DVStatus;
import util.Identifier;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static sun.java2d.opengl.OGLRenderQueue.sync;

public class JfxStreamViewer implements StreamViewer {

  private final Identifier identifier;
  private Clock clockStream;

  private ClockTimerTask streamClockTask;

  private JfxStreamViewer(Identifier identifier) {
    this.identifier = identifier;
    this.clockStream = Clock.createClock();
    this.clockStream.setElapsedTime(DVStreamViewer.INSTANCE.getMasterCurrentTime());
    createClockTimer();

  }

  public static StreamViewer createStreamViewer(Identifier identifier) {
    return new JfxStreamViewer(identifier);
  }

  @Override
  public Identifier getIdentifier() {
    return identifier;
  }

  @Override
  public void play() {
    this.clockStream.start();
  }

  @Override
  public void pause() {
    this.clockStream.stop();
  }

  @Override
  public void stop() {
    this.clockStream.reset();
  }

  @Override
  public void shuttleForward() {
    this.clockStream.setClockRate(DVStreamViewer.INSTANCE.getRate());
  }

  @Override
  public void shuttleBackward() {
    this.clockStream.setClockRate(DVStreamViewer.INSTANCE.getRate());
  }

  @Override
  public void jogForward() {

  }

  @Override
  public void jogBackward() {

  }

  @Override
  public void seek(long timeInMillis) {
    //For now I will just follow the time of the master clock, the update is already done via DVStreamViewer
    this.clockStream.setElapsedTime(DVStreamViewer.INSTANCE.getMasterCurrentTime());
  }

  @Override
  public void back(long timeInMillis) {
    //For now I will just follow the time of the master clock, the update is already done via DVStreamViewer
    this.clockStream.setElapsedTime(DVStreamViewer.INSTANCE.getMasterCurrentTime());
  }

  @Override
  public DVMedia getMedia() {
    return null;
  }

  @Override
  public DVStatus getStatus() {
    return null;
  }

  @Override
  public void setVolume() {

  }

  @Override
  public void getVolume() {

  }

  @Override
  public void visible() {

  }

  @Override
  public void hide() {

  }

  @Override
  public void close() {
    this.clockStream.reset();
    destroyClockTimer();
  }

  void createClockTimer() {
    if(streamClockTask == null) {
      streamClockTask = new ClockTimerTask(clockStream);
      streamClockTask.start();
    }
  }

  void destroyClockTimer() {
    if(streamClockTask != null) {
      streamClockTask.stop();
      streamClockTask = null;
    }
  }

  class ClockTimerTask extends TimerTask {

    /**
     * Clock tick period in milliseconds
     */
    private static final long CLOCK_SYNC_INTERVAL = 100L;

    /**
     * Clock initial delay in milliseconds
     */
    private static final long CLOCK_SYNC_DELAY = 0L;

    private static final long THRESHOLD = 5L;

    private Timer streamClockTimer = null;
    private WeakReference<Clock> streamClockRef;

    private long sumDelta = 0;
    private long cpt = 0;

    ClockTimerTask(Clock streamClock) {
      streamClockRef = new WeakReference<Clock>(streamClock);
    }

    void start() {
      if(streamClockTimer == null) {
        streamClockTimer = new Timer(true);
        streamClockTimer.scheduleAtFixedRate(this, CLOCK_SYNC_DELAY, CLOCK_SYNC_INTERVAL);
      }
    }

    void stop() {
      if(streamClockTimer != null) {
        streamClockTimer.cancel();
        streamClockTimer = null;
        System.out.println(identifier + " Average Delta = " + sumDelta / cpt);
      }
    }

    void sync(){
      clockStream.setElapsedTime(DVStreamViewer.INSTANCE.getMasterCurrentTime());
    }

    @Override
    public void run() {
      final Clock clock = streamClockRef.get();
      if(clock != null) {
        long masterClock = DVStreamViewer.INSTANCE.getMasterCurrentTime();
        long slaveClock = clockStream.elapsedTime();
        long delta = masterClock - slaveClock;
        if (delta > THRESHOLD){
          pause();
          sync();
        }
        sumDelta += Math.abs(delta);
        cpt++;
        System.out.println(identifier + " Master Clock: " + masterClock + " Stream CLock: " + slaveClock + " Delta: " + (masterClock - slaveClock));
      } else {
        cancel();
      }
    }
  }
}
