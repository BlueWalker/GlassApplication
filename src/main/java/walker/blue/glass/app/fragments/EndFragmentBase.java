package walker.blue.glass.app.fragments;

import android.app.Fragment;
import android.os.PowerManager;

/**
 * Base fragment that holds the wakelock
 */
public abstract class EndFragmentBase extends Fragment {

    /**
     * Wakelock used throughout the application
     */
    protected PowerManager.WakeLock wakeLock;

    /**
     * Sets the wakelock field to the given wakelock
     *
     * @param wakeLock Wakelock used throughout the application
     */
    public void setWakeLock(final PowerManager.WakeLock wakeLock) {
        this.wakeLock = wakeLock;
    }
}