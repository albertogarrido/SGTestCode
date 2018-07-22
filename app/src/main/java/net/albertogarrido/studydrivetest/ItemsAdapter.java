package net.albertogarrido.studydrivetest;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemsAdapter extends ListAdapter<Item, ItemsAdapter.ItemViewHolder> {

    public ItemsAdapter() {
        super(new DiffCallback());
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ItemViewHolder(inflater.inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public void submitList(final List<Item> list) {
        super.submitList(list != null ? new ArrayList<>(list) : null);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root)
        ViewGroup root;
        @BindView(R.id.date)
        TextView date;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Item item) {
            date.setText(String.format("Item added:\n%s", item.getDate()));
            root.setBackgroundColor(item.getColor());
        }
    }

    private static class DiffCallback extends DiffUtil.ItemCallback<Item> {

        @Override
        public boolean areItemsTheSame(
                @NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(
                @NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.equals(newItem);
        }
    }
}
