package com.geotechpy.geostock;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.geotechpy.geostock.app.GeotechpyStockApp;
import com.geotechpy.geostock.database.UserManager;
import com.geotechpy.geostock.fragments.ConfirmDialog;
import com.geotechpy.geostock.models.User;

import static com.geotechpy.geostock.app.GeotechpyStockApp.displayMessage;


public class MainActivity extends AppCompatActivity {

    EditText etUserName;
    EditText etPassword;
    TextView tvMainStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (BuildConfig.DEBUG){ //api22 lock screen issue with espresso and travis ci
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        }
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
        tvMainStatus = (TextView) findViewById(R.id.tv_mainstatus);
        tvMainStatus.setText("");
        tvMainStatus.setTextColor(Color.RED);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void onClickReset(View view) {
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        ConfirmDialog confirmDialog = new ConfirmDialog();
        Bundle args = new Bundle();
        args.putInt("btnId", R.id.btn_reset);
        confirmDialog.setArguments(args);
        confirmDialog.show(fragmentManager, "");
    }

    public void onClickSync(View view){
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        ConfirmDialog confirmDialog = new ConfirmDialog();
        Bundle args = new Bundle();
        args.putInt("btnId", R.id.btn_sync);
        confirmDialog.setArguments(args);
        confirmDialog.show(fragmentManager, "");
    }


    public void onClickLogin(View view){
        etUserName = (EditText) findViewById(R.id.et_user);
        etPassword = (EditText) findViewById(R.id.et_password);
        String userText = etUserName.getText().toString();
        String passText = etPassword.getText().toString();
        if (TextUtils.isEmpty(userText)){
            etUserName.requestFocus();
            updateMessage(getString(R.string.empty_field_user));
        }else if (TextUtils.isEmpty(passText)){
            etPassword.requestFocus();
            updateMessage(getString(R.string.empty_field_password));
        }else{
            User user = UserManager.load(this, userText);
            if (TextUtils.isEmpty(user.getCode())){
                updateMessage(getString(R.string.invalid_user));
            }else if (!user.getPassword().equals(passText)) {
                updateMessage(getString(R.string.invalid_password));
                } else{
                    ((GeotechpyStockApp)getApplication()).setUserName(userText);
                    Intent stockTypeActivity = new Intent(this, StockTypeActivity.class);
                    startActivity(stockTypeActivity);
                }
            }
        }

    public void updateMessage(String toastString){
        displayMessage(toastString);
        tvMainStatus.setText(toastString);

    }
}
