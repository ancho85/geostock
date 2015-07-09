package com.geotechpy.geostock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.geotechpy.geostock.adapters.ZoneAdapter;
import com.geotechpy.geostock.database.ZoneManager;
import com.geotechpy.geostock.models.Zone;

import java.util.ArrayList;

public class StockZoneListActivity extends AppCompatActivity {

    TextView tvUserName;

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
        ArrayList<Zone> al_zones = zoneManager.getZones();
        ZoneAdapter zoneAdapter = new ZoneAdapter(this);
        zoneAdapter.updateZones(al_zones);
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
        if (zone != null){
            Intent resultIntent = new Intent();
            resultIntent.putExtra("zone", zone.getSernr().toString());
            setResult(AppCompatActivity.RESULT_OK, resultIntent);
            finish();
        }else{
            Toast.makeText(getApplicationContext(), R.string.must_select_zone, Toast.LENGTH_SHORT).show();
        }
    }
}
