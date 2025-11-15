package com.example.arduinolab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ShowProjectDetails extends AppCompatActivity {

    ImageView backIcon, tutorialImage, editIcon;
    TextView titleText, descriptionText, pinConfigText, sampleCodeText;

    DBHelper dbHelper;
    TutorialItem currentItem;
    int currentItemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_project_details);

        backIcon = findViewById(R.id.backToListIconID);
        editIcon = findViewById(R.id.editIconID);
        tutorialImage = findViewById(R.id.viewImageID);
        titleText = findViewById(R.id.titleNameID);
        descriptionText = findViewById(R.id.descriptionTextID);
        pinConfigText = findViewById(R.id.pinConfigurationTextID);
        sampleCodeText = findViewById(R.id.codeTextID);

        dbHelper = new DBHelper(this);
        currentItem = (TutorialItem) getIntent().getSerializableExtra("TUTORIAL_ITEM");

        if (currentItem != null) {
            currentItemID = currentItem.getId();
            populateData();
        } else {
            Toast.makeText(this, "Error: Could not load project.", Toast.LENGTH_SHORT).show();
            finish();
        }

        backIcon.setOnClickListener(v -> finish());

        editIcon.setOnClickListener(v -> {
            Intent intent = new Intent(ShowProjectDetails.this, UpdateProjectActivity.class);
            intent.putExtra("TUTORIAL_ITEM", currentItem);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        TutorialItem refreshedItem = dbHelper.getTutorial(currentItemID);

        if (refreshedItem != null) {
            currentItem = refreshedItem;
            populateData();
        } else {
            finish();
        }
    }

    private void populateData() {
        titleText.setText(currentItem.getTitle());
        descriptionText.setText(currentItem.getDescription());
        pinConfigText.setText(currentItem.getPinConnection());
        sampleCodeText.setText(currentItem.getSampleCode());

        String imagePath = currentItem.getImageName();
        if (imagePath != null && !imagePath.isEmpty()) {
            Uri imageUri = Uri.parse(imagePath);
            tutorialImage.setImageURI(imageUri);
            tutorialImage.setVisibility(View.VISIBLE);
        } else {
            tutorialImage.setVisibility(View.GONE);
        }
    }
}