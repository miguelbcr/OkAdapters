package app.spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import app.Item;
import butterknife.Bind;
import butterknife.ButterKnife;
import library.okadapters.R;
import library.spinner.OkSpinnerAdapter;

public class ItemSpinnerViewGroup extends FrameLayout implements OkSpinnerAdapter.Binder<Item>, OkSpinnerAdapter.BinderDropDown<Item> {
    @Bind(R.id.tv_value) TextView tv_value;

    public ItemSpinnerViewGroup(Context context) {
        super(context);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item_view_group, this, true);
        ButterKnife.bind(this, view);
    }

    @Override
    public void bindDropDownView(Item item, int position, int total) {
        tv_value.setText(item.toString());
        tv_value.setBackgroundResource(R.color.colorPrimary);
    }

    @Override
    public void bindView(Item item, int position, int total) {
        tv_value.setText(item.toString());
        tv_value.setBackgroundResource(R.color.colorAccent);
    }
}
