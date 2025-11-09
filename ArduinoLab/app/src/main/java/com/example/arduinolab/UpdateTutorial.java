package com.example.arduinolab;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UpdateTutorial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        EditText titleEdit, descriptionEdit, pinConfigEdit, sampleCodeEdit;
        ImageView backIcon, deleteIcon, imagePreview;
        Button saveButton;

        DBHelper dbHelper;
        TutorialItem currentItem;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tutorial);

        backIcon = findViewById(R.id.backToListIconID);
        deleteIcon = findViewById(R.id.deleteIconID);
        saveButton = findViewById(R.id.saveButtonID);

        titleEdit = findViewById(R.id.updateTitleEditID);
        descriptionEdit = findViewById(R.id.updateDescriptionEditID);
        pinConfigEdit = findViewById(R.id.updatePinConfigEditID);
        sampleCodeEdit = findViewById(R.id.updateSampleCodeEditID);
        imagePreview = findViewById(R.id.imagePreviewViewID);

        dbHelper = new DBHelper(this);

        currentItem = (TutorialItem) getIntent().getSerializableExtra("TUTORIAL_ITEM");

        if (currentItem != null) {
            titleEdit.setText(currentItem.getTitle());
            descriptionEdit.setText(currentItem.getDescription());
            pinConfigEdit.setText(currentItem.getPinConnection());
            sampleCodeEdit.setText(currentItem.getSampleCode());
            String imagePath = currentItem.getImageName();
            if (imagePath != null && !imagePath.isEmpty()) {
                Uri imageUri = Uri.parse(imagePath);
                imagePreview.setImageURI(imageUri);
                imagePreview.setVisibility(View.VISIBLE);
            } else {
                imagePreview.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(this, "Error loading item.", Toast.LENGTH_SHORT).show();
            finish();
        }

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = titleEdit.getText().toString().trim();
                String newDesc = descriptionEdit.getText().toString().trim();
                String newPin = pinConfigEdit.getText().toString().trim();
                String newCode = sampleCodeEdit.getText().toString().trim();

                if (newTitle.isEmpty()) {
                    titleEdit.setError("Title cannot be empty");
                    return;
                }

                String originalImagePath = currentItem.getImageName();    // We get the OLD image path and save it back.

                TutorialItem updatedItem = new TutorialItem(
                        currentItem.getId(),
                        currentItem.getCategory(),
                        newTitle,
                        newDesc,
                        originalImagePath,
                        newPin,
                        newCode
                );

                dbHelper.updateTutorial(updatedItem);

                Toast.makeText(UpdateTutorial.this, "Tutorial Updated!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Delete Button
        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(UpdateTutorial.this)
                        .setTitle("Delete Tutorial")
                        .setMessage("Are you sure you want to delete this tutorial? This cannot be undone.")
                        .setIcon(R.drawable.deleteicon)

                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // If "Delete" is clicked, run the delete code
                                if (currentItem != null) {
                                    dbHelper.deleteTutorial(currentItem.getId());
                                    Toast.makeText(UpdateTutorial.this, "Tutorial deleted", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        })

                        .setNegativeButton("Cancel", null) // Does nothing
                        .show(); // Show the dialog
            }
        });
    }
}