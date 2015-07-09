package com.geotechpy.geostock.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.geotechpy.geostock.R;
import com.geotechpy.geostock.models.Zone;

import java.util.ArrayList;

/**
 * Zone Adapter
 */
public class ZoneAdapter extends BaseAdapter {

    private ArrayList<Zone> al_zones = new ArrayList<>();
    Context mContext;

    public ZoneAdapter(Context context) {
        this.mContext = context;
    }

    static class ZoneViewHolder {
        // A ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row.
        TextView tvZoneCode;
        TextView tvZoneName;
    }

    public void updateZones(ArrayList<Zone> al_zones){
        this.al_zones = al_zones;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return al_zones.size();
    }

    @Override
    public Zone getItem(int position) {
        return al_zones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ZoneViewHolder holder;
        // When convertView is not null, we can reuse it directly, there is no need
        // to re inflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_zone, parent, false);
            holder = new ZoneViewHolder();
            holder.tvZoneCode = (TextView) convertView.findViewById(R.id.tv_lizone_code);
            holder.tvZoneName = (TextView) convertView.findViewById(R.id.tv_lizone_name);
            convertView.setTag(holder);
        }
        else {
            holder = (ZoneViewHolder) convertView.getTag();
        }
        holder.tvZoneCode.setText(al_zones.get(position).getSernr().toString());
        holder.tvZoneName.setText(al_zones.get(position).getName());

        return (convertView);
    }
}