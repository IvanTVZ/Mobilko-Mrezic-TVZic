package hr.tvz.elo.mobilkomrezictvzic.ui.location;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.TextView;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import hr.tvz.elo.mobilkomrezictvzic.broadcast.LocationBroadcastReceiver;
import hr.tvz.elo.mobilkomrezictvzic.databinding.FragmentLocationBinding;
import hr.tvz.elo.mobilkomrezictvzic.service.LocationService;

public class LocationFragment extends Fragment {

    private FragmentLocationBinding binding;
    private TextView textProvider, textLatitude, textLongitude, textAltitude, textSpeed;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LocationViewModel locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);

        binding = FragmentLocationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initializeElements();

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(locationBroadcastReceiver, new IntentFilter(LocationService.ACTION_LOCATION_BROADCAST));

        return root;
    }

    private void initializeElements() {
        textProvider = binding.textProvider;
        textLatitude = binding.textLatitude;
        textLongitude = binding.textLongitude;
        textAltitude = binding.textAltitude;
        textSpeed = binding.textSpeed;
    }

    private final LocationBroadcastReceiver locationBroadcastReceiver = new LocationBroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String provider = intent.getStringExtra(LocationService.EXTRA_PROVIDER);
            double latitude = intent.getDoubleExtra(LocationService.EXTRA_LATITUDE, 0);
            double longitude = intent.getDoubleExtra(LocationService.EXTRA_LONGITUDE, 0);
            double altitude = intent.getDoubleExtra(LocationService.EXTRA_ALTITUDE, 0);
            float speed = intent.getFloatExtra(LocationService.EXTRA_SPEED, 0);

            textProvider.setText(String.format("Provider: %s", provider));
            textLatitude.setText(String.format("Latitude: %s", latitude));
            textLongitude.setText(String.format("Longitude: %s", longitude));
            textAltitude.setText(String.format("Altitude: %s", altitude));
            textSpeed.setText(String.format("Speed: %s", speed));
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}