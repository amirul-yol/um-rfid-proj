package com.example.rfidinventory.service;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.example.rfidinventory.model.RFIDTag;
import com.rscja.deviceapi.RFIDWithUHFUART;
import com.rscja.deviceapi.entity.UHFTAGInfo;
import com.rscja.deviceapi.interfaces.IUHFInventoryCallback;
import java.util.HashSet;
import java.util.Set;

/**
 * Service class to handle RFID scanning operations
 */
public class RFIDScannerService {
    private static final String TAG = "RFIDScannerService";

    private final RFIDWithUHFUART reader;
    private boolean isScanning = false;
    private final Set<String> scannedEpcs = new HashSet<>();
    private ScanningCallback callback;
    private final Handler mainHandler;

    public interface ScanningCallback {
        void onTagScanned(RFIDTag tag);
        void onScanningStateChanged(boolean isScanning);
        void onError(String message);
    }

    public RFIDScannerService(RFIDWithUHFUART reader) {
        this.reader = reader;
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void setCallback(ScanningCallback callback) {
        this.callback = callback;
    }

    public boolean isScanning() {
        return isScanning;
    }

    /**
     * Start continuous scanning for RFID tags
     * @return true if scanning started successfully
     */
    public boolean startScanning() {
        if (reader == null || isScanning) {
            return false;
        }

        try {
            scannedEpcs.clear();
            
            // Set power to maximum for better reading
            reader.setPower(30);
            
            // Use continuous inventory mode
            reader.setInventoryCallback(new IUHFInventoryCallback() {
                @Override
                public void callback(UHFTAGInfo tagInfo) {
                    handleTagData(tagInfo);
                }
            });

            isScanning = reader.startInventoryTag();
            
            if (isScanning) {
                if (callback != null) {
                    mainHandler.post(() -> callback.onScanningStateChanged(true));
                }
                return true;
            } else {
                if (callback != null) {
                    mainHandler.post(() -> callback.onError("Failed to start RFID reader"));
                }
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error starting scan", e);
            if (callback != null) {
                mainHandler.post(() -> callback.onError("Failed to start scanning: " + e.getMessage()));
            }
            return false;
        }
    }

    /**
     * Stop scanning for RFID tags
     */
    public void stopScanning() {
        if (reader != null && isScanning) {
            try {
                reader.stopInventory();
                isScanning = false;
                if (callback != null) {
                    mainHandler.post(() -> callback.onScanningStateChanged(false));
                }
            } catch (Exception e) {
                Log.e(TAG, "Error stopping scan", e);
                if (callback != null) {
                    mainHandler.post(() -> callback.onError("Failed to stop scanning: " + e.getMessage()));
                }
            }
        }
    }

    /**
     * Process scanned tag data
     */
    private void handleTagData(UHFTAGInfo tagInfo) {
        if (tagInfo != null && tagInfo.getEPC() != null) {
            String epc = tagInfo.getEPC();
            if (!scannedEpcs.contains(epc)) {
                scannedEpcs.add(epc);
                RFIDTag tag = new RFIDTag(epc, tagInfo.getRssi());
                if (callback != null) {
                    mainHandler.post(() -> callback.onTagScanned(tag));
                }
            }
        }
    }
}
