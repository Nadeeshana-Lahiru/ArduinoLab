package com.example.arduinolab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BasicProgrammings extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView backIcon, addIcon;
    TextView titleTextView;

    DBHelper dbHelper;
    MyAdapter adapter;
    ArrayList<TutorialItem> itemsList;

    String currentCategory = "Basic Programming";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_programmings);

        recyclerView = findViewById(R.id.recyclerViewID);
        backIcon = findViewById(R.id.backToListIconID);
        addIcon = findViewById(R.id.addIconID);
        titleTextView = findViewById(R.id.titleTextID);

        Intent intent = getIntent();
        currentCategory = intent.getStringExtra("CATEGORY_NAME");

        if (currentCategory != null) {
            titleTextView.setText(currentCategory);
        } else {
            titleTextView.setText("Tutorials");
            currentCategory = "Unknown";
        }

        dbHelper = new DBHelper(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemsList = dbHelper.getAllTutorialsByCategory(currentCategory);
        adapter = new MyAdapter(this, itemsList);
        recyclerView.setAdapter(adapter);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(BasicProgrammings.this, ArduinoTutorialPage.class);
                startActivity(backIntent);
                finish();
            }
        });

        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(BasicProgrammings.this, AddTutorial.class);
                addIntent.putExtra("CATEGORY_NAME", currentCategory);
                startActivity(addIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // This reloads data every time you come back to this page
        itemsList = dbHelper.getAllTutorialsByCategory(currentCategory);
        adapter = new MyAdapter(this, itemsList);
        recyclerView.setAdapter(adapter);
    }
}