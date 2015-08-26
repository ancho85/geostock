package com.geotechpy.geostock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


import com.geotechpy.geostock.adapters.ItemAdapter;
import com.geotechpy.geostock.app.GeotechpyStockApp;
import com.geotechpy.geostock.database.StockDetailManager;
import com.geotechpy.geostock.database.ZoneManager;
import com.geotechpy.geostock.models.StockDetail;
import com.geotechpy.geostock.models.Zone;

import java.util.ArrayList;


public class ItemListActivity extends AppCompatActivity {

    TextView tvUserName;
    TextView tvStockSerNr;
    TextView tvZoneCode;
    TextView tvZoneName;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userName = GeotechpyStockApp.getUserName();
        if (userName == null){
            ((GeotechpyStockApp)getApplication()).respawnLogin(this);
        }
        setContentView(R.layout.activity_item_list);
    }

    @Override
    public void onResume(){
        super.onResume();
        tvUserName = (TextView) findViewById(R.id.tv_itemlist_username);
        tvStockSerNr = (TextView) findViewById(R.id.tv_itemlist_stock_sernr);
        tvZoneCode = (TextView) findViewById(R.id.tv_itemlist_stock_zone_code);
        tvZoneName = (TextView) findViewById(R.id.tv_itemlist_zone_name);
        Intent intent = getIntent();
        userName = GeotechpyStockApp.getUserName();
        tvUserName.setText(userName);
        tvStockSerNr.setText(intent.getStringExtra("stockSerNr"));
        tvZoneCode.setText(intent.getStringExtra("zoneCode"));
        Zone zone = ZoneManager.load(getApplicationContext(), Integer.parseInt(tvZoneCode.getText().toString()));
        tvZoneName.setText(zone.getName());
        showItems();
    }

    public void showItems(){
        String stockSerNr = tvStockSerNr.getText().toString();
        String zoneCode = tvZoneCode.getText().toString();

        StockDetailManager stockDetailManager = new StockDetailManager(this);
        ArrayList<StockDetail> al_stockDetail = stockDetailManager.getStockDetails(Integer.valueOf(stockSerNr));
        ItemAdapter itemAdapter = new ItemAdapter(this);
        itemAdapter.updateItems(al_stockDetail);
        itemAdapter.setUserStockZone(userName, stockSerNr, zoneCode);
        ListView lvItems = (ListView) findViewById(R.id.lv_itemlist_items);
        lvItems.setAdapter(itemAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item_list, menu);
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

    public void onClickNew(View view){
        tvStockSerNr = (TextView) findViewById(R.id.tv_itemlist_stock_sernr);
        tvZoneCode = (TextView) findViewById(R.id.tv_itemlist_stock_zone_code);
        Intent itemActivity = new Intent(this, ItemActivity.class);
        itemActivity.putExtra("stockSerNr", tvStockSerNr.getText().toString());
        itemActivity.putExtra("zoneCode", tvZoneCode.getText().toString());
        itemActivity.putExtra("editMode", false);
        startActivity(itemActivity);

    }
}
