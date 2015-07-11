package com.geotechpy.geostock.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.geotechpy.geostock.R;
import com.geotechpy.geostock.database.DBHelper;
import com.geotechpy.geostock.database.ItemManager;
import com.geotechpy.geostock.database.StockDetailManager;
import com.geotechpy.geostock.database.StockManager;
import com.geotechpy.geostock.database.UserManager;
import com.geotechpy.geostock.database.ZoneManager;
import com.geotechpy.geostock.network.SyncFromServer;

/**
 * Confirmation Dialog Fragment
 */
public class ConfirmDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.confirm_action);
        builder.setTitle(R.string.confirm_title);
        final Integer btnId = getArguments().getInt("btnId");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                switch (btnId){
                    case R.id.btn_reset:
                        DBHelper dbh = DBHelper.getInstance(getActivity());
                        SQLiteDatabase db = dbh.getWritableDatabase();
                        dbh.recreateDB(db);
                        db.close();
                        Toast.makeText(getActivity(), R.string.db_reset, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btn_sync:
                        Context ctx = getActivity();
                        SyncFromServer sync = new SyncFromServer(ctx);
                        sync.syncMasters();
                        break;
                    default:
                        break;
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