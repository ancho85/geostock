package com.geotechpy.geostock;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.geotechpy.geostock.app.GeotechpyStockApp;
import com.geotechpy.geostock.database.UserManager;
import com.geotechpy.geostock.models.User;

public class StockTypeActivity extends AppCompatActivity {

    TextView tvUserName;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_type);
    }

    @Override
    public void onResume(){
        super.onResume();
        userName = ((GeotechpyStockApp)getApplication()).getUserName();
        if (userName == null){
            ((GeotechpyStockApp)getApplication()).respawnLogin();
        }
        tvUserName = (TextView) findViewById(R.id.tv_stocktype_username);
        tvUserName.setText(userName);
        User user = UserManager.load(this, userName);
        String userType = user.getType();
        if (!userType.equals(getString(R.string.zone_admin))){
            if (!userType.equals(getString(R.string.zone_both))){
                if (userType.equals(getString(R.string.zone_deposit))){
                    Button btnLab = (Button) findViewById(R.id.btn_laboratory);
                    btnLab.setVisibility(View.INVISIBLE);
                }else if (userType.equals(getString(R.string.zone_lab))){
                    Button btnDep = (Button) findViewById(R.id.btn_deposit);
                    btnDep.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_title)
                .setMessage(R.string.confirm_exit)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        StockTypeActivity.super.onBackPressed();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);
                    }
                }).create().show();
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
        Intent stockList = new Intent(this, StockListActivity.class);
        startActivity(stockList);
    }

    public void onClickLaboratory(View view){
        Intent stockList = new Intent(this, StockListActivity.class);
        startActivity(stockList);
    }
}
