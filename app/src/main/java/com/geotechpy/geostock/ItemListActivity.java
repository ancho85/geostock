package com.geotechpy.geostock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import com.geotechpy.geostock.database.ItemManager;
import com.geotechpy.geostock.database.StockDetailManager;
import com.geotechpy.geostock.fragments.ConfirmDialog;
import com.geotechpy.geostock.models.Item;
import com.geotechpy.geostock.models.StockDetail;

import java.util.ArrayList;


public class ItemListActivity extends AppCompatActivity {

    TextView tvUserName;
    TextView tvStockSerNr;
    TextView tvZoneCode;
    private ArrayList<StockDetail> al_stockDetail = new ArrayList<>();

    static class ViewHolder {
        // A ViewHolder keeps references to children views to avoid unneccessary calls
        // to findViewById() on each row.
        TextView tvItemCode;
        TextView tvItemName;
        TextView tvItemLineNr;
        TextView tvItemQty;
        ImageButton ibEdit;
        ImageButton ibDelete;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
    }

    @Override
    public void onResume(){
        super.onResume();
        tvUserName = (TextView) findViewById(R.id.tv_itemlist_username);
        tvStockSerNr = (TextView) findViewById(R.id.tv_itemlist_stock_sernr);
        tvZoneCode = (TextView) findViewById(R.id.tv_itemlist_stock_zone_code);
        Intent intent = getIntent();
        tvUserName.setText(intent.getStringExtra("username"));
        tvStockSerNr.setText(intent.getStringExtra("stockSerNr"));
        tvZoneCode.setText(intent.getStringExtra("zoneCode"));
        showItems();
    }

    public void showItems(){
        StockDetailManager stockDetailManager = new StockDetailManager(this);
        al_stockDetail = stockDetailManager.getStockDetails();

        class ItemAdapter extends ArrayAdapter<StockDetail> {
            AppCompatActivity context;

            ItemAdapter(AppCompatActivity context) {
                super(context, R.layout.listitem_stock, al_stockDetail);
                this.context = context;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                final ViewHolder holder;
                // When convertView is not null, we can reuse it directly, there is no need
                // to re inflate it. We only inflate a new View when the convertView supplied
                // by ListView is null.

                if (convertView == null) {
                    LayoutInflater inflater = context.getLayoutInflater();
                    convertView = inflater.inflate(R.layout.listitem_item, parent, false);
                    holder = new ViewHolder();
                    holder.tvItemCode = (TextView) convertView.findViewById(R.id.tv_items_code);
                    holder.tvItemName = (TextView) convertView.findViewById(R.id.tv_items_name);
                    holder.tvItemLineNr = (TextView) convertView.findViewById(R.id.tv_items_linenr);
                    holder.tvItemQty = (TextView) convertView.findViewById(R.id.tv_items_qty);
                    holder.ibEdit = (ImageButton) convertView.findViewById(R.id.ib_items_edit);
                    holder.ibDelete = (ImageButton) convertView.findViewById(R.id.ib_items_delete);
                    convertView.setTag(holder);
                }
                else {
                    holder = (ViewHolder) convertView.getTag();
                }
                String itemCode = al_stockDetail.get(position).getItem_code();
                holder.tvItemCode.setText(itemCode);
                Item item = ItemManager.load(context, itemCode);
                holder.tvItemName.setText(item.getName());
                holder.tvItemQty.setText(al_stockDetail.get(position).getQty().toString());
                holder.tvItemLineNr.setText(al_stockDetail.get(position).getLinenr().toString());

                holder.ibEdit.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        tvUserName = (TextView) findViewById(R.id.tv_itemlist_username);
                        tvStockSerNr = (TextView) findViewById(R.id.tv_itemlist_stock_sernr);
                        tvZoneCode = (TextView) findViewById(R.id.tv_itemlist_stock_zone_code);
                        Intent itemEditActivity = new Intent(context, ItemActivity.class);

                        itemEditActivity.putExtra("username", tvUserName.getText().toString());
                        itemEditActivity.putExtra("stockSerNr", tvStockSerNr.getText().toString());
                        itemEditActivity.putExtra("zoneCode", tvZoneCode.getText().toString());
                        itemEditActivity.putExtra("itemCode", holder.tvItemCode.getText().toString());
                        itemEditActivity.putExtra("itemName", holder.tvItemName.getText().toString());
                        itemEditActivity.putExtra("itemLineNr", holder.tvItemLineNr.getText().toString());
                        itemEditActivity.putExtra("itemQty", holder.tvItemQty.getText().toString());
                        itemEditActivity.putExtra("editMode", true);
                        startActivity(itemEditActivity);
                    }
                });

                holder.ibDelete.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        FragmentManager fragmentManager = ItemListActivity.this.getSupportFragmentManager();
                        ConfirmDialog confirmDialog = new ConfirmDialog();
                        Bundle args = new Bundle();
                        args.putInt("btnId", R.id.ib_items_delete);
                        args.putString("stockSerNr", tvStockSerNr.getText().toString());
                        args.putString("lineNr", holder.tvItemLineNr.getText().toString());
                        confirmDialog.setArguments(args);
                        confirmDialog.show(fragmentManager, "");
                    }
                });

                return (convertView);
            }
        }

        ItemAdapter itemAdapter = new ItemAdapter(this);
        ListView lvItems = (ListView) findViewById(R.id.lv_itemlist_items);
        lvItems.setAdapter(itemAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item_list, menu);
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

    public void onClickCancel(View view){
        finish();
    }

    public void onClickNew(View view){
        tvUserName = (TextView) findViewById(R.id.tv_itemlist_username);
        tvStockSerNr = (TextView) findViewById(R.id.tv_itemlist_stock_sernr);
        tvZoneCode = (TextView) findViewById(R.id.tv_itemlist_stock_zone_code);
        Intent itemActivity = new Intent(this, ItemActivity.class);
        itemActivity.putExtra("username", tvUserName.getText().toString());
        itemActivity.putExtra("stockSerNr", tvStockSerNr.getText().toString());
        itemActivity.putExtra("zoneCode", tvZoneCode.getText().toString());
        itemActivity.putExtra("editMode", false);
        startActivity(itemActivity);

    }
}
