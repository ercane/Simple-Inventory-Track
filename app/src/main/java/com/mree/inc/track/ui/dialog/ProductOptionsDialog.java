package com.mree.inc.track.ui.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.LinearLayout;

import com.mree.inc.track.R;
import com.mree.inc.track.db.persist.Product;
import com.mree.inc.track.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductOptionsDialog extends BaseDialog {

    @BindView(R.id.btnView)
    LinearLayout btnView;
    @BindView(R.id.btnEdit)
    LinearLayout btnEdit;
    @BindView(R.id.btnDelete)
    LinearLayout btnDelete;
    private Product product;

    public ProductOptionsDialog(Context context, Product product) {
        super(context, R.layout.dialog_product_options);
        this.product = product;
    }

    @Override
    protected void initActions() {
        ButterKnife.bind(this, mainView);

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewProduct();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProduct();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProduct();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color
                .TRANSPARENT));
    }

    private void viewProduct() {
        ProductViewDialog dialog = new ProductViewDialog(context, product);
        dialog.setDismissListener(onDismissListener);
        dialog.show();
    }

    private void editProduct() {
        ProductEditDialog dialog = new ProductEditDialog(context, product);
        dialog.setDismissListener(onDismissListener);
        dialog.show();
    }

    private void deleteProduct() {
        Utils.startDeleteDialog(context, product);
    }


}
