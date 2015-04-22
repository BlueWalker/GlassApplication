package walker.blue.glass.app.run;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.PowerManager;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import walker.blue.core.lib.direction.OrientationManager;
import walker.blue.core.lib.indicator.IndicatorView;
import walker.blue.core.lib.init.InitializeProcess;
import walker.blue.core.lib.main.MainLoop;
import walker.blue.core.lib.main.UserStateHandler;
import walker.blue.core.lib.speech.SpeechSubmitHandler;
import walker.blue.core.lib.user.UserState;
import walker.blue.glass.app.R;
import walker.blue.glass.app.activities.MainActivity;
import walker.blue.glass.app.factories.FragmentFactory;
import walker.blue.glass.app.fragments.ArrivedFragment;
import walker.blue.glass.app.fragments.OffCourseFragment;

/**
 * Runnable responsible for running the mainloop
 */
public class RunMainLoop implements Runnable {


    private boolean stop;
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
     * SpeechSubmitHandler used to submit speech in the mainloop
     */
    private SpeechSubmitHandler speechSubmitHandler;
    /**
     * OrientationManager used get sensor data in the main loop
     */
    private OrientationManager orientationManager;

    /**
     * Handles what happend when the users state is found
     */
    private UserStateHandler userStateHandler = new UserStateHandler() {

        private static final int OFF_COURSE_MAX = 2;
        private static final int WARNING_MAX = 5;

        private int offCourseCount = 0;
        private int warningZoneCount = 0;

        @Override
        public void newStateFound(final UserState userState) {
            switch (userState) {
                case OFF_COURSE:
                    this.offCourseCount++;
                    if (this.offCourseCount == OFF_COURSE_MAX) {
                        final OffCourseFragment fragment = FragmentFactory.newOffCourseFragment(initOutput);
                        switchContextFragment(fragment);
                    }
                    break;
                case IN_WARNING_ZONE:
                    this.warningZoneCount++;
                    if (this.warningZoneCount == WARNING_MAX) {
                        changeWarningIconVisibility(View.VISIBLE);
                    }
                    if (this.warningZoneCount % WARNING_MAX == 0) {
                        speechSubmitHandler.warnUser();
                    }
                    break;
                case ON_COURSE:
                    this.offCourseCount = 0;
                    this.warningZoneCount = 0;
                    changeWarningIconVisibility(View.GONE);
                    break;
                case ARRIVED:
                    final ArrivedFragment arrivedFragment = new ArrivedFragment();
                    switchContextFragment(arrivedFragment);
                    break;
            }
        }
    };

    /**
     * Contructor. sets the fields to the given values
     *
     * @param initOutput Output of the initialize process
     * @param rootView Root view of the RunFragment
     * @param context Context under which the mainloop is running
     */
    public RunMainLoop(final InitializeProcess.Output initOutput,
                       final View rootView,
                       final Context context) {
        this.initOutput = initOutput;
        this.rootView = rootView;
        this.context = context;
        this.wakeLock = wakeLock;
        this.stop = false;
        this.orientationManager = ((MainActivity) context).getOrientationManager();
    }

    @Override
    public void run() {
        this.stop = false;
        final TextView nextIntructionText = (TextView) rootView.findViewById(R.id.actual_text);
        final TextView currentLocationText = (TextView) rootView.findViewById(R.id.location_text);
        final TextView currentUserStateText = (TextView) rootView.findViewById(R.id.state_text);
        final IndicatorView indicator = (IndicatorView) rootView.findViewById(R.id.indicator);
        final TextToSpeech textToSpeech = ((MainActivity) context).getTextToSpeech();
        this.speechSubmitHandler =
                new SpeechSubmitHandler(textToSpeech, nextIntructionText, context);
        final MainLoop mainLoop = new MainLoop(initOutput,
                this.context,
                this.userStateHandler,
                this.orientationManager,
                this.speechSubmitHandler,
                indicator);

        UserState currentState = null;
        while (currentState != UserState.ARRIVED && !this.stop) {
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
    }

    /**
     * Sets the flag to stop running the main loop
     */
    public void stopTask() {
        this.stop = true;
    }

    /**
     * Switches the fragment
     *
     * @param newFragment new fragment set
     */
    private void switchContextFragment(final Fragment newFragment) {
        ((Activity) context).getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, newFragment)
                .commit();
    }

    /**
     * Sets the visibility of the warning icon to the given value
     *
     * @param visibility new value fo the visibility fo the warning icon
     */
    private void changeWarningIconVisibility(final int visibility) {
        final ImageView warnImage = (ImageView) rootView.findViewById(R.id.warn_img);
        if (warnImage != null && warnImage.getVisibility() != visibility) {
            ((Activity) this.context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    warnImage.setVisibility(visibility);
                }
            });
        }
    }
}
