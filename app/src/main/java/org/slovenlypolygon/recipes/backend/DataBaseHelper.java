package org.slovenlypolygon.recipes.backend;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import org.apache.commons.io.IOUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper extends SQLiteOpenHelper {
    private final Context context;
    private final String DB_PATH;
    private final String DB_NAME;
    private SQLiteDatabase database;

    public DataBaseHelper(Context context) {
        super(context, "global.sqlite3", null, 1);
        this.context = context;

        DB_NAME = super.getDatabaseName();
        DB_PATH = context.getDatabasePath(DB_NAME).getPath();
    }

    public void createDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();

            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }

    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;

        try {
            checkDB = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException ignored) {
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null;
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = context.getAssets().open(DB_NAME);
        OutputStream myOutput = new FileOutputStream(DB_PATH);
        IOUtil.copy(myInput, myOutput);
    }

    public SQLiteDatabase openDataBase() {
        database = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
        return database;
    }

    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}