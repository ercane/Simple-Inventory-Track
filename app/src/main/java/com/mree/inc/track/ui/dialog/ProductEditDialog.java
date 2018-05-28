package com.mree.inc.track.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mree.inc.track.R;
import com.mree.inc.track.TrackApp;
import com.mree.inc.track.db.persist.Product;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductEditDialog extends BaseDialog {


    @BindView(R.id.tvName)
    EditText tvName;
    @BindView(R.id.nameLayout)
    LinearLayout nameLayout;
    @BindView(R.id.tvBarcode)
    EditText tvBarcode;
    @BindView(R.id.barcodeLayout)
    LinearLayout barcodeLayout;
    @BindView(R.id.tvProductCode)
    EditText tvProductCode;
    @BindView(R.id.productCodeLayout)
    LinearLayout productCodeLayout;
    @BindView(R.id.tvStock)
    EditText tvStock;
    @BindView(R.id.stockLayout)
    LinearLayout stockLayout;
    @BindView(R.id.tvSource)
    EditText tvSource;
    @BindView(R.id.sourceLayout)
    LinearLayout sourceLayout;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.buttonLayout)
    LinearLayout buttonLayout;
    private Product product;

    public ProductEditDialog(Context context, Product product) {
        super(context, R.layout.dialog_product_edit);
        this.product = product;
    }

    @Override
    protected void initActions() {
        ButterKnife.bind(this, mainView);

        if (product != null) {
            tvName.setText(product.getName());
            tvName.setSelection(product.getName().length());
            tvBarcode.setText(product.getBarcode());
            tvBarcode.setSelection(product.getBarcode().length());
            tvProductCode.setText(product.getProductCode());
            tvProductCode.setSelection(product.getProductCode().length());
            tvStock.setText(product.getStock());
            tvStock.setSelection(product.getStock().length());
            tvSource.setText(product.getSource());
            tvSource.setSelection(product.getSource().length());
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSave();
            }
        });


    }

    private void attemptSave() {
        String name = tvName.getText().toString();
        String barcode = tvBarcode.getText().toString();
        String productCode = tvProductCode.getText().toString();
        String stock = tvStock.getText().toString();
        String source = tvSource.getText().toString();

        tvName.setError(null);
        tvBarcode.setError(null);
        tvProductCode.setError(null);
        tvStock.setError(null);
        tvSource.setError(null);


        if (TextUtils.isEmpty(name)) {
            tvName.setError(context.getString(R.string.error_required_field));
        } else if (TextUtils.isEmpty(barcode)) {
            tvBarcode.setError(context.getString(R.string.error_required_field));
        } else if (TextUtils.isEmpty(productCode)) {
            tvProductCode.setError(context.getString(R.string.error_required_field));
        } else if (TextUtils.isEmpty(stock)) {
            tvStock.setError(context.getString(R.string.error_required_field));
        } else if (TextUtils.isEmpty(source)) {
            tvSource.setError(context.getString(R.string.error_required_field));
        } else {

            if (product == null) {
                product = new Product();
            }

            product.setName(name);
            product.setBarcode(barcode);
            product.setProductCode(productCode);
            product.setStock(stock);
            product.setSource(source);
            TrackApp.getDatabase().productDao().addProduct(product);
            dialog.dismiss();
        }
    }
}
