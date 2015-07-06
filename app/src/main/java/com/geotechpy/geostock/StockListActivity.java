package com.geotechpy.geostock;

import android.content.Intent;
import android.os.Bundle;
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

import com.geotechpy.geostock.database.StockManager;
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
        tvZone.setText(intent.getStringExtra("zone"));
        showStocks();
    }

    public void showStocks(){
        StockManager stockManager = new StockManager(this);
        al_stocks = stockManager.getStocks();

        class StockAdapter extends ArrayAdapter {
            AppCompatActivity context;

            StockAdapter(AppCompatActivity context) {
                super(context, R.layout.listitem_stock, al_stocks);
                this.context = context;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                final ViewHolder holder;
                // When convertView is not null, we can reuse it directly, there is no need
                // to re inflate it. We only inflate a new View when the convertView supplied
                // by ListView is null.

                if (convertView == null) {
                    LayoutInflater inflater = context.getLayoutInflater();
                    convertView = inflater.inflate(R.layout.listitem_stock, parent, false);
                    holder = new ViewHolder();
                    holder.tvStockSerNr = (TextView) convertView.findViewById(R.id.tv_stock_sernr);
                    holder.tvZoneCode = (TextView) convertView.findViewById(R.id.tv_zone_code);
                    holder.tvStatusCode = (TextView) convertView.findViewById(R.id.tv_status_code);
                    holder.ibSync = (ImageButton) convertView.findViewById(R.id.ib_sync);
                    holder.ibEdit = (ImageButton) convertView.findViewById(R.id.ib_edit);
                    holder.ibDelete = (ImageButton) convertView.findViewById(R.id.ib_delete);
                    convertView.setTag(holder);
                }
                else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.tvStockSerNr.setText(al_stocks.get(position).getSernr().toString());
                holder.tvZoneCode.setText(al_stocks.get(position).getZone_sernr().toString());
                holder.tvStatusCode.setText(al_stocks.get(position).getStatus());

                holder.ibEdit.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        tvUserName = (TextView) findViewById(R.id.tv_stocklist_username);
                        Intent itemList = new Intent(context, ItemListActivity.class);
                        itemList.putExtra("username", tvUserName.getText());
                        itemList.putExtra("stockSerNr", holder.tvStockSerNr.getText());
                        itemList.putExtra("zoneCode", holder.tvZoneCode.getText());
                        startActivity(itemList);
                    }
                });

                return (convertView);
            }
        }

        StockAdapter stockAdapter = new StockAdapter(this);
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
        startActivity(stockZoneList);
    }
}
