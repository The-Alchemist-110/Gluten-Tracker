package com.example.cst8334_glutentracker.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cst8334_glutentracker.R;
import com.example.cst8334_glutentracker.database.GlutenDatabase;
import com.example.cst8334_glutentracker.entity.Receipt;

import java.util.ArrayList;
import java.util.List;

public class ReceiptActivity extends AppCompatActivity {
    ArrayList<Receipt> receipt;
    ListView receiptList;
    private static ReceiptAdapter adapter;
    private SQLiteDatabase database;
    private GlutenDatabase dbOpener = new GlutenDatabase(this);
    Toolbar receiptTbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        receiptList=(ListView)findViewById(R.id.receiptList);
        receipt= new ArrayList<>();

        receiptTbar = (Toolbar)findViewById(R.id.receiptToolbar);

//      insertTestValuesIntoDatabase();
        readFromDatabase();
        adapter=new ReceiptAdapter(receipt,this);
        receiptList.setAdapter(adapter);
        receiptList.setOnItemLongClickListener((adapterView, view, i, l) -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(adapterView.getContext());
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which){

                    case DialogInterface.BUTTON_POSITIVE:
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        Long d=receipt.get(i).getId();
                        dbOpener.deleteReceiptByID(d);
                        receipt.remove(i);
                        adapter.notifyDataSetChanged();

                        break;
                }
            };
            alertDialog.setTitle("Delete Receipt");
            alertDialog.setMessage("Do you want to delete this receipt");
            alertDialog.setNegativeButton("Delete", dialogClickListener);
            alertDialog.setPositiveButton("No", dialogClickListener);
            alertDialog.show();
            return true;
        });

        receiptList.setOnItemClickListener((list,item,position,id)->{
//            position=position+1;
            Intent intent= new Intent(ReceiptActivity.this,DigitalReceipt.class);
//            intent.putExtra("index",position);
//            intent.putExtra("index",receipt.get(position).getId());
                DigitalReceipt.setPassedIndex(receipt.get(position).getId());
            startActivity(intent);
            });
        adapter.notifyDataSetChanged();
    }


    private void readFromDatabase(){
        List<Receipt> rec= dbOpener.selectAllReceipt();
        if(rec!=null)
            receipt.addAll(rec);

       /* database = dbOpener.getReadableDatabase();
        Cursor pc = database.query(false, DatabaseActivity.Products.TABLE_NAME, new String[]{DatabaseActivity.Products.COLUMN_NAME_ID, DatabaseActivity.Products.COLUMN_NAME_PRODUCT_NAME, DatabaseActivity.Products.COLUMN_NAME_DESCRIPTION, DatabaseActivity.Products.COLUMN_NAME_GLUTEN, DatabaseActivity.Products.COLUMN_NAME_PRICE}, null, null, null, null, null, null, null);
       // Cursor rc = database.query(false, DatabaseActivity.Receipts.TABLE_NAME, new String[]{DatabaseActivity.Receipts.COLUMN_NAME_ID, DatabaseActivity.Receipts.COLUMN_NAME_FILE, DatabaseActivity.Receipts.COLUMN_NAME_DATE, DatabaseActivity.Receipts.COLUMN_NAME_TOTAL_PRICE}, null, null, null, null, null, null, null);
        Cursor rc = database.query(false, DatabaseActivity.Receipts.TABLE_NAME, new String[]{DatabaseActivity.Receipts.COLUMN_NAME_ID, DatabaseActivity.Receipts.COLUMN_NAME_FILE, DatabaseActivity.Receipts.COLUMN_NAME_DATE, DatabaseActivity.Receipts.COLUMN_NAME_TOTAL_DEDUCTION}, null, null, null, null, null, null, null);
        int idIndex=rc.getColumnIndex(DatabaseActivity.Receipts.COLUMN_NAME_ID);
        int fileNamename=rc.getColumnIndex(DatabaseActivity.Receipts.COLUMN_NAME_FILE);
        int dateIndex= rc.getColumnIndex(DatabaseActivity.Receipts.COLUMN_NAME_DATE);
        int deductionIndex= rc.getColumnIndex(DatabaseActivity.Receipts.COLUMN_NAME_TOTAL_DEDUCTION);

        while(rc.moveToNext()){
        int rid=rc.getInt(idIndex);
        String fileName= rc.getString(fileNamename);
        String date=rc.getString(dateIndex);
        //Double deduction=rc.getDouble(deductionIndex);
        double deduction = rc.getDouble(deductionIndex);


        //Fix receipt.add
        receipt.add(new Receipt(rid,null,fileName,deduction, 0, date));
        } */
    }

//    private void insertTestValuesIntoDatabase(){
//        database = dbOpener.getWritableDatabase();
//        ContentValues newRowValues = new ContentValues();
//        newRowValues.put(databaseActivity.Receipts.COLUMN_NAME_FILE, "test2");
//        newRowValues.put(databaseActivity.Receipts.COLUMN_NAME_DATE,"2020-08-15");
//        newRowValues.put(databaseActivity.Receipts.COLUMN_NAME_DEDUCTION,20);
//        database.insert(databaseActivity.Receipts.TABLE_NAME, null, newRowValues);
//
//        ContentValues productRowValues = new ContentValues();
//        productRowValues.put(databaseActivity.Products.COLUMN_NAME_PNAME, "Real Apple Juice");
//        productRowValues.put(databaseActivity.Products.COLUMN_NAME_DESCRIPTION, "Apple Juice 1L");
//        productRowValues.put(databaseActivity.Products.COLUMN_NAME_PRICE, 6.25);
//        productRowValues.put(databaseActivity.Products.COLUMN_NAME_GLUTEN, 0);
//        database.insert(databaseActivity.Products.TABLE_NAME, null, productRowValues);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        menu.findItem(R.id.receiptButton).setVisible(false);
        menu.findItem(R.id.search_view).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scannerButton:
                setResult(MainMenuActivity.RESULT_CODE_NAVIGATE_TO_SCANNER);
                finish();
                break;
            case R.id.cartButton:
                setResult(MainMenuActivity.RESULT_CODE_NAVIGATE_TO_CART);
                finish();
                break;
            case R.id.receiptButton:
                setResult(MainMenuActivity.RESULT_CODE_NAVIGATE_TO_RECEIPT);
                finish();
                break;
            case R.id.reportButton:
                setResult(MainMenuActivity.RESULT_CODE_NAVIGATE_TO_REPORT);
                finish();
                break;
        }
        return true;
    }

    public class ReceiptAdapter extends ArrayAdapter<Receipt> {
        private ArrayList<Receipt> rData;

        Context mContext;
        TextView id;
        TextView img;
        TextView amt;
        TextView dte;
        Button edit;

        public ReceiptAdapter(ArrayList<Receipt> data, Context context)  {
            super(context,R.layout.receipt_layout,data);
            this.rData=data;
            this.mContext=context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Receipt rS = getItem(position);
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(R.layout.receipt_layout, parent, false);
                id= (TextView) convertView.findViewById(R.id.rid);
                img = (TextView) convertView.findViewById(R.id.rpt);
                amt = (TextView) convertView.findViewById(R.id.deduction);
                dte = (TextView) convertView.findViewById(R.id.summarydate);
                edit=convertView.findViewById(R.id.edit);
                id.setText(Long.toString(rS.getId()));
                img.setText(rS.getReceiptFile());
                amt.setText(Double.toString(rS.getTaxDeductionTotal()));
                dte.setText(rS.getDate());
            return convertView;
        }
    }
}