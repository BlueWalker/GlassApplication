package walker.blue.glass.app.factories;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

import java.util.Collection;

import walker.blue.beacon.lib.beacon.Beacon;
import walker.blue.beacon.lib.beacon.BluetoothDeviceToBeacon;
import walker.blue.beacon.lib.client.BeaconClientBuilder;
import walker.blue.beacon.lib.client.BeaconScanClient;

/**
 * Created by noname on 1/24/15.
 */
public class BeaconClientFactory {

    private Context context;

    public BeaconClientFactory(final Context context) {
        this.context = context;
    }

    public BeaconScanClient buildBuildingCheckClient(final Collection<Beacon> beacons, final int scanTime) {
        return new BeaconClientBuilder()
                .scanInterval(scanTime)
                .setContext(this.context)
                .setLeScanCallback(buildBuildingCheckCallback(beacons))
                .build();
    }

    private BluetoothAdapter.LeScanCallback buildBuildingCheckCallback(final Collection<Beacon> beacons) {
        return new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                final Beacon beacon = BluetoothDeviceToBeacon.toBeacon(device, rssi, scanRecord);
                if (beacon != null) {
                    beacons.add(beacon);
                }
            }
        };
    }
}
