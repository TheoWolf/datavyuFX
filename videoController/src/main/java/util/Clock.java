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
    private static final long CLOCK_SYNC_INTERVAL = 10L;

    /** Clock initial delay in milliseconds */
    private static final long CLOCK_SYNC_DELAY = 0L;


    // Use a range Slider to represent the onset and offset of the Clock
    /** in milliseconds */
    private long onset = 0L;

    /** in nanoseconds, default value is 1H: MILLISECONDS.convert(1, HOURS); */
    private long offset = NANOSECONDS.convert(1, HOURS); // will use minutes for testing

    /**  */
    private boolean isRunning;

    private boolean isCycling = true;

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
    private Rate rate = Rate.playRate(); //will use this rate for now

//    /**  */
//    private Runnable updateElapsedNanosTask = () -> {
//        currentTime = elapsedNanos();
//        if (currentTime >= offset){
//            stop();
//            System.out.println("Time : " + toString() +  " Clock Offset: " + offset +" Clock is Running: "+ isRunning());
//            if (isCycling){
//                reset();
//                start();
//            }
//        }else{
//            System.out.println("Time : " + toString() +  " Clock Offset: " + offset +" Clock is Running: "+ isRunning());
//            start();
//        }
//    };

    /**
     *
     * @return
     */
    public static Clock createClock() { return new Clock(); }

    /** Constructor */
    Clock(){
        startTime = systemNanoTime();
        currentTimeExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    /**  */
    public Clock start(){
        if(isRunning){ throw new IllegalStateException("The Clock is already running"); }
        isRunning = true;
        startTime = systemNanoTime();
        return this;
    }

    /**  */
    public Clock stop(){
        long lastTime =  systemNanoTime();
        if(!isRunning){ throw new IllegalStateException("The Clock is already stopped"); }
        isRunning = false;
        elapsedNanos =  elapsedNanos + (lastTime - startTime);
        return this;
    }

    /**  */
    public Clock reset(){
        elapsedNanos = onset;
        isRunning = false;
        return this;
    }

    /**  */
    public Clock setElapsedNanos(long timeInMillis){
        long timeInNanos = NANOSECONDS.convert(timeInMillis, MILLISECONDS);
        if(onset < timeInNanos || timeInNanos > offset){
            throw new IllegalStateException("The Time is not in the Onset Offset Boundary");
        }
        elapsedNanos = timeInNanos;
        return this;
    }

    public Clock setClockRate(Rate newRate){
        rate = newRate;
        return this;
    }

    public Clock setOnset(long timeInMillis){
        onset = NANOSECONDS.convert(timeInMillis, MILLISECONDS);
        return this;
    }

    public Clock setOffSet(long timeInMillis){
        onset = NANOSECONDS.convert(timeInMillis, MILLISECONDS);
        return this;
    }

    public long getOnset() {
        return onset;
    }

    public long getOffset() {
        return offset;
    }

    public boolean isCycling() {
        return isCycling;
    }

    public Rate getRate() {
        return rate;
    }

    /**
     *
     * @return
     */
    public boolean isRunning() { return isRunning; }

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
        return isRunning ? (long) ((rate.getValue() * (systemNanoTime() - startTime)) + elapsedNanos) : elapsedNanos;
    }

    public static void main (String[] args) throws InterruptedException {
        Clock clock = Clock.createClock();
        System.out.println(clock.elapsedTime());
        clock.start();
        int cpt = 10;
        while (cpt > 0){
            try{
                TimeUnit.MILLISECONDS.sleep(100);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            System.out.println("Clock is Running: " + clock.isRunning());
            System.out.println("Clock is Running: " + clock.elapsedTime());
            cpt--;
        }

        System.out.println("End of logging");
        System.out.println("Clock Total Time: " + clock.elapsedTime());
        clock.stop();
    }
}

