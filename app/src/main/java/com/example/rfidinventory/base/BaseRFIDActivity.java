package com.example.rfidinventory.base;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.rscja.deviceapi.RFIDWithUHFUART;

/**
 * Base activity that handles RFID device initialization and lifecycle
 */
public abstract class BaseRFIDActivity extends AppCompatActivity {
    private static final String TAG = "BaseRFIDActivity";
    
    protected RFIDWithUHFUART rfidReader;
    protected boolean isRFIDInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRFIDReader();
    }

    @Override
    protected void onDestroy() {
        freeRFIDReader();
        super.onDestroy();
    }

    /**
     * Initialize the RFID reader
     * @return true if initialization successful, false otherwise
     */
    protected boolean initRFIDReader() {
        try {
            if (rfidReader == null) {
                rfidReader = RFIDWithUHFUART.getInstance();
            }
            if (rfidReader != null && !isRFIDInit) {
                isRFIDInit = rfidReader.init();
                if (isRFIDInit) {
                    Log.d(TAG, "RFID reader initialized successfully");
                } else {
                    Log.e(TAG, "Failed to initialize RFID reader");
                }
            }
            return isRFIDInit;
        } catch (Exception e) {
            Log.e(TAG, "Error initializing RFID reader", e);
            return false;
        }
    }

    /**
     * Free RFID reader resources
     */
    protected void freeRFIDReader() {
        try {
            if (rfidReader != null && isRFIDInit) {
                rfidReader.free();
                isRFIDInit = false;
                Log.d(TAG, "RFID reader freed successfully");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error freeing RFID reader", e);
        }
    }

    /**
     * Get the RFID reader instance
     * @return RFIDWithUHFUART instance or null if not initialized
     */
    public RFIDWithUHFUART getRfidReader() {
        return isRFIDInit ? rfidReader : null;
    }

    /**
     * Check if RFID reader is initialized
     * @return true if initialized, false otherwise
     */
    public boolean isRfidReady() {
        return isRFIDInit && rfidReader != null;
    }
}
