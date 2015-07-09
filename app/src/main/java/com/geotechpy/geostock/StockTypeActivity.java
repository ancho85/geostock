package com.geotechpy.geostock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class StockTypeActivity extends AppCompatActivity {

    TextView tvUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_type);
    }

    @Override
    public void onResume(){
        super.onResume();
        tvUserName = (TextView) findViewById(R.id.tv_stocktype_username);
        Intent intent = getIntent();
        tvUserName.setText(intent.getStringExtra("username"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stock_type, menu);
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

    public void onClickDeposit(View view){
        tvUserName = (TextView) findViewById(R.id.tv_stocktype_username);
        Intent stockList = new Intent(this, StockListActivity.class);
        stockList.putExtra("username", tvUserName.getText());
        startActivity(stockList);
    }

    public void onClickLaboratory(View view){
        tvUserName = (TextView) findViewById(R.id.tv_stocktype_username);
        Intent stockList = new Intent(this, StockListActivity.class);
        stockList.putExtra("username", tvUserName.getText());
        startActivity(stockList);
    }
}
