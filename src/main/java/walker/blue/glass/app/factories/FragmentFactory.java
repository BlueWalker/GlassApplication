package walker.blue.glass.app.factories;

import android.os.PowerManager;

import java.util.List;

import walker.blue.core.lib.init.InitError;
import walker.blue.core.lib.init.InitializeProcess;
import walker.blue.glass.app.fragments.FailedFragment;
import walker.blue.glass.app.fragments.InitFragment;
import walker.blue.glass.app.fragments.RunFragment;

/**
 * Factory class in charge of creating new instances and setting up the
 * different fragments
 */
public class FragmentFactory {

    /**
     * Private constructor
     */
    private FragmentFactory() {}

    /**
     * Creates a new instance of the InitFragment and sets the userInput and
     * wakeLock to the given values
     *
     * @param userInput Voice input of from the user
     * @param wakeLock wakelock used throughout the application
     * @return new instance of the InitFragment
     */
    public static InitFragment newInitFragment(final List<String> userInput,
                                               final PowerManager.WakeLock wakeLock) {
        final InitFragment initFragment = new InitFragment();
        initFragment.setUserInput(userInput);
        initFragment.setWakeLock(wakeLock);
        return initFragment;
    }

    /**
     * Creates a new instance of the RunFragment and sets the initOutput and
     * wakeLock to the given values
     *
     * @param initOutput Output of the initialize process
     * @param wakeLock wakelock used throughout the application
     * @return new instance of the RunFragment
     */
    public static RunFragment newRunFragment(final InitializeProcess.Output initOutput,
                                             final PowerManager.WakeLock wakeLock) {
        final RunFragment runFragment = new RunFragment();
        runFragment.setInitOutput(initOutput);
        runFragment.setWakeLock(wakeLock);
        return runFragment;
    }

    /**
     * Creates a new instance of the FailedFragment and sets the error and
     * wakeLock to the given values
     *
     * @param error Error type from the initialize process
     * @param wakeLock wakelock used throughout the application
     * @return new instance of the FailedFragment
     */
    public static FailedFragment newFailedFragment(final InitError error,
                                                   final PowerManager.WakeLock wakeLock) {
        final FailedFragment failedFragment = new FailedFragment();
        failedFragment.setError(error);
        failedFragment.setWakeLock(wakeLock);
        return failedFragment;
    }
}
