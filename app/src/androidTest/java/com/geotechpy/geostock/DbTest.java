package com.geotechpy.geostock;

import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Log;

import com.geotechpy.geostock.database.ItemManager;
import com.geotechpy.geostock.database.StockDetailManager;
import com.geotechpy.geostock.database.StockManager;
import com.geotechpy.geostock.database.UserManager;
import com.geotechpy.geostock.database.ZoneManager;
import com.geotechpy.geostock.models.Item;
import com.geotechpy.geostock.models.Stock;
import com.geotechpy.geostock.models.StockDetail;
import com.geotechpy.geostock.models.User;
import com.geotechpy.geostock.models.Zone;

import java.util.ArrayList;

/**
 * Created by ancho on 28/06/15.
 */
public class DbTest extends AndroidTestCase {

    UserManager user;
    ZoneManager zone;
    ItemManager item;
    StockManager stock;
    StockDetailManager stockDetail;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        Context ctx = getContext();
        user = new UserManager(ctx);
        zone = new ZoneManager(ctx);
        item = new ItemManager(ctx);
        stock = new StockManager(ctx);
        stockDetail = new StockDetailManager(ctx);
    }

    public void testUser() throws Exception {
        user.insert("ancho", "123456", String.valueOf(R.string.zone_admin));
        user.insert("alex", "654321", String.valueOf(R.string.zone_deposit));
        user.insert("liz", "111222", String.valueOf(R.string.zone_lab));
        user.insert("ondina", "333444", String.valueOf(R.string.zone_both));
        assertEquals(4, user.count().intValue());

        user.update("ancho", "666", String.valueOf(R.string.zone_admin));
        User ancho = UserManager.load(getContext(), "ancho");
        assertEquals("666", ancho.getPassword());

        user.delete("liz");
        assertEquals(3, user.count().intValue());
    }

    public void testZone() throws Exception {
        zone.insert(1, "Admin", String.valueOf(R.string.zone_admin));
        zone.insert(2, "Deposit", String.valueOf(R.string.zone_deposit));
        zone.insert(3, "Lab", String.valueOf(R.string.zone_lab));
        zone.insert(4, "Deposit and Lab", String.valueOf(R.string.zone_both));
        assertEquals(4, zone.count().intValue());

        zone.update(4, "Deposit & Lab", String.valueOf(R.string.zone_both));
        Zone lab = ZoneManager.load(getContext(), 4);
        assertEquals(String.valueOf(R.string.zone_both), lab.getType());

        zone.delete(4);
        assertEquals(3, zone.count().intValue());
    }

    public void testItem() throws Exception {
        item.insert("keyb", "Keyboard", String.valueOf(R.string.zone_admin));
        item.insert("xray", "X Ray", String.valueOf(R.string.zone_lab));
        item.insert("paper", "Papers", String.valueOf(R.string.zone_deposit));
        item.insert("stuffs", "Other Stuffs", String.valueOf(R.string.zone_both));
        assertEquals(4, item.count().intValue());

        item.update("stuffs", "X Rayed Keyboard", String.valueOf(R.string.zone_both));
        Item stuff = ItemManager.load(getContext(), "stuffs");
        assertEquals("stuffs", stuff.getCode());
        assertEquals("X Rayed Keyboard", stuff.getName());

        item.delete("stuffs");
        assertEquals(3, item.count().intValue());
    }

    public void testStock() throws Exception {
        stock.insert(1, String.valueOf(R.string.zone_admin), String.valueOf(R.string.stock_active), "ancho", 1);
        stock.insert(2, String.valueOf(R.string.zone_deposit), String.valueOf(R.string.stock_active), "alex", 2);
        stock.insert(3, String.valueOf(R.string.zone_lab), String.valueOf(R.string.stock_confirmed), "ondina", 2);
        stock.insert(4, String.valueOf(R.string.zone_both), String.valueOf(R.string.stock_active), "ondina", 3);
        stock.insert(5, String.valueOf(R.string.zone_both), String.valueOf(R.string.stock_confirmed), "ancho", 4);
        assertEquals(5, stock.count().intValue());

        stock.update(4, String.valueOf(R.string.zone_admin), String.valueOf(R.string.stock_confirmed), "ancho", 1);
        Stock stockNr4 = StockManager.load(getContext(), 4);
        assertEquals(String.valueOf(R.string.stock_confirmed), stockNr4.getStatus());

        stock.delete(5);
        assertEquals(4, stock.count().intValue());
    }

    public void testStockDetail() throws Exception{

        stockDetail.insert(1, 1, "keyb", 20f);
        stockDetail.insert(1, 2, "xray", 3f);
        stockDetail.insert(2, 1, "paper", 1000f);
        stockDetail.insert(3, 1, "xray", 10.5f);
        stockDetail.insert(3, 2, "xray", 2f);
        stockDetail.insert(3, 3, "stuffs", 4000f);
        assertEquals(6, stockDetail.count().intValue());

        stockDetail.update(1, 1, "keyb", 30f);
        StockDetail keybStockDetail = StockDetailManager.load(getContext(), 1, 1);
        assertEquals(30f, keybStockDetail.getQty());

        stockDetail.delete(1);
        assertEquals(4, stockDetail.count().intValue());
    }
}
