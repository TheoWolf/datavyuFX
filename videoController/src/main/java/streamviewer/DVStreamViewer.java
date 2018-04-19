package streamviewer;

import media.DVMedia;
import util.DVStatus;
import util.Identifier;

/**
 * A thread safe singleton and Serializable StreamViewer
 */
public enum DVStreamViewer implements StreamViewer {

    INSTANCE;

    private final static Identifier identifier;
    private static DVMedia dvMedia;

    static {
        identifier = Identifier.createIdentifierZero();
        dvMedia = DVMedia.INSTANCE;
    }

    @Override
    public DVMedia getMedia() { return dvMedia.INSTANCE; }

    @Override
    public Identifier getIdentifier() { return identifier; }

    @Override
    public void play() {
        //Play all StreamViewer
    }

    @Override
    public void pause() {
        //pause all StreamViewer
    }

    @Override
    public void stop() {
        //Stop all StreamViewer
    }

    @Override
    public void shuttleForward() {
        //Shuttle Forward all StreamViewer
    }

    @Override
    public void shuttleBackward() {
        //Shuttle Backward all StreamViewer
    }

    @Override
    public void jogForward() {
        //Jog Forward all StreamViewer
    }

    @Override
    public void jogBackward() {
        //Jog Backward all StreamViewer
    }

    @Override
    public void seek(long timeInMillis) {
        //Seed all StreamViewer
    }

    @Override
    public void back(long timeInMillis) {
        //Jog Backward all StreamViewer
    }

    @Override
    public DVStatus getStatus() {
        return null;
    }

    @Override
    public void setVolume() {
        //Change the Volume of all the StreamViewer
    }

    @Override
    public void getVolume() {

    }

    @Override
    public void visible() {
        //Show all the StreamViewer
    }

    @Override
    public void hide() {
        //Hide all the StreamViewer
    }

    @Override
    public void close() {
        //Close all the StreamViewer and release resources
    }
}
