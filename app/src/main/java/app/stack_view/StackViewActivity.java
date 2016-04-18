package app.stack_view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.StackView;

import java.util.ArrayList;
import java.util.List;

import app.Item;
import butterknife.Bind;
import butterknife.ButterKnife;
import library.base_adapter.OkBaseAdapter;
import library.okadapters.R;

public class StackViewActivity extends AppCompatActivity {
    @Bind(R.id.sv_values) StackView sv_values;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ok_stack_view_activity);

        ButterKnife.bind(this);
        setUpStackViewAdapter();
    }

    private void setUpStackViewAdapter() {
        OkBaseAdapter adapter = new OkBaseAdapter<Item, ItemViewGroup>() {

            @Override public ItemViewGroup inflateView() {
                return new ItemViewGroup(StackViewActivity.this);
            }
        };

        sv_values.setAdapter(adapter);
        adapter.setAll(getItems());
    }

    private List<Item> getItems() {
        List<Item> items = new ArrayList();

        for (int i = 0; i < 5; i++) {
            items.add(new Item("Item: " + i));
        }

        return items;
    }
}
