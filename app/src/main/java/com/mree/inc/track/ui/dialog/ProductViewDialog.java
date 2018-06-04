package com.mree.inc.track.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mree.inc.track.R;
import com.mree.inc.track.db.persist.Product;
import com.mree.inc.track.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductViewDialog extends BaseDialog {

    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.nameLayout)
    LinearLayout nameLayout;
    @BindView(R.id.tvBarcode)
    TextView tvBarcode;
    @BindView(R.id.barcodeLayout)
    LinearLayout barcodeLayout;
    @BindView(R.id.tvProductCode)
    TextView tvProductCode;
    @BindView(R.id.productCodeLayout)
    LinearLayout productCodeLayout;
    @BindView(R.id.tvStock)
    TextView tvStock;
    @BindView(R.id.stockLayout)
    LinearLayout stockLayout;
    @BindView(R.id.tvSource)
    TextView tvSource;
    @BindView(R.id.sourceLayout)
    LinearLayout sourceLayout;
    @BindView(R.id.buttonLayout)
    LinearLayout buttonLayout;
    @BindView(R.id.btnEdit)
    Button btnEdit;
    @BindView(R.id.btnDelete)
    Button btnDelete;
    @BindView(R.id.tvFee)
    TextView tvFee;
    @BindView(R.id.feeLayout)
    LinearLayout feeLayout;
    private Product product;

    public ProductViewDialog(Context context, Product product) {
        super(context, R.layout.dialog_product_view);
        this.product = product;
    }

    @Override
    protected void initActions() {
        ButterKnife.bind(this, mainView);
        tvName.setText(product.getName());
        tvBarcode.setText(product.getBarcode());
        tvProductCode.setText(product.getProductCode());
        tvStock.setText(product.getStock());
        tvFee.setText(product.getFee());
        tvSource.setText(product.getSource());

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.startEditDialog(context, product);
                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.startDeleteDialog(context, product);
                dialog.dismiss();
            }
        });
    }
}
