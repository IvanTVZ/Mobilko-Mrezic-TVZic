package hr.tvz.elo.mobilkomrezictvzic.listener;

import android.telephony.CellInfo;
import android.telephony.TelephonyCallback;
import androidx.annotation.NonNull;
import java.util.List;

public class TelephonyInfoListener extends TelephonyCallback implements TelephonyCallback.CellInfoListener {
    @Override
    public void onCellInfoChanged(@NonNull List<CellInfo> cellInfo) {

    }
}
