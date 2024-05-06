import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.util.Log

class WifiReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == WifiManager.RSSI_CHANGED_ACTION) {
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            val rssi = wifiInfo.rssi
            Log.d("wifi", "RSSI: $rssi")

            // Check if the RSSI is below the threshold
            if (rssi < MIN_RSSI_THRESHOLD) {
                // Device is too far from the router, take appropriate action
                Log.d("wifi", "Device is too far from the router!")
                // Add code here to perform the desired action
            }
        }
    }

    companion object {
        private const val TAG = "WifiReceiver"
        private const val MIN_RSSI_THRESHOLD = -80 // Example threshold value
    }
}
