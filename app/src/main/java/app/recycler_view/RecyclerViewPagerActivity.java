package app.recycler_view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import app.Item;
import butterknife.Bind;
import butterknife.ButterKnife;
import library.okadapters.R;
import library.recycler_view.OkRecyclerViewAdapter;
import library.recycler_view.RxPager;
import library.recycler_view.SwipeRemoveAction;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class RecyclerViewPagerActivity extends AppCompatActivity {
    @Bind(R.id.rv_items) RecyclerView rv_items;
    private boolean isReversed;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ok_rv_adapter_pager_activity);

        ButterKnife.bind(this);
        setUpRecyclerView(false);
    }

    private void setUpRecyclerView(boolean reverseLayout) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(reverseLayout);

        rv_items.setLayoutManager(layoutManager);

        final OkRecyclerViewAdapter<Item, ItemViewGroup> adapter = new OkRecyclerViewAdapter<Item, ItemViewGroup>() {
            @Override protected ItemViewGroup onCreateItemView(ViewGroup parent, int viewType) {
                return new ItemViewGroup(parent.getContext());
            }
        };

        adapter.setOnItemClickListener(new OkRecyclerViewAdapter.Listener<Item, ItemViewGroup>() {
            @Override public void onClickItem(Item item, ItemViewGroup itemViewGroup, int position) {
                Toast.makeText(RecyclerViewPagerActivity.this, item.toString() + " " + "  Pos: " + position, Toast.LENGTH_SHORT).show();
            }
        });

        adapter.setRxPager(R.layout.loading_pager, new RxPager.LoaderPager<Item>() {
                    @Override public Observable<List<Item>> onNextPage(Item lastItem) {
                        return getItems(lastItem);
                    }
                });

        findViewById(R.id.bt_reset).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                adapter.resetPager(getItems(null));
            }
        });

        findViewById(R.id.bt_reverse).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                isReversed = !isReversed;
                setUpRecyclerView(isReversed);
            }
        });

        adapter.swipeToRemoveItemOn(rv_items).withUndoAction()
                .notifyOnRemoved(new SwipeRemoveAction.OnItemRemoved<Item>() {
                    @Override public void onRemoved(Item item) {
                        Toast.makeText(RecyclerViewPagerActivity.this, item.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        rv_items.setAdapter(adapter);
    }

    private Observable<List<Item>> getItems(Item lastItem) {
        int index = lastItem != null ? lastItem.getId() + 1 : 0;
        int max = index + 30;

        List<Item> items = new ArrayList();

        for (; index < max; index++) {
            items.add(new Item(index));
        }

        if (index > 100) {
            return Observable.<List<Item>>error(new RuntimeException("Exception cached :D")).doOnError(new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Toast.makeText(RecyclerViewPagerActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        return Observable.just(items)
                .delay(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread());
    }

}
