package com.mree.inc.track.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.mree.inc.track.R;
import com.mree.inc.track.TrackApp;
import com.mree.inc.track.db.persist.Product;

public class ProductDeleteDialog {


    protected AlertDialog.Builder builder;
    protected AlertDialog dialog;
    private Context context;
    private Product product;

    public ProductDeleteDialog(Context context, Product product) {
        this.context = context;
        this.product = product;
        init();
    }

    private void init() {
        builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.msg_delete);
        builder.setMessage(getMessage());
        builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TrackApp.getDatabase().productDao().delete(product.id);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
    }

    private String getMessage() {
        String msg = product.getName();
        msg += " isimli ürünü silmek istediğinize emin misiniz?";
        return msg;
    }

    public void show() {
        dialog.show();
    }

    public AlertDialog getDialog() {
        return dialog;
    }
}
