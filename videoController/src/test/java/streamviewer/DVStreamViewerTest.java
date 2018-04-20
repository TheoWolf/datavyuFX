package streamviewer;

import media.DVMedia;
import org.junit.Test;
import streamviewer.javafx.JfxStreamViewer;
import util.Clock;
import util.Identifier;

import static org.junit.Assert.*;

public class DVStreamViewerTest {

    @Test
    public void getMedia() {
        assertTrue(DVStreamViewer.INSTANCE.getMedia() instanceof DVMedia);
    }

    @Test
    public void getIdentifier() {
        assertEquals(DVStreamViewer.INSTANCE.getIdentifier().asLong(), 0L);
    }

    @Test
    public void getMasterCurrentTime() {

    }

    @Test
    public void play() {
    }

    @Test
    public void pause() {
    }

    @Test
    public void addStream(){
        int streamNumber = 3;

        for(int i = 0; i < streamNumber; i++){
            StreamViewer stream = JfxStreamViewer.createStreamViewer(Identifier.generateIdentifier());
            DVStreamViewer.INSTANCE.addStream(stream);
        }

        assertEquals(3,DVStreamViewer.INSTANCE.getStreams().size());
    }


    @Test
    public void stop() {
        //Stop all StreamViewer
    }

    @Test
    public void shuttleForward() {
        //Shuttle Forward all StreamViewer
    }

    @Test
    public void shuttleBackward() {
        //Shuttle Backward all StreamViewer
    }
}