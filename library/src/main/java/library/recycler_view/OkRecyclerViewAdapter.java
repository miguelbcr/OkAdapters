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

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for RecyclerViews
 * @param <T> The model data associated with the view.
 * @param <V> The view
 */
public abstract class OkRecyclerViewAdapter<T, V extends View & BindView.Binder<T>> extends RecyclerView.Adapter<BindView<T, V>> {
    protected List<T> items = new ArrayList<>();
    protected Listener<T, V> listener;

    @Override public final BindView<T, V> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BindView<>(onCreateItemView(parent, viewType));
    }

    protected abstract V onCreateItemView(ViewGroup parent, int viewType);

    @Override public final void onBindViewHolder(BindView<T, V> viewHolder, int position) {
        final T item = items.get(position);

        final V view = viewHolder.getView();
        view.bind(item, position);

        if (listener != null) view.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onClickItem(item, view);
            }
        });
    }

    @Override public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickListener(Listener<T, V> lister) {
        this.listener = lister;
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
        addAll(data);
    }

    public List<T> getAll() {
        return items;
    }

    public void clear() {
        items.clear();
    }

    public interface Listener<T, V> {
        void onClickItem(T t, V v);
    }
}
