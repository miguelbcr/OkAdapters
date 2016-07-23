package miguelbcr.ui.spinner;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * OkAdapter for Spinners
 * @param <T> The model data associated with the view.
 * @param <V> The view
 */
public abstract class OkSpinnerAdapter<T, V extends View & OkSpinnerAdapter.Binder<T>, VD extends View & OkSpinnerAdapter.BinderDropDown<T>> extends ArrayAdapter<T> {
    protected List<T> items = new ArrayList<>();

    public interface Binder<T> {
        void bindView(T item, int position, int count);
    }

    public interface BinderDropDown<T> {
        void bindDropDownView(T item, int position, int count);
    }

    public abstract V inflateView();
    public abstract VD inflateDropDownView();

    public List<T> getItems() {
        return items;
    }

    public OkSpinnerAdapter(Context context, List<T> items) {
        // Layout not used
        super(context, android.R.layout.simple_spinner_item, items);
        this.items = items;
    }

    @Override
    public View getDropDownView(final int position, View convertView, ViewGroup parent) {
        final T item = items.get(position);
        final VD view = (convertView == null) ? inflateDropDownView() : (VD) convertView;

        convertView = view;
        view.bindDropDownView(item, position, getCount());

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final T item = items.get(position);
        final V view = (convertView == null) ? inflateView() : (V) convertView;

        convertView = view;
        view.bindView(item, position, getCount());

        return convertView;
    }
}
