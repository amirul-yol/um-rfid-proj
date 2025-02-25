package com.example.rfidinventory.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rfidinventory.R;
import com.example.rfidinventory.model.RFIDTag;
import java.util.ArrayList;
import java.util.List;

public class RFIDTagAdapter extends RecyclerView.Adapter<RFIDTagAdapter.ViewHolder> {
    private List<RFIDTag> tags = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tagEpc;
        private final TextView tagRssi;
        private final TextView tagCount;

        public ViewHolder(View view) {
            super(view);
            tagEpc = view.findViewById(R.id.tag_epc);
            tagRssi = view.findViewById(R.id.tag_rssi);
            tagCount = view.findViewById(R.id.tag_count);
        }

        public void bind(RFIDTag tag) {
            tagEpc.setText(tag.getEpc());
            tagRssi.setText("RSSI: " + tag.getRssi() + " dBm");
            tagCount.setText("Count: " + tag.getCount());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rfid_tag, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(tags.get(position));
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public void updateTag(RFIDTag tag) {
        int index = tags.indexOf(tag);
        if (index >= 0) {
            tags.set(index, tag);
            notifyItemChanged(index);
        } else {
            tags.add(tag);
            notifyItemInserted(tags.size() - 1);
        }
    }

    public void clearTags() {
        tags.clear();
        notifyDataSetChanged();
    }
}
