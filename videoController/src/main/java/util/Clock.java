package util;

import javafx.concurrent.Task;

import java.time.Duration;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.*;


/**  */
public class Clock {

    /**  */
    public static TimeUnit DEFAULT_TIMEUNIT = MICROSECONDS;

    /** Clock tick period in milliseconds */
    private static final long CLOCK_SYNC_INTERVAL = 100L;

    /** Clock initial delay in milliseconds */
    private static final long CLOCK_SYNC_DELAY = 0L;


    // Use a range Slider to represent the onset and offset of the Clock
    /** in milliseconds */
    private long onset = 0L;

    /** in milliseconds, default value is 1H: MILLISECONDS.convert(1, HOURS); */
    private long offset = MILLISECONDS.convert(2, MINUTES); // will use minutes for testing

    /**  */
    private boolean isRunning;

    private boolean isCycling = false;

    /**  */
    private long startTime;

    /**  */
    private long elapsedNanos; // bind to the GUI TEXT and DVStreamViewer

    /**  */
    private long currentTime;

    /**  */
    private ScheduledExecutorService currentTimeExecutor;

    //TODO: fix rate bug
    /** */
    private long rate = 1; //will use this rate for now

    /**  */
    private Runnable updateElapsedNanosTask = () -> {
        if (elapsedTime() >= offset){
            stop();
            if (isCycling){
                reset();
                start();
            }
        }
        System.out.println("Current Time " + toString());
    };

    /**
     *
     * @return
     */
    public static Clock createClock() { return new Clock(); }

    /** Constructor */
    Clock(){
        startTime = systemNanoTime();
        currentTimeExecutor = Executors.newSingleThreadScheduledExecutor();
        currentTimeExecutor.scheduleAtFixedRate(updateElapsedNanosTask
                ,CLOCK_SYNC_DELAY,CLOCK_SYNC_INTERVAL, TimeUnit.MILLISECONDS);
    }

    /**  */
    public void start(){
        if(isRunning){ throw new IllegalStateException("The Clock is already running"); }
        isRunning = true;
        startTime = systemNanoTime();
    }

    /**  */
    public void stop(){
        long lastTime =  systemNanoTime();
        if(!isRunning){ throw new IllegalStateException("The Clock is already stopped"); }
        isRunning = false;
        //save the last
        elapsedNanos =  elapsedNanos + (lastTime - startTime);
    }

    /**  */
    public void reset(){
        elapsedNanos = onset;
        isRunning = false;
    }

    /**  */
    public void setElapsedNanos(long timeInMillis){
        elapsedNanos = NANOSECONDS.convert(timeInMillis, MILLISECONDS);
    }

    /**
     *
     * @return
     */
    public boolean isRunning() {return isRunning; }

    /**
     * Elapsed time in Milliseconds
     *
     * @return
     */
    public long elapsedTime() { return DEFAULT_TIMEUNIT.convert(elapsedNanos(), NANOSECONDS); }

    /**
     *
     * @return
     */
    private long systemNanoTime() {return System.nanoTime(); }

    @Override
    public String toString() {
        long timeStampNano =  elapsedNanos();

        String hours = String.format("%02d", TimeUnit.NANOSECONDS.toHours(timeStampNano));
        String minutes = String.format("%02d",TimeUnit.NANOSECONDS.toMinutes(timeStampNano) - TimeUnit.HOURS.toMinutes(TimeUnit.NANOSECONDS.toHours(timeStampNano)));
        String seconds = String.format("%02d",TimeUnit.NANOSECONDS.toSeconds(timeStampNano) - TimeUnit.MINUTES.toSeconds(TimeUnit.NANOSECONDS.toMinutes(timeStampNano)));
        String millis =  String.format("%03d",TimeUnit.NANOSECONDS.toMillis(timeStampNano) - TimeUnit.SECONDS.toMillis(TimeUnit.NANOSECONDS.toSeconds(timeStampNano)));

        String timeStampString = hours + ":" + minutes +":"+ seconds +":"+ millis;

        return timeStampString;
    }

    /**
     *
     * @param targetUnit
     * @return
     */
    public long elapsedTime(TimeUnit targetUnit) { return targetUnit.convert(elapsedNanos(), NANOSECONDS); }

    /**
     *
     * @return
     */
    public Duration elapsedDuration() { return Duration.ofNanos(elapsedNanos()); }

    private long elapsedNanos() {
        return isRunning ? ((rate * (systemNanoTime() - startTime)) + elapsedNanos) : elapsedNanos;
    }

    public static void main (String[] args) throws InterruptedException {
        Clock clock = Clock.createClock();

        clock.start();

        System.out.println("Clock Started ");


    }
}

