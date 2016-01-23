package com.smartgateapps.saudifootball.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.smartgateapps.saudifootball.saudi.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raafat on 22/12/2015.
 */
public class Legue {

    public static final String TABLE_NAME = "LEGUES";
    public static final String COL_ID = "ID";
    public static final String COL_NAME  = "COL_NAME";
    public static final String[] COLS = new String[] {COL_ID,COL_NAME};

    public static String getCreateSql(){
        return "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME +" ( "+
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"+
                COL_NAME + " TEXT );";
    }

    public static List<Legue> load(Long id){
        String where = " 1 = 1 ";
        where += (id == null)? "" :" AND "+COL_ID +" =? ";

        List<String> selArgs = new ArrayList<>();
        if(id != null)
            selArgs.add(String.valueOf(id));

        Cursor c = MyApplication.dbr.query(TABLE_NAME,COLS,where, (selArgs.toArray(new String[]{})),null,null,null);
        List<Legue> res = null;

        if(c.moveToFirst()){
            res = new ArrayList<>();
            do{
                Legue legue = new Legue(c.getInt(c.getColumnIndex(COL_ID)),c.getString(c.getColumnIndex(COL_NAME)));
                res.add(legue);
            }while (c.moveToNext());
        }

        return res;
    }

    public boolean save (){
        ContentValues cv = new ContentValues();
        cv.put(COL_ID,this.getId());
        cv.put(COL_NAME,this.getName());
        try {
            return MyApplication.dbw.insert(TABLE_NAME,null,cv)>0;
        } catch (Exception e) {
            //Error
        }
        return false;

    }

    private int id;
    private String name;

    public Legue(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        Legue other = (Legue)o;
        return this.getId() == other.getId();
    }
}
