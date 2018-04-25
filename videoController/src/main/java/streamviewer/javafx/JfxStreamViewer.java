package streamviewer.javafx;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import media.DVMedia;
import streamviewer.DVStreamViewer;
import streamviewer.StreamListener;
import streamviewer.StreamViewer;
import util.DVStatus;
import util.Identifier;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

public class JfxStreamViewer extends Stage implements StreamViewer {

  private final Identifier identifier;

  private final MediaPlayer mediaPlayer;
  private Media sourceMedia;
  private MediaView mediaView;

//  private Clock clockStream;
  private ClockTimerTask streamClockTask;
  private StreamListener streamListener;

  private JfxStreamViewer(Identifier identifier, File sourceFile) {
    this.identifier = identifier;

    this.sourceMedia = new Media(sourceFile.toURI().toString());
    this.mediaPlayer = new MediaPlayer(sourceMedia);

    this.mediaView = new MediaView(mediaPlayer);

    final DoubleProperty width = this.mediaView.fitWidthProperty();
    final DoubleProperty height = this.mediaView.fitHeightProperty();

    width.bind(Bindings.selectDouble(this.mediaView.sceneProperty(), "width"));
    height.bind(Bindings.selectDouble(this.mediaView.sceneProperty(), "height"));

    mediaView.setPreserveRatio(true);

    StackPane root = new StackPane();
    root.getChildren().add(this.mediaView);

    final Scene scene = new Scene(root, 960, 540);
    scene.setFill(Color.BLACK);
    this.setOnCloseRequest(new EventHandler<WindowEvent>() {
      @Override
      public void handle(WindowEvent event) {
        close();
      }
    });

    setScene(scene);
    setTitle(getSourceMedia().getSource());
    show();

//    this.clockStream = Clock.createClock();
//    this.clockStream.setElapsedTime(DVStreamViewer.INSTANCE.getMasterCurrentTime());
    this.mediaPlayer.seek(Duration.millis(DVStreamViewer.INSTANCE.getMasterCurrentTime()));

    createClockTimer();
  }

  public Media getSourceMedia(){ return this.sourceMedia; }

  public static StreamViewer createStreamViewer(Identifier identifier, File sourceFile) {
    return new JfxStreamViewer(identifier, sourceFile);
  }

  @Override
  public Identifier getIdentifier() {
    return this.identifier;
  }

  @Override
  public void play() {
    this.mediaPlayer.play();
    //    this.clockStream.start();
  }

  @Override
  public void pause() {
    this.mediaPlayer.pause();
//    this.clockStream.stop();
  }

  @Override
  public void stop() {
    this.mediaPlayer.stop();
//    this.clockStream.reset();
  }

  @Override
  public void shuttleForward() {
    this.mediaPlayer.setRate(DVStreamViewer.INSTANCE.getRate().getValue());
//    this.clockStream.setClockRate(DVStreamViewer.INSTANCE.getRate());
  }

  @Override
  public void shuttleBackward() {
    this.mediaPlayer.setRate(DVStreamViewer.INSTANCE.getRate().getValue());
//    this.clockStream.setClockRate(DVStreamViewer.INSTANCE.getRate());
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
//    this.clockStream.setElapsedTime(DVStreamViewer.INSTANCE.getMasterCurrentTime());
    this.mediaPlayer.seek(Duration.millis(timeInMillis));
  }

  @Override
  public void back(long timeInMillis) {
    //For now I will just follow the time of the master clock, the update is already done via DVStreamViewer
//    this.clockStream.setElapsedTime(DVStreamViewer.INSTANCE.getMasterCurrentTime());
    this.mediaPlayer.seek(Duration.millis(timeInMillis));
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
    this.mediaPlayer.stop();
    destroyClockTimer();
    this.hide();
  }

  @Override
  public void addStreamListener(StreamListener streamListener){
    this.streamListener = streamListener;
  }

  void createClockTimer() {
    if(streamClockTask == null) {
      streamClockTask = new ClockTimerTask(this.mediaPlayer);
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

    /** Clock tick period in milliseconds */
    private static final long CLOCK_SYNC_INTERVAL = 10L;

    /** Clock initial delay in milliseconds */
    private static final long CLOCK_SYNC_DELAY = 0L;

    private static final long THRESHOLD = 25L;

    private Timer streamClockTimer = null;
    private WeakReference<MediaPlayer> streamClockRef;

    private long sumDelta = 0;
    private long cpt = 0;

    ClockTimerTask(MediaPlayer streamClock) {
      streamClockRef = new WeakReference<>(streamClock);
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
      streamListener.requestSync(identifier);
    }

    @Override
    public void run() {
      final MediaPlayer clock = streamClockRef.get();
      if(clock != null) {
        Platform.runLater(() -> {
          long masterClock = DVStreamViewer.INSTANCE.getMasterCurrentTime();
          long slaveClock = (long) mediaPlayer.getCurrentTime().toMillis();
          long delta = masterClock - slaveClock;
          if (delta > THRESHOLD){
//            pause();
            sync();
          }
          sumDelta += Math.abs(delta);
          cpt++;
          System.out.println(identifier + " Master Clock: " + masterClock + " Stream CLock: " + slaveClock + " Delta: " + (masterClock - slaveClock));
        });
      } else {
        cancel();
      }
    }
  }
}
