package com.example.arduinolab;

import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ShowDetails extends AppCompatActivity {

    ImageView backIcon, tutorialImage, editIcon;
    TextView titleText, descriptionText, pinConfigText, sampleCodeText;

    DBHelper dbHelper;
    TutorialItem currentItem;
    int currentItemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        backIcon = findViewById(R.id.backToListIconID);
        editIcon = findViewById(R.id.editIconID);
        tutorialImage = findViewById(R.id.viewImageID);
        titleText = findViewById(R.id.titleNameID);
        descriptionText = findViewById(R.id.descriptionTextID);
        pinConfigText = findViewById(R.id.pinConfigurationTextID);
        sampleCodeText = findViewById(R.id.codeTextID);

        dbHelper = new DBHelper(this);

        Intent intent = getIntent();
        currentItem = (TutorialItem) intent.getSerializableExtra("TUTORIAL_ITEM");

        if (currentItem != null) {
            currentItemID = currentItem.getId();
            titleText.setText(currentItem.getTitle());
            descriptionText.setText(currentItem.getDescription());
            pinConfigText.setText(currentItem.getPinConnection());
            sampleCodeText.setText(currentItem.getSampleCode());

        } else {
            Toast.makeText(this, "Error: Could not load tutorial.", Toast.LENGTH_SHORT).show();
            finish();
        }

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowDetails.this, UpdateTutorial.class);
                intent.putExtra("TUTORIAL_ITEM", currentItem);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Re-fetch the item from the database using its ID
        TutorialItem refreshedItem = dbHelper.getTutorial(currentItemID);

        if (refreshedItem != null) {
            currentItem = refreshedItem;  // If item was UPDATED, set it as the new 'currentItem'
            titleText.setText(currentItem.getTitle());
            descriptionText.setText(currentItem.getDescription());
            pinConfigText.setText(currentItem.getPinConnection());
            sampleCodeText.setText(currentItem.getSampleCode());
            String imagePath = currentItem.getImageName();
            if (imagePath != null && !imagePath.isEmpty()) {
                Uri imageUri = Uri.parse(imagePath);   // If we have a path, parse it to a URI and set it
                tutorialImage.setImageURI(imageUri);
                tutorialImage.setVisibility(View.VISIBLE);
            } else {
                tutorialImage.setVisibility(View.GONE);   // No image was saved, so hide the ImageView
            }
        } else {
            finish();  // item was DELETED, it will be 'null'. So we close this page and go back to the list.
        }
    }
}