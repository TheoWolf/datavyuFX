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

    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void shuttleForward() {

    }

    @Override
    public void shuttleBackward() {

    }

    @Override
    public void jogForward() {

    }

    @Override
    public void jogBackward() {

    }

    @Override
    public void seek(long timeInMillis) {

    }

    @Override
    public void back(long timeInMillis) {

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

    }
}
