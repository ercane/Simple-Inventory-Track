package com.mree.inc.track.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by MREE on 29.03.2018.
 */

public abstract class BaseDialog {

    protected Context context;
    protected @LayoutRes
    int layout;
    protected View mainView;
    protected AlertDialog.Builder builder;
    protected AlertDialog dialog;
    protected DialogInterface.OnDismissListener onDismissListener;
    private boolean isCancellable;

    public BaseDialog(Context context, @LayoutRes int layout) {
        this.context = context;
        this.layout = layout;
        isCancellable = true;
        initDialog();
    }

    public BaseDialog(Context context, @LayoutRes int layout, boolean isCancellable) {
        this.context = context;
        this.layout = layout;
        this.isCancellable = isCancellable;
        initDialog();
    }

    protected abstract void initActions();

    public void initDialog() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(layout, null);
        builder = new AlertDialog.Builder(context);
        builder.setView(mainView);
        dialog = builder.create();
        dialog.setCancelable(isCancellable);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public void show() {
        if (dialog == null)
            initDialog();

        initActions();
        dialog.show();
    }

    public void hide() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    public void setDismissListener(DialogInterface.OnDismissListener listener) {
        if (dialog != null) {
            dialog.setOnDismissListener(listener);
        }
        this.onDismissListener = listener;
    }

    public DialogInterface.OnDismissListener getOnDismissListener() {
        return onDismissListener;
    }
}
