package com.mree.inc.track.ui.act;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.mree.inc.track.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class QRScannerActivity extends AppCompatActivity implements DecoratedBarcodeView
        .TorchListener, BarcodeCallback {

    @BindView(R.id.zxing_barcode_scanner)
    DecoratedBarcodeView barcodeScannerView;


    private CaptureManager captureManager;
    private BeepManager beepManager;
    private Integer direction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);
        ButterKnife.bind(this);

        barcodeScannerView.decodeContinuous(this);
        barcodeScannerView.setTorchListener(this);
        beepManager = new BeepManager(this);
        beepManager.setBeepEnabled(false);
        beepManager.setVibrateEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeScannerView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeScannerView.pause();
    }

    public void switchFlashlight(View view) {
        if (true) {
            barcodeScannerView.setTorchOn();
        } else {
            barcodeScannerView.setTorchOff();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    /**
     * Check if the device's camera has a Flashlight.
     *
     * @return true if there is Flashlight, otherwise false.
     */
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    @Override
    public void barcodeResult(BarcodeResult result) {
        if (result.getText() == null) {
            return;
        }
        barcodeScannerView.pauseAndWait();
        beepManager.playBeepSoundAndVibrate();
        Log.i(getClass().getSimpleName(), "QR result is " + result.getText());
        sendResult(result.getText());
    }

    private void sendResult(String text) {
        Bundle data = new Bundle();
        data.putString(MainActivity.BARCODE_PARAM, text);
        Message msg = new Message();
        msg.setData(data);
        if (MainActivity.barcodeHandler != null)
            MainActivity.barcodeHandler.sendMessage(msg);
        finish();
    }

    @Override
    public void possibleResultPoints(List<ResultPoint> resultPoints) {

    }

    @Override
    public void onTorchOn() {

    }

    @Override
    public void onTorchOff() {

    }
}
