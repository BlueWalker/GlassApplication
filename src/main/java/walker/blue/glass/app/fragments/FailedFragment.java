package walker.blue.glass.app.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import walker.blue.core.lib.init.InitError;
import walker.blue.glass.app.R;

/**
 * Fragment used to display an error message from the initialize process
 */
public class FailedFragment extends EndFragmentBase {

    /**
     * Error type
     */
    private InitError error;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        final LinearLayout mainLinear = (LinearLayout) inflater.inflate(R.layout.failed_layout, null);
        if (this.wakeLock.isHeld()) {
            this.wakeLock.release();
        }
        final TextView reasonText = (TextView) mainLinear.findViewById(R.id.reason_failed);
        switch (this.error) {
            case BD_FAIL:
                reasonText.setText("Failed detecting building");
                break;
            case NULL_BUILDING:
                reasonText.setText("Failed getting building data");
                break;
            case NULL_DEST_TYPE:
                reasonText.setText("Failed resolving destination type");
                break;
            case INVALID_INPUT:
                reasonText.setText("Destination not found");
                break;
            case BEACONS_FAIL:
                reasonText.setText("Failed scanning beacons");
                break;
            case LOCATION_FAIL:
                reasonText.setText("Failed getting user location");
                break;
            case PATH_FAIL:
                reasonText.setText("Failed calculating path");
                break;
        }
        return mainLinear;
    }

    /**
     * Sets the error field to the given InitError
     *
     * @param error Error from the initialize process
     */
    public void setError(final InitError error) {
        this.error = error;
    }
}
