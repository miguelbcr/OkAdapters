package app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import app.recycler_view.RecyclerViewActivity;
import app.spinner.SpinnerActivity;
import butterknife.Bind;
import butterknife.ButterKnife;
import library.okadapters.R;

/**
 * Created by miguel on 08/04/2016.
 */
public class MainActivity extends AppCompatActivity {
    @Bind(R.id.bt_recyclerview) View btRecyclerView;
    @Bind(R.id.bt_spinner) View btSpinner;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ButterKnife.bind(this);
        setListeners();
    }

    private void setListeners() {
        btRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RecyclerViewActivity.class));
            }
        });

        btSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SpinnerActivity.class));
            }
        });
    }
}
