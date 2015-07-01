package com.geotechpy.geostock;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.geotechpy.geostock.database.DBHelper;
import com.geotechpy.geostock.database.UserManager;
import com.geotechpy.geostock.models.User;


public class MainActivity extends AppCompatActivity {

    DBHelper dbh; // by some reason, I cannot instantiate here as final
    EditText etUserName;
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public static class ConfirmDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.confirm_action);
            builder.setTitle(R.string.confirm_title);
            final Integer btnId = getArguments().getInt("btnId");
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    if (btnId == R.id.btn_reset) {
                        DBHelper dbh = DBHelper.getInstance(getActivity());
                        SQLiteDatabase db = dbh.getWritableDatabase();
                        dbh.recreateDB(db);
                        Toast.makeText(getActivity(), R.string.db_reset, Toast.LENGTH_SHORT).show();
                    }
                    else if (btnId == R.id.btn_sync){
                        UserManager um = new UserManager(getActivity());
                        um.insert("ancho", "666", getString(R.string.zone_admin));
                        Toast.makeText(getActivity(), R.string.db_sync, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            return builder.create();
        }
    }

    public void onClickLogin(View view){
        etUserName = (EditText) findViewById(R.id.et_user);
        etPassword = (EditText) findViewById(R.id.et_password);
        String userText = etUserName.getText().toString();
        String passText = etPassword.getText().toString();
        if (TextUtils.isEmpty(userText)){
            etUserName.requestFocus();
            displayMessage(getString(R.string.empty_field_user));
        }else if (TextUtils.isEmpty(passText)){
            etPassword.requestFocus();
            displayMessage(getString(R.string.empty_field_password));
        }else{
            User user = UserManager.load(this, userText);
            if (TextUtils.isEmpty(user.getCode())){
                displayMessage(getString(R.string.invalid_user));
            }else if (!user.getPassword().equals(passText)) {
                displayMessage(getString(R.string.invalid_password));
                } else{
                    displayMessage("OK!");
                }
            }
        }

    public void displayMessage(String toastString){
        Toast.makeText(getApplicationContext(), toastString, Toast.LENGTH_LONG).show();
    }
}
