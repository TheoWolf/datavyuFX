package util;

public enum DVStatus {
    /**
     * State of the StreamViewer immediately after creation.
     */
    UNKNOWN,
    /**
     * State of the player once it is prepared to play.
     * This state is entered only once when DVmedia is able to process te file and if
     * DatavyuFX can read the format.
     */
    READY,
    /**
     * State of the SteamViewer when is paused.
     */
    PAUSED,
    /**
     * State of the SteamViewer when it is currently playing.
     */
    PLAYING,
    /**
     * State of the player when is stopped.  playing again will cause it
     * to rewind the playback from the beginning.
     */
    STOPPED,
    /**
     * State of the StreamViewer when one of the StreamViewers is not synced with
     * the master clock or stopped. If paused or stopped in this state, then
     * buffering will continue but playback will not resume automatically
     * when sufficient data are buffered.
     */
    STALLED,
    /**
     * State of the player when a critical error has occurred. The
     * player is no longer functional and a new player should be created.
     */
    HALTED,
    /**
     * State of the player after disposing all the resources.
     */
    DISPOSED
}
