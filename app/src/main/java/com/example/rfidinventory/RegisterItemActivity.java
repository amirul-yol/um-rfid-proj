package com.example.rfidinventory;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.rfidinventory.api.ApiClient;
import com.example.rfidinventory.models.InventoryItemModel;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterItemActivity extends AppCompatActivity {
    public static final String EXTRA_RFID_TAG = "rfid_tag";
    private static final int DEFAULT_LOCATION_ID = 5; // Outside Room

    private TextInputEditText editRfidTag;
    private TextInputEditText editName;
    private TextInputEditText editDescription;
    private Button btnRegister;
    private ApiClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_item);

        // Initialize views
        editRfidTag = findViewById(R.id.edit_rfid_tag);
        editName = findViewById(R.id.edit_name);
        editDescription = findViewById(R.id.edit_description);
        btnRegister = findViewById(R.id.btn_register);

        // Get RFID tag from intent
        String rfidTag = getIntent().getStringExtra(EXTRA_RFID_TAG);
        editRfidTag.setText(rfidTag);

        // Initialize API client
        apiClient = new ApiClient(this);

        // Setup register button
        btnRegister.setOnClickListener(v -> registerItem());
    }

    private void registerItem() {
        String name = editName.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String rfidTag = editRfidTag.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            editName.setError("Name is required");
            return;
        }

        if (TextUtils.isEmpty(rfidTag)) {
            Toast.makeText(this, "Error: RFID tag is missing", Toast.LENGTH_SHORT).show();
            return;
        }

        InventoryItemModel item = new InventoryItemModel();
        item.setName(name);
        item.setDescription(description);
        item.setRfidTag(rfidTag);
        item.setLocationId(DEFAULT_LOCATION_ID);

        btnRegister.setEnabled(false);
        apiClient.createItem(item,
            createdItem -> {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Item registered successfully", Toast.LENGTH_SHORT).show();
                    finish();
                });
            },
            error -> {
                runOnUiThread(() -> {
                    Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                    btnRegister.setEnabled(true);
                });
            }
        );
    }
}
