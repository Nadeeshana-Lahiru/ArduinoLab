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

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddTutorial extends AppCompatActivity {

    EditText addTitle, addDescription, addPinConfig, addSampleCode;
    Button saveButton, selectImageButton;
    ImageView backIcon, imagePreview;
    TextView categoryText;

    DBHelper dbHelper;
    String currentCategory;

    // This will hold the path to the selected image
    private Uri imageUri = null;
    private ActivityResultLauncher<String> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tutorial);

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
            currentCategory = "Unknown";
        }
        categoryText.setText(currentCategory);

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null) {
                            imageUri = uri;   // Image was selected
                            getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            // we must take "persistent" permission to read the image URI or it will not show up on the ShowDetails page.
                            imagePreview.setImageURI(imageUri);
                            imagePreview.setVisibility(View.VISIBLE);
                        }
                    }
                });

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryLauncher.launch("image/*");
            }
        });

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
                String pinConfig = addPinConfig.getText().toString().trim();
                String sampleCode = addSampleCode.getText().toString().trim();

                if (title.isEmpty() || description.isEmpty() || pinConfig.isEmpty() || sampleCode.isEmpty()) {
                    Toast.makeText(AddTutorial.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Convert the image URI to a String. If no image is selected, save an empty string.
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
                Toast.makeText(AddTutorial.this, "Tutorial saved!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}