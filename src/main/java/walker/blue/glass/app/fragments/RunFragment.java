package walker.blue.glass.app.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import walker.blue.core.lib.init.InitializeProcess;
import walker.blue.glass.app.R;
import walker.blue.glass.app.run.RunMainLoop;

/**
 * Fragment displayed while the mainloop is running
 */
public class RunFragment extends Fragment {

    /**
     * Output of the initialize process
     */
    private InitializeProcess.Output initOutput;
    /**
     * RunMainLoop running the mainloop
     */
    private RunMainLoop mainLoop;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        final LinearLayout mainLinear = (LinearLayout) inflater.inflate(R.layout.run_layout, null);
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        this.mainLoop = new RunMainLoop(this.initOutput, mainLinear, this.getActivity());
        executorService.submit(mainLoop);
        return mainLinear;
    }

    /**
     * Sets the initOutput field to the given value
     *
     * @param initOutput Output of the initialize process
     */
    public void setInitOutput(final InitializeProcess.Output initOutput) {
        this.initOutput = initOutput;
    }

    @Override
    public void onPause() {
        super.onPause();
        this.mainLoop.stopTask();
    }
}