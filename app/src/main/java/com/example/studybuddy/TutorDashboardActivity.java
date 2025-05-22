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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class TutorDashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TutorSessionAdapter adapter;
    private List<StudySession> mySessions;
    private DatabaseReference databaseRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_dashboard);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseRef = FirebaseDatabase.getInstance().getReference("sessions");

        initializeUI();
        loadMySessions();
    }


    private void initializeUI() {
        recyclerView = findViewById(R.id.recyclerViewMySessions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialiser la liste AVANT l'adapter
        mySessions = new ArrayList<>();

        // Vérifier la création de l'adapter
        // Dans TutorDashboardActivity.java
        adapter = new TutorSessionAdapter(
                mySessions,
                this::onDeleteSessionClicked,
                TutorDashboardActivity.this // Passer le contexte
        );

        recyclerView.setAdapter(adapter);
        // Bouton de création de session
        Button btnCreateSession = findViewById(R.id.btnCreateSession);
        btnCreateSession.setOnClickListener(v ->
                startActivity(new Intent(this, CreateSessionActivity.class)));

        // Bouton de déconnexion
        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> logoutUser());
    }

    private void loadMySessions() {
        Query query = databaseRef.orderByChild("creatorId").equalTo(currentUser.getUid());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mySessions.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    StudySession session = snapshot.getValue(StudySession.class);
                    if (session != null) {
                        session.setId(snapshot.getKey());
                        mySessions.add(session);
                    }
                }
                    adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TutorDashboardActivity.this,
                        "Erreur: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void onDeleteSessionClicked(StudySession session) {
        databaseRef.child(session.getId()).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Session supprimée", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Échec de la suppression", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finishAffinity();
    }
}