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
                if (btnId == R.id.btn_reset) {
                    DBHelper dbh = DBHelper.getInstance(getActivity());
                    SQLiteDatabase db = dbh.getWritableDatabase();
                    dbh.recreateDB(db);
                    db.close();
                    Toast.makeText(getActivity(), R.string.db_reset, Toast.LENGTH_SHORT).show();
                }
                else if (btnId == R.id.btn_sync){
                    Context ctx = getActivity();
                    UserManager um = new UserManager(ctx);
                    um.insert("ancho", "666", getString(R.string.zone_deposit));
                    um.insert("alex", "777", getString(R.string.zone_lab));

                    ZoneManager zm = new ZoneManager(ctx);
                    zm.insert(1, "Deposit Nr. 1", getString(R.string.zone_deposit));
                    zm.insert(2, "Deposit Nr. 2", getString(R.string.zone_deposit));
                    zm.insert(3, "Deposit Nr. 3", getString(R.string.zone_deposit));
                    zm.insert(4, "Lab Nr. 1", getString(R.string.zone_lab));
                    zm.insert(5, "Lab Nr. 2", getString(R.string.zone_lab));

                    ItemManager it = new ItemManager(ctx);
                    it.insert("keyboard", "Keyboard", getString(R.string.zone_deposit));
                    it.insert("engine", "Fusion Engine", getString(R.string.zone_lab));
                    it.insert("quantum", "Quantum Engine", getString(R.string.zone_lab));

                    StockManager sm = new StockManager(ctx);
                    sm.insert(1, getString(R.string.zone_deposit), getString(R.string.stock_active), "ancho", 1);
                    sm.insert(2, getString(R.string.zone_deposit), getString(R.string.stock_confirmed), "ancho", 3);
                    sm.insert(3, getString(R.string.zone_lab), getString(R.string.stock_confirmed), "alex", 4);

                    StockDetailManager sdm = new StockDetailManager(ctx);
                    sdm.insert(1, 1, "keyboard", 10f);
                    sdm.insert(1, 2, "engine", 20f);
                    sdm.insert(2, 1, "keyboard", 30f);
                    sdm.insert(3, 1, "engine", 40f);
                    sdm.insert(3, 2, "quantum", 50f);

                    Toast.makeText(getActivity(), R.string.db_sync, Toast.LENGTH_SHORT).show();
                }
                else if (btnId == R.id.ib_items_delete){
                    Integer stockSerNr = Integer.valueOf(getArguments().getString("stockSerNr"));
                    Integer lineNr = Integer.valueOf(getArguments().getString("lineNr"));
                    StockDetailManager sdm = new StockDetailManager(getActivity());
                    sdm.delete(stockSerNr, lineNr);
                    Toast.makeText(getActivity(), R.string.stock_detail_deleted, Toast.LENGTH_SHORT).show();
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