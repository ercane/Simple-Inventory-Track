package com.mree.inc.track.ui.act;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.mree.inc.track.R;
import com.mree.inc.track.TrackApp;
import com.mree.inc.track.db.AppDatabase;
import com.mree.inc.track.db.persist.Product;
import com.mree.inc.track.ui.adapter.ProductAdapter;
import com.mree.inc.track.ui.dialog.ProductOptionsDialog;
import com.mree.inc.track.util.IconUtils;
import com.mree.inc.track.util.ReadUtils;
import com.mree.inc.track.util.Utils;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.obsez.android.lib.filechooser.ChooserDialog;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialMenuInflater;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String BARCODE_PARAM = "barcode_param";
    public static final String CSV_REGEX = ".*\\.(csv)";
    public static final String EXCEL_REGEX = ".*\\.(xls|xlsx)";
    private static final int CAMERA_PERMISSION_REQUEST = 2525;
    public static Handler barcodeHandler;
    @BindView(R.id.headerLayout)
    LinearLayout headerLayout;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.boomMenu)
    BoomMenuButton boomMenu;
    @BindView(R.id.etSearch)
    EditText etSearch;

    private ProductAdapter adapter;
    private List<Product> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        prepareBoomMenu();
        listview.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listview.setMultiChoiceModeListener(getMultiChoiceListener());
        list = AppDatabase.getDatabase(this).productDao().getAllProduct();

        if (list.isEmpty()) {
            showTapTarget();
        }

        barcodeHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle data = msg.getData();
                String barcode = data.getString(BARCODE_PARAM);
                Toast.makeText(getApplicationContext(), barcode, Toast.LENGTH_LONG).show();
                if (etSearch != null) {
                    etSearch.setText(barcode);
                    etSearch.setSelection(barcode.length());
                }
            }
        };

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(etSearch.getText()) && adapter != null)
                    adapter.getFilter().filter(etSearch.getText());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        adapter = new ProductAdapter(this, list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Product product = adapter.getItem(i);
                ProductOptionsDialog dialog = new ProductOptionsDialog(MainActivity.this, product);
                dialog.show();
                dialog.setDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        adapter.clear();
                        list = TrackApp.getDatabase().productDao().getAllProduct();
                        adapter.addAll(list);
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MaterialMenuInflater.with(this).inflate(R.menu.search_menu, menu);
        Drawable icon = IconUtils.getActionBarIcon(MaterialDrawableBuilder.IconValue
                .BARCODE_SCAN, Color.WHITE);
        menu.getItem(0).setIcon(icon);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_search:
                if (Utils.checkPermission(Manifest.permission.CAMERA)) {
                    Intent i = new Intent(this, QRScannerActivity.class);
                    startActivity(i);
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            CAMERA_PERMISSION_REQUEST);
                }
                break;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (Utils.checkPermission(Manifest.permission.CAMERA)) {
                Intent i = new Intent(this, QRScannerActivity.class);
                startActivity(i);
            }
        }

    }

    private AbsListView.MultiChoiceModeListener getMultiChoiceListener() {
        return new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean
                    checked) {
                final int checkedCount = listview.getCheckedItemCount();
                mode.setTitle(checkedCount + " Seçili");
                adapter.toggleSelection(position);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MaterialMenuInflater.with(MainActivity.this).inflate(R.menu.context_product, menu);
                Drawable selectIcon = IconUtils.getActionBarIcon(MaterialDrawableBuilder
                        .IconValue.SELECT_ALL, Color.WHITE);
                Drawable deleteIcon = IconUtils.getActionBarIcon(MaterialDrawableBuilder
                        .IconValue.DELETE, Color.WHITE);
                menu.getItem(0).setIcon(selectIcon);
                menu.getItem(1).setIcon(deleteIcon);

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return true;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
                SparseBooleanArray selected;
                switch (item.getItemId()) {
                    case R.id.action_select_all:
                        adapter.removeSelection();
                        listview.clearChoices();
                        for (int i = 0; i < adapter.getList().size(); i++) {
                            listview.setItemChecked(i, true);
                        }
                        break;
                    case R.id.action_delete:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Ürünleri Sil");
                        builder.setMessage("Seçili ürünleri silmek istediğinize emin misiniz?");
                        builder.setCancelable(true);

                        builder.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        SparseBooleanArray selected = adapter
                                                .getSelectedIds();
                                        // Captures all selected ids with a loop
                                        for (int i = (selected.size() - 1); i >= 0; i--) {
                                            if (selected.valueAt(i)) {
                                                Product acc = adapter.getItem
                                                        (selected.keyAt(i));
                                                if (acc != null) {
                                                    TrackApp.getDatabase().productDao().delete
                                                            (acc.id);
                                                    adapter.remove(acc);
                                                }
                                            }
                                        }
                                        mode.finish();
                                    }
                                });

                        builder.setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        mode.finish();
                                    }
                                });

                        AlertDialog alert11 = builder.create();
                        alert11.show();
                        break;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adapter.removeSelection();
            }
        };
    }

    private void showTapTarget() {
        TapTargetView.showFor(this,                 // `this` is an Activity
                TapTarget
                        .forView(boomMenu,
                                getString(R.string.msg_empty_product),
                                getString(R.string.msg_empty_product_msg))
                        .outerCircleColor(R.color.colorPrimary)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColorInt(Color.TRANSPARENT)
                        .titleTextSize(30)
                        .titleTextColorInt(Color.WHITE)
                        .descriptionTextSize(16)
                        .descriptionTextColorInt(Color.WHITE)
                        .textColorInt(Color.WHITE)
                        .dimColorInt(Color.WHITE)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(true)
                        .targetRadius(60),
                new TapTargetView.Listener() {
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional
                        boomMenu.boom();
                    }
                });
    }

    private void prepareBoomMenu() {
        int redColor = Utils.getColor(R.color.colorPrimary);
        Drawable plus = IconUtils.getIcon(MaterialDrawableBuilder.IconValue.PLUS, redColor);
        Drawable send = IconUtils.getIcon(MaterialDrawableBuilder.IconValue.FILE, redColor);
        Drawable delete = IconUtils.getIcon(MaterialDrawableBuilder.IconValue.FILE_EXCEL, redColor);
        for (int i = 0; i < boomMenu.getPiecePlaceEnum().pieceNumber(); i++) {
            HamButton.Builder builder = null;
            if (i == 0) {
                builder = new HamButton.Builder()
                        .normalImageDrawable(plus)
                        .normalTextRes(R.string.msg_title_add_product)
                        .subNormalTextRes(R.string.msg_add_product)
                        .normalTextColor(redColor)
                        .subNormalTextColor(Color.GRAY)
                        .listener(new OnBMClickListener() {
                            @Override
                            public void onBoomButtonClick(int index) {
                                Utils.startEditDialog(MainActivity.this, null);
                            }
                        })
                        .rippleEffect(true)
                        .normalColor(Color.WHITE);
            } else if (i == 1) {
                builder = new HamButton.Builder()
                        .normalImageDrawable(send)
                        .normalTextRes(R.string.msg_title_add_csv)
                        .subNormalTextRes(R.string.msg_add_csv)
                        .normalTextColor(redColor)
                        .subNormalTextColor(Color.GRAY)
                        .listener(new OnBMClickListener() {
                            @Override
                            public void onBoomButtonClick(int index) {
                                chooseCsvFile();
                            }
                        })
                        .rippleEffect(true)
                        .normalColor(Color.WHITE);
            } else {
                builder = new HamButton.Builder()
                        .normalImageDrawable(delete)
                        .normalTextRes(R.string.msg_title_add_excel)
                        .subNormalTextRes(R.string.msg_add_excel)
                        .normalTextColor(redColor)
                        .subNormalTextColor(Color.GRAY)
                        .listener(new OnBMClickListener() {
                            @Override
                            public void onBoomButtonClick(int index) {
                                chooseExcelFile();
                            }
                        })
                        .rippleEffect(true)
                        .normalColor(Color.WHITE);
            }
            boomMenu.addBuilder(builder);
            boomMenu.setAutoHide(true);
        }
    }

    private void chooseCsvFile() {
        new ChooserDialog().with(this)
                .withFilterRegex(false, true, CSV_REGEX)
                .withStartFile("/")
                .withResources(R.string.title_choose_file,
                        R.string.title_choose, R.string
                                .dialog_cancel)
                .withChosenListener(new ChooserDialog.Result() {
                    @Override
                    public void onChoosePath(String path, File pathFile) {
                        List<Product> products = ReadUtils.readCsvFile(path);
                        if (adapter != null) {
                            adapter.addAll(products);
                        }

                        for (Product p : products)
                            TrackApp.getDatabase().productDao().addProduct(p);

                    }
                })
                .build()
                .show();
    }

    private void chooseExcelFile() {
        new ChooserDialog().with(this)
                .withFilterRegex(false, true, EXCEL_REGEX)
                .withStartFile("/")
                .withResources(R.string.title_choose_file,
                        R.string.title_choose, R.string
                                .dialog_cancel)
                .withChosenListener(new ChooserDialog.Result() {
                    @Override
                    public void onChoosePath(String path, File pathFile) {
                        List<Product> products = ReadUtils.readExcelFile(path);
                        if (adapter != null) {
                            adapter.addAll(products);
                        }
                        for (Product p : products)
                            TrackApp.getDatabase().productDao().addProduct(p);
                    }
                })
                .build()
                .show();
    }
}
