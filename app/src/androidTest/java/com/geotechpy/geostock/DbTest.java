package com.geotechpy.geostock;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.geotechpy.geostock.app.GeotechpyStockApp;
import com.geotechpy.geostock.database.DBHelper;
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
    private DBHelper db;
    RenamingDelegatingContext ctx;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        ctx = new RenamingDelegatingContext(getContext(), "test_");
        db = DBHelper.getInstance(ctx);
        SQLiteDatabase testDB = db.getWritableDatabase();
        db.recreateDB(testDB);
        user = new UserManager(ctx);
        zone = new ZoneManager(ctx);
        item = new ItemManager(ctx);
        stock = new StockManager(ctx);
        stockDetail = new StockDetailManager(ctx);
    }

    @Override
    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }

    public void testUser() throws Exception {
        user.insert("ancho", "123456", ctx.getString(R.string.zone_admin));
        user.insert("alex", "654321", ctx.getString(R.string.zone_deposit));
        user.insert("liz", "111222", ctx.getString(R.string.zone_lab));
        user.insert("ondina", "333444", ctx.getString(R.string.zone_both));
        assertEquals(4, user.count().intValue());

        user.delete("liz");
        assertEquals(3, user.count().intValue());

        ArrayList<User> aluser = user.getUsers();
        User posUser = aluser.get(0);
        posUser.setPassword("newPass");
        posUser.setCode("newCode");
        posUser.setType("newType");
        assertEquals("newPass", posUser.getPassword());
        assertEquals("newCode", posUser.getCode());
        assertEquals("newType", posUser.getType());

        user.update("ancho", "666", ctx.getString(R.string.zone_admin));
        User ancho = UserManager.load(ctx, "ancho");
        assertEquals("666", ancho.getPassword());
    }

    public void testZone() throws Exception {
        zone.insert(1, "Admin", ctx.getString(R.string.zone_admin), "DepNameAdmin");
        zone.insert(2, "Deposit", ctx.getString(R.string.zone_deposit), "DepNameDepo");
        zone.insert(3, "Lab", ctx.getString(R.string.zone_lab), "DepNameLab");
        zone.insert(4, "Deposit and Lab", ctx.getString(R.string.zone_both), "DepNameBoth");
        assertEquals(4, zone.count().intValue());

        zone.delete(1);
        assertEquals(3, zone.count().intValue());

        ArrayList<Zone> alzone = zone.getZones(ctx.getString(R.string.zone_deposit));
        Zone posZone = alzone.get(0);
        posZone.setSernr(5);
        posZone.setType(ctx.getString(R.string.zone_both));
        posZone.setName("Other");
        assertEquals(5, posZone.getSernr().intValue());
        assertEquals(ctx.getString(R.string.zone_both), posZone.getType());
        assertEquals("Other", posZone.getName());

        zone.update(4, "Deposit & Lab", ctx.getString(R.string.zone_both), "DepNameBoth");
        Zone lab = ZoneManager.load(ctx, 4);
        assertEquals(ctx.getString(R.string.zone_both), lab.getType());
    }

    public void testItem() throws Exception {
        item.insert("keyb", "Keyboard", 1234567890123L, ctx.getString(R.string.zone_admin));
        item.insert("xray", "X Ray", 1234567890124L, ctx.getString(R.string.zone_lab));
        item.insert("paper", "Papers", 1234567890125L, ctx.getString(R.string.zone_deposit));
        item.insert("stuffs", "Other Stuffs", 1234567890126L, ctx.getString(R.string.zone_both));
        assertEquals(4, item.count().intValue());

        item.delete("paper");
        assertEquals(3, item.count().intValue());

        ArrayList<Item> alitem = item.getItems();
        Item posItem = alitem.get(0);
        posItem.setCode("keyboard");
        posItem.setType(ctx.getString(R.string.zone_lab));
        posItem.setName("KeyB");
        assertEquals("keyboard", posItem.getCode());
        assertEquals(ctx.getString(R.string.zone_lab), posItem.getType());
        assertEquals("KeyB", posItem.getName());

        item.update("stuffs", "X Rayed Keyboard", 1234567890126L, ctx.getString(R.string.zone_both));
        Item stuff = ItemManager.load(ctx, "stuffs");
        assertEquals("stuffs", stuff.getCode());
        assertEquals("X Rayed Keyboard", stuff.getName());
    }

    public void testStock() throws Exception {
        zone.insert(1, "Admin", ctx.getString(R.string.zone_admin), "DepNameAdmin");
        user.insert("ancho", "123456", ctx.getString(R.string.zone_admin));
        item.insert("keyboard", "Keyboard", 1234567890123L, ctx.getString(R.string.zone_admin));

        GeotechpyStockApp.setUserName("ancho");

        stock.insert(1, ctx.getString(R.string.zone_admin), ctx.getString(R.string.stock_active), "ancho", 1);
        stock.insert(stock.getNextSerNr(), ctx.getString(R.string.zone_admin), ctx.getString(R.string.stock_active), "ancho", 1);
        assertEquals(2, stock.count().intValue());

        stock.delete(2);
        assertEquals(1, stock.count().intValue());

        ArrayList<Stock> alstock = stock.getStocks(ctx.getString(R.string.zone_admin));
        Stock posStock = alstock.get(0);
        posStock.setSernr(1);
        posStock.setStatus(ctx.getString(R.string.stock_confirmed));
        posStock.setType(ctx.getString(R.string.zone_admin));
        posStock.setZone_sernr(1);
        posStock.setUser_code("liz");
        assertEquals(1, posStock.getSernr().intValue());
        assertEquals(ctx.getString(R.string.stock_confirmed), posStock.getStatus());
        assertEquals(ctx.getString(R.string.zone_admin), posStock.getType());
        assertEquals(1, posStock.getZone_sernr().intValue());
        assertEquals("liz", posStock.getUser_code());

        stock.update(1, ctx.getString(R.string.zone_admin), ctx.getString(R.string.stock_confirmed), "ancho", 1);
        Stock stockNr1 = StockManager.load(ctx, 1);
        assertEquals(ctx.getString(R.string.stock_confirmed), stockNr1.getStatus());
    }

    public void testStockDetail() throws Exception{
        user.insert("alex", "654321", ctx.getString(R.string.zone_deposit));
        zone.insert(2, "Deposit", ctx.getString(R.string.zone_deposit), "DepNameDepo");
        item.insert("paper", "Papers", 1234567890123L, ctx.getString(R.string.zone_deposit));
        stock.insert(100, ctx.getString(R.string.zone_deposit), ctx.getString(R.string.stock_active), "alex", 2);
        stock.insert(200, ctx.getString(R.string.zone_deposit), ctx.getString(R.string.stock_active), "alex", 2);

        stockDetail.insert(100, "paper", 20f);
        stockDetail.insert(100, "paper", 3f);
        stockDetail.insert(100, "paper", 1000f);
        stockDetail.insert(200, "paper", 1f);
        stockDetail.insert(200, "paper", 2f);
        assertEquals(5, stockDetail.count().intValue());

        stockDetail.delete(100, 3);
        assertEquals(4, stockDetail.count().intValue());

        stockDetail.deleteBySerNr(100);
        assertEquals(2, stockDetail.count().intValue());

        ArrayList<StockDetail> al_stockDetail = stockDetail.getStockDetails(200);
        assertEquals(2, al_stockDetail.size());

        StockDetail posStockDetail = al_stockDetail.get(0);
        posStockDetail.setStock_sernr(300);
        posStockDetail.setLinenr(1);
        posStockDetail.setItem_code("keyboard");
        posStockDetail.setQty(2f);
        assertEquals(300, posStockDetail.getStock_sernr().intValue());
        assertEquals(1, posStockDetail.getLinenr().intValue());
        assertEquals("keyboard", posStockDetail.getItem_code());
        assertEquals(2f, posStockDetail.getQty());

        int nextRow = stockDetail.getNextStockRowNr(200);
        assertEquals(3, nextRow);

        stockDetail.update(200, 2, "paper", 30f);
        StockDetail paperStockDetail = StockDetailManager.load(ctx, 200, 2);
        assertEquals(30f, paperStockDetail.getQty());
        StockDetail paperStockDetail2 = StockDetailManager.load(ctx, 200, 1);
        assertEquals(1f, paperStockDetail2.getQty());
    }

    public void testUpdateDB(){
        ctx = new RenamingDelegatingContext(getContext(), "test_");
        db = DBHelper.getInstance(ctx);
        SQLiteDatabase newDB = db.getWritableDatabase();
        db.onUpgrade(newDB, 1, 2);
        Cursor c = newDB.rawQuery("SELECT COUNT(*) FROM Stock", null);
        c.moveToFirst();
        Integer count = c.getInt(0);
        assertEquals(0, count.intValue());
        c.close();
    }
}
