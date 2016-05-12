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

import android.view.View;

public class RxPager<T, V extends View & OkRecyclerViewAdapter.Binder<T>> {
/*    private final RecyclerView recyclerView;
    private final OkRecyclerViewAdapter<T, V> adapter;
    private LoaderPager<T> loaderPager;
    private boolean allLoaded;

    public RxPager(RecyclerView recyclerView, OkRecyclerViewAdapter<T, V> adapter) {
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.adapter.clear();
    }

    public void setLoaderPager(LoaderPager<T> loaderPager) {
        this.loaderPager = loaderPager;

        setupMoreListenerRecyclerView();
        showItems(loaderPager.onNextPage(null));
    }

    public void setResetPager(ResetPager<T> resetPager) {
        //recyclerView.setRefreshListener(() -> resetItems(resetPager.onReset()));
    }

    private void resetItems(Observable<List<T>> oItems) {
        oItems.doOnNext(new Action1<List<T>>() {
            @Override public void call(List<T> items) {
                setupMoreListenerRecyclerView();
                adapter.clear();
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void showItems(Observable<List<T>> oItems) {
        oItems.subscribe(new Action1<List<T>>() {
            @Override public void call(List<T> items) {
                if (recyclerView.getAdapter() == null) recyclerView.setAdapter(adapter);

                adapter.addAll(items);
            }
        });
    }

    private void nextPage(T item) {
        if (allLoaded) {
            removeMoreListener();
            return;
        }

        Observable<List<T>> oItems = loaderPager.onNextPage(item)
                .map(items -> {
                    allLoaded = items.isEmpty();
                    return items;
                });

        showItems(oItems);
    }

    private void removeMoreListener() {
        recyclerView.getMoreProgressView().setVisibility(View.GONE);
        recyclerView.removeMoreListener();
    }

    private void setupMoreListenerRecyclerView() {
        recyclerView.setupMoreListener((overallItemsCount, itemsBeforeMore, maxLastVisiblePosition) -> {
            T user = adapter.getAll().get(adapter.getItemCount() - 1);
            nextPage(user);
        }, 5);
    }

    public interface LoaderPager<T> {
        Observable<List<T>> onNextPage(T t);
    }

    public interface ResetPager<T> {
        Observable<List<T>> onReset();
    }*/

}
