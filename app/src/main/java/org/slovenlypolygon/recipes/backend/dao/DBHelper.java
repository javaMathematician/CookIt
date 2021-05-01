package org.slovenlypolygon.recipes.backend.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.apache.commons.io.IOUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DBHelper extends SQLiteOpenHelper {
    private final File dbFile;
    private final String dbName;
    private final Context context;

    public DBHelper(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbName, factory, version);
        this.dbName = dbName;
        this.context = context;
        this.dbFile = context.getDatabasePath(dbName);
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        if (!dbFile.exists()) {
            copyDataBase(super.getWritableDatabase().getPath());
        }

        return super.getWritableDatabase();
    }

    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        if (!dbFile.exists()) {
            copyDataBase(super.getReadableDatabase().getPath());
        }

        return super.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private void copyDataBase(String dbPath) {
        try {
            IOUtil.copy(context.getAssets().open("databases/" + dbName), new FileOutputStream(dbPath, false));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
