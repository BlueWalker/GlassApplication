package walker.blue.glass.app.run;

import android.app.Fragment;

import java.util.List;

import walker.blue.core.lib.init.InitError;
import walker.blue.core.lib.init.InitializeProcess;
import walker.blue.glass.app.R;
import walker.blue.glass.app.factories.FragmentFactory;
import walker.blue.glass.app.fragments.ArrivedFragment;

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
     * Contructor. Sets the fields to the given values
     *
     * @param fragment Fragment under which the initialize process is running
     * @param userInput The voice input from the user
     */
    public RunInitProcess(final Fragment fragment,
                          final List<String> userInput) {
        this.fragment = fragment;
        this.userInput = userInput;
    }

    @Override
    public void run() {
        final InitializeProcess initializeProcess = new InitializeProcess(fragment.getActivity(), this.userInput);
        final InitializeProcess.Output output =  initializeProcess.call();
        final Fragment nextFragment;
        if (output.getError() != null) {
            nextFragment = output.getError() == InitError.ALREADY_ARRIVED ?
                    new ArrivedFragment() :
                    FragmentFactory.newFailedFragment(output.getError());
        } else {
            nextFragment = FragmentFactory.newRunFragment(output);
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
}
