package com.example.arduinolab;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UpdateProjectActivity extends AppCompatActivity {

    EditText titleEdit, descriptionEdit, pinConfigEdit, sampleCodeEdit;
    ImageView backIcon, deleteIcon, imagePreview;
    Button saveButton;

    DBHelper dbHelper;
    TutorialItem currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_project);

        setContentView(R.layout.activity_update_project);

        backIcon = findViewById(R.id.backToListIconID);
        deleteIcon = findViewById(R.id.deleteIconID);
        saveButton = findViewById(R.id.saveButtonID);

        titleEdit = findViewById(R.id.updateTitleEditID);
        descriptionEdit = findViewById(R.id.updateDescriptionEditID);
        imagePreview = findViewById(R.id.imagePreviewViewID);

        pinConfigEdit = findViewById(R.id.updatePinConfigEditID);
        sampleCodeEdit = findViewById(R.id.updateSampleCodeEditID);

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

        backIcon.setOnClickListener(v -> finish());
        saveButton.setOnClickListener(v -> saveChanges());
        deleteIcon.setOnClickListener(v -> showPopupMenu());
    }

    private void saveChanges() {
        String newTitle = titleEdit.getText().toString().trim();
        String newDesc = descriptionEdit.getText().toString().trim();
        String newPin = pinConfigEdit.getText().toString().trim();
        String newCode = sampleCodeEdit.getText().toString().trim();

        if (newTitle.isEmpty()) {
            titleEdit.setError("Title cannot be empty");
            return;
        }

        String originalImagePath = currentItem.getImageName();
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
        Toast.makeText(this, "Project Updated!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void showPopupMenu() {
        PopupMenu popup = new PopupMenu(this, deleteIcon);
        popup.getMenuInflater().inflate(R.menu.update_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_delete) {
                showDeleteConfirmationDialog();
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Project")
                .setMessage("Are you sure you want to delete this project?")
                .setIcon(R.drawable.deleteicon)
                .setPositiveButton("Delete", (dialog, which) -> {
                    if (currentItem != null) {
                        dbHelper.deleteTutorial(currentItem.getId());
                        Toast.makeText(this, "Project deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}