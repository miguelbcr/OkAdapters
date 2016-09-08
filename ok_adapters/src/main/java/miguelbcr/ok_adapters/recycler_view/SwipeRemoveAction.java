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

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.List;

public class SwipeRemoveAction<T> {
    private final RecyclerView recyclerView;
    private OnItemRemoved onItemRemoved;
    private String titleAction = "Undo", descriptionAction = "Item removed";
    private boolean redrawOnRemovedItem, undoAction;
    private final List<T> items;
    private final OkRecyclerViewAdapter adapter;

    public SwipeRemoveAction(RecyclerView recyclerView, List<T> aItems, OkRecyclerViewAdapter anAdapter) {
        this.recyclerView = recyclerView;
        this.items = aItems;
        this.adapter = anAdapter;

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (viewHolder instanceof OkRecyclerViewAdapter.ViewHolderPagerLoading) return 0;
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                T itemRemoved = items.get(position);
                items.remove(position);
                adapter.notifyItemRemoved(position);

                if (undoAction) showSnackbarUndo(itemRemoved, position);
                else if (onItemRemoved != null) onItemRemoved.onRemoved(itemRemoved);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public SwipeRemoveAction<T> notifyOnRemoved(OnItemRemoved<T> onItemRemoved) {
        this.onItemRemoved = onItemRemoved;
        return this;
    }

    public SwipeRemoveAction<T> withUndoAction() {
        this.undoAction = true;
        return this;
    }

    public SwipeRemoveAction<T> withTitleAction(String titleAction) {
        this.titleAction = titleAction;
        return this;
    }

    public SwipeRemoveAction<T> withDescriptionAction(String descriptionAction) {
        this.descriptionAction = descriptionAction;
        return this;
    }

    public SwipeRemoveAction<T> redrawAfterRemoved(boolean redrawOnRemovedItem) {
        this.redrawOnRemovedItem = redrawOnRemovedItem;
        return this;
    }

    private void showSnackbarUndo(final T itemRemoved, final int position) {
        Snackbar.make(recyclerView, descriptionAction, Snackbar.LENGTH_LONG)
                .setCallback(new Snackbar.Callback() {
                    @Override public void onDismissed(Snackbar snackbar, int event) {
                        if (redrawOnRemovedItem) adapter.notifyDataSetChanged();
                        if (onItemRemoved != null
                                && (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT
                                || event == Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE)) {
                            onItemRemoved.onRemoved(itemRemoved);
                        }
                    }
                })
                .setAction(titleAction, new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        v.setEnabled(false); //prevent multiple clicks
                        items.add(position, itemRemoved);
                        adapter.notifyItemInserted(position);
                    }
                }).show();
    }

    public interface OnItemRemoved<T> {
        void onRemoved(T item);
    }
}
