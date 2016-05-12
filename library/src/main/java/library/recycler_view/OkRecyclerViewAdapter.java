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

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * OkAdapter for RecyclerViews
 * @param <T> The model data associated with the view.
 * @param <V> The view
 */
public abstract class OkRecyclerViewAdapter<T, V extends View & OkRecyclerViewAdapter.Binder<T>> extends RecyclerView.Adapter<BindView<T, V>> {
    protected List<T> items = new ArrayList<>();
    protected Listener<T, V> listener;

    @Override public final BindView<T, V> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BindView<>(onCreateItemView(parent, viewType));
    }

    protected abstract V onCreateItemView(ViewGroup parent, int viewType);

    @Override public final void onBindViewHolder(BindView<T, V> viewHolder, final int position) {
        final T item = items.get(position);

        final V view = viewHolder.getView();
        view.bind(item, position);

        if (listener != null) view.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onClickItem(item, view, position);
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
        void bind(T data, int position);
    }

    public SwipeRemoveAction swipeToRemoveItemOn(final RecyclerView recyclerView) {
        return new SwipeRemoveAction(recyclerView);
    }

    public class SwipeRemoveAction {
        private final RecyclerView recyclerView;
        private OnItemRemoved onItemRemoved;
        private String titleAction = "Undo", descriptionAction = "Item removed";
        private boolean redrawOnRemovedItem, undoAction;

        public SwipeRemoveAction(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    final int position = viewHolder.getAdapterPosition();
                    T itemRemoved = items.get(position);
                    items.remove(position);
                    notifyItemRemoved(position);

                    if (undoAction) showSnackbarUndo(itemRemoved, position);
                    else if (onItemRemoved != null) onItemRemoved.onRemoved(itemRemoved);
                }
            });
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }

        public SwipeRemoveAction notifyOnRemoved(OnItemRemoved<T> onItemRemoved) {
            this.onItemRemoved = onItemRemoved;
            return this;
        }

        public SwipeRemoveAction withUndoAction() {
            this.undoAction = true;
            return this;
        }

        public SwipeRemoveAction withTitleAction(String titleAction) {
            this.titleAction = titleAction;
            return this;
        }

        public SwipeRemoveAction withDescriptionAction(String descriptionAction) {
            this.descriptionAction = descriptionAction;
            return this;
        }

        public SwipeRemoveAction redrawAfterRemoved(boolean redrawOnRemovedItem) {
            this.redrawOnRemovedItem = redrawOnRemovedItem;
            return this;
        }

        private void showSnackbarUndo(final T itemRemoved, final int position) {
            Snackbar.make(recyclerView, descriptionAction, Snackbar.LENGTH_LONG)
                    .setCallback(new Snackbar.Callback() {
                        @Override public void onDismissed(Snackbar snackbar, int event) {
                            if (redrawOnRemovedItem) notifyDataSetChanged();
                            if (onItemRemoved != null && event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) onItemRemoved.onRemoved(itemRemoved);
                        }
                    })
                    .setAction(titleAction, new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            v.setEnabled(false); //prevent multiple clicks
                            items.add(position, itemRemoved);
                            notifyItemInserted(position);
                        }
                    }).show();
        }
    }


    public interface OnItemRemoved<T> {
        void onRemoved(T item);
    }
}
