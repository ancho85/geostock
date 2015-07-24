package com.geotechpy.geostock.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.geotechpy.geostock.R;
import com.geotechpy.geostock.ItemListActivity;
import com.geotechpy.geostock.database.StockDetailManager;
import com.geotechpy.geostock.database.StockManager;
import com.geotechpy.geostock.models.Stock;
import com.geotechpy.geostock.network.SyncToServer;

import java.util.ArrayList;

/**
 * Stock Adapter
 */
public class StockAdapter extends BaseAdapter{
    private ArrayList<Stock> al_stocks = new ArrayList<>();
    private Context mContext;
    private String userName;
    StockAdapter currentInstance;

    public StockAdapter(Context context){
        this.mContext = context;
        this.currentInstance = this;
    }

    static class StockViewHolder {
        // A ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row.
        TextView tvStockSerNr;
        TextView tvZoneCode;
        TextView tvStatusCode;
        ImageButton ibSync;
        ImageButton ibEdit;
        ImageButton ibDelete;
        int position;
    }

    public void updateStocks(ArrayList<Stock> al_stocks){
        this.al_stocks = al_stocks;
        notifyDataSetChanged();
    }

    public void deleteStock(int position){
        this.al_stocks.remove(position);
        notifyDataSetChanged();
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    @Override
    public int getCount() {
        return al_stocks.size();
    }

    @Override
    public Stock getItem(int position) {
        return al_stocks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final StockViewHolder holder;
        // When convertView is not null, we can reuse it directly, there is no need
        // to re inflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_stock, parent, false);
            holder = new StockViewHolder();
            holder.tvStockSerNr = (TextView) convertView.findViewById(R.id.tv_stock_sernr);
            holder.tvZoneCode = (TextView) convertView.findViewById(R.id.tv_zone_code);
            holder.tvStatusCode = (TextView) convertView.findViewById(R.id.tv_status_code);
            holder.ibSync = (ImageButton) convertView.findViewById(R.id.ib_sync);
            holder.ibEdit = (ImageButton) convertView.findViewById(R.id.ib_edit);
            holder.ibDelete = (ImageButton) convertView.findViewById(R.id.ib_delete);
            holder.position = position;

            convertView.setTag(holder);
        }
        else {
            holder = (StockViewHolder) convertView.getTag();
        }
        holder.tvStockSerNr.setText(al_stocks.get(position).getSernr().toString());
        holder.tvZoneCode.setText(al_stocks.get(position).getZone_sernr().toString());
        holder.tvStatusCode.setText(al_stocks.get(position).getStatus());

        holder.ibEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent itemList = new Intent(mContext, ItemListActivity.class);
                itemList.putExtra("username", userName);
                itemList.putExtra("stockSerNr", holder.tvStockSerNr.getText().toString());
                itemList.putExtra("zoneCode", holder.tvZoneCode.getText().toString());
                mContext.startActivity(itemList);
            }
        });

        holder.ibDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage(R.string.confirm_action);
                builder.setTitle(R.string.confirm_title);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        Integer stockNr = Integer.valueOf(holder.tvStockSerNr.getText().toString());
                        StockDetailManager stockDetailManager = new StockDetailManager(mContext);
                        stockDetailManager.deleteBySerNr(stockNr);
                        StockManager smd = new StockManager(mContext);
                        smd.delete(stockNr);
                        deleteStock(holder.position);
                        Toast.makeText(mContext, R.string.stock_deleted, Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.create();
                builder.show();

            }
        });

        holder.ibSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SyncToServer sync = new SyncToServer(mContext);
                sync.setStockAdapter(currentInstance);
                sync.setUserName(userName);
                sync.setStockSerNr(Integer.valueOf(holder.tvStockSerNr.getText().toString()));
                sync.setZoneCode(Integer.valueOf(holder.tvZoneCode.getText().toString()));
                sync.syncStock();
            }
        });

        return convertView;
    }

}