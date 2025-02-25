package com.example.rfidinventory.api;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.rfidinventory.BuildConfig;
import com.example.rfidinventory.models.InventoryItemModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ApiClient {
    private static final String TAG = "ApiClient";
    private static final String BASE_URL = BuildConfig.API_BASE_URL;
    
    private RequestQueue requestQueue;
    private Gson gson;

    public ApiClient(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();
    }

    public interface ApiCallback<T> {
        void onSuccess(T result);
        void onError(String error);
    }

    public void getAllItems(Consumer<List<InventoryItemModel>> onSuccess, Consumer<String> onError) {
        String url = BASE_URL + "/inventory";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        List<InventoryItemModel> items = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            InventoryItemModel item = gson.fromJson(response.getJSONObject(i).toString(), InventoryItemModel.class);
                            items.add(item);
                        }
                        onSuccess.accept(items);
                    } catch (Exception e) {
                        onError.accept("Failed to parse response: " + e.getMessage());
                    }
                },
                error -> handleError(error, onError));

        requestQueue.add(request);
    }

    public void getItemByRfidTag(String rfidTag, ApiCallback<InventoryItemModel> callback) {
        String url = BASE_URL + "/inventory/tag/" + rfidTag;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
            response -> {
                try {
                    InventoryItemModel item = new InventoryItemModel();
                    item.setName(response.getString("name"));
                    item.setRfidTag(response.getString("rfid_tag"));
                    JSONObject location = response.optJSONObject("location");
                    item.setLocation(location != null ? location.getString("name") : "Unknown");
                    
                    callback.onSuccess(item);
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing response", e);
                    callback.onError("Error parsing item data");
                }
            },
            error -> {
                Log.e(TAG, "Error fetching item", error);
                if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                    callback.onError("Unknown Tag");
                } else {
                    callback.onError("Network error: " + error.getMessage());
                }
            }
        );

        requestQueue.add(request);
    }

    public void createItem(InventoryItemModel item, Consumer<InventoryItemModel> onSuccess, Consumer<String> onError) {
        String url = BASE_URL + "/inventory";
        
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("name", item.getName());
            jsonBody.put("rfid_tag", item.getRfidTag());
            jsonBody.put("description", item.getDescription());
            jsonBody.put("current_location", item.getLocationId());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    try {
                        if (response.has("data")) {
                            JSONObject data = response.getJSONObject("data");
                            InventoryItemModel createdItem = new InventoryItemModel();
                            createdItem.setName(data.getString("name"));
                            createdItem.setRfidTag(data.getString("rfid_tag"));
                            createdItem.setDescription(data.optString("description"));
                            createdItem.setLocationId(data.optInt("current_location"));
                            onSuccess.accept(createdItem);
                        } else {
                            onSuccess.accept(item);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing response", e);
                        onSuccess.accept(item);
                    }
                },
                error -> {
                    Log.e(TAG, "Error creating item", error);
                    if (error.networkResponse != null) {
                        try {
                            String errorResponse = new String(error.networkResponse.data);
                            onError.accept("Error: " + errorResponse);
                        } catch (Exception e) {
                            onError.accept("Error creating item");
                        }
                    } else {
                        onError.accept("Network error");
                    }
                }
            );

            requestQueue.add(request);
        } catch (Exception e) {
            Log.e(TAG, "Error creating request", e);
            onError.accept("Error creating request");
        }
    }

    public void updateItem(int itemId, InventoryItemModel item, Consumer<InventoryItemModel> onSuccess, Consumer<String> onError) {
        String url = BASE_URL + "/inventory/" + itemId;
        JSONObject jsonBody;
        try {
            jsonBody = new JSONObject(gson.toJson(item));
        } catch (JSONException e) {
            onError.accept("Failed to create request body: " + e.getMessage());
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonBody,
                response -> {
                    try {
                        InventoryItemModel updatedItem = gson.fromJson(response.toString(), InventoryItemModel.class);
                        onSuccess.accept(updatedItem);
                    } catch (Exception e) {
                        onError.accept("Failed to parse response: " + e.getMessage());
                    }
                },
                error -> handleError(error, onError));

        requestQueue.add(request);
    }

    public void deleteItem(int itemId, Runnable onSuccess, Consumer<String> onError) {
        String url = BASE_URL + "/inventory/" + itemId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                response -> onSuccess.run(),
                error -> handleError(error, onError));

        requestQueue.add(request);
    }

    public void recordMovement(int itemId, String fromLocation, String toLocation,
                             Consumer<InventoryItemModel> onSuccess, Consumer<String> onError) {
        String url = BASE_URL + "/inventory/" + itemId + "/movement";
        
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("from_location", fromLocation);
            jsonBody.put("to_location", toLocation);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                    response -> {
                        try {
                            InventoryItemModel updatedItem = gson.fromJson(response.toString(), InventoryItemModel.class);
                            onSuccess.accept(updatedItem);
                        } catch (Exception e) {
                            onError.accept("Failed to parse response: " + e.getMessage());
                        }
                    },
                    error -> handleError(error, onError));

            requestQueue.add(request);
        } catch (JSONException e) {
            onError.accept("Failed to create request: " + e.getMessage());
        }
    }

    private void handleError(VolleyError error, Consumer<String> onError) {
        String errorMessage = "Network error";
        if (error.networkResponse != null) {
            errorMessage = String.format("Error: %d - %s", error.networkResponse.statusCode,
                    new String(error.networkResponse.data));
        }
        Log.e(TAG, errorMessage, error);
        onError.accept(errorMessage);
    }
}
