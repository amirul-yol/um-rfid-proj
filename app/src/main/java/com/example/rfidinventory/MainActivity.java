package com.example.rfidinventory;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.rfidinventory.base.BaseRFIDActivity;
import com.example.rfidinventory.service.RFIDScannerService;
import com.example.rfidinventory.model.RFIDTag;
import com.example.rfidinventory.api.ApiClient;
import com.example.rfidinventory.models.InventoryItemModel;

public class MainActivity extends BaseRFIDActivity {
    private Button btnStartScan;
    private Button btnRegister;
    private TextView txtStatus;
    private TextView txtRegisteredTags;
    private TextView txtUnregisteredTags;
    private RFIDScannerService scannerService;
    private ApiClient apiClient;
    private StringBuilder registeredTagsLog = new StringBuilder();
    private StringBuilder unregisteredTagsLog = new StringBuilder();
    private String lastUnregisteredTag = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize views
        btnStartScan = findViewById(R.id.btn_start_scan);
        btnRegister = findViewById(R.id.btn_register);
        txtStatus = findViewById(R.id.txt_status);
        txtRegisteredTags = findViewById(R.id.txt_registered_tags);
        txtUnregisteredTags = findViewById(R.id.txt_unregistered_tags);
        
        // Initialize services
        scannerService = new RFIDScannerService(getRfidReader());
        apiClient = new ApiClient(this);
        
        // Setup scanner callback
        scannerService.setCallback(new RFIDScannerService.ScanningCallback() {
            @Override
            public void onTagScanned(RFIDTag tag) {
                // Look up tag in database
                apiClient.getItemByRfidTag(tag.getEpc(), new ApiClient.ApiCallback<InventoryItemModel>() {
                    @Override
                    public void onSuccess(InventoryItemModel item) {
                        runOnUiThread(() -> {
                            String itemInfo = String.format("%s\nTag: %s\nLocation: %s\n\n",
                                item.getName(),
                                item.getRfidTag(),
                                item.getLocation());
                            registeredTagsLog.insert(0, itemInfo);
                            txtRegisteredTags.setText(registeredTagsLog.toString());
                        });
                    }

                    @Override
                    public void onError(String error) {
                        runOnUiThread(() -> {
                            if (error.startsWith("Unknown Tag")) {
                                lastUnregisteredTag = tag.getEpc();
                                String tagInfo = String.format("Unknown Tag: %s (RSSI: %d)\n\n",
                                    tag.getEpc(), tag.getRssi());
                                unregisteredTagsLog.insert(0, tagInfo);
                                txtUnregisteredTags.setText(unregisteredTagsLog.toString());
                            } else {
                                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }

            @Override
            public void onScanningStateChanged(boolean isScanning) {
                runOnUiThread(() -> {
                    btnStartScan.setText(isScanning ? "STOP SCAN" : "START SCAN");
                    txtStatus.setText(isScanning ? "Scanning..." : "Ready");
                    txtStatus.setVisibility(isScanning ? View.VISIBLE : View.GONE);
                    btnRegister.setVisibility(!isScanning && lastUnregisteredTag != null ? View.VISIBLE : View.GONE);
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    txtStatus.setText(message);
                    txtStatus.setVisibility(View.VISIBLE);
                });
            }
        });
        
        // Setup click listeners
        btnStartScan.setOnClickListener(v -> toggleScan());
        btnRegister.setOnClickListener(v -> {
            if (lastUnregisteredTag != null) {
                Intent intent = new Intent(this, RegisterItemActivity.class);
                intent.putExtra(RegisterItemActivity.EXTRA_RFID_TAG, lastUnregisteredTag);
                startActivity(intent);
            }
        });
    }
    
    private void toggleScan() {
        if (!scannerService.isScanning()) {
            if (scannerService.startScanning()) {
                registeredTagsLog.setLength(0);
                unregisteredTagsLog.setLength(0);
                lastUnregisteredTag = null;
                txtRegisteredTags.setText("");
                txtUnregisteredTags.setText("");
                btnRegister.setVisibility(View.GONE);
            }
        } else {
            scannerService.stopScanning();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_clear) {
            clearScannedItems();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearScannedItems() {
        txtRegisteredTags.setText("");
        txtUnregisteredTags.setText("");
        registeredTagsLog.setLength(0);
        unregisteredTagsLog.setLength(0);
        btnRegister.setVisibility(View.GONE);
        lastUnregisteredTag = null;
    }

    @Override
    protected void onDestroy() {
        if (scannerService != null && scannerService.isScanning()) {
            scannerService.stopScanning();
        }
        super.onDestroy();
    }
}
