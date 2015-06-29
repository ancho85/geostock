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
        user.insert("ancho", "123456", "A");
        user.insert("alex", "654321", "D");
        user.insert("liz", "111222", "L");
        user.insert("ondina", "333444", "Y");
        assertEquals(4, user.count().intValue());

        user.update("ancho", "666", "A");
        User ancho = UserManager.load(getContext(), "ancho");
        assertEquals("666", ancho.getPassword());

        user.delete("liz");
        assertEquals(3, user.count().intValue());
    }

    public void testZone() throws Exception {
        zone.insert(1, "Admin", "A");
        zone.insert(2, "Deposit", "D");
        zone.insert(3, "Lab", "L");
        zone.insert(4, "Deposit and Lab", "Y");
        assertEquals(4, zone.count().intValue());

        zone.update(4, "Deposit & Lab", "Y");
        Zone lab = ZoneManager.load(getContext(), 3);
        assertEquals("L", lab.getType());

        zone.delete(4);
        assertEquals(3, zone.count().intValue());
    }

    public void testItem() throws Exception {
        item.insert("keyb", "Keyboard", "D");
        item.insert("xray", "X Ray", "L");
        item.insert("stuffs", "Other Stuffs", "Y");
        assertEquals(3, item.count().intValue());

        item.update("stuffs", "X Rayed Keyboard", "Y");
        Item stuff = ItemManager.load(getContext(), "stuffs");
        assertEquals("stuffs", stuff.getCode());
        assertEquals("X Rayed Keyboard", stuff.getName());

        item.delete("stuffs");
        assertEquals(2, item.count().intValue());
    }

    public void testStock() throws Exception {
        stock.insert(1, "A", "C", "ancho", 1);
        stock.insert(2, "D", "A", "alex", 2);
        stock.insert(3, "L", "C", "ondina", 2);
        stock.insert(4, "Y", "A", "ondina", 3);
        stock.insert(5, "Y", "C", "ancho", 4);
        assertEquals(5, stock.count().intValue());

        /*stock.update("stuffs", "X Rayed Keyboard", "Y");
        Item stuff = ItemManager.load(getContext(), "stuffs");
        assertEquals("stuffs", stuff.getCode());
        assertEquals("X Rayed Keyboard", stuff.getName());

        stock.delete("stuffs");
        assertEquals(2, stock.count().intValue());*/
    }
}
