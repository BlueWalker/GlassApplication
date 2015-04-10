package walker.blue.glass.app.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import walker.blue.glass.app.R;

/**
 * Fragment displayed when user goes off course
 */
public class OffCourseFragment extends EndFragmentBase {

    /**
     * Text being displayed on the screen
     */
    private static final String OFF_COURSE_RECALCULATING = "off-course!\nrecalculating route";
    private static final String OFF_COURSE_SPEECH = "you are off-course route is being recalculated";

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        final LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.progress_layout, null);
        final TextView textView = (TextView) rootView.findViewById(R.id.progress_text);
        textView.setText(OFF_COURSE_RECALCULATING);
        return rootView;
    }
}