package walker.blue.glass.app.run;

import android.app.Fragment;
import android.os.PowerManager;

import java.util.List;

import walker.blue.core.lib.init.InitializeProcess;
import walker.blue.glass.app.R;
import walker.blue.glass.app.factories.FragmentFactory;

/**
 * Runnable in charge of executing the Initialize process
 */
public class RunInitProcess implements Runnable {

    /**
     * Fragment under which the initialize process is running
     */
    private Fragment fragment;
    /**
     * The voice input from the user
     */
    private List<String> userInput;
    /**
     * Wakelock keeping the device on
     */
    private PowerManager.WakeLock wakeLock;

    /**
     * Contructor. Sets the fields to the given values
     *
     * @param fragment Fragment under which the initialize process is running
     * @param userInput The voice input from the user
     * @param wakeLock Wakelock keeping the device on
     */
    public RunInitProcess(final Fragment fragment,
                          final List<String> userInput,
                          final PowerManager.WakeLock wakeLock) {
        this.fragment = fragment;
        this.userInput = userInput;
        this.wakeLock = wakeLock;
    }

    @Override
    public void run() {
        final InitializeProcess initializeProcess = new InitializeProcess(fragment.getActivity(), this.userInput);
        final InitializeProcess.Output output =  initializeProcess.call();
        final Fragment nextFragment;
        if (output.getError() != null) {
            nextFragment = FragmentFactory.newFailedFragment(output.getError(), wakeLock);
        } else {
            nextFragment = FragmentFactory.newRunFragment(output, this.wakeLock);
        }
        this.fragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fragment.getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, nextFragment)
                        .commit();
            }
        });
    }

    /**
     * Cleans up the runner
     */
    public void clean() {
        if (this.wakeLock.isHeld()) {
            this.wakeLock.release();
        }
    }
}
