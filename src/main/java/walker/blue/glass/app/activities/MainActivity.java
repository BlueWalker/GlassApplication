package walker.blue.glass.app.activities;

import android.app.Activity;
import android.os.Bundle;

import walker.blue.beacon.lib.client.BeaconScanClient;
import walker.blue.glass.app.BuildingDetector;
import walker.blue.glass.app.R;

public class MainActivity extends Activity {

    private BeaconScanClient beaconClient;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BuildingDetector buildingDetector = new BuildingDetector(this);
    }
}
