/*
 * Copyright 2016 miguelbcr
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

package miguelbcr.ok_adapters.recycler_view;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * OkAdapter for RecyclerViews
 * @param <T> The model data associated with the view.
 * @param <V> The view
 */
public abstract class OkRecyclerViewAdapter<T, V extends View & OkRecyclerViewAdapter.Binder<T>> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected List<T> items = new ArrayList<>();
    protected Listener<T, V> listener;
    private Pager<T, V> pager;
    private final static int LOADING_VIEW_TYPE = 1, ITEM_VIEW_TYPE = 2;
    boolean removeMoreListener;

    @Override public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LOADING_VIEW_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(pager.getIdResourceLoading(), parent, false);
            return new ViewHolderPagerLoading(view);
        }

        return new BindView<>(onCreateItemView(parent, viewType));
    }

    protected abstract V onCreateItemView(ViewGroup parent, int viewType);

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == LOADING_VIEW_TYPE) {
            if (!removeMoreListener) {
                if (!pager.isStillLoading()) pager.lastItemReached();
            }
            return;
        }

        final BindView<T, V> viewHolder = (BindView<T, V>) holder;

        final T item = items.get(position);

        final V view = viewHolder.getView();
        view.bind(item, position, getItemCount());

        if (listener != null) view.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onClickItem(item, view, viewHolder.getAdapterPosition());
            }
        });
    }

    @Override public int getItemViewType(int position) {
        if (position == items.size() && pager != null && !pager.isAllLoaded()) return LOADING_VIEW_TYPE;
        return ITEM_VIEW_TYPE;
    }

    @Override public int getItemCount() {
        return pager != null && !pager.isAllLoaded() ? items.size() + 1 : items.size();
    }

    public void setOnItemClickListener(Listener<T, V> listener) {
        this.listener = listener;
    }

    public void add(T item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void addAll(List<T> data) {
        items.addAll(data);
        notifyDataSetChanged();
    }

    public void setAll(List<T> data) {
        clear();
        items.addAll(data);
        notifyDataSetChanged();
    }

    public List<T> getAll() {
        return items;
    }

    public void clear() {
        items.clear();
    }

    public interface Listener<T, V> {
        void onClickItem(T t, V v, int position);
    }

    public interface Binder<T> {
        void bind(T data, int position, int count);
    }

    public SwipeRemoveAction<T> swipeToRemoveItemOn(final RecyclerView recyclerView) {
        return new SwipeRemoveAction<T>(recyclerView, items, this);
    }

    static class ViewHolderPagerLoading extends RecyclerView.ViewHolder {
        public ViewHolderPagerLoading(View itemView) {
            super(itemView);
        }
    }

    void removeMoreListener() {
        removeMoreListener = true;
    }

    public void setPager(@LayoutRes int idResourceLoading, List<T> initialLoad, Pager.LoaderPager<T> loaderPager) {
        this.pager = new Pager(idResourceLoading, initialLoad, loaderPager, this);
    }

    public void resetPager(Pager.Call<T> call) {
        if (pager != null) pager.reset(call);
    }

    public void setStillLoadingPager(boolean stillLoading) {
        this.pager.setStillLoading(stillLoading);
    }

    public interface LastItemListener {
        void lastItemReached();
    }

    public void configureGridLayoutManagerForPagination(final GridLayoutManager gridLayoutManager) {
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override public int getSpanSize(int position) {
                if (position == getItemCount() - 1 && !pager.isAllLoaded()) {
                    return gridLayoutManager.getSpanCount();
                } else {
                    return 1;
                }
            }
        });
    }
}
