package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentDashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StudentSessionAdapter adapter;
    private DatabaseReference databaseRef;
    private List<StudySession> sessions;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        // Initialisation Firebase
        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("sessions");
        sessions = new ArrayList<>();

        // Configuration RecyclerView
        recyclerView = findViewById(R.id.recyclerViewSessions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialisation de l'adapter
        // Dans onCreate()
        adapter = new StudentSessionAdapter(
                sessions,
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                this::onJoinSessionClicked,this);
        recyclerView.setAdapter(adapter);

        // Bouton de déconnexion
        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> logoutUser());

        // Chargement des sessions
        loadSessions();
    }

    private void loadSessions() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sessions.clear();
                for (DataSnapshot sessionSnapshot : snapshot.getChildren()) {
                    StudySession session = sessionSnapshot.getValue(StudySession.class);
                    if (session != null) {
                        session.setId(sessionSnapshot.getKey());
                        sessions.add(session);
                    }
                }
                adapter.notifyDataSetChanged(); // Rafraîchir l'adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentDashboardActivity.this,
                        "Erreur de chargement: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onJoinSessionClicked(StudySession session) {
        String userId = mAuth.getCurrentUser().getUid();

        // Vérifier si déjà inscrit
        if (!session.getParticipants().containsKey(userId)) {
            databaseRef.child(session.getId()).child("participants").child(userId)
                    .setValue(true)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Session rejointe!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Erreur: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void logoutUser() {
        mAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finishAffinity();
    }
}