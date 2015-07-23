package com.geotechpy.geostock.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ancho on 27/06/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper mInstance = null;

    private static final String DATABASE_NAME = "GeoStock.db";
    private static final int DATABASE_VERSION = 1;

    String sqlUserCreate = "CREATE TABLE User (" +
            "code TEXT PRIMARY KEY NOT NULL, " +
            "password TEXT NOT NULL, " +
            "type TEXT NOT NULL)";
    String sqlItemCreate = "CREATE TABLE Item (" +
            "code TEXT PRIMARY KEY NOT NULL, " +
            "name TEXT NOT NULL, " +
            "barcode INTEGER NOT NULL," +
            "type TEXT NOT NULL)";
    String sqlZoneCreate = "CREATE TABLE Zone (" +
            "sernr INTEGER PRIMARY KEY NOT NULL, " +
            "name TEXT NOT NULL, " +
            "type TEXT NOT NULL)";
    String sqlStockCreate = "CREATE TABLE Stock (" +
            "sernr INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "type TEXT NOT NULL, " +
            "status TEXT NOT NULL, " +
            "user_code TEXT NOT NULL, " +
            "zone_sernr INTEGER NOT NULL, "+
            "FOREIGN KEY (user_code) REFERENCES User(code), " +
            "FOREIGN KEY (zone_sernr) REFERENCES Zone(sernr))";
    String sqlStockDetailCreate = "CREATE TABLE StockDetail (" +
            "sernr INTEGER PRIMARY KEY AUTOINCREMENT," +
            "stock_sernr INTEGER NOT NULL," +
            "linenr INTEGER NOT NULL, " +
            "item_code TEXT NOT NULL, " +
            "qty REAL NOT NULL, " +
            "FOREIGN KEY (stock_sernr) REFERENCES Stock(sernr)," +
            "FOREIGN KEY (item_code) REFERENCES Item(code))";


    public static DBHelper getInstance(Context ctx) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new DBHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation. make call to static factory method "getInstance()" instead.
     */
    private DBHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createAll(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        recreateDB(db);
    }

    public void recreateDB(SQLiteDatabase db){
        dropAll(db);
        createAll(db);
    }

    public void createAll(SQLiteDatabase db){
        try {
            db.execSQL(sqlUserCreate);
            db.execSQL(sqlItemCreate);
            db.execSQL(sqlZoneCreate);
            db.execSQL(sqlStockCreate);
            db.execSQL(sqlStockDetailCreate);
        }
        catch (Exception ex) {
            Log.e("SQLite", "Error!", ex);
        }
    }

    public void dropAll(SQLiteDatabase db){
        try {
            db.execSQL("DROP TABLE IF EXISTS StockDetail");
            db.execSQL("DROP TABLE IF EXISTS Stock");
            db.execSQL("DROP TABLE IF EXISTS Item");
            db.execSQL("DROP TABLE IF EXISTS User");
            db.execSQL("DROP TABLE IF EXISTS Zone");
        }
        catch (Exception ex) {
            Log.e("SQLite", "Error!", ex);
        }
    }

}
