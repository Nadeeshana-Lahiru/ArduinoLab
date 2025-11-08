package com.example.arduinolab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ArduinoTutorialPage extends AppCompatActivity {

    RelativeLayout basicProgrammingRel , serialPortRel , ledSRel , buttonsSRel , servoRel ,
            ledDisplaysRel , lcdDisplaysRel , movementDistanceRel , temperatureRel , lightSensorRel ,
            soundModulesRel , pirSensorRel ;

    ImageView projectsIconRel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arduino_tutorial_page);

        projectsIconRel = findViewById(R.id.projectsIconID);
        basicProgrammingRel = findViewById(R.id.basicProgrammingRelativeID);
        serialPortRel = findViewById(R.id.serialPortRelativeID);
        ledSRel = findViewById(R.id.LEDsRelativeID);
        buttonsSRel = findViewById(R.id.buttonsRelativeID);
        servoRel = findViewById(R.id.servoRelativeID);
        ledDisplaysRel = findViewById(R.id.ledDisplayRelativeID);
        lcdDisplaysRel = findViewById(R.id.lcdDisplayRelativeID);
        movementDistanceRel = findViewById(R.id.movementRelativeDisplay);
        temperatureRel = findViewById(R.id.temperatureRelativeID);
        lightSensorRel = findViewById(R.id.lightSensorRelativeID);
        soundModulesRel = findViewById(R.id.soundModuleRelativeID);
        pirSensorRel = findViewById(R.id.pirSensorRelativeID);

        // Example 1: Basic Programming
        basicProgrammingRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listIntent = new Intent(ArduinoTutorialPage.this, BasicProgrammings.class);
                listIntent.putExtra("CATEGORY_NAME", "Basic Programming");
                startActivity(listIntent);
            }
        });

        // Example 2: Serial Port
        serialPortRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listIntent = new Intent(ArduinoTutorialPage.this, BasicProgrammings.class);
                listIntent.putExtra("CATEGORY_NAME", "Serial Port");
                startActivity(listIntent);
            }
        });

        // Example 3: LEDs
        ledSRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listIntent = new Intent(ArduinoTutorialPage.this, BasicProgrammings.class);
                listIntent.putExtra("CATEGORY_NAME", "LEDs");
                startActivity(listIntent);
            }
        });

// 4. Buttons
        buttonsSRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listIntent = new Intent(ArduinoTutorialPage.this, BasicProgrammings.class);
                listIntent.putExtra("CATEGORY_NAME", "Buttons");
                startActivity(listIntent);
            }
        });

        // 5. Servo
        servoRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listIntent = new Intent(ArduinoTutorialPage.this, BasicProgrammings.class);
                listIntent.putExtra("CATEGORY_NAME", "Servo");
                startActivity(listIntent);
            }
        });

        // 6. LED Displays
        ledDisplaysRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listIntent = new Intent(ArduinoTutorialPage.this, BasicProgrammings.class);
                listIntent.putExtra("CATEGORY_NAME", "LED Displays");
                startActivity(listIntent);
            }
        });

        // 7. LCD / OLED Displays
        lcdDisplaysRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listIntent = new Intent(ArduinoTutorialPage.this, BasicProgrammings.class);
                listIntent.putExtra("CATEGORY_NAME", "LCD/OLED Displays");
                startActivity(listIntent);
            }
        });

        // 8. Movement / Distance
        movementDistanceRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listIntent = new Intent(ArduinoTutorialPage.this, BasicProgrammings.class);
                listIntent.putExtra("CATEGORY_NAME", "Movement/Distance");
                startActivity(listIntent);
            }
        });

        // 9. Temperature / Humidity
        temperatureRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listIntent = new Intent(ArduinoTutorialPage.this, BasicProgrammings.class);
                listIntent.putExtra("CATEGORY_NAME", "Temperature/Humidity");
                startActivity(listIntent);
            }
        });

        // 10. Light Sensor
        lightSensorRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listIntent = new Intent(ArduinoTutorialPage.this, BasicProgrammings.class);
                listIntent.putExtra("CATEGORY_NAME", "Light Sensor");
                startActivity(listIntent);
            }
        });

        // 11. Sound Modules
        soundModulesRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listIntent = new Intent(ArduinoTutorialPage.this, BasicProgrammings.class);
                listIntent.putExtra("CATEGORY_NAME", "Sound Modules");
                startActivity(listIntent);
            }
        });

        // 12. PIR Sensor
        pirSensorRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listIntent = new Intent(ArduinoTutorialPage.this, BasicProgrammings.class);
                listIntent.putExtra("CATEGORY_NAME", "PIR Sensor");
                startActivity(listIntent);
            }
        });
    }
}