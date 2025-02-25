package com.example.rfidinventory.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rfidinventory.MainActivity;
import com.example.rfidinventory.R;
import com.example.rfidinventory.adapter.RFIDTagAdapter;
import com.example.rfidinventory.model.RFIDTag;
import com.example.rfidinventory.service.RFIDScannerService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

public class ScannerFragment extends Fragment implements RFIDScannerService.ScanningCallback {
    private TextView statusText;
    private TextView tagCount;
    private MaterialButton scanButton;
    private MaterialButton clearButton;
    private RecyclerView tagsList;
    private RFIDTagAdapter adapter;
    private RFIDScannerService scannerService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scanner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        statusText = view.findViewById(R.id.status_text);
        tagCount = view.findViewById(R.id.tag_count);
        scanButton = view.findViewById(R.id.scan_button);
        clearButton = view.findViewById(R.id.clear_button);
        tagsList = view.findViewById(R.id.scanned_tags_list);

        // Setup RecyclerView
        adapter = new RFIDTagAdapter();
        tagsList.setLayoutManager(new LinearLayoutManager(requireContext()));
        tagsList.setAdapter(adapter);

        // Get scanner service from MainActivity
        MainActivity activity = (MainActivity) requireActivity();
        if (activity.isRfidReady()) {
            scannerService = new RFIDScannerService(activity.getRfidReader());
            scannerService.setCallback(this);
            setupButtons();
        } else {
            handleRFIDNotAvailable();
        }
    }

    private void setupButtons() {
        scanButton.setOnClickListener(v -> {
            if (!scannerService.isScanning()) {
                startScanning();
            } else {
                stopScanning();
            }
        });

        clearButton.setOnClickListener(v -> {
            adapter.clearTags();
            tagCount.setText("Tags found: 0");
        });
    }

    private void startScanning() {
        if (scannerService.startScanning()) {
            scanButton.setText("Stop Scanning");
            statusText.setText("Scanning...");
        } else {
            showError("Failed to start scanning");
        }
    }

    private void stopScanning() {
        scannerService.stopScanning();
        scanButton.setText("Start Scanning");
        statusText.setText("Ready to scan");
    }

    private void handleRFIDNotAvailable() {
        statusText.setText("RFID not available");
        scanButton.setEnabled(false);
        clearButton.setEnabled(false);
        showError("RFID reader not available");
    }

    private void showError(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onTagScanned(RFIDTag tag) {
        requireActivity().runOnUiThread(() -> {
            adapter.updateTag(tag);
            tagCount.setText("Tags found: " + adapter.getItemCount());
        });
    }

    @Override
    public void onScanningStateChanged(boolean isScanning) {
        requireActivity().runOnUiThread(() -> {
            scanButton.setText(isScanning ? "Stop Scanning" : "Start Scanning");
            statusText.setText(isScanning ? "Scanning..." : "Ready to scan");
        });
    }

    @Override
    public void onError(String message) {
        requireActivity().runOnUiThread(() -> showError(message));
    }

    @Override
    public void onStop() {
        super.onStop();
        if (scannerService != null) {
            scannerService.stopScanning();
        }
    }
}
