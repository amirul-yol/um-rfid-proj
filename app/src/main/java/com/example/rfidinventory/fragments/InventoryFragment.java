package com.example.rfidinventory.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.rfidinventory.R;

public class InventoryFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inventory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize views and setup listeners
    }

    private void navigateToItemDetails(int itemId) {
        Bundle args = new Bundle();
        args.putInt("itemId", itemId);
        Navigation.findNavController(requireView())
                 .navigate(R.id.action_inventory_to_details, args);
    }
}
