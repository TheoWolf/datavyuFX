package util;

import javafx.concurrent.Task;

import java.time.Duration;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;


/**
 *
 */
public class Clock {

    //TODO: Create a class of static variables
    /**
     *
     */
    public static TimeUnit DEFAULT_TIMEUNIT = MICROSECONDS;

    /**
     * Clock tick period in milliseconds
     */
    private static final long CLOCK_SYNC_INTERVAL = 100L;

    /**
     * Clock initial delay in milliseconds
     */
    private static final long CLOCK_SYNC_DELAY = 0L;


    // Use a range Slider to represent the onset and offset of the Clock
    /**
     * //TODO: bind to the UI
     */
    private long onset = 0;

    /**
     * //TODO: bind to UI
     */
    private long offset = 5000000L;

    /**
     *
     */
    private boolean isRunning;

    /**
     *
     */
    private long startTime;

    /**
     *
     */
    private long elapsedNanos; // bind to the GUI TEXT and DVStreamViewer

    /**
     * //TODO: bind to UI
     */
    private long currentTime;

    /**
     *
     */
    private ScheduledExecutorService currentTimeExecutor;

    /**
     *
     */
    private Runnable updateElapsedNanosTask = () -> {
        if (elapsedTime() >= offset){
            stop();
            reset();
            start();
        }
        System.out.println("Current Time " + toString());
    };

    /**
     * TODO: bind to UI
     * TODO: fix rate bug
     */
    private long rate = 1; //will use this rate for now

    /**
     *
     * @return
     */
    public static Clock createClock() { return new Clock(); }

    /**
     * Constructor
     */
    Clock(){

        startTime = systemNanoTime();

        currentTimeExecutor = Executors.newSingleThreadScheduledExecutor();

        currentTimeExecutor.scheduleAtFixedRate(updateElapsedNanosTask
                ,CLOCK_SYNC_DELAY,CLOCK_SYNC_INTERVAL, TimeUnit.MILLISECONDS);
    }

    /**
     *
     */
    public void start(){
        if(isRunning){ throw new IllegalStateException("The Clock is already running"); }
        isRunning = true;
        startTime = systemNanoTime();
    }

    /**
     *
     */
    public void stop(){
        long lastTime =  systemNanoTime();
        if(!isRunning){ throw new IllegalStateException("The Clock is already stopped"); }
        isRunning = false;
        //save the last
        elapsedNanos =  elapsedNanos + (lastTime - startTime);
    }

    /**
     *
     */
    public void reset(){
        elapsedNanos = onset;
        isRunning = false;
    }

    /**
     *
     * @return
     */
    public boolean isRunning() {return isRunning; }

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
    public long elapsedTime() { return DEFAULT_TIMEUNIT.convert(elapsedNanos(), NANOSECONDS); }

    /**
     *
     * @return
     */
    public Duration elapsedDuration() { return Duration.ofNanos(elapsedNanos()); }

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


    private long elapsedNanos() {
        return isRunning ? ((rate * (systemNanoTime() - startTime)) + elapsedNanos) : elapsedNanos;
    }

    public static void main (String[] args) throws InterruptedException {
        Clock clock = Clock.createClock();

        clock.start();
        System.out.println("Clock Started ");
    }
}

