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

        // ... ADD setOnClickListener FOR ALL OTHER RelativeLayouts ...
        // (buttonsSRel, servoRel, etc.)
        // Just copy the block above and change the "CATEGORY_NAME" string.
    }
}