package app.recycler_view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.Item;
import butterknife.Bind;
import butterknife.ButterKnife;
import miguelbcr.okadapters.R;
import miguelbcr.ok_adapters.recycler_view.OkRecyclerViewAdapter;
import miguelbcr.ok_adapters.recycler_view.SwipeRemoveAction;

public class RecyclerViewActivity extends AppCompatActivity {
    @Bind(R.id.rv_items) RecyclerView rv_items;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ok_rv_adapter_activity);

        ButterKnife.bind(this);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        rv_items.setLayoutManager(new LinearLayoutManager(this));

        OkRecyclerViewAdapter<Item, ItemViewGroup> adapter = new OkRecyclerViewAdapter<Item, ItemViewGroup>() {
            @Override protected ItemViewGroup onCreateItemView(ViewGroup parent, int viewType) {
                return new ItemViewGroup(parent.getContext());
            }
        };

        adapter.setAll(getItems());

        adapter.setOnItemClickListener(new OkRecyclerViewAdapter.Listener<Item, ItemViewGroup>() {
            @Override public void onClickItem(Item item, ItemViewGroup itemViewGroup, int position) {
                Toast.makeText(RecyclerViewActivity.this, item.toString() + "   Pos: " + position, Toast.LENGTH_SHORT).show();
            }
        });

        adapter.setOnItemLongClickListener(new OkRecyclerViewAdapter.ListenerLongClick<Item, ItemViewGroup>() {
            @Override
            public boolean onLongClickItem(Item item, ItemViewGroup itemViewGroup, int position) {
                Toast.makeText(RecyclerViewActivity.this, item.toString() + "   Pos: " + position + " (Long click)", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        adapter.swipeToRemoveItemOn(rv_items).withUndoAction()
                .notifyOnRemoved(new SwipeRemoveAction.OnItemRemoved<Item>() {
                    @Override public void onRemoved(Item item) {
                        Toast.makeText(RecyclerViewActivity.this, item.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        rv_items.setAdapter(adapter);
    }

    private List<Item> getItems() {
        List<Item> items = new ArrayList();

        for (int i = 0; i < 50; i++) {
            items.add(new Item(i));
        }

        return items;
    }

}
