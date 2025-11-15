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
    private static final int DATABASE_VERSION = 4;

    // Table
    public static final String TABLE_TUTORIALS = "tutorials";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMAGE_NAME = "imageName";
    public static final String COLUMN_PIN_CONNECTION = "pinConnection";
    public static final String COLUMN_SAMPLE_CODE = "sampleCode";

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
        populateInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TUTORIALS);
        onCreate(db);
    }

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

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_TUTORIALS + " WHERE " + COLUMN_CATEGORY + " = ?",
                new String[]{ category }
        );

        while (cursor.moveToNext()) {
            TutorialItem item = new TutorialItem(
                    cursor.getInt(0),      // 0 = id
                    cursor.getString(1),   // 1 = category
                    cursor.getString(2),   // 2 = title
                    cursor.getString(3),   // 3 = description
                    cursor.getString(4),   // 4 = imageName
                    cursor.getString(5),   // 5 = pinConnection
                    cursor.getString(6)    // 6 = sampleCode
            );
            itemList.add(item);
        }
        cursor.close();
        db.close();
        return itemList;
    }

    // UPDATE
    public int updateTutorial(TutorialItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY, item.getCategory());
        values.put(COLUMN_TITLE, item.getTitle());
        values.put(COLUMN_DESCRIPTION, item.getDescription());
        values.put(COLUMN_IMAGE_NAME, item.getImageName());
        values.put(COLUMN_PIN_CONNECTION, item.getPinConnection());
        values.put(COLUMN_SAMPLE_CODE, item.getSampleCode());

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

    private void populateInitialData(SQLiteDatabase db) {
        // Tutorial 1: Blink LED
        ContentValues values1 = new ContentValues();
        values1.put(COLUMN_CATEGORY, "LEDs");
        values1.put(COLUMN_TITLE, "Blink an LED");
        values1.put(COLUMN_DESCRIPTION, "The classic 'Hello, World!' of microcontrollers. This tutorial shows you how to make an LED turn on and off repeatedly.");
        values1.put(COLUMN_IMAGE_NAME, "blink_led");
        values1.put(COLUMN_PIN_CONNECTION, "LED Anode (long leg) -> Pin 13\nLED Cathode (short leg) -> GND");
        values1.put(COLUMN_SAMPLE_CODE, "void setup() {\n  pinMode(13, OUTPUT);\n}\n\nvoid loop() {\n  digitalWrite(13, HIGH);\n  delay(1000);\n  digitalWrite(13, LOW);\n  delay(1000);\n}");
        db.insert(TABLE_TUTORIALS, null, values1);

        //  Tutorial 2: Fading LED
        ContentValues values2 = new ContentValues();
        values2.put(COLUMN_CATEGORY, "LEDs");
        values2.put(COLUMN_TITLE, "Fade an LED");
        values2.put(COLUMN_DESCRIPTION, "Learn how to use Pulse Width Modulation (PWM) to make an LED gently fade in and out.");
        values2.put(COLUMN_IMAGE_NAME, "fade_led");
        values2.put(COLUMN_PIN_CONNECTION, "LED Anode (long leg) -> Pin 9 (PWM pin)\nLED Cathode (short leg) -> GND");
        values2.put(COLUMN_SAMPLE_CODE, "int ledPin = 9;\nint brightness = 0;\nint fadeAmount = 5;\n\nvoid setup() {\n  pinMode(ledPin, OUTPUT);\n}\n\nvoid loop() {\n  analogWrite(ledPin, brightness);\n  brightness = brightness + fadeAmount;\n  if (brightness <= 0 || brightness >= 255) {\n    fadeAmount = -fadeAmount;\n  }\n  delay(30);\n}");
        db.insert(TABLE_TUTORIALS, null, values2);

        // Tutorial 3: Button Press
        ContentValues values3 = new ContentValues();
        values3.put(COLUMN_CATEGORY, "Buttons");
        values3.put(COLUMN_TITLE, "Read a Button Press");
        values3.put(COLUMN_DESCRIPTION, "Learn how to use a digital input to read the state of a push-button.");
        values3.put(COLUMN_IMAGE_NAME, "read_button_press");
        values3.put(COLUMN_PIN_CONNECTION, "Button Leg 1 -> Pin 2\nButton Leg 2 -> GND\n(We will use the internal PULLUP resistor)");
        values3.put(COLUMN_SAMPLE_CODE, "const int buttonPin = 2;\nint buttonState = 0;\n\nvoid setup() {\n  Serial.begin(9600);\n  pinMode(buttonPin, INPUT_PULLUP);\n}\n\nvoid loop() {\n  buttonState = digitalRead(buttonPin);\n  if (buttonState == LOW) {\n    Serial.println(\"Button is pressed!\");\n  } else {\n    Serial.println(\"Button is not pressed.\");\n  }\n  delay(100);\n}");
        db.insert(TABLE_TUTORIALS, null, values3);

        //  Project 1: Example Project
//        ContentValues values4 = new ContentValues();
//        values4.put(COLUMN_CATEGORY, "PROJECTS");
//        values4.put(COLUMN_TITLE, "My First Project");
//        values4.put(COLUMN_DESCRIPTION, "This is an example project. You can edit or delete it.");
//        values4.put(COLUMN_IMAGE_NAME, "ic_launcher_background");
//        values4.put(COLUMN_PIN_CONNECTION, "No pin connections defined yet.");
//        values4.put(COLUMN_SAMPLE_CODE, "// No code defined yet.");
//        db.insert(TABLE_TUTORIALS, null, values4);

    }
}
