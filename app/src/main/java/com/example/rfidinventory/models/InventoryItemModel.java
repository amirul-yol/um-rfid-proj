package com.example.rfidinventory.models;

import java.util.Date;
import java.util.List;

public class InventoryItemModel {
    private int id;
    private String rfidTag;
    private String name;
    private String location;
    private String status;
    private Date lastScan;
    private Date createdAt;
    private Date updatedAt;
    private int movementCount;
    private List<MovementHistory> movementHistory;
    private String description;
    private int locationId;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getRfidTag() { return rfidTag; }
    public void setRfidTag(String rfidTag) { this.rfidTag = rfidTag; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getLastScan() { return lastScan; }
    public void setLastScan(Date lastScan) { this.lastScan = lastScan; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    public int getMovementCount() { return movementCount; }
    public void setMovementCount(int movementCount) { this.movementCount = movementCount; }

    public List<MovementHistory> getMovementHistory() { return movementHistory; }
    public void setMovementHistory(List<MovementHistory> movementHistory) { this.movementHistory = movementHistory; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getLocationId() { return locationId; }
    public void setLocationId(int locationId) { this.locationId = locationId; }
}
