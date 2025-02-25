package com.example.rfidinventory.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rfidinventory.api.ApiClient;
import com.example.rfidinventory.databinding.FragmentConnectionTestBinding;
import com.example.rfidinventory.models.InventoryItemModel;

public class ConnectionTestFragment extends Fragment {
    private FragmentConnectionTestBinding binding;
    private ApiClient apiClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiClient = new ApiClient(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentConnectionTestBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupListeners();
    }

    private void setupListeners() {
        // Test getting all items
        binding.btnTestGetAll.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.txtResult.setText("Loading...");
            
            apiClient.getAllItems(
                items -> {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.txtResult.setText("Found " + items.size() + " items");
                },
                error -> {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.txtResult.setText("Error: " + error);
                }
            );
        });

        // Test creating an item
        binding.btnTestCreate.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.txtResult.setText("Creating test item...");

            InventoryItemModel newItem = new InventoryItemModel();
            newItem.setRfidTag("TEST_" + System.currentTimeMillis());
            newItem.setName("Test Item");
            newItem.setLocation("Test Location");
            newItem.setStatus("available");

            apiClient.createItem(newItem,
                createdItem -> {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.txtResult.setText("Created item with ID: " + createdItem.getId());
                },
                error -> {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.txtResult.setText("Error: " + error);
                }
            );
        });

        // Test RFID lookup
        binding.btnTestRfid.setOnClickListener(v -> {
            String rfidTag = binding.edtRfidTag.getText().toString();
            if (rfidTag.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter an RFID tag", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.progressBar.setVisibility(View.VISIBLE);
            binding.txtResult.setText("Looking up RFID: " + rfidTag);

            apiClient.getItemByRfidTag(rfidTag, new ApiClient.ApiCallback<InventoryItemModel>() {
                @Override
                public void onSuccess(InventoryItemModel item) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.txtResult.setText("Found item: " + item.getName() + "\nLocation: " + item.getLocation());
                }

                @Override
                public void onError(String error) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.txtResult.setText("Error: " + error);
                }
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
