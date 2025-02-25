package com.example.rfidinventory.model;

import android.util.Log;

/**
 * Model class for RFID tag data
 */
public class RFIDTag {
    private static final String TAG = "RFIDTag";
    
    private String id;
    private String epc;
    private int rssi;
    private long timestamp;
    private int count;

    public RFIDTag(String epc, String rssi) {
        this.epc = epc;
        try {
            this.rssi = Integer.parseInt(rssi);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Error parsing RSSI value: " + rssi, e);
            this.rssi = -100; // Default weak signal value
        }
        this.timestamp = System.currentTimeMillis();
        this.count = 1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEpc() {
        return epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void updateTimestamp() {
        this.timestamp = System.currentTimeMillis();
    }

    public int getCount() {
        return count;
    }

    public void incrementCount() {
        this.count++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RFIDTag tag = (RFIDTag) o;
        return epc != null ? epc.equals(tag.epc) : tag.epc == null;
    }

    @Override
    public int hashCode() {
        return epc != null ? epc.hashCode() : 0;
    }
}
