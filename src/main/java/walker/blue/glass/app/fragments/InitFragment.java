package walker.blue.glass.app.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import walker.blue.glass.app.R;
import walker.blue.glass.app.run.RunInitProcess;

/**
 * Fragment displayed while application is initializing
 */
public class InitFragment extends EndFragmentBase {

    /**
     * Text displayed
     */
    private static final String INITIALIZING_APPLICATION = "initializing application";

    /**
     * Voice input from the user
     */
    private List<String> userInput;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        final LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.progress_layout, null);
        final TextView textView = (TextView) rootView.findViewById(R.id.progress_text);
        textView.setText(INITIALIZING_APPLICATION);
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new RunInitProcess(this, this.userInput, this.wakeLock));
        return rootView;
    }

    /**
     * Sets the value of the userInput field
     *
     * @param userInput Voice input from the user
     */
    public void setUserInput(final List<String> userInput) {
        this.userInput = userInput;
    }
}