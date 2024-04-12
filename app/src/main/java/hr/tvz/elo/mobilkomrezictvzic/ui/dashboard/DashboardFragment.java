package hr.tvz.elo.mobilkomrezictvzic.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import hr.tvz.elo.mobilkomrezictvzic.broadcast.LocationBroadcastReceiver;
import hr.tvz.elo.mobilkomrezictvzic.databinding.FragmentDashboardBinding;
import hr.tvz.elo.mobilkomrezictvzic.service.CellularService;
import hr.tvz.elo.mobilkomrezictvzic.service.LocationService;
import hr.tvz.elo.mobilkomrezictvzic.ui.cellular.CellularFragment;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private SwitchCompat switchLocation, switchCellular;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initializeElements();

        switchLocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Log.i("DashboardFragment", "Location Service Enabled!");
                requireActivity().startService(new Intent(getActivity(), LocationService.class));
            }
            else {
                Log.i("DashboardFragment", "Location Service Disabled!");
                requireActivity().stopService(new Intent(getActivity(), LocationService.class));
            }
        });

        switchCellular.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Log.i("DashboardFragment", "Cellular Service Enabled!");
                requireActivity().startService(new Intent(getActivity(), CellularService.class));
            }
            else {
                Log.i("DashboardFragment", "Cellular Service Disabled!");
                requireActivity().stopService(new Intent(getActivity(), CellularService.class));
            }
        });

        return root;
    }

    private void initializeElements() {
        switchLocation = binding.switchLocation;
        switchCellular = binding.switchCellular;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}