package com.example.aitor.flickry;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.example.aitor.flickry.util.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class DataListAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int INVALID_SELECTED_INDEX = -1;
    private int mSelectedPosition;
    private List<T> items;

    // each time data is set, we update this variable so that if DiffUtil calculation returns
    // after repetitive updates, we can ignore the old calculation
    private int dataVersion = 0;

    public DataListAdapter() {
        mSelectedPosition = INVALID_SELECTED_INDEX;
        items = new ArrayList<>();
    }

    @Override
    public DataListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return createNewViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setSelected(position == mSelectedPosition);
        ((DataListViewHolder) holder).bindTo(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected abstract DataListViewHolder createNewViewHolder(ViewGroup parent, int viewType);

    //call this method to clear all data in the list. and it will also deselect the selected item.
    public final void clearData() {
        // we do this because the list of items might be immutable
        items = new ArrayList<>();
        notifyDataSetChanged();
    }

    /**
     * @return returns an unmodifiable copy of the data of null if it is empty
     */
    @Nullable
    public final List<T> getData() {
        return Lists.isEmpty(items) ? null : Collections.unmodifiableList(items);
    }

    public final boolean isEmpty() {
        return Lists.isEmpty(items);
    }

    // call this method to add data to the current list. The old data is still there.
    @CallSuper
    public void addAll(List<T> list) {
        int pos = items.size(); // first position that will be modified
        items.addAll(list);
        notifyItemRangeChanged(pos, list.size());
    }

    @SuppressLint("StaticFieldLeak")
    @CallSuper
    public void replace(List<T> update) {
        dataVersion++;
        if (Lists.isEmpty(items)) { // we use Lists helper because items can be null or empty
            if (update == null) {
                return;
            }
            items = update;
            notifyDataSetChanged();
        } else if (update == null) {
            int oldSize = items.size();
            items = new ArrayList<>();
            notifyItemRangeRemoved(0, oldSize);
        } else {
            final int startVersion = dataVersion;
            final List<T> oldItems = items;
            new AsyncTask<Void, Void, DiffUtil.DiffResult>() {
                @Override
                protected DiffUtil.DiffResult  doInBackground(Void... voids) {
                    return DiffUtil.calculateDiff(new DiffUtil.Callback() {
                        @Override
                        public int getOldListSize() {
                            return oldItems.size();
                        }

                        @Override
                        public int getNewListSize() {
                            return update.size();
                        }

                        @Override
                        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                            T oldItem = oldItems.get(oldItemPosition);
                            T newItem = update.get(newItemPosition);
                            return DataListAdapter.this.areItemsTheSame(oldItem, newItem);
                        }

                        @Override
                        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                            T oldItem = oldItems.get(oldItemPosition);
                            T newItem = update.get(newItemPosition);
                            return DataListAdapter.this.areContentsTheSame(oldItem, newItem);
                        }
                    });
                }

                @Override
                protected void onPostExecute(DiffUtil.DiffResult diffResult) {
                    if (startVersion != dataVersion) {
                        // ignore update
                        return;
                    }
                    items = update;
                    diffResult.dispatchUpdatesTo(DataListAdapter.this);

                }
            }.execute();
        }

    }

    protected T getItem(int position) {
        return items.get(position);
    }

    protected abstract boolean areItemsTheSame(T oldItem, T newItem);
    protected abstract boolean areContentsTheSame(T oldItem, T newItem);

    public abstract class DataListViewHolder extends RecyclerView.ViewHolder {

        public DataListViewHolder(View itemView) {
            super(itemView);
        }

        // call this method to bind to data.
        void bindTo(int index) {
            bindDataToView(getItem(index));
        }

        protected abstract void bindDataToView(T data);
    }
}

