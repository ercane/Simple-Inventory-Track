package com.mree.inc.track.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.mree.inc.track.R;
import com.mree.inc.track.db.persist.Product;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductAdapter extends ArrayAdapter<Product> implements Filterable {

    @NonNull
    private final Context context;
    @BindView(R.id.tvOrder)
    TextView tvOrder;
    @BindView(R.id.tvProductCode)
    TextView tvProductCode;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvStock)
    TextView tvStock;
    @BindView(R.id.tvSource)
    TextView tvSource;
    @BindView(R.id.tvFee)
    TextView tvFee;
    private SparseBooleanArray mSelectedItemsIds;
    private List<Product> list;
    private List<Product> firstList = new ArrayList<>();

    public ProductAdapter(@NonNull Context context, @NonNull List<Product> list) {
        super(context, R.layout.list_product_item, list);
        this.context = context;
        this.list = list;
        firstList.addAll(list);
        mSelectedItemsIds = new SparseBooleanArray();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_product_item, parent, false);
        }
        ButterKnife.bind(this, row);
        Product item = getItem(position);

        tvOrder.setText((position + 1) + "");
        tvName.setText(item.getName());
        tvProductCode.setText(item.getProductCode());
        tvStock.setText(item.getStock());
        tvFee.setText(item.getFee());
        tvSource.setText(item.getSource());

        if (mSelectedItemsIds.get(position)) {
            row.setBackgroundResource(R.drawable.adapter_selected_bckgrnd);
        } else {
            row.setBackgroundColor(Color.TRANSPARENT);
        }

        return row;
    }


    public List<Product> getList() {
        return list;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                addAll((ArrayList<Product>) results.values);
                notifyDataSetChanged();
            }


            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<Product> filteredList = new ArrayList<Product>();

                // perform your search here using the searchConstraint String.
                if (TextUtils.isEmpty(constraint)) {
                    filteredList.addAll(firstList);
                    return results;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < list.size(); i++) {
                        String dataNames = list.get(i).toString();
                        if (!TextUtils.isEmpty(dataNames) && dataNames.toLowerCase().contains
                                (constraint.toString())) {
                            filteredList.add(list.get(i));
                        }
                    }
                }


                results.count = filteredList.size();
                results.values = filteredList;
                return results;
            }
        };

        return filter;
    }


}
