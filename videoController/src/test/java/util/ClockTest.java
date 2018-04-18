package util;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class ClockTest {


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
    }

    @Test
    public void stop() {
        Clock clock =  Clock.createClock();

        long startTime = clock.elapsedTime();
        clock.start();

        waistTime(1000);

        clock.stop();
        long endTime = clock.elapsedTime();

        assertTrue( endTime > startTime);
        assertFalse(clock.isRunning());

        waistTime(1000);

        long endTime2 = clock.elapsedTime();
        assertEquals( endTime, endTime2);
        assertFalse(clock.isRunning());
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

        assertTrue(clock.elapsedTime()> 0);
        assertTrue(clock.isRunning());

        clock.reset();

        assertEquals(0,clock.elapsedTime());
        assertFalse(clock.isRunning());
    }

    @Test
    public void setElapsedNanos() {
    }

    @Test
    public void setClockRate() {
    }

    @Test
    public void setOnset() {
    }

    @Test
    public void setOffSet() {
    }

    @Test
    public void isRunning() {
    }

    private void waistTime(long timeInMillis){
        try {
            TimeUnit.MILLISECONDS.sleep(timeInMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}