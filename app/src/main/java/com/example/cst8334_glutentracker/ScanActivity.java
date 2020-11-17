package com.example.cst8334_glutentracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.example.entity.Product;
import com.google.zxing.Result;

import static java.lang.Long.parseLong;

public class ScanActivity extends AppCompatActivity {
    String apiReturnMessage;
    int CAMERA_PERMISSION_CODE;
    Button acceptScannerButton;
    Button cancelScannerButton;
    EditText upcBarcode;
    GlutenDatabase dbOpener;
    SQLiteDatabase db;
    CodeScanner codeScanner;
    CodeScannerView scannerView;
    Toolbar scannerTbar;

    // Adding six second delay between scans, https://github.com/journeyapps/zxing-android-embedded/issues/59
    static final int DELAY = 6000;
    long delayTimeStamp = 0;

    String test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        scannerTbar = (Toolbar)findViewById(R.id.scannerToolbar);

        upcBarcode = (EditText) findViewById(R.id.barcodeEditText);
        acceptScannerButton = (Button) findViewById(R.id.acceptScannerButton);
        cancelScannerButton = (Button) findViewById(R.id.cancelScannerButton);
        dbOpener = new GlutenDatabase(this);
        db = dbOpener.getWritableDatabase();

        if (ContextCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ScanActivity.this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
        scannerView = (CodeScannerView) findViewById(R.id.barcodeScanner);
        codeScanner = new CodeScanner(this, scannerView);
        codeScanner.getCamera();
        codeScanner.setScanMode(ScanMode.CONTINUOUS);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                ScanActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (System.currentTimeMillis() - delayTimeStamp < DELAY){
                            return;
                        } else {
                            ScanActivity.this.runQuery(parseLong(result.getText()));
                            delayTimeStamp = System.currentTimeMillis();
                        }
                    }
                });
            }
        });

        if (acceptScannerButton != null) {
            acceptScannerButton.setOnClickListener(acceptClick -> {
                long test2 = getUPCEditText();
                if (upcBarcode.toString().trim().length() > 0) {
                    this.runQuery(getUPCEditText());
                }
            });
        }

        if (cancelScannerButton != null) {
            cancelScannerButton.setOnClickListener(cancelClick -> {
                finish();
            });
        }

        if (codeScanner.isFlashEnabled()) {
            codeScanner.setFlashEnabled(true);
        } else {
            codeScanner.setFlashEnabled(false);
        }
    }

    // Run the API query to Edamam
    private void runQuery(long upc) {
        boolean boolCartItem = false;
        dbOpener = new GlutenDbHelper(this);
        db = dbOpener.getReadableDatabase();
        Product barcodeCheck = dbOpener.selectProductByID(db, upc);

        // if iterator found in array, Toast.maketext (Scanactivity.this, "message", Toast.LENGTH_LONG).show();
        if (CartActivity.getProductsArrayList().size() != 0){
            for (Product prod : CartActivity.getProductsArrayList()) {
                if (prod.getId() == upc) {
                    boolCartItem = true;
                    Toast.makeText(ScanActivity.this, "Item already exists in the cart", Toast.LENGTH_LONG).show();
                }
            }
            // if select id from products; == 1, add item to cart
        }

        if (barcodeCheck != null && boolCartItem == false) {
            CartActivity.getProductsArrayList().add(barcodeCheck);
            Toast.makeText(this, barcodeCheck.getProductName() + " added to the cart from database", Toast.LENGTH_LONG).show();
        } else if (barcodeCheck == null && boolCartItem == false) {
            new EdamamQuery(ScanActivity.this, upc).execute();
        }
    }

    // Get Edit text field
    private long getUPCEditText() {
        return Long.valueOf(upcBarcode.getText().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scannerButton:
                Intent goToScanner = new Intent(ScanActivity.this, ScanActivity.class);
                startActivity(goToScanner);
                break;
            case R.id.cartButton:
                Intent goToCart = new Intent(ScanActivity.this, CartActivity.class);
                startActivity(goToCart);
                break;
            case R.id.receiptButton:
                Intent goToReceipt = new Intent(ScanActivity.this, ReceiptActivity.class);
                startActivity(goToReceipt);
                break;
            case R.id.reportButton:
                Intent goToReport = new Intent(ScanActivity.this, ReportActivity.class);
                startActivity(goToReport);
                break;
        }
        return true;
    }
}