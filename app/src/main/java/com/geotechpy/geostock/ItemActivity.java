package com.geotechpy.geostock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.geotechpy.geostock.database.ItemManager;
import com.geotechpy.geostock.database.StockDetailManager;


public class ItemActivity extends AppCompatActivity {

    TextView tvUserName;
    TextView tvStockSerNr;
    TextView tvZoneCode;
    Boolean editMode;
    EditText etCode;
    EditText etName;
    EditText etQty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
    }

    @Override
    public void onResume(){
        super.onResume();
        tvUserName = (TextView) findViewById(R.id.tv_item_username);
        tvStockSerNr = (TextView) findViewById(R.id.tv_item_stock_sernr);
        tvZoneCode = (TextView) findViewById(R.id.tv_item_stock_zone_code);
        Intent intent = getIntent();
        tvUserName.setText(intent.getStringExtra("username"));
        tvStockSerNr.setText(intent.getStringExtra("stockSerNr"));
        tvZoneCode.setText(intent.getStringExtra("zoneCode"));
        editMode = intent.getBooleanExtra("editMode", false);
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
        etQty = (EditText) findViewById(R.id.et_item_qty);
        String code = etCode.getText().toString();
        String name = etName.getText().toString();
        String qty = etQty.getText().toString();
        if (!editMode) {
            if (TextUtils.isEmpty(code)){
                displayMessage(getString(R.string.invalid_item_code));
                return;
            }
            if (TextUtils.isEmpty(name)){
                displayMessage(getString(R.string.invalid_item_name));
                return;
            }
        }
        if (TextUtils.isEmpty(qty)){
            displayMessage(getString(R.string.invalid_item_qty));
            return;
        }
        StockDetailManager sdm = new StockDetailManager(this);
        if (!editMode){
            ItemManager im = new ItemManager(this);
            im.insert(code, name, getString(R.string.zone_deposit));
            sdm.insert(Integer.valueOf(tvStockSerNr.getText().toString()),
                    1,
                    code,
                    Float.valueOf(qty));
            displayMessage(getString(R.string.item_created));
        }else{
            sdm.update(Integer.valueOf(tvStockSerNr.getText().toString()),
                    1,
                    code,
                    Float.valueOf(qty));
            displayMessage(getString(R.string.item_updated));
        }
        finish();
    }

    public void displayMessage(String toastString){
        Toast.makeText(getApplicationContext(), toastString, Toast.LENGTH_LONG).show();
    }
}
