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
    private static final int DATABASE_VERSION = 7;

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
        // Tutorial 1: Arduino Introduction
        ContentValues values1 = new ContentValues();
        values1.put(COLUMN_CATEGORY, "Basic Programming");
        values1.put(COLUMN_TITLE, "Arduino Introduction");
        values1.put(COLUMN_DESCRIPTION,
                "A high-level introduction to Arduino: what an Arduino board is, MCU basics, digital vs analog pins, "
                        + "power rails, common modules (sensors/actuators), and how sketches run on the board. "
                        + "Covers safety (power limits), recommended tools, and how to approach prototyping.");
        values1.put(COLUMN_IMAGE_NAME, "arduino_introduction");
        values1.put(COLUMN_PIN_CONNECTION,
                "No specific wiring required for this conceptual tutorial.\n"
                        + "Notes:\n"
                        + " - Power: Use 5V from USB or regulated 5V supply (do not exceed Vcc ratings).\n"
                        + " - Ground: Always connect sensor GND to Arduino GND when using external power.\n"
                        + " - Protect outputs with resistors (e.g., 220Ω for LEDs) and avoid sourcing >20-40mA per pin.");
        values1.put(COLUMN_SAMPLE_CODE,
                "/* Minimal skeleton sketch to demonstrate structure */\n"
                        + "void setup() {\n"
                        + "  // runs once: initialization, pinMode, serial begin\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  // runs repeatedly: main logic\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, values1);

        //  Tutorial 2: Using the Arduino IDE
        ContentValues values2 = new ContentValues();
        values2.put(COLUMN_CATEGORY, "Basic Programming");
        values2.put(COLUMN_TITLE, "Using the Arduino IDE");
        values2.put(COLUMN_DESCRIPTION,
                "Step-by-step guide to installing and configuring the Arduino IDE (or Arduino CLI):\n"
                        + " - Selecting the correct board and port\n"
                        + " - Installing libraries via Library Manager\n"
                        + " - Compiling and uploading sketches\n"
                        + " - Using the Serial Monitor and Serial Plotter for debugging.");
        values2.put(COLUMN_IMAGE_NAME, "using_the_arduino_ide");
        values2.put(COLUMN_PIN_CONNECTION,
                "No hardware required — IDE configuration tutorial.\n"
                        + "Notes:\n"
                        + " - Ensure the USB driver for your board (e.g., CH340/FTDI) is installed when needed.\n"
                        + " - For Leonardo/Micro use the correct COM port after uploading (auto-reset behavior).");
        values2.put(COLUMN_SAMPLE_CODE,
                "// Verify IDE and serial: prints a heartbeat message\n"
                        + "void setup() {\n"
                        + "  Serial.begin(9600);\n"
                        + "  while (!Serial) { /* wait for Serial on Leonardo/Micro */ }\n"
                        + "  Serial.println(\"Arduino IDE configured correctly.\");\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  Serial.println(millis());\n"
                        + "  delay(1000);\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, values2);

        // Tutorial 3: Your First Program (Blink)
        ContentValues values3 = new ContentValues();
        values3.put(COLUMN_CATEGORY, "Basic Programming");
        values3.put(COLUMN_TITLE, "Your First Program (Blink)");
        values3.put(COLUMN_DESCRIPTION,
                "Hands-on first hardware program: blink an LED connected to a digital pin. "
                        + "Explains pinMode, digitalWrite, delays, and protecting the LED with a resistor. "
                        + "Teaches how upload/verify and observe physical behavior.");
        values3.put(COLUMN_IMAGE_NAME, "blink_led");
        values3.put(COLUMN_PIN_CONNECTION,
                "Wiring:\n"
                        + " - LED Anode (long leg) → Digital Pin 13 (or any digital pin)\n"
                        + " - LED Cathode (short leg) → GND\n"
                        + " - Place a 220Ω resistor in series with the LED anode for protection.\n"
                        + "Notes:\n"
                        + " - If using a breadboard, ensure common ground with Arduino.\n"
                        + " - Pin 13 on some boards has a built-in LED, making it ideal for first tests.");
        values3.put(COLUMN_SAMPLE_CODE,
                "void setup() {\n"
                        + "  pinMode(13, OUTPUT); // configure pin 13 as output\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  digitalWrite(13, HIGH); // LED on\n"
                        + "  delay(1000);            // wait 1 second\n"
                        + "  digitalWrite(13, LOW);  // LED off\n"
                        + "  delay(1000);            // wait 1 second\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, values3);

        // Tutorial 4: Functions: setup(), loop() and Custom Functions
        ContentValues values4 = new ContentValues();
        values4.put(COLUMN_CATEGORY, "Basic Programming");
        values4.put(COLUMN_TITLE, "Functions: setup(), loop() and Custom Functions");
        values4.put(COLUMN_DESCRIPTION,
                "Explains the role of setup() and loop(), how functions organize code, parameter passing, return values, and scope. "
                        + "Shows practical use of helper functions to keep code modular and readable.");
        values4.put(COLUMN_IMAGE_NAME, "functions_setup_loop");
        values4.put(COLUMN_PIN_CONNECTION,
                "Example uses LED on a pin — wiring:\n"
                        + " - LED (+) → Pin 8 via 220Ω resistor\n"
                        + " - LED (GND) → GND\n"
                        + "Notes:\n"
                        + " - Keep helper functions short and single-responsibility.\n"
                        + " - Use descriptive names and comments for clarity.");
        values4.put(COLUMN_SAMPLE_CODE,
                "const int LED_PIN = 8;\n\n"
                        + "void blink(int pin, unsigned long ms) {\n"
                        + "  digitalWrite(pin, HIGH);\n"
                        + "  delay(ms);\n"
                        + "  digitalWrite(pin, LOW);\n"
                        + "  delay(ms);\n"
                        + "}\n\n"
                        + "void setup() {\n"
                        + "  pinMode(LED_PIN, OUTPUT);\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  blink(LED_PIN, 300); // use helper function\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, values4);

        // Tutorial 5: Curly Braces, Blocks and Scoping
        ContentValues values5 = new ContentValues();
        values5.put(COLUMN_CATEGORY, "Basic Programming");
        values5.put(COLUMN_TITLE, "Curly Braces, Blocks and Scoping");
        values5.put(COLUMN_DESCRIPTION,
                "Teaches how curly braces ({}) define blocks in functions, control structures, and local scope. "
                        + "Explains variable lifetime: local vs global, and common scoping mistakes to avoid.");
        values5.put(COLUMN_IMAGE_NAME, "curly_braces_scope");
        values5.put(COLUMN_PIN_CONNECTION,
                "No special wiring required — conceptual plus small LED example:\n"
                        + " - LED (+) → Pin 12 via 220Ω resistor\n"
                        + " - LED (–) → GND");
        values5.put(COLUMN_SAMPLE_CODE,
                "int counter = 0; // global\n\n"
                        + "void setup() {\n"
                        + "  pinMode(12, OUTPUT);\n"
                        + "  Serial.begin(9600);\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  {\n"
                        + "    int counter = 5; // local to this block, shadows global counter\n"
                        + "    Serial.print(\"Inner counter: \");\n"
                        + "    Serial.println(counter);\n"
                        + "  }\n"
                        + "  // outer counter remains unchanged\n"
                        + "  Serial.print(\"Global counter: \");\n"
                        + "  Serial.println(counter);\n"
                        + "  counter++;\n"
                        + "  digitalWrite(12, counter % 2);\n"
                        + "  delay(1000);\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, values5);

        // Tutorial 6: statements & Instructions (Syntax Basics)
        ContentValues values6 = new ContentValues();
        values6.put(COLUMN_CATEGORY, "Basic Programming");
        values6.put(COLUMN_TITLE, "Statements & Instructions (Syntax Basics)");
        values6.put(COLUMN_DESCRIPTION,
                "Covers statement termination with semicolons, expression evaluation, operator precedence, and how to structure clear, maintainable statements. "
                        + "Also discusses common syntax errors and how to interpret compiler messages.");
        values6.put(COLUMN_IMAGE_NAME, "statements_syntax");
        values6.put(COLUMN_PIN_CONNECTION,
                "No hardware required. Example uses Serial output for demonstration.\n"
                        + "Notes:\n"
                        + " - Keep one statement per line for readability.\n"
                        + " - Use parentheses to disambiguate complex expressions.");
        values6.put(COLUMN_SAMPLE_CODE,
                "void setup() {\n"
                        + "  Serial.begin(9600);\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  int a = 5; int b = 3; // multiple statements\n"
                        + "  int c = (a + b) * 2; // use parentheses to control order\n"
                        + "  Serial.print(\"c = \");\n"
                        + "  Serial.println(c);\n"
                        + "  delay(1000);\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, values6);

        // Tutorial 7: Comments: Single-line and Multi-line
        ContentValues values7 = new ContentValues();
        values7.put(COLUMN_CATEGORY, "Basic Programming");
        values7.put(COLUMN_TITLE, "Comments: Single-line and Multi-line");
        values7.put(COLUMN_DESCRIPTION,
                "Shows how to use // single-line and /* multi-line */ comments effectively to document intent, explain tricky logic, "
                        + "and temporarily disable code. Gives best practices for meaningful comments and avoiding redundancy.");
        values7.put(COLUMN_IMAGE_NAME, "comments_documentation");
        values7.put(COLUMN_PIN_CONNECTION,
                "No hardware required. Comments only affect source readability.");
        values7.put(COLUMN_SAMPLE_CODE,
                "// Single-line comment example\n"
                        + "/*\n"
                        + "  Multi-line comment example\n"
                        + "  Use to explain algorithm or pin mappings.\n"
                        + "*/\n\n"
                        + "void setup() {\n"
                        + "  Serial.begin(9600);\n"
                        + "  Serial.println(\"Comments example\");\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  // nothing to do repeatedly\n"
                        + "  delay(1000);\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, values7);

        // Tutorial 8: Data Types: int, float, char, boolean, unsigned
        ContentValues values8 = new ContentValues();
        values8.put(COLUMN_CATEGORY, "Basic Programming");
        values8.put(COLUMN_TITLE, "Data Types: int, float, char, boolean, unsigned");
        values8.put(COLUMN_DESCRIPTION,
                "Detailed overview of common Arduino data types, their size, range, and good use-cases:\n"
                        + " - byte/uint8_t for small positive integers\n"
                        + " - int (16-bit on AVR) and long for larger integers\n"
                        + " - float for decimals (single precision)\n"
                        + " - char for characters\n"
                        + " - bool/boolean for true/false flags\n"
                        + "Discusses memory considerations on small microcontrollers.");
        values8.put(COLUMN_IMAGE_NAME, "data_types_overview");
        values8.put(COLUMN_PIN_CONNECTION,
                "No specific wiring required; example prints values over Serial.\n"
                        + "Notes:\n"
                        + " - Avoid large arrays of floats on AVR boards due to RAM limits.\n"
                        + " - Use appropriate types for sensor ranges to save memory.");
        values8.put(COLUMN_SAMPLE_CODE,
                "int i = -123;              // integer\n"
                        + "unsigned int u = 50000;    // unsigned (range matters per MCU)\n"
                        + "long l = 1234567890L;      // large integer\n"
                        + "float f = 3.14159;         // decimal\n"
                        + "char c = 'A';              // single character\n"
                        + "bool flag = true;          // boolean\n\n"
                        + "void setup() {\n"
                        + "  Serial.begin(9600);\n"
                        + "  Serial.println(i);\n"
                        + "  Serial.println(u);\n"
                        + "  Serial.println(l);\n"
                        + "  Serial.println(f);\n"
                        + "  Serial.println(c);\n"
                        + "  Serial.println(flag);\n"
                        + "}\n\n"
                        + "void loop() {}\n");
        db.insert(TABLE_TUTORIALS, null, values8);

        // Tutorial 9: Variable Declaration & Scope
        ContentValues values9 = new ContentValues();
        values9.put(COLUMN_CATEGORY, "Basic Programming");
        values9.put(COLUMN_TITLE, "Variable Declaration & Scope");
        values9.put(COLUMN_DESCRIPTION,
                "Explains how and where to declare variables (global vs local), initialization, const vs mutable, and using PROGMEM for constants. "
                        + "Covers best practices: meaningful names, const correctness, and avoiding large globals.");
        values9.put(COLUMN_IMAGE_NAME, "variable_declaration_scope");
        values9.put(COLUMN_PIN_CONNECTION,
                "No wiring required for examples. Example uses LED on pin 6 to show a changing global variable effect.\n"
                        + "Wiring:\n"
                        + " - LED (+) → Pin 6 via 220Ω resistor\n"
                        + " - LED (–) → GND");
        values9.put(COLUMN_SAMPLE_CODE,
                "int blinkInterval = 500; // global variable\n\n"
                        + "void setup() {\n"
                        + "  pinMode(6, OUTPUT);\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  toggleWithLocal();\n"
                        + "  delay(blinkInterval);\n"
                        + "}\n\n"
                        + "void toggleWithLocal() {\n"
                        + "  int tempCounter = 0; // local to this function\n"
                        + "  digitalWrite(6, HIGH);\n"
                        + "  delay(100);\n"
                        + "  digitalWrite(6, LOW);\n"
                        + "  tempCounter++; // disappears after function returns\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, values9);

        // Tutorial 10: Keywords and Control Structures (if, for, while, switch
        ContentValues values10 = new ContentValues();
        values10.put(COLUMN_CATEGORY, "Basic Programming");
        values10.put(COLUMN_TITLE, "Keywords and Control Structures (if, for, while, switch)");
        values10.put(COLUMN_DESCRIPTION,
                "Covers common control keywords: if/else, switch/case, for loops, while/do-while, break/continue, and return. "
                        + "Shows practical patterns for finite loops, event polls, and state machines.");
        values10.put(COLUMN_IMAGE_NAME, "control_structures");
        values10.put(COLUMN_PIN_CONNECTION,
                "Example uses a single LED for visual loop feedback:\n"
                        + " - LED (+) → Pin 7 via 220Ω resistor\n"
                        + " - LED (–) → GND");
        values10.put(COLUMN_SAMPLE_CODE,
                "void setup() {\n"
                        + "  pinMode(7, OUTPUT);\n"
                        + "  Serial.begin(9600);\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  for (int i = 0; i < 3; i++) {\n"
                        + "    digitalWrite(7, HIGH);\n"
                        + "    delay(150);\n"
                        + "    digitalWrite(7, LOW);\n"
                        + "    delay(150);\n"
                        + "  }\n"
                        + "  int x = 10;\n"
                        + "  if (x > 5) {\n"
                        + "    Serial.println(\"x is greater than 5\");\n"
                        + "  } else {\n"
                        + "    Serial.println(\"x is 5 or less\");\n"
                        + "  }\n"
                        + "  delay(1000);\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, values10);

        // Tutorial 11: Arithmetic Operations and Sensor Calculations
        ContentValues values11 = new ContentValues();
        values11.put(COLUMN_CATEGORY, "Basic Programming");
        values11.put(COLUMN_TITLE, "Arithmetic Operations and Sensor Calculations");
        values11.put(COLUMN_DESCRIPTION,
                "Explains arithmetic operators (+, -, *, /, %), order of operations, type casting, and common sensor-related calculations "
                        + "(e.g., converting ADC units to voltages, averaging, scaling).");
        values11.put(COLUMN_IMAGE_NAME, "arithmetic_sensor_calculations");
        values11.put(COLUMN_PIN_CONNECTION,
                "Example: use PWM-capable pin for brightness control and a mock sensor on A0.\n"
                        + "Wiring:\n"
                        + " - LED (+) → Pin 9 via 220Ω resistor (PWM)\n"
                        + " - LED (–) → GND\n"
                        + " - (Optional) Potentiometer middle → A0, ends → 5V and GND for variable input.");
        values11.put(COLUMN_SAMPLE_CODE,
                "const int ledPin = 9;\n"
                        + "const int sensorPin = A0;\n\n"
                        + "void setup() {\n"
                        + "  pinMode(ledPin, OUTPUT);\n"
                        + "  Serial.begin(9600);\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  int raw = analogRead(sensorPin); // 0..1023\n"
                        + "  float voltage = (raw / 1023.0) * 5.0; // convert to volts\n"
                        + "  int pwm = (int)((voltage / 5.0) * 255); // scale to 0..255\n"
                        + "  analogWrite(ledPin, pwm);\n"
                        + "  Serial.print(\"Raw:\"); Serial.print(raw);\n"
                        + "  Serial.print(\" V:\"); Serial.println(voltage);\n"
                        + "  delay(200);\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, values11);

        // Tutorial 12: Loops: for, while, do-while (Practical Examples
        ContentValues values12 = new ContentValues();
        values12.put(COLUMN_CATEGORY, "Basic Programming");
        values12.put(COLUMN_TITLE, "Loops: for, while, do-while (Practical Examples)");
        values12.put(COLUMN_DESCRIPTION,
                "Deep dive into loops with practical examples: repeat actions a fixed number of times (for), poll until a condition (while), "
                        + "and guarantee at least one execution (do-while). Demonstrates timing considerations and avoiding blocking where possible.");
        values12.put(COLUMN_IMAGE_NAME, "loops_examples");
        values12.put(COLUMN_PIN_CONNECTION,
                "Example blinks a sequence of LEDs: pins 4-7 used. Wiring:\n"
                        + " - LEDs (+) → Pins 4,5,6,7 each via 220Ω resistor\n"
                        + " - LEDs (–) → GND");
        values12.put(COLUMN_SAMPLE_CODE,
                "int leds[] = {4,5,6,7};\n\n"
                        + "void setup() {\n"
                        + "  for (int i = 0; i < 4; i++) pinMode(leds[i], OUTPUT);\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  // for loop: sequential chase\n"
                        + "  for (int i = 0; i < 4; i++) {\n"
                        + "    digitalWrite(leds[i], HIGH);\n"
                        + "    delay(100);\n"
                        + "    digitalWrite(leds[i], LOW);\n"
                        + "  }\n"
                        + "  // while loop: blink first LED 3 times\n"
                        + "  int count = 0;\n"
                        + "  while (count < 3) {\n"
                        + "    digitalWrite(leds[0], HIGH);\n"
                        + "    delay(150);\n"
                        + "    digitalWrite(leds[0], LOW);\n"
                        + "    delay(150);\n"
                        + "    count++;\n"
                        + "  }\n"
                        + "  // do-while: ensure at least one blink\n"
                        + "  int n = 0;\n"
                        + "  do {\n"
                        + "    digitalWrite(leds[1], HIGH);\n"
                        + "    delay(80);\n"
                        + "    digitalWrite(leds[1], LOW);\n"
                        + "    delay(80);\n"
                        + "    n++;\n"
                        + "  } while (n < 2);\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, values12);

        // Tutorial 13: Arrays: Storing Multiple Values (LED Control Example
        ContentValues values13 = new ContentValues();
        values13.put(COLUMN_CATEGORY, "Basic Programming");
        values13.put(COLUMN_TITLE, "Arrays: Storing Multiple Values (LED Control Example)");
        values13.put(COLUMN_DESCRIPTION,
                "Shows how to declare and use arrays to manage multiple related pins or sensor values. "
                        + "Includes index handling, bounds cautions, and using arrays to simplify repetitive tasks such as LED sequences.");
        values13.put(COLUMN_IMAGE_NAME, "arrays_led_control");
        values13.put(COLUMN_PIN_CONNECTION,
                "Wiring for 4 LEDs using pins 2..5:\n"
                        + " - LED1 (+) → Pin 2 via 220Ω\n"
                        + " - LED2 (+) → Pin 3 via 220Ω\n"
                        + " - LED3 (+) → Pin 4 via 220Ω\n"
                        + " - LED4 (+) → Pin 5 via 220Ω\n"
                        + " - All LED cathodes → GND\n"
                        + "Notes:\n"
                        + " - Use current-limiting resistors for each LED.\n"
                        + " - Ensure total current draw from the Arduino board does not exceed safe limits.");
        values13.put(COLUMN_SAMPLE_CODE,
                "int leds[] = {2, 3, 4, 5};\n\n"
                        + "void setup() {\n"
                        + "  for (int i = 0; i < 4; i++) pinMode(leds[i], OUTPUT);\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  // light each LED in sequence\n"
                        + "  for (int i = 0; i < 4; i++) {\n"
                        + "    digitalWrite(leds[i], HIGH);\n"
                        + "    delay(200);\n"
                        + "    digitalWrite(leds[i], LOW);\n"
                        + "  }\n"
                        + "  // reverse sequence\n"
                        + "  for (int i = 3; i >= 0; i--) {\n"
                        + "    digitalWrite(leds[i], HIGH);\n"
                        + "    delay(150);\n"
                        + "    digitalWrite(leds[i], LOW);\n"
                        + "  }\n"
                        + "  delay(300);\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, values13);

        // Tutorial 14: Understanding Arithmetic Operations
        ContentValues values14 = new ContentValues();
        values14.put(COLUMN_CATEGORY, "Basic Programming");
        values14.put(COLUMN_TITLE, "Understanding Arithmetic Operations");
        values14.put(COLUMN_DESCRIPTION,
                "This tutorial explains how arithmetic operators (+, -, *, /, %) work in Arduino programming. "
                        + "These operations are used in calculations, sensor data processing, LED brightness control, "
                        + "and many other tasks. You will learn how to perform basic math operations and view the result "
                        + "through the Serial Monitor.");

        values14.put(COLUMN_IMAGE_NAME, "arithmetic_operations");
        values14.put(COLUMN_PIN_CONNECTION,
                "No hardware components required.\n"
                        + "This is a pure programming concept tutorial.\n"
                        + "You only need Arduino + USB cable + Serial Monitor.");

        values14.put(COLUMN_SAMPLE_CODE,
                "int a = 20;\n"
                        + "int b = 6;\n\n"
                        + "void setup() {\n"
                        + "  Serial.begin(9600);\n"
                        + "  Serial.print(\"Addition (a+b): \"); Serial.println(a + b);\n"
                        + "  Serial.print(\"Subtraction (a-b): \"); Serial.println(a - b);\n"
                        + "  Serial.print(\"Multiplication (a*b): \"); Serial.println(a * b);\n"
                        + "  Serial.print(\"Division (a/b): \"); Serial.println(a / b);\n"
                        + "  Serial.print(\"Modulo (a%b): \"); Serial.println(a % b);\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  // Nothing here\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, values14);

        // Tutorial 15: Using Loops in Arduino
        ContentValues values15 = new ContentValues();
        values15.put(COLUMN_CATEGORY, "Basic Programming");
        values15.put(COLUMN_TITLE, "Using Loops in Arduino");
        values15.put(COLUMN_DESCRIPTION,
                "Loops allow you to repeat instructions automatically. This tutorial demonstrates how to use "
                        + "for-loops, while-loops, and do-while-loops to control LED blinking patterns. "
                        + "Understanding loops is essential for controlling motors, LEDs, sensors, and repetitive tasks.");

        values15.put(COLUMN_IMAGE_NAME, "loops_control_led");
        values15.put(COLUMN_PIN_CONNECTION,
                "LED (+) → Pin 8\n"
                        + "LED (–) → GND\n"
                        + "Use a 220Ω current-limiting resistor in series with the LED.");

        values15.put(COLUMN_SAMPLE_CODE,
                "int led = 8;\n\n"
                        + "void setup() {\n"
                        + "  pinMode(led, OUTPUT);\n"
                        + "  Serial.begin(9600);\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  Serial.println(\"FOR LOOP BLINK\");\n"
                        + "  for (int i = 0; i < 5; i++) {\n"
                        + "    digitalWrite(led, HIGH);\n"
                        + "    delay(300);\n"
                        + "    digitalWrite(led, LOW);\n"
                        + "    delay(300);\n"
                        + "  }\n\n"
                        + "  Serial.println(\"WHILE LOOP BLINK\");\n"
                        + "  int count = 0;\n"
                        + "  while (count < 5) {\n"
                        + "    digitalWrite(led, HIGH);\n"
                        + "    delay(200);\n"
                        + "    digitalWrite(led, LOW);\n"
                        + "    delay(200);\n"
                        + "    count++;\n"
                        + "  }\n\n"
                        + "  Serial.println(\"DO-WHILE LOOP BLINK\");\n"
                        + "  int j = 0;\n"
                        + "  do {\n"
                        + "    digitalWrite(led, HIGH);\n"
                        + "    delay(150);\n"
                        + "    digitalWrite(led, LOW);\n"
                        + "    delay(150);\n"
                        + "    j++;\n"
                        + "  } while (j < 5);\n\n"
                        + "  delay(1000);\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, values15);

        // Tutorial 16: Working With Arrays
        ContentValues values16 = new ContentValues();
        values16.put(COLUMN_CATEGORY, "Basic Programming");
        values16.put(COLUMN_TITLE, "Working With Arrays");
        values16.put(COLUMN_DESCRIPTION,
                "Arrays allow you to store multiple values under one variable name. "
                        + "In Arduino, arrays are commonly used to control multiple LEDs, store sensor readings, "
                        + "or manage servo positions. This tutorial teaches how to declare arrays and loop through them "
                        + "to create a running LED effect.");

        values16.put(COLUMN_IMAGE_NAME, "led_array_chasing");
        values16.put(COLUMN_PIN_CONNECTION,
                "LED1 (+) → Pin 4 | LED1 (–) → GND\n"
                        + "LED2 (+) → Pin 5 | LED2 (–) → GND\n"
                        + "LED3 (+) → Pin 6 | LED3 (–) → GND\n"
                        + "LED4 (+) → Pin 7 | LED4 (–) → GND\n"
                        + "Use 220Ω resistors on each LED.\n"
                        + "This creates a 4-LED chasing pattern example.");

        values16.put(COLUMN_SAMPLE_CODE,
                "int leds[] = {4, 5, 6, 7};\n"
                        + "int total = 4;\n\n"
                        + "void setup() {\n"
                        + "  for (int i = 0; i < total; i++) {\n"
                        + "    pinMode(leds[i], OUTPUT);\n"
                        + "  }\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  for (int i = 0; i < total; i++) {\n"
                        + "    digitalWrite(leds[i], HIGH);\n"
                        + "    delay(200);\n"
                        + "    digitalWrite(leds[i], LOW);\n"
                        + "  }\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, values16);

        // Tutorial 17: Understanding Arduino Keywords
        ContentValues values17 = new ContentValues();
        values17.put(COLUMN_CATEGORY, "Basic Programming");
        values17.put(COLUMN_TITLE, "Understanding Arduino Keywords");
        values17.put(COLUMN_DESCRIPTION,
                "This tutorial introduces important Arduino keywords such as pinMode, digitalWrite, delay, "
                        + "HIGH/LOW, INPUT/OUTPUT, const, and return. Understanding keywords helps you read and write "
                        + "Arduino programs effectively. Examples are printed on the Serial Monitor.");

        values17.put(COLUMN_IMAGE_NAME, "arduino_keywords");
        values17.put(COLUMN_PIN_CONNECTION,
                "No external components required.\n"
                        + "Demonstration uses only Serial Monitor.");

        values17.put(COLUMN_SAMPLE_CODE,
                "void setup() {\n"
                        + "  Serial.begin(9600);\n"
                        + "  const int pin = 10;\n"
                        + "  pinMode(pin, OUTPUT);\n"
                        + "  Serial.println(\"Keywords demonstration running...\");\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  digitalWrite(10, HIGH);\n"
                        + "  delay(500);\n"
                        + "  digitalWrite(10, LOW);\n"
                        + "  delay(500);\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, values17);

        // Tutorial 18: Simple Serial Print
        ContentValues values18 = new ContentValues();
        values18.put(COLUMN_CATEGORY, "Serial Port");
        values18.put(COLUMN_TITLE, "Simple Serial Print");
        values18.put(COLUMN_DESCRIPTION,
                "This tutorial introduces the Serial Monitor, one of the most important tools for debugging Arduino "
                        + "programs. You will learn how to send text, numbers, and sensor values from Arduino to your computer "
                        + "using Serial.print() and Serial.println(). This is the foundation of serial communication, useful "
                        + "for testing sensors, reading data, and understanding program behavior.");

        values18.put(COLUMN_IMAGE_NAME, "serial_print_basic");
        values18.put(COLUMN_PIN_CONNECTION,
                "No external hardware required.\n"
                        + "Only Arduino + USB Cable + Serial Monitor.");

        values18.put(COLUMN_SAMPLE_CODE,
                "void setup() {\n"
                        + "  Serial.begin(9600);          // Start serial communication\n"
                        + "  Serial.println(\"Serial Print Test Started!\");\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  Serial.print(\"Current Time (ms): \");\n"
                        + "  Serial.println(millis());     // Print time since Arduino started\n"
                        + "  delay(1000);\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, values18);

        // Tutorial 19: Reading Data From Serial Monitor
        ContentValues values19 = new ContentValues();
        values19.put(COLUMN_CATEGORY, "Serial Port");
        values19.put(COLUMN_TITLE, "Reading Data From Serial Monitor");
        values19.put(COLUMN_DESCRIPTION,
                "This tutorial explains how to read user input from the Serial Monitor. By typing commands or numbers "
                        + "into the Serial Monitor, you can control Arduino behavior. This is useful for configuration, testing, "
                        + "manual command input, and interactive projects.");

        values19.put(COLUMN_IMAGE_NAME, "serial_read_input");
        values19.put(COLUMN_PIN_CONNECTION,
                "No external hardware required.\n"
                        + "Open Serial Monitor → Set baud rate to 9600 → Set mode to 'No Line Ending' or 'Newline'.");

        values19.put(COLUMN_SAMPLE_CODE,
                "String inputData = \"\";\n\n"
                        + "void setup() {\n"
                        + "  Serial.begin(9600);\n"
                        + "  Serial.println(\"Type something and press Enter...\");\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  if (Serial.available() > 0) {   // Check if data is available\n"
                        + "    inputData = Serial.readString();\n"
                        + "    Serial.print(\"You entered: \");\n"
                        + "    Serial.println(inputData);\n"
                        + "  }\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, values19);

        // Tutorial 20: Control LED Using Serial Commands
        ContentValues values20 = new ContentValues();
        values20.put(COLUMN_CATEGORY, "Serial Port");
        values20.put(COLUMN_TITLE, "Control LED Using Serial Commands");
        values20.put(COLUMN_DESCRIPTION,
                "This tutorial demonstrates how to control an LED using commands sent from the Serial Monitor. "
                        + "You will type ON or OFF, and Arduino will turn the LED on or off accordingly. This method is very "
                        + "useful for debugging, remote controlling hardware, or creating command-based interfaces.");

        values20.put(COLUMN_IMAGE_NAME, "serial_led_control");
        values20.put(COLUMN_PIN_CONNECTION,
                "LED (+) → Pin 8\n"
                        + "LED (–) → GND\n"
                        + "Use a 220Ω resistor to protect the LED.\n"
                        + "Commands to type in Serial Monitor:\n"
                        + "  • ON → Turn LED On\n"
                        + "  • OFF → Turn LED Off");

        values20.put(COLUMN_SAMPLE_CODE,
                "int led = 8;\n"
                        + "String cmd = \"\";\n\n"
                        + "void setup() {\n"
                        + "  Serial.begin(9600);\n"
                        + "  pinMode(led, OUTPUT);\n"
                        + "  Serial.println(\"Type ON or OFF to control the LED.\");\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  if (Serial.available() > 0) {\n"
                        + "    cmd = Serial.readString();\n"
                        + "    cmd.trim();  // Remove spaces/newline\n\n"
                        + "    if (cmd == \"ON\") {\n"
                        + "      digitalWrite(led, HIGH);\n"
                        + "      Serial.println(\"LED Turned ON\");\n"
                        + "    }\n"
                        + "    else if (cmd == \"OFF\") {\n"
                        + "      digitalWrite(led, LOW);\n"
                        + "      Serial.println(\"LED Turned OFF\");\n"
                        + "    }\n"
                        + "    else {\n"
                        + "      Serial.println(\"Invalid Command. Type ON or OFF.\");\n"
                        + "    }\n"
                        + "  }\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, values20);

        // Tutorial 21: Built-in LED
        ContentValues values21 = new ContentValues();
        values21.put(COLUMN_CATEGORY, "LEDs");
        values21.put(COLUMN_TITLE, "Built-in LED");
        values21.put(COLUMN_DESCRIPTION,
                "Use the board's built-in LED to verify uploads and learn basic digital output control. "
                        + "Shows how to toggle a pin and observe timing behavior.");
        values21.put(COLUMN_IMAGE_NAME, "led_builtin");
        values21.put(COLUMN_PIN_CONNECTION,
                "Using onboard LED (no external wiring required).\n"
                        + "Notes:\n"
                        + " - On many Arduino boards the built-in LED is attached to digital pin 13.\n"
                        + " - No external resistor required for the onboard LED.");
        values21.put(COLUMN_SAMPLE_CODE,
                "void setup() {\n"
                        + "  pinMode(13, OUTPUT);\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  digitalWrite(13, HIGH);\n"
                        + "  delay(500);\n"
                        + "  digitalWrite(13, LOW);\n"
                        + "  delay(500);\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, values21);

        // Tutorial 22: LED Control via Serial Monitor
        ContentValues values22 = new ContentValues();
        values22.put(COLUMN_CATEGORY, "LEDs");
        values22.put(COLUMN_TITLE, "LED Control via Serial Monitor");
        values22.put(COLUMN_DESCRIPTION,
                "Control an external LED from the Serial Monitor using simple text commands. "
                        + "Demonstrates serial parsing and mapping commands to hardware actions.");
        values22.put(COLUMN_IMAGE_NAME, "led_serial_control");
        values22.put(COLUMN_PIN_CONNECTION,
                "LED (+) -> Digital Pin 8 via 220Ω resistor\n"
                        + "LED (−) -> GND\n"
                        + "Serial Monitor settings: 9600 baud, send newline or carriage return.");
        values22.put(COLUMN_SAMPLE_CODE,
                "int ledPin = 8;\n"
                        + "String cmd = \"\";\n\n"
                        + "void setup() {\n"
                        + "  Serial.begin(9600);\n"
                        + "  pinMode(ledPin, OUTPUT);\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  if (Serial.available() > 0) {\n"
                        + "    cmd = Serial.readString();\n"
                        + "    cmd.trim();\n"
                        + "    if (cmd == \"ON\") {\n"
                        + "      digitalWrite(ledPin, HIGH);\n"
                        + "    } else if (cmd == \"OFF\") {\n"
                        + "      digitalWrite(ledPin, LOW);\n"
                        + "    }\n"
                        + "  }\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, values22);

        // Tutorial 23: Controlling Multiple LEDs with an Array
        ContentValues values23 = new ContentValues();
        values23.put(COLUMN_CATEGORY, "LEDs");
        values23.put(COLUMN_TITLE, "Controlling Multiple LEDs with an Array");
        values23.put(COLUMN_DESCRIPTION,
                "Use an integer array to control multiple LEDs in a sequence. "
                        + "Illustrates indexing, loop-based control, and reversing sequences.");
        values23.put(COLUMN_IMAGE_NAME, "led_array_control");
        values23.put(COLUMN_PIN_CONNECTION,
                "LED1 (+) -> Pin 4 via 220Ω\n"
                        + "LED2 (+) -> Pin 5 via 220Ω\n"
                        + "LED3 (+) -> Pin 6 via 220Ω\n"
                        + "LED4 (+) -> Pin 7 via 220Ω\n"
                        + "All LED cathodes -> GND");
        values23.put(COLUMN_SAMPLE_CODE,
                "int leds[] = {4, 5, 6, 7};\n\n"
                        + "void setup() {\n"
                        + "  for (int i = 0; i < 4; i++) pinMode(leds[i], OUTPUT);\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  for (int i = 0; i < 4; i++) {\n"
                        + "    digitalWrite(leds[i], HIGH);\n"
                        + "    delay(150);\n"
                        + "    digitalWrite(leds[i], LOW);\n"
                        + "  }\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, values23);

        // Tutorial 24: Turn LED On/Off with Button
        ContentValues values24 = new ContentValues();
        values24.put(COLUMN_CATEGORY, "Buttons");
        values24.put(COLUMN_TITLE, "Turn LED On/Off with Button");
        values24.put(COLUMN_DESCRIPTION,
                "Interface a push-button to toggle an LED state. Covers wiring for pull-down or internal pull-up, debouncing basics, and reading digital inputs.");
        values24.put(COLUMN_IMAGE_NAME, "button_toggle_led");
        values24.put(COLUMN_PIN_CONNECTION,
                "Button leg A -> Digital Pin 2\n"
                        + "Button leg B -> GND\n"
                        + "LED (+) -> Pin 13 via 220Ω\n"
                        + "LED (−) -> GND\n"
                        + "Notes:\n"
                        + " - Uses INPUT_PULLUP; button connects pin to GND when pressed.");
        values24.put(COLUMN_SAMPLE_CODE,
                "const int buttonPin = 2;\n"
                        + "const int ledPin = 13;\n"
                        + "int buttonState = HIGH;\n\n"
                        + "void setup() {\n"
                        + "  pinMode(buttonPin, INPUT_PULLUP);\n"
                        + "  pinMode(ledPin, OUTPUT);\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  int reading = digitalRead(buttonPin);\n"
                        + "  if (reading == LOW && buttonState == HIGH) {\n"
                        + "    digitalWrite(ledPin, !digitalRead(ledPin));\n"
                        + "  }\n"
                        + "  buttonState = reading;\n"
                        + "  delay(50);\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, values24);

        // Tutorial 25: Button Press Counter
        ContentValues values25 = new ContentValues();
        values25.put(COLUMN_CATEGORY, "Buttons");
        values25.put(COLUMN_TITLE, "Button Press Counter");
        values25.put(COLUMN_DESCRIPTION,
                "Count button presses and display the count on the Serial Monitor. Demonstrates debouncing and persistent counting during run-time.");
        values25.put(COLUMN_IMAGE_NAME, "button_press_counter");
        values25.put(COLUMN_PIN_CONNECTION,
                "Button leg A -> Digital Pin 3\n"
                        + "Button leg B -> GND\n"
                        + "Serial Monitor at 9600 baud to view counts\n"
                        + "Notes:\n"
                        + " - Uses internal pull-up; button pulls pin to GND when pressed.");
        values25.put(COLUMN_SAMPLE_CODE,
                "const int buttonPin = 3;\n"
                        + "int lastState = HIGH;\n"
                        + "unsigned long lastDebounce = 0;\n"
                        + "int count = 0;\n\n"
                        + "void setup() {\n"
                        + "  pinMode(buttonPin, INPUT_PULLUP);\n"
                        + "  Serial.begin(9600);\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  int reading = digitalRead(buttonPin);\n"
                        + "  if (reading != lastState) {\n"
                        + "    lastDebounce = millis();\n"
                        + "  }\n"
                        + "  if ((millis() - lastDebounce) > 50) {\n"
                        + "    if (reading == LOW && lastState == HIGH) {\n"
                        + "      count++;\n"
                        + "      Serial.println(count);\n"
                        + "    }\n"
                        + "  }\n"
                        + "  lastState = reading;\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, values25);

        // Tutorial 26: Simple Servo Sweep
        ContentValues values26 = new ContentValues();
        values26.put(COLUMN_CATEGORY, "Servo");
        values26.put(COLUMN_TITLE, "Simple Servo Sweep");
        values26.put(COLUMN_DESCRIPTION,
                "Control a standard hobby servo using the Servo library. Demonstrates sweeping angle from 0 to 180 degrees and back.");
        values26.put(COLUMN_IMAGE_NAME, "servo_sweep");
        values26.put(COLUMN_PIN_CONNECTION,
                "Servo signal -> Digital Pin 9\n"
                        + "Servo VCC -> 5V (or external stable supply if under load)\n"
                        + "Servo GND -> GND\n"
                        + "Notes:\n"
                        + " - If using an external supply, connect grounds together.");
        values26.put(COLUMN_SAMPLE_CODE,
                "#include <Servo.h>\n\n"
                        + "Servo myservo;\n\n"
                        + "void setup() {\n"
                        + "  myservo.attach(9);\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  for (int pos = 0; pos <= 180; pos += 1) {\n"
                        + "    myservo.write(pos);\n"
                        + "    delay(15);\n"
                        + "  }\n"
                        + "  for (int pos = 180; pos >= 0; pos -= 1) {\n"
                        + "    myservo.write(pos);\n"
                        + "    delay(15);\n"
                        + "  }\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, values26);

        // Tutorial 27: Servo Control with Serial Commands
        ContentValues values27 = new ContentValues();
        values27.put(COLUMN_CATEGORY, "Servo");
        values27.put(COLUMN_TITLE, "Servo Control with Serial Commands");
        values27.put(COLUMN_DESCRIPTION,
                "Control servo position by sending numeric angle values over Serial. Useful for testing, calibration, and remote control scenarios.");
        values27.put(COLUMN_IMAGE_NAME, "servo_serial_control");
        values27.put(COLUMN_PIN_CONNECTION,
                "Servo signal -> Digital Pin 9\n"
                        + "Servo VCC -> 5V\n"
                        + "Servo GND -> GND\n"
                        + "Serial Monitor 9600 baud for sending angle values (0-180).");
        values27.put(COLUMN_SAMPLE_CODE,
                "#include <Servo.h>\n\n"
                        + "Servo myservo;\n"
                        + "String input = \"\";\n\n"
                        + "void setup() {\n"
                        + "  myservo.attach(9);\n"
                        + "  Serial.begin(9600);\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  if (Serial.available() > 0) {\n"
                        + "    input = Serial.readStringUntil('\\n');\n"
                        + "    input.trim();\n"
                        + "    int angle = input.toInt();\n"
                        + "    if (angle >= 0 && angle <= 180) {\n"
                        + "      myservo.write(angle);\n"
                        + "    }\n"
                        + "  }\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, values27);

        // Tutorial 28: Single Digit 7-Segment Display
        ContentValues value28 = new ContentValues();
        value28.put(COLUMN_CATEGORY, "LED Displays");
        value28.put(COLUMN_TITLE, "Single Digit 7-Segment Display");
        value28.put(COLUMN_DESCRIPTION,
                "Controls a common-cathode single-digit 7-segment display. "
                        + "Shows digit mapping, segment wiring, and displaying numbers 0–9 using an array-based pattern.");
        value28.put(COLUMN_IMAGE_NAME, "7seg_display");
        value28.put(COLUMN_PIN_CONNECTION,
                "Common Cathode -> GND\n"
                        + "Segments:\n"
                        + " a -> Pin 2\n"
                        + " b -> Pin 3\n"
                        + " c -> Pin 4\n"
                        + " d -> Pin 5\n"
                        + " e -> Pin 6\n"
                        + " f -> Pin 7\n"
                        + " g -> Pin 8\n"
                        + "All segments must use 220Ω resistors.");
        value28.put(COLUMN_SAMPLE_CODE,
                "int segPins[7] = {2,3,4,5,6,7,8};\n"
                        + "byte digits[10][7] = {\n"
                        + "  {1,1,1,1,1,1,0},\n"
                        + "  {0,1,1,0,0,0,0},\n"
                        + "  {1,1,0,1,1,0,1},\n"
                        + "  {1,1,1,1,0,0,1},\n"
                        + "  {0,1,1,0,0,1,1},\n"
                        + "  {1,0,1,1,0,1,1},\n"
                        + "  {1,0,1,1,1,1,1},\n"
                        + "  {1,1,1,0,0,0,0},\n"
                        + "  {1,1,1,1,1,1,1},\n"
                        + "  {1,1,1,1,0,1,1}\n"
                        + "};\n\n"
                        + "void setup() {\n"
                        + "  for (int i=0;i<7;i++) pinMode(segPins[i], OUTPUT);\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  for (int d=0; d<10; d++) {\n"
                        + "    for (int s=0; s<7; s++) {\n"
                        + "      digitalWrite(segPins[s], digits[d][s]);\n"
                        + "    }\n"
                        + "    delay(600);\n"
                        + "  }\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, value28);

        // Tutorial 29: 16x2 LCD Display (I2C)
        ContentValues value29 = new ContentValues();
        value29.put(COLUMN_CATEGORY, "LCD/OLED Displays");
        value29.put(COLUMN_TITLE, "16x2 LCD Display (I2C)");
        value29.put(COLUMN_DESCRIPTION,
                "Displays text on a 16x2 liquid crystal display using an I2C backpack. "
                        + "Reduces wiring using the I2C protocol and allows easy text updates and cursor control.");
        value29.put(COLUMN_IMAGE_NAME, "lcd_i_two_c");
        value29.put(COLUMN_PIN_CONNECTION,
                "LCD Module (I2C Backpack):\n"
                        + " VCC -> 5V\n"
                        + " GND -> GND\n"
                        + " SDA -> A4 (Arduino Uno)\n"
                        + " SCL -> A5 (Arduino Uno)\n"
                        + "Default I2C Address: 0x27 or 0x3F (varies by module)");
        value29.put(COLUMN_SAMPLE_CODE,
                "#include <Wire.h>\n"
                        + "#include <LiquidCrystal_I2C.h>\n\n"
                        + "LiquidCrystal_I2C lcd(0x27, 16, 2);\n\n"
                        + "void setup() {\n"
                        + "  lcd.init();\n"
                        + "  lcd.backlight();\n"
                        + "  lcd.setCursor(0,0);\n"
                        + "  lcd.print(\"Hello LCD\");\n"
                        + "  lcd.setCursor(0,1);\n"
                        + "  lcd.print(\"I2C Display\");\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, value29);

        // Tutorial 30: OLED Display 128x64 (I2C)
        ContentValues value30 = new ContentValues();
        value30.put(COLUMN_CATEGORY, "LCD/OLED Displays");
        value30.put(COLUMN_TITLE, "OLED Display 128x64 (I2C)");
        value30.put(COLUMN_DESCRIPTION,
                "Uses an SSD1306-based OLED display to render text and graphics. "
                        + "Ideal for dashboards, sensor readouts, and UI elements. "
                        + "Supports high-contrast white-on-black rendering.");
        value30.put(COLUMN_IMAGE_NAME, "oled_display");
        value30.put(COLUMN_PIN_CONNECTION,
                "OLED SSD1306:\n"
                        + " VCC -> 3.3V or 5V\n"
                        + " GND -> GND\n"
                        + " SDA -> A4\n"
                        + " SCL -> A5\n"
                        + "Typical Address: 0x3C");
        value30.put(COLUMN_SAMPLE_CODE,
                "#include <Wire.h>\n"
                        + "#include <Adafruit_GFX.h>\n"
                        + "#include <Adafruit_SSD1306.h>\n\n"
                        + "Adafruit_SSD1306 display(128, 64, &Wire);\n\n"
                        + "void setup() {\n"
                        + "  display.begin(SSD1306_SWITCHCAPVCC, 0x3C);\n"
                        + "  display.clearDisplay();\n"
                        + "  display.setTextSize(2);\n"
                        + "  display.setTextColor(WHITE);\n"
                        + "  display.setCursor(0,0);\n"
                        + "  display.println(\"OLED Test\");\n"
                        + "  display.display();\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, value30);

        // Tutorial 31: Ultrasonic Sensor (HC-SR04) Distance Measurement
        ContentValues value31 = new ContentValues();
        value31.put(COLUMN_CATEGORY, "Movement/Distance");
        value31.put(COLUMN_TITLE, "Ultrasonic Sensor (HC-SR04) Distance Measurement");
        value31.put(COLUMN_DESCRIPTION,
                "Measures distance using an HC-SR04 ultrasonic module by sending a trigger pulse and reading the echo return time. "
                        + "Useful for obstacle detection, robotics, automation, and distance monitoring applications.");
        value31.put(COLUMN_IMAGE_NAME, "ultrasonic_sensor");
        value31.put(COLUMN_PIN_CONNECTION,
                "Ultrasonic HC-SR04:\n"
                        + " VCC   -> 5V\n"
                        + " GND   -> GND\n"
                        + " TRIG  -> Pin 9\n"
                        + " ECHO  -> Pin 10\n"
                        + "Echo should be connected through a voltage divider when using 3.3V boards (not needed for 5V Arduino Uno).");
        value31.put(COLUMN_SAMPLE_CODE,
                "long duration;\n"
                        + "int distance;\n\n"
                        + "void setup() {\n"
                        + "  pinMode(9, OUTPUT);\n"
                        + "  pinMode(10, INPUT);\n"
                        + "  Serial.begin(9600);\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  digitalWrite(9, LOW);\n"
                        + "  delayMicroseconds(2);\n"
                        + "  digitalWrite(9, HIGH);\n"
                        + "  delayMicroseconds(10);\n"
                        + "  digitalWrite(9, LOW);\n\n"
                        + "  duration = pulseIn(10, HIGH);\n"
                        + "  distance = duration * 0.034 / 2;\n\n"
                        + "  Serial.println(distance);\n"
                        + "  delay(200);\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, value31);

        // Tutorial 32: LM35 Temperature Sensor Reading
        ContentValues value32 = new ContentValues();
        value32.put(COLUMN_CATEGORY, "Temperature/Humidity");
        value32.put(COLUMN_TITLE, "LM35 Temperature Sensor Reading");
        value32.put(COLUMN_DESCRIPTION,
                "Reads analog temperature values from the LM35 sensor. "
                        + "Outputs temperature in Celsius by converting millivolts to degrees. "
                        + "Accuracy is typically ±0.5°C at room temperature.");
        value32.put(COLUMN_IMAGE_NAME, "lm_sensor");
        value32.put(COLUMN_PIN_CONNECTION,
                "LM35:\n"
                        + " Pin 1 (VCC)  -> 5V\n"
                        + " Pin 2 (OUT)  -> A0\n"
                        + " Pin 3 (GND)  -> GND");
        value32.put(COLUMN_SAMPLE_CODE,
                "int sensorPin = A0;\n"
                        + "float tempC;\n\n"
                        + "void setup() {\n"
                        + "  Serial.begin(9600);\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  int value = analogRead(sensorPin);\n"
                        + "  tempC = (value * 5.0 / 1023.0) * 100.0;\n"
                        + "  Serial.println(tempC);\n"
                        + "  delay(500);\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, value32);

        // Tutorial 33: DHT11 Temperature & Humidity Sensor
        ContentValues value33 = new ContentValues();
        value33.put(COLUMN_CATEGORY, "Temperature/Humidity");
        value33.put(COLUMN_TITLE, "DHT11 Temperature & Humidity Sensor");
        value33.put(COLUMN_DESCRIPTION,
                "Reads temperature and humidity from the DHT11 sensor. "
                        + "The DHT11 is a low-cost digital sensor suitable for basic environmental monitoring.");
        value33.put(COLUMN_IMAGE_NAME, "dht_eleven_sensor");
        value33.put(COLUMN_PIN_CONNECTION,
                "DHT11 Connections:\n"
                        + " VCC  -> 5V\n"
                        + " GND  -> GND\n"
                        + " DATA -> Pin 2\n"
                        + "Use a 10k pull-up resistor between DATA and VCC.");
        value33.put(COLUMN_SAMPLE_CODE,
                "#include <DHT.h>\n\n"
                        + "#define DHTPIN 2\n"
                        + "#define DHTTYPE DHT11\n\n"
                        + "DHT dht(DHTPIN, DHTTYPE);\n\n"
                        + "void setup() {\n"
                        + "  Serial.begin(9600);\n"
                        + "  dht.begin();\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  float h = dht.readHumidity();\n"
                        + "  float t = dht.readTemperature();\n\n"
                        + "  Serial.print(\"Temp: \");\n"
                        + "  Serial.print(t);\n"
                        + "  Serial.print(\" °C  Humidity: \");\n"
                        + "  Serial.println(h);\n\n"
                        + "  delay(1000);\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, value33);

        // Tutorial 34: DHT22 High Precision Temperature & Humidity Sensor
        ContentValues value34 = new ContentValues();
        value34.put(COLUMN_CATEGORY, "Temperature/Humidity");
        value34.put(COLUMN_TITLE, "DHT22 High Precision Temperature & Humidity Sensor");
        value34.put(COLUMN_DESCRIPTION,
                "Uses the DHT22 high-accuracy sensor to measure temperature and humidity. "
                        + "It offers better resolution and stability compared to the DHT11.");
        value34.put(COLUMN_IMAGE_NAME, "dht_twenty_two_sensor");
        value34.put(COLUMN_PIN_CONNECTION,
                "DHT22 Connections:\n"
                        + " VCC  -> 5V\n"
                        + " GND  -> GND\n"
                        + " DATA -> Pin 3\n"
                        + "Use a 10k pull-up resistor between DATA and VCC.");
        value34.put(COLUMN_SAMPLE_CODE,
                "#include <DHT.h>\n\n"
                        + "#define DHTPIN 3\n"
                        + "#define DHTTYPE DHT22\n\n"
                        + "DHT dht(DHTPIN, DHTTYPE);\n\n"
                        + "void setup() {\n"
                        + "  Serial.begin(9600);\n"
                        + "  dht.begin();\n"
                        + "}\n\n"
                        + "void loop() {\n"
                        + "  float h = dht.readHumidity();\n"
                        + "  float t = dht.readTemperature();\n\n"
                        + "  Serial.print(\"Temperature: \");\n"
                        + "  Serial.print(t);\n"
                        + "  Serial.print(\" °C  Humidity: \");\n"
                        + "  Serial.println(h);\n\n"
                        + "  delay(1500);\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, value34);

        // Tutorial 35: DR Sensor (Analog Light Detection)
        ContentValues value35 = new ContentValues();
        value35.put(COLUMN_CATEGORY, "Light Sensor");
        value35.put(COLUMN_TITLE, "LDR Sensor (Analog Light Detection)");
        value35.put(COLUMN_DESCRIPTION, "This tutorial demonstrates how to measure light intensity using an LDR (Light Dependent Resistor) and an analog input pin on the Arduino. As light levels change, the LDR's resistance varies, producing different voltage levels that the Arduino reads. This method is widely used in automatic street lights, display brightness control, and smart home systems.");
        value35.put(COLUMN_IMAGE_NAME, "ldr_sensor_analog");
        value35.put(COLUMN_PIN_CONNECTION,
                "• LDR → One side to 5V\n" +
                        "• LDR → Other side connected to A0 and 10kΩ resistor\n" +
                        "• 10kΩ resistor → Connected from A0 to GND\n" +
                        "• Arduino analog pin A0 → Reads voltage divider output");
        value35.put(COLUMN_SAMPLE_CODE,
                "int ldr = A0;\n" +
                        "\n" +
                        "void setup(){\n" +
                        "  Serial.begin(9600);\n" +
                        "}\n" +
                        "\n" +
                        "void loop(){\n" +
                        "  int value = analogRead(ldr);\n" +
                        "  Serial.println(value);\n" +
                        "  delay(500);\n" +
                        "}");
        db.insert(TABLE_TUTORIALS, null, value35);

        // Tutorial 36: Photoresistor Digital Light ON/OFF Detection
        ContentValues value36 = new ContentValues();
        value36.put(COLUMN_CATEGORY, "Light Sensor");
        value36.put(COLUMN_TITLE, "Photoresistor Digital Light ON/OFF Detection");
        value36.put(COLUMN_DESCRIPTION, "This tutorial shows how to use an LDR as a digital light detector. Instead of measuring intensity, the circuit simply detects LIGHT or DARK using a voltage divider and a digital input. When light intensity drops below a threshold, the output becomes LOW or HIGH depending on the wiring. This method is commonly used in daylight detection, security alarms, and energy-saving systems.");
        value36.put(COLUMN_IMAGE_NAME, "photoresistor_digital");
        value36.put(COLUMN_PIN_CONNECTION,
                "• LDR → Connected in series with 10kΩ resistor\n" +
                        "• Junction between LDR & resistor → Digital pin 2\n" +
                        "• LDR → Connected to 5V\n" +
                        "• Resistor → Connected to GND\n" +
                        "• Arduino digital pin 2 → Reads HIGH/LOW light state");
        value36.put(COLUMN_SAMPLE_CODE,
                "int sensor = 2;\n" +
                        "int led = 7;\n" +
                        "\n" +
                        "void setup(){\n" +
                        "  pinMode(sensor, INPUT);\n" +
                        "  pinMode(led, OUTPUT);\n" +
                        "}\n" +
                        "\n" +
                        "void loop(){\n" +
                        "  int state = digitalRead(sensor);\n" +
                        "  if(state == HIGH){\n" +
                        "    digitalWrite(led, HIGH);\n" +
                        "  } else {\n" +
                        "    digitalWrite(led, LOW);\n" +
                        "  }\n" +
                        "}");
        db.insert(TABLE_TUTORIALS, null, value36);

        // Tutorial 37: Simple Buzzer On/Off Using Digital Pin
        ContentValues value37 = new ContentValues();
        value37.put(COLUMN_CATEGORY, "Sound Modules");
        value37.put(COLUMN_TITLE, "Simple Buzzer On/Off Using Digital Pin");
        value37.put(COLUMN_DESCRIPTION, "This tutorial demonstrates how to activate a simple 2-pin buzzer using an Arduino digital output. These buzzers generate a fixed tone when powered and are commonly used in alarms, notifications, and basic sound indicators. The Arduino only controls when the buzzer turns ON or OFF, making it ideal for beginners.");
        value37.put(COLUMN_IMAGE_NAME, "buzzer_on_off");
        value37.put(COLUMN_PIN_CONNECTION,
                "• Buzzer positive (+) → Arduino digital pin 7\n" +
                        "• Buzzer negative (–) → GND\n" +
                        "• No resistors are required for standard 5V active buzzers");
        value37.put(COLUMN_SAMPLE_CODE,
                "int buzzer = 7;\n" +
                        "\n" +
                        "void setup(){\n" +
                        "  pinMode(buzzer, OUTPUT);\n" +
                        "}\n" +
                        "\n" +
                        "void loop(){\n" +
                        "  digitalWrite(buzzer, HIGH);\n" +
                        "  delay(1000);\n" +
                        "  digitalWrite(buzzer, LOW);\n" +
                        "  delay(1000);\n" +
                        "}");
        db.insert(TABLE_TUTORIALS, null, value37);

        // Tutorial 38: 3-Pin Passive Buzzer Tone Output
        ContentValues value38 = new ContentValues();
        value38.put(COLUMN_CATEGORY, "Sound Modules");
        value38.put(COLUMN_TITLE, "3-Pin Passive Buzzer Tone Output");
        value38.put(COLUMN_DESCRIPTION, "This tutorial explains how to generate tones using a passive 3-pin buzzer. Unlike active buzzers, passive modules do not contain an internal oscillator, allowing the Arduino to produce custom tones such as alerts, melodies, and signal patterns. This is achieved by sending frequency-controlled square waves to the buzzer pin.");
        value38.put(COLUMN_IMAGE_NAME, "passive_buzzer_tone");
        value38.put(COLUMN_PIN_CONNECTION,
                "• SIG (signal) pin → Arduino digital pin 8\n" +
                        "• VCC → 5V\n" +
                        "• GND → GND\n" +
                        "• The passive buzzer requires tone() for frequency control");
        value38.put(COLUMN_SAMPLE_CODE,
                "int buzzer = 8;\n" +
                        "\n" +
                        "void setup(){\n" +
                        "}\n" +
                        "\n" +
                        "void loop(){\n" +
                        "  tone(buzzer, 600);\n" +
                        "  delay(1000);\n" +
                        "  noTone(buzzer);\n" +
                        "  delay(1000);\n" +
                        "}");
        db.insert(TABLE_TUTORIALS, null, value38);

        // Tutorial 39: Playing Multiple Frequencies on Passive Buzzer
        ContentValues value39 = new ContentValues();
        value39.put(COLUMN_CATEGORY, "Sound Modules");
        value39.put(COLUMN_TITLE, "Playing Multiple Frequencies on Passive Buzzer");
        value39.put(COLUMN_DESCRIPTION, "This tutorial demonstrates how to play multiple frequencies to create varying sound patterns using a passive buzzer. By rapidly changing the tone frequency, the Arduino can generate alarms, melodies, and user-defined sound sequences. This method is useful for robotics, notifications, and user interaction feedback.");
        value39.put(COLUMN_IMAGE_NAME, "buzzer_frequency_play");
        value39.put(COLUMN_PIN_CONNECTION,
                "• Buzzer signal (S) → Arduino pin 9\n" +
                        "• VCC → 5V\n" +
                        "• GND → GND\n" +
                        "• Passive buzzer required for frequency-based sound generation");
        value39.put(COLUMN_SAMPLE_CODE,
                "int buzzer = 9;\n" +
                        "\n" +
                        "void setup(){\n" +
                        "}\n" +
                        "\n" +
                        "void loop(){\n" +
                        "  tone(buzzer, 300);\n" +
                        "  delay(500);\n" +
                        "  tone(buzzer, 600);\n" +
                        "  delay(500);\n" +
                        "  tone(buzzer, 900);\n" +
                        "  delay(500);\n" +
                        "  noTone(buzzer);\n" +
                        "  delay(500);\n" +
                        "}");
        db.insert(TABLE_TUTORIALS, null, value39);

        // Tutorial 40: IR Obstacle Detection Sensor
        ContentValues value40 = new ContentValues();
        value40.put(COLUMN_CATEGORY, "IR/PIR Sensor");
        value40.put(COLUMN_TITLE, "IR Obstacle Detection Sensor");
        value40.put(COLUMN_DESCRIPTION,
                "This tutorial demonstrates how to use an Infrared (IR) obstacle detection sensor with Arduino. "
                        + "The sensor works by emitting infrared light and measuring its reflection from nearby objects. "
                        + "When an obstacle enters the detection range, the sensor outputs a LOW or HIGH signal depending on the module type. "
                        + "IR sensors are widely used in robots, line-following systems, automatic lighting, and proximity-based triggers.");
        value40.put(COLUMN_IMAGE_NAME, "ir_obstacle_sensor");
        value40.put(COLUMN_PIN_CONNECTION,
                "• VCC → 5V\n"
                        + "• GND → GND\n"
                        + "• OUT (Digital Output) → Arduino digital pin 7\n"
                        + "• Adjustable sensitivity via onboard potentiometer\n"
                        + "• Detection method: Infrared reflection");
        value40.put(COLUMN_SAMPLE_CODE,
                "int ir = 7;\n"
                        + "int led = 13;\n"
                        + "\n"
                        + "void setup(){\n"
                        + "  pinMode(ir, INPUT);\n"
                        + "  pinMode(led, OUTPUT);\n"
                        + "}\n"
                        + "\n"
                        + "void loop(){\n"
                        + "  int val = digitalRead(ir);\n"
                        + "  if(val == LOW){\n"
                        + "    digitalWrite(led, HIGH);\n"
                        + "  } else {\n"
                        + "    digitalWrite(led, LOW);\n"
                        + "  }\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, value40);

        // Tutorial 41: PIR Motion Sensor
        ContentValues value41 = new ContentValues();
        value41.put(COLUMN_CATEGORY, "IR/PIR Sensor");
        value41.put(COLUMN_TITLE, "PIR Motion Sensor");
        value41.put(COLUMN_DESCRIPTION,
                "This tutorial explains how to use a Passive Infrared (PIR) motion sensor with Arduino. "
                        + "A PIR sensor detects movement by measuring changes in infrared radiation produced by human bodies or animals. "
                        + "It is commonly used in automatic lights, home security systems, and motion-activated alarms. "
                        + "The sensor provides a digital HIGH output when motion is detected and LOW when the area is clear. "
                        + "Sensitivity and delay time can be adjusted using onboard potentiometers.");
        value41.put(COLUMN_IMAGE_NAME, "pir_motion_sensor");
        value41.put(COLUMN_PIN_CONNECTION,
                "• VCC → 5V\n"
                        + "• GND → GND\n"
                        + "• OUT (Digital Output) → Arduino digital pin 8\n"
                        + "• Detection Range: Typically 3–7 meters\n"
                        + "• Adjustable sensitivity and delay time\n"
                        + "• Warm-up time: 20–60 seconds depending on module");
        value41.put(COLUMN_SAMPLE_CODE,
                "int pir = 8;\n"
                        + "int led = 13;\n"
                        + "\n"
                        + "void setup(){\n"
                        + "  pinMode(pir, INPUT);\n"
                        + "  pinMode(led, OUTPUT);\n"
                        + "}\n"
                        + "\n"
                        + "void loop(){\n"
                        + "  int motion = digitalRead(pir);\n"
                        + "  if(motion == HIGH){\n"
                        + "    digitalWrite(led, HIGH);\n"
                        + "  } else {\n"
                        + "    digitalWrite(led, LOW);\n"
                        + "  }\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, value41);

        // Tutorial 42: PIR Motion Sensor with Serial Output
        ContentValues value42 = new ContentValues();
        value42.put(COLUMN_CATEGORY, "IR/PIR Sensor");
        value42.put(COLUMN_TITLE, "PIR Motion Sensor with Serial Output");
        value42.put(COLUMN_DESCRIPTION,
                "This tutorial extends the basic PIR motion sensor setup by adding Serial Monitor output. "
                        + "When the sensor detects movement, it prints a notification to the Serial Monitor along with turning on the onboard LED. "
                        + "This method is useful for debugging, data logging, home automation systems, and motion-triggered event tracking. "
                        + "The PIR module detects infrared changes caused by human or animal movement, outputting a HIGH signal when motion is present.");
        value42.put(COLUMN_IMAGE_NAME, "pir_with_serial");
        value42.put(COLUMN_PIN_CONNECTION,
                "• VCC → 5V\n"
                        + "• GND → GND\n"
                        + "• OUT (Digital Output) → Arduino digital pin 8\n"
                        + "• Adjustable delay & sensitivity using onboard potentiometers\n"
                        + "• Warm-up time: 20–60 seconds\n"
                        + "• Recommended usage: indoor motion detection, alarms, security monitoring");
        value42.put(COLUMN_SAMPLE_CODE,
                "int pir = 8;\n"
                        + "\n"
                        + "void setup(){\n"
                        + "  Serial.begin(9600);\n"
                        + "  pinMode(pir, INPUT);\n"
                        + "}\n"
                        + "\n"
                        + "void loop(){\n"
                        + "  int motion = digitalRead(pir);\n"
                        + "  if(motion == HIGH){\n"
                        + "    Serial.println(\"Motion Detected\");\n"
                        + "  } else {\n"
                        + "    Serial.println(\"No Motion\");\n"
                        + "  }\n"
                        + "  delay(300);\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, value42);

        // Tutorial 43: Joystick Controlling 4 LEDs
        ContentValues value43 = new ContentValues();
        value43.put(COLUMN_CATEGORY, "Joystick");
        value43.put(COLUMN_TITLE, "Joystick Controlling 4 LEDs");
        value43.put(COLUMN_DESCRIPTION,
                "This tutorial shows how to use a two-axis analog joystick to control four LEDs in the UP, DOWN, LEFT, and RIGHT directions. "
                        + "The joystick provides two analog values (X and Y), each ranging from 0 to 1023. "
                        + "Based on the joystick position, the Arduino activates the LED corresponding to the direction pushed. "
                        + "This setup is commonly used in robotic controls, menu navigation, and gaming interfaces.");
        value43.put(COLUMN_IMAGE_NAME, "joystick_four_leds");
        value43.put(COLUMN_PIN_CONNECTION,
                "• VCC → 5V\n"
                        + "• GND → GND\n"
                        + "• VRx (X-axis) → A0\n"
                        + "• VRy (Y-axis) → A1\n"
                        + "• SW (Push Button, optional) → Not used in this example\n"
                        + "• LED_TOP → Pin 2\n"
                        + "• LED_BOTTOM → Pin 3\n"
                        + "• LED_LEFT → Pin 4\n"
                        + "• LED_RIGHT → Pin 5\n"
                        + "• Joystick center position: approx. 512 on both X & Y axes\n"
                        + "• Movement direction detected by thresholding analog values");
        value43.put(COLUMN_SAMPLE_CODE,
                "int xPin = A0;\n"
                        + "int yPin = A1;\n"
                        + "int topLed = 2;\n"
                        + "int bottomLed = 3;\n"
                        + "int leftLed = 4;\n"
                        + "int rightLed = 5;\n"
                        + "\n"
                        + "void setup(){\n"
                        + "  pinMode(topLed, OUTPUT);\n"
                        + "  pinMode(bottomLed, OUTPUT);\n"
                        + "  pinMode(leftLed, OUTPUT);\n"
                        + "  pinMode(rightLed, OUTPUT);\n"
                        + "}\n"
                        + "\n"
                        + "void loop(){\n"
                        + "  int xValue = analogRead(xPin);\n"
                        + "  int yValue = analogRead(yPin);\n"
                        + "\n"
                        + "  digitalWrite(topLed,    yValue < 300);\n"
                        + "  digitalWrite(bottomLed, yValue > 700);\n"
                        + "  digitalWrite(leftLed,   xValue < 300);\n"
                        + "  digitalWrite(rightLed,  xValue > 700);\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, value43);

        // Tutorial 44: Rain Drop Sensor (Basic Read)
        ContentValues value44 = new ContentValues();
        value44.put(COLUMN_CATEGORY, "Rain Drop Sensor");
        value44.put(COLUMN_TITLE, "Rain Drop Sensor (Basic Read)");
        value44.put(COLUMN_DESCRIPTION,
                "This tutorial explains how to read basic rain detection using a raindrop sensor module. "
                        + "The sensor consists of a rain board and a control module. As water droplets accumulate, "
                        + "the module’s digital output changes state, indicating dry or wet conditions. "
                        + "This setup is commonly used in weather stations, garden systems, window automation, and rain alarms.");
        value44.put(COLUMN_IMAGE_NAME, "raindrop_basic");
        value44.put(COLUMN_PIN_CONNECTION,
                "• VCC → 5V\n"
                        + "• GND → GND\n"
                        + "• DO (Digital Output) → Arduino digital pin 7\n"
                        + "• Sensor Board → Connect to module via provided 2-pin cable\n"
                        + "• Digital Output: HIGH = Dry, LOW = Rain Detected\n"
                        + "• Sensitivity adjustable via onboard potentiometer");
        value44.put(COLUMN_SAMPLE_CODE,
                "int rain = 7;\n"
                        + "int led = 13;\n"
                        + "\n"
                        + "void setup(){\n"
                        + "  pinMode(rain, INPUT);\n"
                        + "  pinMode(led, OUTPUT);\n"
                        + "}\n"
                        + "\n"
                        + "void loop(){\n"
                        + "  int state = digitalRead(rain);\n"
                        + "  if(state == LOW){\n"
                        + "    digitalWrite(led, HIGH);\n"
                        + "  } else {\n"
                        + "    digitalWrite(led, LOW);\n"
                        + "  }\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, value44);

        // Tutorial 45: Rain Drop Sensor with Serial Output
        ContentValues value45 = new ContentValues();
        value45.put(COLUMN_CATEGORY, "Rain Drop Sensor");
        value45.put(COLUMN_TITLE, "Rain Drop Sensor with Serial Output");
        value45.put(COLUMN_DESCRIPTION,
                "This tutorial expands the basic raindrop sensor usage by adding Serial Monitor output. "
                        + "The rain sensor module detects moisture on the rain board and outputs a digital signal. "
                        + "With Serial printing enabled, you can monitor rain detection status in real-time, making it ideal for data logging, "
                        + "environmental monitoring, smart agriculture, and automated window systems.");
        value45.put(COLUMN_IMAGE_NAME, "raindrop_serial");
        value45.put(COLUMN_PIN_CONNECTION,
                "• VCC → 5V\n"
                        + "• GND → GND\n"
                        + "• DO (Digital Output) → Arduino digital pin 7\n"
                        + "• Rain Board → Connected to module via 2-pin cable\n"
                        + "• Digital Output States:\n"
                        + "    - HIGH = Dry surface\n"
                        + "    - LOW  = Rain detected / Wet surface\n"
                        + "• Onboard potentiometer allows sensitivity adjustment");
        value45.put(COLUMN_SAMPLE_CODE,
                "int rain = 7;\n"
                        + "\n"
                        + "void setup(){\n"
                        + "  Serial.begin(9600);\n"
                        + "  pinMode(rain, INPUT);\n"
                        + "}\n"
                        + "\n"
                        + "void loop(){\n"
                        + "  int state = digitalRead(rain);\n"
                        + "  if(state == LOW){\n"
                        + "    Serial.println(\"Rain Detected\");\n"
                        + "  } else {\n"
                        + "    Serial.println(\"No Rain\");\n"
                        + "  }\n"
                        + "  delay(300);\n"
                        + "}");
        db.insert(TABLE_TUTORIALS, null, value45);

        // Tutorial 46: Relay With Water Pump
        ContentValues values46 = new ContentValues();
        values46.put(COLUMN_CATEGORY, "Actuators");
        values46.put(COLUMN_TITLE, "Relay Control with Water Pump");
        values46.put(COLUMN_DESCRIPTION, "Learn how to control a water pump using a relay module.");
        values46.put(COLUMN_IMAGE_NAME, "relay_water_pump");
        values46.put(COLUMN_PIN_CONNECTION, "Relay IN -> D7\nVCC -> 5V\nGND -> GND");
        values46.put(COLUMN_SAMPLE_CODE,
                "const int relayPin = 7;\n\n" +
                        "void setup() {\n" +
                        "  pinMode(relayPin, OUTPUT);\n" +
                        "}\n\n" +
                        "void loop() {\n" +
                        "  digitalWrite(relayPin, HIGH); // Pump ON\n" +
                        "  delay(5000);\n" +
                        "  digitalWrite(relayPin, LOW);  // Pump OFF\n" +
                        "  delay(5000);\n" +
                        "}");
        db.insert(TABLE_TUTORIALS, null, values46);

        // Tutorial 47: Keypad - Print to Serial
        ContentValues values47 = new ContentValues();
        values47.put(COLUMN_CATEGORY, "Keypad");
        values47.put(COLUMN_TITLE, "Keypad Serial Print");
        values47.put(COLUMN_DESCRIPTION, "Learn how to read a 4x4 keypad and print key presses to the Serial Monitor.");
        values47.put(COLUMN_IMAGE_NAME, "keypad_serial");
        values47.put(COLUMN_PIN_CONNECTION,
                "Keypad Rows -> D9, D8, D7, D6\nKeypad Cols -> D5, D4, D3, D2");

        values47.put(COLUMN_SAMPLE_CODE,
                "#include <Keypad.h>\n\n" +
                        "const byte ROWS = 4;\n" +
                        "const byte COLS = 4;\n\n" +
                        "char keys[ROWS][COLS] = {\n" +
                        "  {'1','2','3','A'},\n" +
                        "  {'4','5','6','B'},\n" +
                        "  {'7','8','9','C'},\n" +
                        "  {'*','0','#','D'}\n" +
                        "};\n\n" +
                        "byte rowPins[ROWS] = {9, 8, 7, 6};\n" +
                        "byte colPins[COLS] = {5, 4, 3, 2};\n\n" +
                        "Keypad keypad = Keypad(makeKeymap(keys), rowPins, colPins, ROWS, COLS);\n\n" +
                        "void setup() {\n" +
                        "  Serial.begin(9600);\n" +
                        "}\n\n" +
                        "void loop() {\n" +
                        "  char key = keypad.getKey();\n" +
                        "  if (key) {\n" +
                        "    Serial.println(key);\n" +
                        "  }\n" +
                        "}");

        db.insert(TABLE_TUTORIALS, null, values47);

        // Tutorial 48: Keypad with Password
        ContentValues values48 = new ContentValues();
        values48.put(COLUMN_CATEGORY, "Keypad");
        values48.put(COLUMN_TITLE, "Keypad Password System");
        values48.put(COLUMN_DESCRIPTION,
                "Enter a password using a 4x4 keypad. If the password is correct, an LED turns ON.");
        values48.put(COLUMN_IMAGE_NAME, "keypad_password");
        values48.put(COLUMN_PIN_CONNECTION,
                "Keypad Rows -> D9, D8, D7, D6\nKeypad Cols -> D5, D4, D3, D2\nLED -> D10");

        values48.put(COLUMN_SAMPLE_CODE,
                "#include <Keypad.h>\n\n" +
                        "const byte ROWS = 4;\n" +
                        "const byte COLS = 4;\n\n" +
                        "char keys[ROWS][COLS] = {\n" +
                        "  {'1','2','3','A'},\n" +
                        "  {'4','5','6','B'},\n" +
                        "  {'7','8','9','C'},\n" +
                        "  {'*','0','#','D'}\n" +
                        "};\n\n" +
                        "byte rowPins[ROWS] = {9, 8, 7, 6};\n" +
                        "byte colPins[COLS] = {5, 4, 3, 2};\n\n" +
                        "Keypad keypad = Keypad(makeKeymap(keys), rowPins, colPins, ROWS, COLS);\n\n" +
                        "String password = \"1234\";\n" +
                        "String input = \"\";\n" +
                        "int ledPin = 10;\n\n" +
                        "void setup() {\n" +
                        "  Serial.begin(9600);\n" +
                        "  pinMode(ledPin, OUTPUT);\n" +
                        "}\n\n" +
                        "void loop() {\n" +
                        "  char key = keypad.getKey();\n" +
                        "  if (key) {\n" +
                        "    if (key == '#') {\n" +
                        "      if (input == password) {\n" +
                        "        digitalWrite(ledPin, HIGH);\n" +
                        "        Serial.println(\"Correct Password\");\n" +
                        "      } else {\n" +
                        "        digitalWrite(ledPin, LOW);\n" +
                        "        Serial.println(\"Wrong Password\");\n" +
                        "      }\n" +
                        "      input = \"\"; // reset\n" +
                        "    } else {\n" +
                        "      input += key;\n" +
                        "      Serial.print(\"Entered: \");\n" +
                        "      Serial.println(input);\n" +
                        "    }\n" +
                        "  }\n" +
                        "}");

        db.insert(TABLE_TUTORIALS, null, values48);

        // Tutorial 49: I2C Communication
        ContentValues values49 = new ContentValues();
        values49.put(COLUMN_CATEGORY, "I2C Connection");
        values49.put(COLUMN_TITLE, "I2C: Arduino Master & Slave");
        values49.put(COLUMN_DESCRIPTION,
                "Learn how to communicate between two Arduino boards using the I2C protocol.");
        values49.put(COLUMN_IMAGE_NAME, "i_two_c_comm");
        values49.put(COLUMN_PIN_CONNECTION,
                "SDA -> A4 on both Arduinos\nSCL -> A5 on both Arduinos\nGND -> GND");

        values49.put(COLUMN_SAMPLE_CODE,
                "// --- MASTER CODE ---\n" +
                        "#include <Wire.h>\n\n" +
                        "void setup() {\n" +
                        "  Wire.begin();\n" +
                        "  Serial.begin(9600);\n" +
                        "}\n\n" +
                        "void loop() {\n" +
                        "  Wire.beginTransmission(8); // send to slave ID 8\n" +
                        "  Wire.write(\"Hi\");\n" +
                        "  Wire.endTransmission();\n" +
                        "  delay(1000);\n" +
                        "}\n\n" +

                        "// --- SLAVE CODE ---\n" +
                        "#include <Wire.h>\n\n" +
                        "void setup() {\n" +
                        "  Wire.begin(8); // slave address\n" +
                        "  Wire.onReceive(receiveEvent);\n" +
                        "  Serial.begin(9600);\n" +
                        "}\n\n" +
                        "void loop() {}\n\n" +
                        "void receiveEvent(int bytes) {\n" +
                        "  while (Wire.available()) {\n" +
                        "    char c = Wire.read();\n" +
                        "    Serial.print(c);\n" +
                        "  }\n" +
                        "}");

        db.insert(TABLE_TUTORIALS, null, values49);



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
