package com.example.rfidinventory.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.rfidinventory.R;
import com.example.rfidinventory.utils.DeviceUtils;
import com.google.android.material.card.MaterialCardView;

public class ScannerFragment extends Fragment {
    private MaterialCardView statusCard;
    private TextView statusText;
    private Button scanButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scanner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize views
        statusCard = view.findViewById(R.id.status_card);
        statusText = view.findViewById(R.id.status_text);
        scanButton = view.findViewById(R.id.scan_button);

        if (!DeviceUtils.isC66Device()) {
            // Running on emulator or non-C66 device
            statusText.setText(R.string.non_c66_device_message);
            scanButton.setEnabled(false);
        } else {
            // Initialize RFID functionality here when running on C66 device
            statusText.setText(R.string.ready_to_scan);
            scanButton.setEnabled(true);
            setupRfidScanner();
        }
    }

    private void setupRfidScanner() {
        // RFID initialization code will go here
        // This method will only be called on C66 devices
    }
}
