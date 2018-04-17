package util;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;


// The clock will use the DV StreamViewer Rate to update the elapsed time according to the rate speed
public class Clock {

    //TODO: Create a class of static variables
    public static TimeUnit DEFAULT_TIMEUNIT = MICROSECONDS;

    // Use a range Slider to represent the onset and offset of the Ccock
    private long onset = 0;
    private long offset = 0;

    private boolean isRunning;
    private long startTime;
    private long elapsedNanos; // bind to the GUI TEXT and DVStreamViewer

    private long rate = 1; //will use this rate for now

    public static Clock createClock() { return new Clock(); }

    Clock(){ startTime = systemNanoTime(); }

    static long systemNanoTime() {return System.nanoTime(); }

    public void start(){
        if(isRunning){ throw new IllegalStateException("The Clock is already running"); }
        isRunning = true;
        startTime = systemNanoTime();
    }

    public void stop(){
        long lastTime =  systemNanoTime();
        if(!isRunning){ throw new IllegalStateException("The Clock is already stopped"); }
        isRunning = false;
        //save the last
        elapsedNanos =  elapsedNanos + (lastTime - startTime);
    }

    public void reset(){
        elapsedNanos = onset;
        isRunning = false;
    }

    public boolean isRunning() {return isRunning; }

    public long elapsedTime(TimeUnit targetUnit) { return targetUnit.convert(elapsedNanos(), NANOSECONDS); }

    public long elapsedTime() { return DEFAULT_TIMEUNIT.convert(elapsedNanos(), NANOSECONDS); }

    public Duration elapsedDuration() { return Duration.ofNanos(elapsedNanos); }

    @Override
    public String toString() {
        long timeStampNano =  elapsedNanos();

        String timeStampString = TimeUnit.NANOSECONDS.toHours(timeStampNano)
                +":"+ TimeUnit.NANOSECONDS.toMinutes(timeStampNano)
                +":"+ TimeUnit.NANOSECONDS.toSeconds(timeStampNano)
                +":"+ TimeUnit.NANOSECONDS.toMillis(timeStampNano);

        return timeStampString;
    }

    private long elapsedNanos() {
        return isRunning ? rate * (systemNanoTime() - startTime) + elapsedNanos : elapsedNanos;
    }


    public static void main (String[] args) throws InterruptedException {
        Clock clock = Clock.createClock();

        long start = System.nanoTime();
        clock.start();

        System.out.println("System: "+ TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) +" Clock: "+ clock.toString());

        TimeUnit.MILLISECONDS.sleep(10);

        long elapsedTime =  System.nanoTime() - start;

        clock.stop();
        System.out.println("System: "+ TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) +" Clock: "+ clock.toString());

        TimeUnit.MILLISECONDS.sleep(10);

        System.out.println("Clock: "+ clock.toString());

        clock.start();

        TimeUnit.MILLISECONDS.sleep(40);

        System.out.println("Clock: "+ clock.toString());

    }
}

