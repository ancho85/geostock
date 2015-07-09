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

import com.geotechpy.geostock.ItemActivity;
import com.geotechpy.geostock.R;
import com.geotechpy.geostock.database.ItemManager;
import com.geotechpy.geostock.database.StockDetailManager;
import com.geotechpy.geostock.models.Item;
import com.geotechpy.geostock.models.StockDetail;

import java.util.ArrayList;

/**
 * Item adapter
 */
public class ItemAdapter extends BaseAdapter {
    private ArrayList<StockDetail> al_items = new ArrayList<>();
    private Context mContext;
    private String userName;
    private String stockSerNr;
    private String zoneCode;

    public ItemAdapter(Context context){
        this.mContext = context;
    }

    public void updateItems(ArrayList<StockDetail> al_items){
        this.al_items = al_items;
        notifyDataSetChanged();
    }

    public void deleteItem(int position){
        this.al_items.remove(position);
        notifyDataSetChanged();
    }

    public void setUserStockZone(String user, String stock, String zone){
        this.userName = user;
        this.stockSerNr = stock;
        this.zoneCode = zone;
    }

    @Override
    public int getCount() {
        return al_items.size();
    }

    @Override
    public StockDetail getItem(int position) {
        return al_items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ItemViewHolder holder;
        // When convertView is not null, we can reuse it directly, there is no need
        // to re inflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_item, parent, false);
            holder = new ItemViewHolder();
            holder.tvItemCode = (TextView) convertView.findViewById(R.id.tv_items_code);
            holder.tvItemName = (TextView) convertView.findViewById(R.id.tv_items_name);
            holder.tvItemLineNr = (TextView) convertView.findViewById(R.id.tv_items_linenr);
            holder.tvItemQty = (TextView) convertView.findViewById(R.id.tv_items_qty);
            holder.ibEdit = (ImageButton) convertView.findViewById(R.id.ib_items_edit);
            holder.ibDelete = (ImageButton) convertView.findViewById(R.id.ib_items_delete);
            holder.position = position;
            convertView.setTag(holder);
        }
        else {
            holder = (ItemViewHolder) convertView.getTag();
        }
        String itemCode = al_items.get(position).getItem_code();
        holder.tvItemCode.setText(itemCode);
        Item item = ItemManager.load(mContext, itemCode);
        holder.tvItemName.setText(item.getName());
        holder.tvItemQty.setText(al_items.get(position).getQty().toString());
        holder.tvItemLineNr.setText(al_items.get(position).getLinenr().toString());

        holder.ibEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent itemEditActivity = new Intent(mContext, ItemActivity.class);

                itemEditActivity.putExtra("username", userName);
                itemEditActivity.putExtra("stockSerNr", String.valueOf(stockSerNr));
                itemEditActivity.putExtra("zoneCode", String.valueOf(zoneCode));
                itemEditActivity.putExtra("itemCode", holder.tvItemCode.getText().toString());
                itemEditActivity.putExtra("itemName", holder.tvItemName.getText().toString());
                itemEditActivity.putExtra("itemLineNr", holder.tvItemLineNr.getText().toString());
                itemEditActivity.putExtra("itemQty", holder.tvItemQty.getText().toString());
                itemEditActivity.putExtra("editMode", true);
                mContext.startActivity(itemEditActivity);
            }
        });

        holder.ibDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage(R.string.confirm_action);
                builder.setTitle(R.string.confirm_title);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        Integer lineNr = Integer.valueOf(holder.tvItemLineNr.getText().toString());
                        StockDetailManager sdmd = new StockDetailManager(mContext);
                        sdmd.delete(Integer.valueOf(stockSerNr), lineNr);
                        deleteItem(holder.position);
                        Toast.makeText(mContext, R.string.stock_detail_deleted, Toast.LENGTH_SHORT).show();
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

        return (convertView);
    }
}

class ItemViewHolder {
    // A ViewHolder keeps references to children views to avoid unnecessary calls
    // to findViewById() on each row.
    TextView tvItemCode;
    TextView tvItemName;
    TextView tvItemLineNr;
    TextView tvItemQty;
    ImageButton ibEdit;
    ImageButton ibDelete;
    int position;
}