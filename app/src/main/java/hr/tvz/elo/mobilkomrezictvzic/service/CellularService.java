package hr.tvz.elo.mobilkomrezictvzic.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.telephony.*;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import hr.tvz.elo.mobilkomrezictvzic.listener.TelephonyInfoListener;
import java.util.List;

public class CellularService extends Service {

    public static final String ACTION_CELLULAR_BROADCAST = CellularService.class.getName() + "CellularBroadcast"
            , EXTRA_RSRP = "extra_rsrp"
            , EXTRA_RSRQ = "extra_rsrq"
            , EXTRA_SINR = "extra_sinr";
    private TelephonyManager telephonyManager;
    private List<CellInfo> currentCellInfo;
    private CellInfo currentServingCellInfo;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getCurrentCellInfo();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("CellularService", "Cellular Service Started!");

        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            getCurrentCellInfo();
            telephonyManager.registerTelephonyCallback(getMainExecutor(), cellInfoListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("CellularService", "Cellular Service Stopped!");
        telephonyManager = null;
    }

    private final TelephonyInfoListener cellInfoListener = new TelephonyInfoListener() {
        @Override
        public void onCellInfoChanged(@NonNull List<CellInfo> cellInfo) {
            currentCellInfo = cellInfo;
            sendBroadcastMessage(currentCellInfo);
        }
    };

    public void getCurrentCellInfo() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            currentCellInfo = telephonyManager.getAllCellInfo();
            sendBroadcastMessage(currentCellInfo);
        }
    }

    private void sendBroadcastMessage(List<CellInfo> cellInfo) {
        if (cellInfo != null) {
            Intent intent = new Intent(ACTION_CELLULAR_BROADCAST);
            intent.putExtra(EXTRA_RSRP, 1);
            intent.putExtra(EXTRA_RSRQ, 2);
            intent.putExtra(EXTRA_SINR, 3);

            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }
}
