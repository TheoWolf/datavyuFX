package util;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.junit.Assert.*;

public class ClockTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void start() {
        Clock clock =  Clock.createClock();

        long startTime = clock.elapsedTime();
        clock.start();

        waistTime(1000);

        long endTime = clock.elapsedTime();

        assertTrue( endTime > startTime);
        assertTrue(clock.isRunning());

        clock.stop();
        clock.reset();
    }

    @Test
    public void stop() {
        Clock clock =  Clock.createClock();

        long startTime = clock.elapsedTime();
        //Start the clock
        clock.start();

        waistTime(1000);

        //Stop the clock
        clock.stop();
        long endTime = clock.elapsedTime();

        assertTrue( endTime > startTime);
        assertFalse(clock.isRunning());

        waistTime(1000);

        //Check if the clock did not change the elapsed time while stopped
        long endTime2 = clock.elapsedTime();

        assertEquals( endTime, endTime2);
        assertFalse(clock.isRunning());

        //Start the clock and check if we are resuming from the last reported time
        clock.start();

        waistTime(100);

        assertTrue(clock.elapsedTime() > endTime2 && clock.isRunning());

        clock.stop();
        clock.reset();
    }

    @Test
    public void reset() {
        Clock clock =  Clock.createClock();

        clock.start();

        waistTime(100);

        clock.stop();
        clock.reset();

        assertEquals(0,clock.elapsedTime());
        assertFalse(clock.isRunning());

        clock.start();

        waistTime(100);

        assertTrue(clock.elapsedTime() > 0 && clock.isRunning());

        clock.reset();

        assertEquals(0,clock.elapsedTime());
        assertFalse(clock.isRunning());
    }

    @Test
    public void setElapsedNanos() {
        long newElapsedTime = 500;

        Clock clock = Clock.createClock();
        clock.start();

        waistTime(1500);

        long beforeNewElapsedTime = clock.elapsedTime();

        //Set new elapsed time when running
        clock.setElapsedTime(newElapsedTime);

        assertTrue(clock.elapsedTime() > beforeNewElapsedTime && clock.isRunning());

        waistTime(1000);

        clock.stop();
        clock.setElapsedTime(newElapsedTime);

        assertEquals(clock.elapsedTime(),newElapsedTime);

        //Check if the New Time is in the boundary

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("The Time is not in the Onset Offset Boundary");

        clock.setElapsedTime(-1);

        clock.setElapsedTime( MILLISECONDS.convert(2, HOURS) );
        clock.reset();
    }

    @Test
    public void setClockRate() {
        long timeToWaist = 2000;
        long threshold = 20;
        Rate newRate =  Rate.X4;
        Clock clock = Clock.createClock();
        clock.setClockRate(newRate);
        clock.start();
        waistTime(timeToWaist);
        assertTrue(Math.abs(clock.elapsedTime()-timeToWaist*newRate.getValue()) <= threshold);
        clock.stop();
        clock.reset();
        clock.start();
        waistTime(timeToWaist);
        assertTrue(Math.abs(clock.elapsedTime()-timeToWaist*newRate.getValue()) <= threshold);
        clock.stop();
        System.out.println(clock.elapsedTime());
        clock.reset();
    }

    @Test
    public void setOnset() {
        Clock clock = Clock.createClock();
        long newOnset = 100;

        clock.start();

        waistTime(200);

        clock.setOnset(newOnset);

        assertEquals(clock.getOnset(),NANOSECONDS.convert(newOnset,MILLISECONDS));

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Onset Can't be (-), >= Offset or > current elapsed time");

        clock.stop();

        clock.setOnset(clock.elapsedTime()+100);

        clock.setOnset(-1);

        clock.setOnset(MILLISECONDS.convert(2, HOURS));

        clock.reset();
    }

    @Test
    public void setOffset() {
        Clock clock = Clock.createClock();
        long newOffset = 500;

        clock.start();

        waistTime(200);

        clock.setOffset(newOffset);

        assertEquals(clock.getOffset(),NANOSECONDS.convert(newOffset,MILLISECONDS));

        clock.stop();

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Offset Can't be <= Onset or < the current elapsed time");

        clock.setOffset(clock.elapsedTime()-100);

        clock.setOffset(-1);

        clock.setOffset(clock.getOnset()-100);

        clock.reset();
    }

    @Test
    public void isRunning() {
        Clock clock = Clock.createClock();

        clock.start();
        assertTrue(clock.isRunning());

        clock.stop();
        assertFalse(clock.isRunning());

        clock.start();
        assertTrue(clock.isRunning());

        clock.reset();
        assertFalse(clock.isRunning());
    }

    @Test
    public void checkBoundary(){
        Clock clock = Clock.createClock();
        clock.start();
        clock.setCycling(true);
        clock.setOffset(300);
        waistTime(3000);

        clock.reset();
    }

    private void waistTime(final long timeInMillis){
        try {
            TimeUnit.MILLISECONDS.sleep(timeInMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}