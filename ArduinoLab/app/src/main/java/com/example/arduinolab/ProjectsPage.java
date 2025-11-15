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

public class ProjectsPage extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView backIcon, addIcon;
    TextView titleTextView;

    DBHelper dbHelper;
    ProjectAdapter adapter;
    ArrayList<TutorialItem> itemsList;

    String currentCategory = "PROJECTS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_page);

        recyclerView = findViewById(R.id.recyclerViewID);
        backIcon = findViewById(R.id.backToListIconID);
        addIcon = findViewById(R.id.addIconID);
        titleTextView = findViewById(R.id.titleTextID);

        titleTextView.setText("My Projects");

        dbHelper = new DBHelper(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemsList = dbHelper.getAllTutorialsByCategory(currentCategory);
        adapter = new ProjectAdapter(this, itemsList);
        recyclerView.setAdapter(adapter);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to main tutorial page
                Intent backIntent = new Intent(ProjectsPage.this, ArduinoTutorialPage.class);
                startActivity(backIntent);
                finish();
            }
        });

        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 7. Open AddProjectActivity
                Intent addIntent = new Intent(ProjectsPage.this, AddProjectActivity.class);
                // 8. Pass the hard-coded category
                addIntent.putExtra("CATEGORY_NAME", currentCategory);
                startActivity(addIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        itemsList = dbHelper.getAllTutorialsByCategory(currentCategory);
        adapter = new ProjectAdapter(this, itemsList);
        recyclerView.setAdapter(adapter);
    }
}