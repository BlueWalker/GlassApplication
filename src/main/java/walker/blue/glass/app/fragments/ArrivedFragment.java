package walker.blue.glass.app.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import walker.blue.glass.app.R;

/**
 * Fragment displayed while application is initializing
 */
public class ArrivedFragment extends Fragment {

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.arrived_layout, null);
        final ScheduledExecutorService closeAppExecutor = Executors.newSingleThreadScheduledExecutor();
        final Runnable closeRunnable = new Runnable() {
            @Override
            public void run() {
                getActivity().finish();
            }
        };
        closeAppExecutor.schedule(closeRunnable, 5, TimeUnit.SECONDS);
        return layout;
    }
}