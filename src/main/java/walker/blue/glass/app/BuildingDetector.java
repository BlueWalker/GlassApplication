package walker.blue.glass.app;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import walker.blue.beacon.lib.beacon.Beacon;
import walker.blue.beacon.lib.client.BeaconScanClient;
import walker.blue.glass.app.factories.BeaconClientFactory;

/**
 * Created by noname on 1/24/15.
 */
public class BuildingDetector {

    private static final int SCAN_TIME = 5000;
    private static final int MIN_BEACONS = 1;
    private static final int SLEEP_TIME = 200;
    private BeaconScanClient beaconScanClient;
    private Set<Beacon> beacons;

    public BuildingDetector(final Context context) {
        final BeaconClientFactory clientFactory = new BeaconClientFactory(context);
        this.beacons = new HashSet<>();
        this.beaconScanClient = clientFactory.buildBuildingCheckClient(this.beacons, SCAN_TIME);
    }

    public String getBuildingID() {
        if (!beacons.isEmpty()) {
            beacons.clear();
        }
        this.beaconScanClient.startScanning();
        // TODO make this better
        while(this.beacons.size() < MIN_BEACONS && this.beaconScanClient.isScanning()) {
            try {
                Log.d("#######", "Sleeping: currently have " + beacons.size() + " beacons");
                Thread.sleep(SLEEP_TIME, 0);
            } catch (final InterruptedException e) {/*TODO*/}
        }
        if (this.beaconScanClient.isScanning()) {
            this.beaconScanClient.stopScanning();
        }
        final Map<String, Integer> idOccurances = new HashMap<>();
        for(final Beacon beacon : this.beacons) {
            if (idOccurances.containsKey(beacon.getUUID())) {
                int currentOccurances = idOccurances.get(beacon.getUUID());
                idOccurances.put(beacon.getUUID(), currentOccurances + 1);
            } else {
                idOccurances.put(beacon.getUUID(), 0);
            }
        }
        String result = null;
        int currentMax = 0;
        for (Map.Entry<String, Integer> entry : idOccurances.entrySet()) {
            if (result == null || entry.getValue() > currentMax) {
                result = entry.getKey();
                currentMax = entry.getValue();
            }
        }
        return result;
    }
}
