/*
 * Copyright 2017 Vector Creations Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.adapters;

import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomSummary;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsListAdapter<T, R extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<R> implements Filterable {

    private final int mLayoutRes;
    private final List<T> mItems;
    private final List<T> mFilteredItems;
    private final OnSelectItemListener<T> mListener;
    private OnLongClickItemListener<T> mOnLongClickListener;

    protected int mSelectedPosition = -1;

    /*
     * *********************************************************************************************
     * Constructor
     * *********************************************************************************************
     */

    public AbsListAdapter(@LayoutRes final int layoutRes, final OnSelectItemListener<T> listener) {
        mLayoutRes = layoutRes;
        mItems = new ArrayList<>();
        mFilteredItems = new ArrayList<>();
        mListener = listener;
    }

    public void setOnLongClickListener(OnLongClickItemListener<T> onLongClickListener) {
        mOnLongClickListener = onLongClickListener;
    }

    /*
     * *********************************************************************************************
     * RecyclerView.Adapter methods
     * *********************************************************************************************
     */

    @Override
    public R onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        final View itemView = layoutInflater.inflate(mLayoutRes, viewGroup, false);
        return createViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final R viewHolder, int position) {
        final T item = mFilteredItems.get(position);
        populateViewHolder(viewHolder, item);


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSelectItem(item, viewHolder.getAdapterPosition());
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != mOnLongClickListener) {
                    mOnLongClickListener.onItemLongClick(item, viewHolder.getAdapterPosition());
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredItems.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults results = new FilterResults();

                mFilteredItems.clear();
                if (TextUtils.isEmpty(constraint)) {
                    mFilteredItems.addAll(mItems);
                } else {
                    final String filterPattern = constraint.toString().trim();
                    mFilteredItems.addAll(getFilterItems(mItems, filterPattern));
                }

                results.values = mFilteredItems;
                results.count = mFilteredItems.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }

    /*
     * *********************************************************************************************
     * Public methods
     * *********************************************************************************************
     */

    /**
     * Feed the adapter with items
     *
     * @param items
     */
    @CallSuper
    public void setItems(final List<T> items) {
        if (items != null) {
            mItems.clear();
            mItems.addAll(items);

            mFilteredItems.clear();
            mFilteredItems.addAll(items);
        }

        notifyDataSetChanged();
    }

    /**
     * Move a child view in the roomSummary dir tree
     */
    public void move(int fromPosition, int toPosition) {
        {
            T item = mItems.get(fromPosition);
            mItems.remove(fromPosition);
            if (toPosition >= mItems.size()) {
                mItems.add(item);
            } else {
                mItems.add(toPosition, item);
            }
        }

        {
            T item = mFilteredItems.get(fromPosition);
            mFilteredItems.remove(fromPosition);
            if (toPosition >= mFilteredItems.size()) {
                mFilteredItems.add(item);
            } else {
                mFilteredItems.add(toPosition, item);
            }
        }

        notifyDataSetChanged();
    }

    public void insertAt(T item, int position) {
        mItems.add(position, item);
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        return mItems.get(position);
    }

    public void remove(T object) {
        mItems.remove(object);
        mFilteredItems.remove(object);
        notifyDataSetChanged();
    }

    /*
     * *********************************************************************************************
     * Abstract methods
     * *********************************************************************************************
     */

    protected abstract R createViewHolder(final View itemView);

    protected abstract void populateViewHolder(final R viewHolder, final T item);

    protected abstract List<T> getFilterItems(final List<T> items, final String pattern);

    /*
     * *********************************************************************************************
     * Inner classes
     * *********************************************************************************************
     */

    public interface OnSelectItemListener<T> {
        void onSelectItem(T item, int position);
    }

    public interface OnLongClickItemListener<T> {
        void onItemLongClick(T item, int position);
    }
}
