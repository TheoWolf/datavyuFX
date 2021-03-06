package util;

import java.lang.ref.WeakReference;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.*;


/**  */
public class Clock {

  /**  */
  private static final TimeUnit DEFAULT_TIMEUNIT = MILLISECONDS;

  /** in milliseconds */
  private long onset = 0L;

  /** in nanoseconds, default value is 1H: MILLISECONDS.convert(1, HOURS); */
  private long offset = NANOSECONDS.convert(1, HOURS); // will use minutes for testing

  /**  */
  private boolean isRunning;
  private boolean isCycling = false;

  /**  */
  private long startTime;

  /** On Nanos used internally */
  private long elapsedNanos;

  /** In Milliseconds and shared with other object */
  private long currentTime;

  /**  */
  private ClockTimerTask clockTimerTask;

  /** */
  private Rate rate = Rate.playRate(); //will use this rate for now

  /**
   * @return
   */
  public static Clock createClock() { return new Clock(); }

  /** Constructor */
  Clock() { startTime = systemNanoTime(); }

  /**  */
  public Clock start() {
    if(isRunning) { throw new IllegalStateException("The Clock is already running"); }
    if(getRate() == Rate.X0){ rate = Rate.playRate(); }
    isRunning = true;
    startTime = systemNanoTime();
    createClockTimer();
    System.out.println("Clock is Starting");
    return this;
  }

  /**  */
  public Clock stop() {
    long lastTime = systemNanoTime();
    if(! isRunning) { throw new IllegalStateException("The Clock is already stopped"); }
    isRunning = false;
    System.out.println("Clock is Stopping");
    elapsedNanos = (long) (elapsedNanos + (rate.getValue() * (lastTime - startTime)));
    destroyClockTimer();
    return this;
  }

  /**  */
  public Clock reset() {
    elapsedNanos = onset;
    isRunning = false;
    rate = Rate.stopRate();
    System.out.println("Clock Reset");
    destroyClockTimer();
    return this;
  }

  /**  */
  public Clock setElapsedTime(final long timeInMillis) {
    long timeInNanos = convertToNanos(timeInMillis);
    if(!isValidTime(timeInNanos)) {
      throw new IllegalStateException("The Time is not in the Onset Offset Boundary");
    }
    elapsedNanos = timeInNanos;
    System.out.println("Clock Elpased Time : " + elapsedNanos);
    return this;
  }

  public Clock setClockRate(final Rate newRate) {
    rate = newRate;
    System.out.println("Clock Elpased Time : " + elapsedNanos);
    return this;
  }

  public Clock setOnset(final long timeInMillis) {
    long newOnset = convertToNanos(timeInMillis);
    if(newOnset < 0 || newOnset >= offset || newOnset > elapsedNanos()) {
      throw new IllegalStateException("Onset Can't be (-), >= Offset or > current elapsed time");
    }
    onset = newOnset;
    System.out.println("Clock Onset: " + onset);
    return this;
  }

  public Clock setOffset(final long timeInMillis) {
    long newOffset = convertToNanos(timeInMillis);
    if(newOffset <= onset || newOffset < elapsedNanos()) {
      throw new IllegalStateException("Offset Can't be <= Onset or < the current elapsed time");
    }
    offset = convertToNanos(timeInMillis);
    System.out.println("Clock Offset: " + offset);
    return this;
  }

  /**
   * Elapsed time in Milliseconds
   *
   * @return
   */
  public long elapsedTime() { return DEFAULT_TIMEUNIT.convert(elapsedNanos(), NANOSECONDS); }

  /**
   * @param targetUnit
   * @return
   */
  public long elapsedTime(final TimeUnit targetUnit) { return targetUnit.convert(elapsedNanos(), NANOSECONDS); }

  /**
   * @return
   */
  public Duration elapsedDuration() { return Duration.ofNanos(elapsedNanos()); }

  public Rate getRate() { return rate; }

  public long getOnset() { return onset; }

  public long getOffset() { return offset; }

  /**
   * @return
   */
  public boolean isRunning() { return isRunning; }

  public boolean isCycling() { return isCycling; }

  public Clock setCycling(boolean cycling) {
    isCycling = cycling;
    return this;
  }

  @Override
  public String toString() {
    long timeStampNano = elapsedNanos();

    String hours = String.format("%02d", TimeUnit.NANOSECONDS.toHours(timeStampNano));
    String minutes = String.format("%02d", TimeUnit.NANOSECONDS.toMinutes(timeStampNano) - TimeUnit.HOURS.toMinutes(TimeUnit.NANOSECONDS.toHours(timeStampNano)));
    String seconds = String.format("%02d", TimeUnit.NANOSECONDS.toSeconds(timeStampNano) - TimeUnit.MINUTES.toSeconds(TimeUnit.NANOSECONDS.toMinutes(timeStampNano)));
    String millis = String.format("%03d", TimeUnit.NANOSECONDS.toMillis(timeStampNano) - TimeUnit.SECONDS.toMillis(TimeUnit.NANOSECONDS.toSeconds(timeStampNano)));

    String timeStampString = hours + ":" + minutes + ":" + seconds + ":" + millis;

    return timeStampString;
  }

  void createClockTimer(){
    if(clockTimerTask == null){
      clockTimerTask = new ClockTimerTask(this);
      clockTimerTask.start();
    }
  }

  void destroyClockTimer(){
    if(clockTimerTask != null){
      clockTimerTask.stop();
      clockTimerTask = null;
    }
  }

  class ClockTimerTask extends TimerTask {

    /** Clock tick period in milliseconds */
    private static final long CLOCK_SYNC_INTERVAL = 100L;

    /** Clock initial delay in milliseconds */
    private static final long CLOCK_SYNC_DELAY = 0L;

    private Timer masterClockTimer = null;
    private WeakReference<Clock> masterClockRef;

    ClockTimerTask(Clock masterClock){
      masterClockRef = new WeakReference<>(masterClock);
    }

    void start(){
      if (masterClockTimer ==  null){
        masterClockTimer = new Timer(true);
        masterClockTimer.scheduleAtFixedRate(this,CLOCK_SYNC_DELAY,CLOCK_SYNC_INTERVAL);
      }
    }

    void stop(){
      if(masterClockTimer != null){
        masterClockTimer.cancel();
        masterClockTimer = null;
      }
    }

    @Override
    public void run() {
      final Clock clock =  masterClockRef.get();
      if (clock != null){
        clock.updateElapsedNanos();
      }else{
        cancel();
      }
    }
  }

  /** TODO: current time should be removed as there is no reference to this var
   * Only use this method to check the boundary
   */
  private void updateElapsedNanos() {
    if(!isValidTime(elapsedNanos())) {
      stop();
      currentTime = elapsedTime();
      if(isCycling()) {
        reset();
        start();
      }
    } else {
      currentTime = elapsedTime();
    }
  }

  private boolean isValidTime(long timeInNanos) {
    if(timeInNanos < onset || timeInNanos > offset) { return false; }
    return true;
  }

  private long elapsedNanos() {
    return isRunning ? (long) ((rate.getValue() * (systemNanoTime() - startTime)) + elapsedNanos) : elapsedNanos;
  }

  private long systemNanoTime() { return System.nanoTime(); }

  private long convertToNanos(long timeInMillis) { return NANOSECONDS.convert(timeInMillis, MILLISECONDS); }
}

