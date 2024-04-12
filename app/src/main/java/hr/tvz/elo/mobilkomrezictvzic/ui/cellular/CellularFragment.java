package hr.tvz.elo.mobilkomrezictvzic.ui.cellular;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import hr.tvz.elo.mobilkomrezictvzic.broadcast.CellularBroadcastReceiver;
import hr.tvz.elo.mobilkomrezictvzic.broadcast.LocationBroadcastReceiver;
import hr.tvz.elo.mobilkomrezictvzic.databinding.FragmentCellularBinding;
import hr.tvz.elo.mobilkomrezictvzic.service.CellularService;
import hr.tvz.elo.mobilkomrezictvzic.service.LocationService;

public class CellularFragment extends Fragment {

    private FragmentCellularBinding binding;
    private TextView textProvider, textMcc, textMnc, textNodeBId, textPci, textTac, textType, textArfcn, textBand;
    private TextView textRsrp, textRsrq, textRssi, textSinr;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CellularViewModel cellularViewModel = new ViewModelProvider(this).get(CellularViewModel.class);

        binding = FragmentCellularBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initializeElements();

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(cellularBroadcastReceiver, new IntentFilter(CellularService.ACTION_CELLULAR_BROADCAST));

        return root;
    }

    private void initializeElements() {
        textProvider = binding.textProvider;
        textMcc = binding.textMcc;
        textMnc = binding.textMnc;
        textNodeBId = binding.textNbId;
        textPci = binding.textPci;
        textTac = binding.textTac;
        textType = binding.textType;
        textArfcn = binding.textARFCN;
        textBand = binding.textBand;

        textRsrp = binding.textRsrp;
        textRsrq = binding.textRsrq;
        textRssi = binding.textRssi;
        textSinr = binding.textSinr;
    }

    private final CellularBroadcastReceiver cellularBroadcastReceiver = new CellularBroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int rsrp = intent.getIntExtra(CellularService.EXTRA_RSRP, 0);
            int rsrq = intent.getIntExtra(CellularService.EXTRA_RSRQ, 0);
            int sinr = intent.getIntExtra(CellularService.EXTRA_SINR, 0);

            textRsrp.setText(String.format("RSRP: %s", rsrp));
            textRsrq.setText(String.format("RSRQ: %s", rsrq));
            textSinr.setText(String.format("SINR: %s", sinr));
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}