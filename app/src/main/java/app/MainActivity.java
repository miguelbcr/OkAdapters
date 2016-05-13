package app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import app.recycler_view.RecyclerViewActivity;
import app.recycler_view.RecyclerViewPagerActivity;
import app.spinner.SpinnerActivity;
import app.stack_view.StackViewActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;
import library.okadapters.R;

public class MainActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt_recycler_view) void bt_recycler_view() {
        startActivity(new Intent(MainActivity.this, RecyclerViewActivity.class));
    }

    @OnClick(R.id.bt_recycler_view_pager) void bt_recycler_view_pager() {
        startActivity(new Intent(MainActivity.this, RecyclerViewPagerActivity.class));
    }

    @OnClick(R.id.bt_spinner) void bt_spinner() {
        startActivity(new Intent(MainActivity.this, SpinnerActivity.class));
    }

    @OnClick(R.id.bt_stack_view) void bt_stack_view() {
        startActivity(new Intent(MainActivity.this, StackViewActivity.class));
    }

}
