package walker.blue.glass.app.activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;

import java.util.ArrayList;
import java.util.List;

import walker.blue.core.lib.direction.OrientationManager;
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
     * Rate at which the TextToSpeech is set
     */
    private static final float SPEECH_RATE = 2.0f;

    /**
     * Wakelock used throughout the application
     */
    private PowerManager.WakeLock wakeLock;
    /**
     * TextToSpeech used throughout the lifecycle of the application
     */
    private TextToSpeech textToSpeech;
    /**
     * OrientationManager used throughout the lifecycle of the application
     */
    private OrientationManager orientationManager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, WAKELOCK_TAG);
        this.wakeLock.acquire();
//        final List<String> userInput = this.getVoiceResultsDebug("room 456");
        final List<String> userInput = this.getVoiceResults();
        final Fragment initFragment = FragmentFactory.newInitFragment(userInput);
        this.textToSpeech = new TextToSpeech(this, null);
        this.textToSpeech.setSpeechRate(SPEECH_RATE);
        final SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        this.orientationManager = new OrientationManager(sensorManager, locationManager);
        this.textToSpeech.speak(" ", TextToSpeech.QUEUE_FLUSH, null);
        this.getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, initFragment)
                .commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.wakeLock.isHeld()) {
            this.wakeLock.release();
        }
        this.textToSpeech.shutdown();
        this.orientationManager.stop();
    }

    /**
     * Getter for the textToSpeech field
     *
     * @return current value of the textToSpeech field
     */
    public TextToSpeech getTextToSpeech() {
        return this.textToSpeech;
    }

    /**
     * Getter for the orientationManager field
     *
     * @return current value of the orientationManager field
     */
    public OrientationManager getOrientationManager() {
        return this.orientationManager;
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

    /** ------------ DEBUG STUFF ------------ */

    private List<String> getVoiceResultsDebug(final String str) {
        final List<String> res = new ArrayList<>();
        res.add(str);
        return res;
    }
}