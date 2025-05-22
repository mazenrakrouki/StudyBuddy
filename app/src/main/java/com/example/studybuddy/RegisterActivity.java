package com.example.studybuddy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;

    private TextInputLayout emailLayout, passwordLayout, confirmPasswordLayout;
    private TextInputEditText emailEditText, passwordEditText, confirmPasswordEditText;
    private RadioGroup roleRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialisation des composants UI
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        roleRadioGroup = findViewById(R.id.roleRadioGroup);

        // Configuration du ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Création du compte...");
        progressDialog.setCancelable(false);

        // Écouteurs de modification de texte pour la validation en temps réel
        setupTextWatchers();

        findViewById(R.id.registerButton).setOnClickListener(v -> attemptRegistration());
    }

    private void setupTextWatchers() {
        TextWatcher validationWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clearErrors();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        emailEditText.addTextChangedListener(validationWatcher);
        passwordEditText.addTextChangedListener(validationWatcher);
        confirmPasswordEditText.addTextChangedListener(validationWatcher);
    }

    private void clearErrors() {
        emailLayout.setError(null);
        passwordLayout.setError(null);
        confirmPasswordLayout.setError(null);
    }

    private void attemptRegistration() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String role = getSelectedRole();

        if (validateForm(email, password, confirmPassword)) {
            progressDialog.show();
            createUser(email, password, role);
        }
    }

    private boolean validateForm(String email, String password, String confirmPassword) {
        boolean isValid = true;

        // Validation email
        if (email.isEmpty()) {
            emailLayout.setError("L'email est obligatoire");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Format d'email invalide");
            isValid = false;
        }

        // Validation mot de passe
        if (password.isEmpty()) {
            passwordLayout.setError("Le mot de passe est obligatoire");
            isValid = false;
        } else if (password.length() < 6) {
            passwordLayout.setError("6 caractères minimum");
            isValid = false;
        }

        // Validation confirmation mot de passe
        if (confirmPassword.isEmpty()) {
            confirmPasswordLayout.setError("Confirmation requise");
            isValid = false;
        } else if (!confirmPassword.equals(password)) {
            confirmPasswordLayout.setError("Les mots de passe ne correspondent pas");
            isValid = false;
        }

        return isValid;
    }

    private String getSelectedRole() {
        int selectedId = roleRadioGroup.getCheckedRadioButtonId();
        return selectedId == R.id.tutorRadio ? "tuteur" : "étudiant";
    }

    private void createUser(String email, String password, String role) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Enregistrement réussi
                        FirebaseUser user = mAuth.getCurrentUser();

                        // Créer un objet User
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("email", email);
                        userData.put("role", role);
                        userData.put("createdAt", ServerValue.TIMESTAMP);

                        // Envoyer à la base de données
                        mDatabase.child("users").child(user.getUid())
                                .setValue(userData)
                                .addOnCompleteListener(dbTask -> {
                                    if (dbTask.isSuccessful()) {
                                        // Redirection
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this,
                                                "Erreur d'enregistrement des données",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        // Gérer les erreurs
                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                        showRegistrationError(errorCode);
                    }
                });
    }
    private void showRegistrationError(String errorCode) {
        switch (errorCode) {
            case "ERROR_INVALID_EMAIL":
                emailEditText.setError("Format d'email invalide");
                break;
            case "ERROR_EMAIL_ALREADY_IN_USE":
                emailEditText.setError("Cet email est déjà utilisé");
                break;
            case "ERROR_WEAK_PASSWORD":
                passwordEditText.setError("Le mot de passe doit faire au moins 6 caractères");
                break;
            default:
                Toast.makeText(this, "Erreur d'inscription", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserToDatabase(String role) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            DatabaseReference userRef = mDatabase.child("users").child(user.getUid());
            userRef.child("email").setValue(user.getEmail());
            userRef.child("role").setValue(role);
            userRef.child("createdAt").setValue(System.currentTimeMillis());
        }
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, SessionsListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}