package com.example.arduinolab;

import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
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

    ImageView backIcon, deleteIcon, tutorialImage;
    TextView titleText, descriptionText, pinConfigText, sampleCodeText;

    DBHelper dbHelper;
    TutorialItem currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        backIcon = findViewById(R.id.backToListIconID);
        deleteIcon = findViewById(R.id.deleteIconID);
        tutorialImage = findViewById(R.id.viewImageID);
        titleText = findViewById(R.id.titleNameID);
        descriptionText = findViewById(R.id.descriptionTextID);
        pinConfigText = findViewById(R.id.pinConfigurationTextID);
        sampleCodeText = findViewById(R.id.codeTextID);

        Intent intent = getIntent();
        currentItem = (TutorialItem) intent.getSerializableExtra("TUTORIAL_ITEM");

        if (currentItem != null) {
            titleText.setText(currentItem.getTitle());
            descriptionText.setText(currentItem.getDescription());
            pinConfigText.setText(currentItem.getPinConnection());
            sampleCodeText.setText(currentItem.getSampleCode());


            int imageResId = getDrawableResourceId(this, currentItem.getImageName());
            if (imageResId != 0) {
                tutorialImage.setImageResource(imageResId);
            } else {
                tutorialImage.setImageResource(R.drawable.ic_launcher_background);
            }

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

        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });
    }

        private void showDeleteConfirmationDialog() {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Tutorial")
                    .setMessage("Are you sure you want to delete this tutorial? This cannot be undone.")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteCurrentTutorial();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .setIcon(R.drawable.deleteicon)
                    .show();
        }


        private void deleteCurrentTutorial() {
            if (currentItem != null) {
                dbHelper.deleteTutorial(currentItem.getId());
                Toast.makeText(this, "Tutorial deleted", Toast.LENGTH_SHORT).show();
                finish();
            }
        }


        public static int getDrawableResourceId(Context context, String resourceName) {
            try {
                return context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
}