package streamviewer;

import media.DVMedia;
import util.DVStatus;
import util.Identifier;

public interface StreamViewer {

    Identifier getIdentifier();

    /** Always Play at 1X */
    void play();

    /** Toggle between Play and Stop with a specific rate*/
    void pause();

    /** Stop Streaming and change the rate to 0X */
    void stop();

    /** Update to the next Rate */
    void shuttleForward();

    /** Update to the previous Rate */
    void shuttleBackward();

    /** Jump forward by 1 frame if the possible, if not possible seek to the approximate time of the next frame */
    void jogForward();

    /** Jump backward by 1 frame if the possible, if not possible seek to the approximate time of the previous frame */
    void jogBackward();

    /** Seek to a specific timestamp */
    void seek(long timeInMillis);

    /** Jump back by time in milliseconds */
    void back(long timeInMillis);

    /** get the Media associated with the StreamViewer */
    DVMedia getMedia();

    /** Get the current Status of the StreamViewer */
    DVStatus getStatus();

    /** Set Audio Volume */
    void setVolume();

    /** Get Audio Volume */
    void getVolume();

    /** Show the Window */
    void visible();

    /** Hide the Window */
    void hide();

    /** Close the Window, and destroy allocated resources */
    void close();
}
