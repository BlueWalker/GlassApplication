package walker.blue.glass.app.run;

import android.app.Activity;
import android.content.Context;
import android.os.PowerManager;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import walker.blue.core.lib.init.InitializeProcess;
import walker.blue.core.lib.main.MainLoop;
import walker.blue.core.lib.main.SpeechSubmitRunnable;
import walker.blue.core.lib.main.UserStateHandler;
import walker.blue.core.lib.user.UserState;
import walker.blue.glass.app.R;

/**
 * Runnable responsible for running the mainloop
 */
public class RunMainLoop implements Runnable {

    /**
     * Output of the initialize process
     */
    private InitializeProcess.Output initOutput;
    /**
     * Root view of the RunFragment
     */
    private View rootView;
    /**
     * Context under which the mainloop is running
     */
    private Context context;
    /**
     * WakeLock keeping the device on
     */
    private PowerManager.WakeLock wakeLock;
    /**
     * Future for the speech submitter
     */
    private ScheduledFuture scheduledFuture;
    /**
     * Speech submitter runnable
     */
    private SpeechSubmitRunnable submitSpeechRunnable;
    /**
     * Handles what happend when the users state is found
     */
    private UserStateHandler handler = new UserStateHandler() {
        @Override
        public void newStateFound(final UserState userState) {
        }
    };

    /**
     * Contructor. sets the fields to the given values
     *
     * @param initOutput Output of the initialize process
     * @param rootView Root view of the RunFragment
     * @param context Context under which the mainloop is running
     * @param wakeLock WakeLock keeping the device on
     */
    public RunMainLoop(final InitializeProcess.Output initOutput,
                       final View rootView,
                       final Context context,
                       final PowerManager.WakeLock wakeLock) {
        this.initOutput = initOutput;
        this.rootView = rootView;
        this.context = context;
        this.wakeLock = wakeLock;
    }

    @Override
    public void run() {
        final TextView currentLocationText = (TextView) rootView.findViewById(R.id.location_text);
        final TextView currentUserStateText = (TextView) rootView.findViewById(R.id.state_text);
        final MainLoop mainLoop = new MainLoop(initOutput, this.context, this.handler);

        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        this.submitSpeechRunnable = new SpeechSubmitRunnable(mainLoop.getUserTracker(),
                this.initOutput.getBuilding(),
                this.initOutput.getPath(),
                this.context);
        this.scheduledFuture = executorService.scheduleWithFixedDelay(submitSpeechRunnable, 2, 10, TimeUnit.SECONDS);

        UserState currentState = null;
        while (currentState != UserState.ARRIVED) {
            final MainLoop.Output loopOutput = mainLoop.call();
            ((Activity) this.context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    currentLocationText.setText(loopOutput.getCurrentLocation().toString());
                    currentUserStateText.setText(loopOutput.getUserState().name());
                    if (currentLocationText.getVisibility() != View.VISIBLE) {
                        currentLocationText.setVisibility(View.VISIBLE);
                    }
                    if (currentUserStateText.getVisibility() != View.VISIBLE) {
                        currentUserStateText.setVisibility(View.VISIBLE);
                    }
                }
            });
            currentState = loopOutput.getUserState();
        }
        this.clean();
    }

    /**
     * Cleans up the runner
     */
    public void clean() {
        this.scheduledFuture.cancel(true);
        this.submitSpeechRunnable.kill();
        if (this.wakeLock.isHeld()) {
            this.wakeLock.release();
        }
    }
}
