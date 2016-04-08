package app.spinner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.Item;
import butterknife.Bind;
import butterknife.ButterKnife;
import library.okadapters.R;
import library.spinner.OkSpinnerAdapter;

public class SpinnerActivity extends AppCompatActivity {
    @Bind(R.id.spinner) Spinner spinner;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ok_spinner_activity);

        ButterKnife.bind(this);
        setUpSpinner();
    }

    private void setUpSpinner() {
        OkSpinnerAdapter<Item, ItemSpinnerViewGroup> adapter = new OkSpinnerAdapter<Item, ItemSpinnerViewGroup>(SpinnerActivity.this, getItems()) {
            @Override
            public ItemSpinnerViewGroup inflateView() {
                return new ItemSpinnerViewGroup(SpinnerActivity.this);
            }
        };

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SpinnerActivity.this, getItems().get(position).getValue(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner.setAdapter(adapter);
    }

    private List<Item> getItems() {
        List<Item> items = new ArrayList();

        for (int i = 0; i < 50; i++) {
            items.add(new Item("Item: " + i));
        }

        return items;
    }

}
