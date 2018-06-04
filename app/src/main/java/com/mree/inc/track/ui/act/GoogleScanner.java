package com.mree.inc.track.ui.act;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.mree.inc.track.R;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;

public class GoogleScanner extends AppCompatActivity implements BarcodeReader
        .BarcodeReaderListener {

    private BarcodeReader barcodeReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_scanner);
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id
                .barcode_fragment);
    }


    @Override
    public void onScanned(Barcode barcode) {
        // play beep sound
        barcodeReader.playBeep();
        sendResult(barcode.displayValue);
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
    public void onScannedMultiple(List<Barcode> list) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String s) {

    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(getApplicationContext(), "Camera permission denied!", Toast.LENGTH_LONG)
                .show();
    }
}
