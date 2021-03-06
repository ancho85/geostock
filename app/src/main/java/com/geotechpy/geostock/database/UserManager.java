package com.geotechpy.geostock.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.geotechpy.geostock.models.User;

import java.util.ArrayList;

/**
 * Manages connections to table User
 */
public class UserManager {
    public static final String TABLE_NAME = "User";
    public static final String CN_CODE = "code";
    public static final String CN_PASSWORD = "password";
    public static final String CN_TYPE = "type";

    private SQLiteDatabase db;


    public UserManager(Context ctx){
        DBHelper helper = DBHelper.getInstance(ctx);
        db = helper.getWritableDatabase();
    }

    private ContentValues setContentValues(String code, String password, String type) {
        ContentValues values = new ContentValues();
        values.put(CN_CODE, code);
        values.put(CN_PASSWORD, password);
        values.put(CN_TYPE, type);
        return values;
    }

    public void insert(String code, String password, String type) {
        db.insert(TABLE_NAME, null, setContentValues(code, password, type));
    }

    public void delete(String code) {
        db.delete(TABLE_NAME, CN_CODE + "=?", new String[]{code});
    }

    public void update(String code, String password, String type) {
        db.update(TABLE_NAME, setContentValues(code, password, type), CN_CODE + "=?", new String[]{code});
    }

    public ArrayList<User> getUsers() {
        String[] columns = new String[]{CN_CODE, CN_PASSWORD, CN_TYPE};
        Cursor c = null;
        ArrayList<User> aluser = new ArrayList<>();
        try{
            c = db.query(TABLE_NAME, columns, null, null, null, null, null);
            while (c.moveToNext()){
                Integer position = c.getPosition();
                User user = new User(c.getString(0), c.getString(1), c.getString(2));
                aluser.add(position, user);
            }
        }finally {
            if (c != null){
                c.close();
            }
        }
        return aluser;
    }

    public Integer count() {
        Integer count = 0;
        Cursor c = db.rawQuery("SELECT COUNT(*) AS count FROM " + TABLE_NAME, null);
        if (c.moveToFirst()) {
            count = c.getInt(0);
        }
        c.close();
        return count;
    }

    public static User load(Context ctx, String code) {
        DBHelper sdb = DBHelper.getInstance(ctx);
        SQLiteDatabase db = null;
        User user = new User();
        try{
            db = sdb.getReadableDatabase();
            String[] columns = new String[]{CN_CODE, CN_PASSWORD, CN_TYPE};
            String where = "code = '" + code + "'";
            Cursor c = null;
            try{
                c = db.query(TABLE_NAME, columns, where, null, null, null, null);
                if (c.moveToFirst()) {
                    user.setCode(c.getString(0));
                    user.setPassword(c.getString(1));
                    user.setType(c.getString(2));
                }
            }finally {
                if (c != null){
                    c.close();
                }
            }
        }finally {
            if (db != null){
                db.close();
            }
        }
        return user;
    }
}
