package walker.blue.glass.app.factories;

import android.os.PowerManager;

import java.util.List;

import walker.blue.core.lib.init.InitError;
import walker.blue.core.lib.init.InitializeProcess;
import walker.blue.glass.app.fragments.FailedFragment;
import walker.blue.glass.app.fragments.InitFragment;
import walker.blue.glass.app.fragments.OffCourseFragment;
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
     * @return new instance of the InitFragment
     */
    public static InitFragment newInitFragment(final List<String> userInput) {
        final InitFragment initFragment = new InitFragment();
        initFragment.setUserInput(userInput);
        return initFragment;
    }

    /**
     * Creates a new instance of the RunFragment and sets the initOutput and
     * wakeLock to the given values
     *
     * @param initOutput Output of the initialize process
     * @return new instance of the RunFragment
     */
    public static RunFragment newRunFragment(final InitializeProcess.Output initOutput) {
        final RunFragment runFragment = new RunFragment();
        runFragment.setInitOutput(initOutput);
        return runFragment;
    }

    /**
     * Creates a new instance of the FailedFragment and sets the error and
     * wakeLock to the given values
     *
     * @param error Error type from the initialize process
     * @return new instance of the FailedFragment
     */
    public static FailedFragment newFailedFragment(final InitError error) {
        final FailedFragment failedFragment = new FailedFragment();
        failedFragment.setError(error);
        return failedFragment;
    }

    /**
     * Creates a new instance of the OffCourseFragment using the given values
     *
     * @param prevOutput previous output of the Initialize output
     * @return new instance of the OffCourseFragment
     */
    public static OffCourseFragment newOffCourseFragment(final InitializeProcess.Output prevOutput) {
        final OffCourseFragment offCourseFragment = new OffCourseFragment();
        offCourseFragment.setPrevOutput(prevOutput);
        return offCourseFragment;
    }
}
