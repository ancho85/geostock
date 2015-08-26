package com.geotechpy.geostock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.geotechpy.geostock.app.GeotechpyStockApp;
import com.geotechpy.geostock.database.ItemManager;
import com.geotechpy.geostock.database.StockDetailManager;
import com.geotechpy.geostock.database.StockManager;
import com.geotechpy.geostock.models.Item;
import com.geotechpy.geostock.models.Stock;

import static com.geotechpy.geostock.app.GeotechpyStockApp.displayMessage;


public class ItemActivity extends AppCompatActivity {

    TextView tvUserName;
    TextView tvStockSerNr;
    TextView tvZoneCode;
    Boolean editMode;
    EditText etCode;
    EditText etName;
    EditText etBarCode;
    EditText etQty;
    Integer lineNr;
    String userName;
    Stock stock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
    }

    @Override
    public void onResume(){
        super.onResume();
        userName = GeotechpyStockApp.getUserName();
        if (userName == null){
            ((GeotechpyStockApp)getApplication()).respawnLogin(this);
        }
        tvUserName = (TextView) findViewById(R.id.tv_item_username);
        tvStockSerNr = (TextView) findViewById(R.id.tv_item_stock_sernr);
        tvZoneCode = (TextView) findViewById(R.id.tv_item_stock_zone_code);
        etCode = (EditText) findViewById(R.id.et_item_code);
        etName = (EditText) findViewById(R.id.et_item_name);
        etBarCode = (EditText) findViewById(R.id.et_item_barcode);
        etQty = (EditText) findViewById(R.id.et_item_qty);
        Intent intent = getIntent();
        tvUserName.setText(userName);
        tvStockSerNr.setText(intent.getStringExtra("stockSerNr"));
        tvZoneCode.setText(intent.getStringExtra("zoneCode"));
        editMode = intent.getBooleanExtra("editMode", false);
        etBarCode.setInputType(InputType.TYPE_CLASS_NUMBER);

        stock = StockManager.load(this, Integer.valueOf(tvStockSerNr.getText().toString()));
        if (stock.getType().equals(getString(R.string.zone_deposit))) {
            etCode.setInputType(InputType.TYPE_CLASS_NUMBER);
            etQty.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        if (editMode){
            etCode.setText(intent.getStringExtra("itemCode"));
            etCode.setKeyListener(null);
            etName.setText(intent.getStringExtra("itemName"));
            etBarCode.setText(intent.getStringExtra("itemBarCode"));
            etQty.setText(intent.getStringExtra("itemQty"));
            lineNr = Integer.valueOf(intent.getStringExtra("itemLineNr"));
            etQty.requestFocus();
        }

        etCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    etName.setText("");
                    etBarCode.setText("");
                    Context mContext = getApplicationContext();
                    Item it = ItemManager.load(mContext, etCode.getText().toString());
                    if (it.getCode().equals("")){
                        displayMessage(mContext.getString(R.string.invalid_item_code));
                    }
                    else if(!it.getType().equals(GeotechpyStockApp.getStockType())){ //item type not the same as chosen
                        displayMessage(mContext.getString(R.string.invalid_item_zone));
                    }
                    else{
                        etName.setText(it.getName());
                        etBarCode.setText(it.getBarcode().toString());
                    }
                }
            }
        });

        etCode.setNextFocusDownId(etQty.getId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickCancel(View view){
        finish();
    }

    public void onClickOk(View view){
        etCode = (EditText) findViewById(R.id.et_item_code);
        etName = (EditText) findViewById(R.id.et_item_name);
        etBarCode = (EditText) findViewById(R.id.et_item_barcode);
        etQty = (EditText) findViewById(R.id.et_item_qty);
        String code = etCode.getText().toString();
        String name = etName.getText().toString();
        String barCode = etBarCode.getText().toString();
        String qty = etQty.getText().toString();
        Integer stockSerNr = Integer.valueOf(tvStockSerNr.getText().toString());
        if (!editMode) {
            if (TextUtils.isEmpty(code)){
                displayMessage(getString(R.string.invalid_item_code));
                etCode.requestFocus();
                return;
            }else{
                Item item = ItemManager.load(this, code);
                if (TextUtils.isEmpty(item.getCode())){
                    displayMessage(getString(R.string.invalid_item_code));
                    etCode.requestFocus();
                    return;
                } else if(!item.getType().equals(GeotechpyStockApp.getStockType())) { //item type not the same as chosen
                    displayMessage(getString(R.string.invalid_item_zone));
                    etCode.requestFocus();
                    return;
                }
            }
            if (TextUtils.isEmpty(name)){
                displayMessage(getString(R.string.invalid_item_name));
                etName.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(barCode)){
                displayMessage(getString(R.string.invalid_item_barcode));
                etBarCode.requestFocus();
                return;
            }
        }
        if (TextUtils.isEmpty(qty)){
            displayMessage(getString(R.string.invalid_item_qty));
            etQty.requestFocus();
            return;
        }
        Long longBarCode = Long.valueOf(barCode);
        Float fQty = Float.valueOf(qty);
        StockDetailManager sdm = new StockDetailManager(this);
        ItemManager im = new ItemManager(this);
        if (!editMode){
            //im.insert(code, name, longBarCode, stock.getType());
            sdm.insert(stockSerNr, code, fQty);
            displayMessage(getString(R.string.item_created));
        }else{
            im.update(code, name, longBarCode, stock.getType());
            sdm.update(stockSerNr, lineNr, code, fQty);
            displayMessage(getString(R.string.item_updated));
        }
        //finish();
        etCode.setText("");
        etName.setText("");
        etBarCode.setText("");
        etQty.setText("");
        etCode.requestFocus();
    }

}
