package com.example.arduinolab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddTutorial extends AppCompatActivity {

    ImageView backIcon;
    TextView categoryText;
    EditText addTitle, addDescription, addImageName, addPinConfig, addSampleCode;
    Button saveButton;

    DBHelper dbHelper;
    String currentCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tutorial);

        backIcon = findViewById(R.id.backIconID);
        categoryText = findViewById(R.id.categoryTextID);
        addTitle = findViewById(R.id.addTitleEditID);
        addDescription = findViewById(R.id.addDescriptionEditID);
        addImageName = findViewById(R.id.addImageNameEditID);
        addPinConfig = findViewById(R.id.addPinConfigEditID);
        addSampleCode = findViewById(R.id.addSampleCodeEditID);
        saveButton = findViewById(R.id.saveButtonID);

        dbHelper = new DBHelper(this);

        Intent intent = getIntent();
        currentCategory = intent.getStringExtra("CATEGORY_NAME");
        if (currentCategory == null || currentCategory.isEmpty()) {
            currentCategory = "Unknown";
        }
        categoryText.setText(currentCategory);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = addTitle.getText().toString().trim();
                String description = addDescription.getText().toString().trim();
                String imageName = addImageName.getText().toString().trim();
                String pinConfig = addPinConfig.getText().toString().trim();
                String sampleCode = addSampleCode.getText().toString().trim();

                if (title.isEmpty() || description.isEmpty() || pinConfig.isEmpty() || sampleCode.isEmpty()) {
                    Toast.makeText(AddTutorial.this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (imageName.isEmpty()) {
                    imageName = "ic_launcher_background";
                }

                TutorialItem newItem = new TutorialItem(
                        currentCategory,
                        title,
                        description,
                        imageName,
                        pinConfig,
                        sampleCode
                );
                
                dbHelper.insertTutorial(newItem);

                Toast.makeText(AddTutorial.this, "Tutorial saved!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}