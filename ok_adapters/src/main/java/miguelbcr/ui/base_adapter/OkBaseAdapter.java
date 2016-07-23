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

package miguelbcr.ui.base_adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * OkAdapter for BaseAdapter
 * @param <T> The model data associated with the view.
 * @param <V> The view
 */
public abstract class OkBaseAdapter<T, V extends View & OkBaseAdapter.Binder<T>> extends BaseAdapter {
    private List<T> items = new ArrayList<>();
    private ViewHolder<T, V> holder = null;

    public abstract V inflateView();

    @Override public int getCount() {
        return items.size();
    }

    @Override public T getItem(int position) {
        return items.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public View getView(int position, View view, ViewGroup parent) {
        final T item = items.get(position);

        if (view == null) {
            view = inflateView();
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.getView().bind(item, position, getCount());

        return view;
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

    public static class ViewHolder<T, V extends View & OkBaseAdapter.Binder<T>> {
        private final V view;

        public ViewHolder(V itemView) {
            view = itemView;
        }

        public V getView() {
            return view;
        }
    }

    public interface Binder<T> {
        void bind(T item, int position, int count);
    }

}
