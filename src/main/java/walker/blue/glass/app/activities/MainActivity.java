package walker.blue.glass.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;

import java.util.List;

import walker.blue.glass.app.R;

public class MainActivity extends Activity {

    private List<String> destinationInput;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.destinationInput = getVoiceResults();
        Log.d("######", this.destinationInput.toString());
    }

    private List<String> getVoiceResults() {
        return getIntent().getExtras()
                .getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
    }
}