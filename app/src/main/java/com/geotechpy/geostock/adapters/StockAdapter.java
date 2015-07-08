package com.geotechpy.geostock.adapters;

import android.content.Context;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.geotechpy.geostock.R;
import com.geotechpy.geostock.models.Stock;

import java.util.ArrayList;

/**
 * BaseAdapterTest
 */
public class StockAdapter extends BaseAdapter implements AdapterViewCompat.OnItemClickListener{
    private ArrayList<Stock> al_stocks = new ArrayList<>();
    private Context mContext;

    public StockAdapter(Context context){
        this.mContext = context;
    }

    public void updateStocks(ArrayList<Stock> al_stocks){
        this.al_stocks = al_stocks;
        notifyDataSetChanged();
    }

    public void deleteStock(int position){
        this.al_stocks.remove(position);
        notifyDataSetChanged();
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
        ViewHolder holder;
        // When convertView is not null, we can reuse it directly, there is no need
        // to re inflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_stock, parent, false);
            holder = new ViewHolder();
            holder.tvStockSerNr = (TextView) convertView.findViewById(R.id.tv_stock_sernr);
            holder.tvZoneCode = (TextView) convertView.findViewById(R.id.tv_zone_code);
            holder.tvStatusCode = (TextView) convertView.findViewById(R.id.tv_status_code);
            holder.ibSync = (ImageButton) convertView.findViewById(R.id.ib_sync);
            holder.ibEdit = (ImageButton) convertView.findViewById(R.id.ib_edit);
            holder.ibDelete = (ImageButton) convertView.findViewById(R.id.ib_delete);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvStockSerNr.setText(al_stocks.get(position).getSernr().toString());
        holder.tvZoneCode.setText(al_stocks.get(position).getZone_sernr().toString());
        holder.tvStatusCode.setText(al_stocks.get(position).getStatus());
        return convertView;
    }

    @Override
    public void onItemClick(AdapterViewCompat<?> parent, View view, int position, long id) {

    }
}

class ViewHolder {
    // A ViewHolder keeps references to children views to avoid unnecessary calls
    // to findViewById() on each row.
    TextView tvStockSerNr;
    TextView tvZoneCode;
    TextView tvStatusCode;
    ImageButton ibSync;
    ImageButton ibEdit;
    ImageButton ibDelete;
}
