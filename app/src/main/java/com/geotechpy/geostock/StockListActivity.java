package com.geotechpy.geostock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.geotechpy.geostock.adapters.StockAdapter;
import com.geotechpy.geostock.database.StockManager;
import com.geotechpy.geostock.fragments.ConfirmDialog;
import com.geotechpy.geostock.models.Stock;

import java.util.ArrayList;

public class StockListActivity extends AppCompatActivity {

    TextView tvUserName;
    TextView tvZone;
    private ArrayList<Stock> al_stocks = new ArrayList<>();

    static class ViewHolder {
        // A ViewHolder keeps references to children views to avoid unneccessary calls
        // to findViewById() on each row.
        TextView tvStockSerNr;
        TextView tvZoneCode;
        TextView tvStatusCode;
        ImageButton ibSync;
        ImageButton ibEdit;
        ImageButton ibDelete;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);
    }

    @Override
    public void onResume(){
        super.onResume();
        tvUserName = (TextView) findViewById(R.id.tv_stocklist_username);
        tvZone = (TextView) findViewById(R.id.tv_stocklist_zone);
        Intent intent = getIntent();
        tvUserName.setText(intent.getStringExtra("username"));
        showStocks();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (100) : {
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    tvZone.setText(data.getStringExtra("zone"));
                }
                break;
            }
            default:
                break;
        }
    }

    public void showStocks(){
        StockManager stockManager = new StockManager(this);
        al_stocks = stockManager.getStocks();

        StockAdapter stockAdapter = new StockAdapter(this);
        stockAdapter.updateStocks(al_stocks);
        ListView lvStocks = (ListView) findViewById(R.id.lv_stocks);
        lvStocks.setAdapter(stockAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stock_list, menu);
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

    public void onClickNew(View view){
        tvUserName = (TextView) findViewById(R.id.tv_stocklist_username);
        Intent stockZoneList = new Intent(this, StockZoneListActivity.class);
        stockZoneList.putExtra("username", tvUserName.getText());
        startActivityForResult(stockZoneList, 100);
    }
}
