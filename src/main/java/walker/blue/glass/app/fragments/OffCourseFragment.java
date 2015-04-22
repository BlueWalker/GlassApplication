package walker.blue.glass.app.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import walker.blue.core.lib.init.InitializeProcess;
import walker.blue.glass.app.R;
import walker.blue.glass.app.run.RunRecalcProcess;

/**
 * Fragment displayed when user goes off course
 */
public class OffCourseFragment extends Fragment {

    /**
     * Text being displayed on the screen
     */
    private static final String OFF_COURSE_RECALCULATING = "off-course!\nrecalculating route";

    /**
     * Previous output of the initialize process
     */
    private InitializeProcess.Output prevOutput;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        final LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.progress_layout, null);
        final TextView textView = (TextView) rootView.findViewById(R.id.progress_text);
        textView.setText(OFF_COURSE_RECALCULATING);
        final RunRecalcProcess recalcProcess = new RunRecalcProcess(this.prevOutput, this.getActivity());
        final ExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.submit(recalcProcess);
        return rootView;
    }

    /**
     * Setter for the prevOutput field
     *
     * @param prevOutput new value for the prevOutput field
     */
    public void setPrevOutput(final InitializeProcess.Output prevOutput) {
        this.prevOutput = prevOutput;
    }
}