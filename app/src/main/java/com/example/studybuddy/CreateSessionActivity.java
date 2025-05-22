package com.example.studybuddy;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateSessionActivity extends AppCompatActivity {

    private TextInputEditText etSubject, etDescription, etLevel, etDate, etLocation;
    private DatabaseReference databaseRef;
    private FirebaseAuth mAuth;
    // Dans la section des déclarations


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_session);

        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("sessions");
        etSubject = findViewById(R.id.etSubject);
        etDescription = findViewById(R.id.etDescription);
        etLevel = findViewById(R.id.etLevel);
        etDate = findViewById(R.id.etDate);
        etLocation = findViewById(R.id.etLocation);
    }

    public void onCreateSession(View view) {
        String creatorId = mAuth.getCurrentUser().getUid();

        StudySession session = new StudySession(
                etSubject.getText().toString(),
                etDescription.getText().toString(),
                etLevel.getText().toString(),
                etDate.getText().toString(),
                etLocation.getText().toString(),
                creatorId
        );

        databaseRef.push().setValue(session)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Session créée!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Erreur: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}