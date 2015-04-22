package walker.blue.glass.app.run;

import android.app.Activity;
import android.content.Context;
import android.os.PowerManager;

import walker.blue.core.lib.init.InitializeProcess;
import walker.blue.core.lib.init.RecalcProcess;
import walker.blue.glass.app.R;
import walker.blue.glass.app.factories.FragmentFactory;
import walker.blue.glass.app.fragments.RunFragment;

/**
 * Runnable responsible for running the mainloop
 */
public class RunRecalcProcess implements Runnable {

    /**
     * Output of the initialize process
     */
    private InitializeProcess.Output prevOutput;
    /**
     * Context under which the RecalcProcess is being run
     */
    private Context context;
    /**
     * WakeLock keeping the device on
     */
    private PowerManager.WakeLock wakeLock;

    /**
     * Contructor. sets the fields to the given values
     *
     * @param prevOutput Output of the initialize process
     * @param context Context under which the mainloop is running
     */
    public RunRecalcProcess(final InitializeProcess.Output prevOutput,
                            final Context context) {
        this.prevOutput = prevOutput;
        this.context = context;
    }

    @Override
    public void run() {
        final RecalcProcess recalcProcess = new RecalcProcess(this.context, this.prevOutput);
        final InitializeProcess.Output output = recalcProcess.call();
//        final TextToSpeech textToSpeech = ((MainActivity) this.context).getTextToSpeech();
        if (output.getError() == null) {
            final RunFragment runFragment = FragmentFactory.newRunFragment(output);
            ((Activity) context).getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, runFragment)
                    .commit();
        } else {
            // TODO: display error message
            ((Activity) this.context).finish();
        }
    }
}