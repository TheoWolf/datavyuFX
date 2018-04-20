package streamviewer.javafx;

import media.DVMedia;
import streamviewer.DVStreamViewer;
import streamviewer.StreamViewer;
import util.Clock;
import util.DVStatus;
import util.Identifier;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class JfxStreamViewer implements StreamViewer {

    private final Identifier identifier;
    private Clock clockStream;

    private ExecutorService checkSyncTask;


    private JfxStreamViewer(Identifier identifier){
        this.identifier = identifier;
        this.clockStream = Clock.createClock();
        this.clockStream.setElapsedTime(DVStreamViewer.INSTANCE.getMasterCurrentTime());
        this.checkSyncTask = Executors.newSingleThreadExecutor();

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
        clockStream.start();

        this.checkSyncTask.execute(() -> {
            long sumDelta = 0;
            long cpt = 0;
            while(clockStream.isRunning()) {
                long masterClock = DVStreamViewer.INSTANCE.getMasterCurrentTime();
                long slaveClock = clockStream.elapsedTime();
                sumDelta += Math.abs(masterClock-slaveClock);
                cpt++;
                System.out.println(this.identifier + " Master Clock: " + masterClock
                        + " Stream CLock: " + slaveClock + " Delta: " + (masterClock - slaveClock));
                try {
                    TimeUnit.MINUTES.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println(this.identifier + " Average Delta = " + sumDelta/cpt);
        });
    }

    @Override
    public void pause() {
        clockStream.reset();
    }

    @Override
    public void stop() {
        clockStream.stop();
    }

    @Override
    public void shuttleForward() {
        clockStream.setClockRate(DVStreamViewer.INSTANCE.getRate());
    }

    @Override
    public void shuttleBackward() {
        clockStream.setClockRate(DVStreamViewer.INSTANCE.getRate());
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
        clockStream.setElapsedTime(DVStreamViewer.INSTANCE.getMasterCurrentTime());
    }

    @Override
    public void back(long timeInMillis) {
        //For now I will just follow the time of the master clock, the update is already done via DVStreamViewer
        clockStream.setElapsedTime(DVStreamViewer.INSTANCE.getMasterCurrentTime());
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
        clockStream.reset();
        checkSyncTask.shutdownNow();
    }
}
