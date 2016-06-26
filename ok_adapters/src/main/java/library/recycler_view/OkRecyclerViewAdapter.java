/*
 * Copyright 2016 FuckBoilerplate
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

package library.recycler_view;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * OkAdapter for RecyclerViews
 * @param <T> The model data associated with the view.
 * @param <V> The view
 */
public abstract class OkRecyclerViewAdapter<T, V extends View & OkRecyclerViewAdapter.Binder<T>> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected List<T> items = new ArrayList<>();
    protected Listener<T, V> listener;
    private RxPager<T, V> rxPager;
    private final static int LOADING_VIEW_TYPE = 1, ITEM_VIEW_TYPE = 2;
    boolean removeMoreListener;

    @Override public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LOADING_VIEW_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(rxPager.getIdResourceLoading(), parent, false);
            return new ViewHolderPagerLoading(view);
        }

        return new BindView<>(onCreateItemView(parent, viewType));
    }

    protected abstract V onCreateItemView(ViewGroup parent, int viewType);

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == LOADING_VIEW_TYPE) {
            if (!removeMoreListener) {
                if (!rxPager.isStillLoading()) rxPager.lastItemReached();
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
        if (position == items.size() && rxPager != null && !rxPager.isAllLoaded()) return LOADING_VIEW_TYPE;
        return ITEM_VIEW_TYPE;
    }

    @Override public int getItemCount() {
        return rxPager != null && !rxPager.isAllLoaded() ? items.size() + 1 : items.size();
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

    public void setRxPager(@LayoutRes int idResourceLoading, RxPager.LoaderPager<T> loaderPager) {
        this.rxPager = new RxPager(idResourceLoading, loaderPager, this);
    }

    public void resetPager(Observable<List<T>> oItems) {
        if (rxPager != null) rxPager.reset(oItems);
    }

    public interface LastItemListener {
        void lastItemReached();
    }

    public void configureGridLayoutManagerForPagination(final GridLayoutManager gridLayoutManager) {
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override public int getSpanSize(int position) {
                if (position == getItemCount() - 1 && !rxPager.isAllLoaded()) {
                    return gridLayoutManager.getSpanCount();
                } else {
                    return 1;
                }
            }
        });
    }
}
