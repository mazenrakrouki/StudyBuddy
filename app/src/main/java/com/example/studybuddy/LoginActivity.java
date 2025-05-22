package com.example.studybuddy;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private TextInputEditText emailEditText, passwordEditText;
    private RadioGroup roleRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize UI components
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        roleRadioGroup = findViewById(R.id.roleRadioGroup);

        // Login Button Click Listener
        findViewById(R.id.loginButton).setOnClickListener(v -> attemptLogin());

        // Register Text Click Listener
        findViewById(R.id.registerTextView).setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void attemptLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String role = getSelectedRole();

        if (validateInputs(email, password)) {
            signInUser(email, password, role);
        }
    }

    private boolean validateInputs(String email, String password) {
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email invalide");
            return false;
        }
        if (password.isEmpty() || password.length() < 6) {
            passwordEditText.setError("Mot de passe doit contenir au moins 6 caractères");
            return false;
        }
        return true;
    }

    private String getSelectedRole() {
        return roleRadioGroup.getCheckedRadioButtonId() == R.id.tutorRadio ? "tuteur" : "étudiant";
    }

    private void signInUser(String email, String password, String role) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Récupérer les données utilisateur
                        FirebaseUser user = mAuth.getCurrentUser();
                        mDatabase.child("users").child(user.getUid())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String role = snapshot.child("role").getValue(String.class);
                                        redirectBasedOnRole(role);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(LoginActivity.this,
                                                "Erreur de lecture des données",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        // Gérer les erreurs
                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                        showLoginError(errorCode);
                    }
                });
    }
    private void redirectBasedOnRole(String role) {
        Intent intent;
        if (role.equals("tuteur")) {
            intent = new Intent(this, TutorDashboardActivity.class);
        } else {
            intent = new Intent(this, StudentDashboardActivity.class);
        }
        startActivity(intent);
        finish();
    }


    private void updateUserRole(String role) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mDatabase.child("users").child(user.getUid()).child("role").setValue(role);
        }
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, SessionsListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    private void showLoginError(String errorCode) {
        switch (errorCode) {
            case "ERROR_INVALID_EMAIL":
                emailEditText.setError("Email non reconnu");
                break;
            case "ERROR_WRONG_PASSWORD":
                passwordEditText.setError("Mot de passe incorrect");
                break;
            case "ERROR_USER_NOT_FOUND":
                emailEditText.setError("Aucun compte associé à cet email");
                break;
            default:
                Toast.makeText(this, "Échec de la connexion", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Utilisateur déjà connecté
            checkUserRoleAndRedirect(currentUser.getUid());
        }
    }

    private void checkUserRoleAndRedirect(String userId) {
        mDatabase.child("users").child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String role = snapshot.child("role").getValue(String.class);
                        redirectBasedOnRole(role);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LoginActivity.this,
                                "Erreur de lecture du profil",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void logoutUser() {
        mAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}