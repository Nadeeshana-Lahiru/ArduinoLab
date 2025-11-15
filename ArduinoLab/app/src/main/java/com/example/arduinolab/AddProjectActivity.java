package com.example.arduinolab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddProjectActivity extends AppCompatActivity {

    EditText addTitle, addDescription, addPinConfig, addSampleCode;
    Button saveButton, selectImageButton;
    ImageView backIcon, imagePreview;
    TextView categoryText;

    DBHelper dbHelper;
    String currentCategory;
    private Uri imageUri = null;
    private ActivityResultLauncher<String> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        backIcon = findViewById(R.id.backIconID);
        saveButton = findViewById(R.id.saveButtonID);
        categoryText = findViewById(R.id.categoryTextID);
        addTitle = findViewById(R.id.addTitleEditID);
        addDescription = findViewById(R.id.addDescriptionEditID);
        addPinConfig = findViewById(R.id.addPinConfigEditID);
        addSampleCode = findViewById(R.id.addSampleCodeEditID);

        selectImageButton = findViewById(R.id.selectImageButtonID);
        imagePreview = findViewById(R.id.imagePreviewViewID);

        dbHelper = new DBHelper(this);

        Intent intent = getIntent();
        currentCategory = intent.getStringExtra("CATEGORY_NAME");
        if (currentCategory == null || currentCategory.isEmpty()) {
            currentCategory = "PROJECTS"; // Default to PROJECTS
        }
        categoryText.setText(currentCategory);

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        imageUri = uri;
                        // 5. Take persistent permission
                        getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        imagePreview.setImageURI(imageUri);
                        imagePreview.setVisibility(View.VISIBLE);
                    }
                });

        selectImageButton.setOnClickListener(v -> galleryLauncher.launch("image/*"));
        backIcon.setOnClickListener(v -> finish());
        saveButton.setOnClickListener(v -> saveProject());
    }

    private void saveProject() {
        String title = addTitle.getText().toString().trim();
        String description = addDescription.getText().toString().trim();
        String pinConfig = addPinConfig.getText().toString().trim();
        String sampleCode = addSampleCode.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty() || pinConfig.isEmpty() || sampleCode.isEmpty()) {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        String imagePath = "";
        if (imageUri != null) {
            imagePath = imageUri.toString();
        }

        TutorialItem newItem = new TutorialItem(
                currentCategory,
                title,
                description,
                imagePath,
                pinConfig,
                sampleCode
        );

        dbHelper.insertTutorial(newItem);
        Toast.makeText(this, "Project saved!", Toast.LENGTH_SHORT).show();
        finish();
    }
}