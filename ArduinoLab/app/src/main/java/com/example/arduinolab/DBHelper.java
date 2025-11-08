package com.example.arduinolab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    // Database metadata
    private static final String DATABASE_NAME = "ArduinoLab.db";
    private static final int DATABASE_VERSION = 1;

    // Table
    public static final String TABLE_TUTORIALS = "tutorials";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMAGE_NAME = "imageName";
    public static final String COLUMN_PIN_CONNECTION = "pinConnection";
    public static final String COLUMN_SAMPLE_CODE = "sampleCode";

    // SQL statement to create the table
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_TUTORIALS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CATEGORY + " TEXT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_IMAGE_NAME + " TEXT," +
                    COLUMN_PIN_CONNECTION + " TEXT, " +
                    COLUMN_SAMPLE_CODE + " TEXT" +
                    ");";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TUTORIALS);
        onCreate(db);
    }

    // --- C.R.U.D. Operations ---

    // CREATE (Insert)
    public void insertTutorial(TutorialItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY, item.getCategory());
        values.put(COLUMN_TITLE, item.getTitle());
        values.put(COLUMN_DESCRIPTION, item.getDescription());
        values.put(COLUMN_IMAGE_NAME, item.getImageName());
        values.put(COLUMN_PIN_CONNECTION, item.getPinConnection());
        values.put(COLUMN_SAMPLE_CODE, item.getSampleCode());

        // Insert the new row
        db.insert(TABLE_TUTORIALS, null, values);
        db.close();
    }

    // READ (Get one item by ID)
    public TutorialItem getTutorial(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TUTORIALS,
                new String[]{COLUMN_ID, COLUMN_CATEGORY, COLUMN_TITLE, COLUMN_DESCRIPTION, COLUMN_IMAGE_NAME, COLUMN_PIN_CONNECTION, COLUMN_SAMPLE_CODE},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            TutorialItem item = new TutorialItem(
                    cursor.getInt(0),      // id
                    cursor.getString(1),   // category
                    cursor.getString(2),   // title
                    cursor.getString(3),   // description
                    cursor.getString(4),    // imageName
                    cursor.getString(5),   // pinConnection
                    cursor.getString(6)    // sampleProgram
            );
            cursor.close();
            db.close();
            return item;
        }
        db.close();
        return null;
    }

    // READ (Get all items for a specific category)
    public ArrayList<TutorialItem> getAllTutorialsByCategory(String category) {
        ArrayList<TutorialItem> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_CATEGORY + "=?";
        String[] selectionArgs = {category};

        Cursor cursor = db.query(TABLE_TUTORIALS, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                TutorialItem item = new TutorialItem(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PIN_CONNECTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SAMPLE_CODE))
                );
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return itemList;
    }

    // UPDATE (Not requested yet, but good to have)
    public int updateTutorial(TutorialItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY, item.getCategory());
        values.put(COLUMN_TITLE, item.getTitle());
        values.put(COLUMN_DESCRIPTION, item.getDescription());
        values.put(COLUMN_IMAGE_NAME, item.getImageName());
        values.put(COLUMN_PIN_CONNECTION, item.getImageName());
        values.put(COLUMN_SAMPLE_CODE, item.getImageName());

        return db.update(TABLE_TUTORIALS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(item.getId())});
    }

    // DELETE
    public void deleteTutorial(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TUTORIALS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
}
