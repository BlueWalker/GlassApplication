package walker.blue.glass.app.activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.speech.RecognizerIntent;

import java.util.List;

import walker.blue.glass.app.R;
import walker.blue.glass.app.factories.FragmentFactory;

/**
 * Main activity of the application
 */
public class MainActivity extends Activity {

    /**
     * Tag used to create the wakelock for the application
     */
    private static final String WAKELOCK_TAG = "BeaconLibExample";

    /**
     * Wakelock used throughout the application
     */
    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, WAKELOCK_TAG);
        this.wakeLock.acquire();
        final List<String> userInput = this.getVoiceResults();
        final Fragment initFragment = FragmentFactory.newInitFragment(userInput, wakeLock);
        this.getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, initFragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (this.wakeLock.isHeld()) {
            this.wakeLock.release();
        }
        this.finish();
    }

    /**
     * Gets the results fom the voice prompt
     *
     * @return user voice input
     */
    private List<String> getVoiceResults() {
        return getIntent().getExtras()
                .getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
    }
}