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
import android.widget.ListView;
import android.widget.TextView;

import com.geotechpy.geostock.database.ZoneManager;
import com.geotechpy.geostock.models.Zone;

import java.util.ArrayList;

public class StockZoneListActivity extends AppCompatActivity {

    TextView tvUserName;
    private ArrayList<Zone> al_zones = new ArrayList<>();

    static class ViewHolder {
        // A ViewHolder keeps references to children views to avoid unneccessary calls
        // to findViewById() on each row.
        TextView tvZoneCode;
        TextView tvZoneName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_zone_list);
    }

    @Override
    public void onResume(){
        super.onResume();
        tvUserName = (TextView) findViewById(R.id.tv_stockzonelist_username);
        Intent intent = getIntent();
        tvUserName.setText(intent.getStringExtra("username"));
        showZones();
    }

    public void showZones(){
        ZoneManager zoneManager = new ZoneManager(this);
        al_zones = zoneManager.getZones();

        class ZoneAdapter extends ArrayAdapter {
            AppCompatActivity context;

            ZoneAdapter(AppCompatActivity context) {
                super(context, R.layout.listitem_stock, al_zones);
                this.context = context;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                ViewHolder holder;
                // When convertView is not null, we can reuse it directly, there is no need
                // to re inflate it. We only inflate a new View when the convertView supplied
                // by ListView is null.

                if (convertView == null) {
                    LayoutInflater inflater = context.getLayoutInflater();
                    convertView = inflater.inflate(R.layout.listitem_zone, parent, false);
                    holder = new ViewHolder();
                    holder.tvZoneCode = (TextView) convertView.findViewById(R.id.tv_lizone_code);
                    holder.tvZoneName = (TextView) convertView.findViewById(R.id.tv_lizone_name);
                    convertView.setTag(holder);
                }
                else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.tvZoneCode.setText(al_zones.get(position).getSernr().toString());
                holder.tvZoneName.setText(al_zones.get(position).getName());

                return (convertView);
            }
        }

        ZoneAdapter zoneAdapter = new ZoneAdapter(this);
        ListView lvZones = (ListView) findViewById(R.id.lv_zones);
        lvZones.setAdapter(zoneAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stock_zone_list, menu);
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
        ListView lvZones = (ListView) findViewById(R.id.lv_zones);
        Integer position = lvZones.getCheckedItemPosition();
        Zone zone = (Zone) lvZones.getItemAtPosition(position);
        tvUserName = (TextView) findViewById(R.id.tv_stockzonelist_username);
        Intent stockList = new Intent(this, StockListActivity.class);
        stockList.putExtra("zone", zone.getSernr().toString());
        stockList.putExtra("username", tvUserName.getText());
        startActivity(stockList);
    }
}
