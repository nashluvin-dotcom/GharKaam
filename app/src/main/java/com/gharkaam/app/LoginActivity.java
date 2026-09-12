package com.gharkaam.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LoginActivity extends Activity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private EditText phoneInput;
    private EditText usernameInput;
    private EditText passwordInput;
    private Spinner roleSpinner;

    private Button loginButton;
    private Button createButton;

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private TextView text(String value, float size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);

        if (bold) {
            t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        }

        return t;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(dp(24), dp(30), dp(24), dp(24));
        root.setBackgroundColor(Color.rgb(248, 247, 252));

        TextView title = text(
                "GharKaam",
                30,
                Color.rgb(108, 77, 255),
                true
        );
        title.setGravity(Gravity.CENTER);

        root.addView(title,
                new LinearLayout.LayoutParams(-1, dp(55)));

        TextView subtitle = text(
                "Login / Create Account",
                19,
                Color.rgb(45, 45, 55),
                true
        );
        subtitle.setGravity(Gravity.CENTER);

        root.addView(subtitle,
                new LinearLayout.LayoutParams(-1, dp(45)));

        TextView info = text(
                "Choose your account type before creating your account.",
                14,
                Color.GRAY,
                false
        );
        info.setGravity(Gravity.CENTER);

        root.addView(info,
                new LinearLayout.LayoutParams(-1, dp(50)));

        phoneInput = new EditText(this);
        phoneInput.setHint("10-digit Phone Number");
        phoneInput.setSingleLine(true);
        phoneInput.setInputType(InputType.TYPE_CLASS_PHONE);

        root.addView(phoneInput,
                new LinearLayout.LayoutParams(-1, dp(58)));

        usernameInput = new EditText(this);
        usernameInput.setHint("Username");
        usernameInput.setSingleLine(true);

        LinearLayout.LayoutParams usernameParams =
                new LinearLayout.LayoutParams(-1, dp(58));
        usernameParams.topMargin = dp(10);

        root.addView(usernameInput, usernameParams);

        passwordInput = new EditText(this);
        passwordInput.setHint("Password");
        passwordInput.setSingleLine(true);
        passwordInput.setInputType(
                InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD
        );

        LinearLayout.LayoutParams passwordParams =
                new LinearLayout.LayoutParams(-1, dp(58));
        passwordParams.topMargin = dp(10);

        root.addView(passwordInput, passwordParams);

        TextView roleLabel = text(
                "Account Type",
                14,
                Color.DKGRAY,
                true
        );

        LinearLayout.LayoutParams roleLabelParams =
                new LinearLayout.LayoutParams(-1, dp(35));
        roleLabelParams.topMargin = dp(12);

        root.addView(roleLabel, roleLabelParams);

        roleSpinner = new Spinner(this);

        String[] roles = {
                "Worker",
                "Client"
        };

        ArrayAdapter<String> roleAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        roles
                );

        roleSpinner.setAdapter(roleAdapter);

        root.addView(
                roleSpinner,
                new LinearLayout.LayoutParams(-1, dp(52))
        );

        loginButton = new Button(this);
        loginButton.setText("Login");

        LinearLayout.LayoutParams loginParams =
                new LinearLayout.LayoutParams(-1, dp(58));
        loginParams.topMargin = dp(20);

        root.addView(loginButton, loginParams);

        createButton = new Button(this);
        createButton.setText("Create Account");

        LinearLayout.LayoutParams createParams =
                new LinearLayout.LayoutParams(-1, dp(58));
        createParams.topMargin = dp(10);

        root.addView(createButton, createParams);

        loginButton.setOnClickListener(v -> loginUser());
        createButton.setOnClickListener(v -> createAccount());

        setContentView(root);
    }

    private String getPhone() {
        return phoneInput.getText()
                .toString()
                .trim()
                .replace(" ", "")
                .replace("-", "");
    }

    private String getUsername() {
        return usernameInput.getText()
                .toString()
                .trim()
                .toLowerCase(Locale.US);
    }

    private String getPassword() {
        return passwordInput.getText().toString();
    }

    private String getRole() {
        return roleSpinner.getSelectedItem()
                .toString()
                .toLowerCase(Locale.US);
    }

    private String firebaseEmail(String username) {
        return username + "@gharkaam.local";
    }

    private boolean validate(
            String phone,
            String username,
            String password
    ) {

        if (!phone.matches("[0-9]{10}")) {
            phoneInput.setError("Enter a valid 10-digit phone number.");
            phoneInput.requestFocus();

            Toast.makeText(
                    this,
                    "Enter a valid 10-digit phone number.",
                    Toast.LENGTH_LONG
            ).show();

            return false;
        }

        if (username.length() < 4) {
            usernameInput.setError(
                    "Username must be at least 4 characters."
            );
            usernameInput.requestFocus();

            Toast.makeText(
                    this,
                    "Username must be at least 4 characters.",
                    Toast.LENGTH_LONG
            ).show();

            return false;
        }

        if (!username.matches("[a-z0-9._]+")) {
            usernameInput.setError(
                    "Username can use letters, numbers, dot and underscore."
            );
            usernameInput.requestFocus();

            Toast.makeText(
                    this,
                    "Username can use letters, numbers, dot and underscore.",
                    Toast.LENGTH_LONG
            ).show();

            return false;
        }

        if (password.length() < 6) {
            passwordInput.setError(
                    "Password must be at least 6 characters."
            );
            passwordInput.requestFocus();

            Toast.makeText(
                    this,
                    "Password must be at least 6 characters.",
                    Toast.LENGTH_LONG
            ).show();

            return false;
        }

        return true;
    }

    private void createAccount() {

        String phone = getPhone();
        String username = getUsername();
        String password = getPassword();
        String role = getRole();

        if (!validate(phone, username, password)) {
            return;
        }

        createButton.setEnabled(false);
        createButton.setText("Creating...");

        // First create/authenticate the Firebase user.
        // Firestore requires request.auth != null.
        auth.createUserWithEmailAndPassword(
                firebaseEmail(username),
                password
        ).addOnSuccessListener(result -> {

            if (auth.getCurrentUser() == null) {
                createButton.setEnabled(true);
                createButton.setText("Create Account");

                Toast.makeText(
                        this,
                        "Account created but user session was not found.",
                        Toast.LENGTH_LONG
                ).show();
                return;
            }

            String uid = auth.getCurrentUser().getUid();

            createButton.setText("Checking Phone...");

            // Now the user is authenticated, so Firestore read is allowed.
            db.collection("userAccounts")
                    .document(phone)
                    .get()
                    .addOnSuccessListener(snapshot -> {

                        if (snapshot.exists()) {

                            String existingRole =
                                    snapshot.getString("role");

                            if (existingRole == null) {
                                existingRole = "account";
                            }

                            final String finalExistingRole = existingRole;

                            // Remove the Firebase account we just created,
                            // because this phone number is already registered.
                            auth.getCurrentUser()
                                    .delete()
                                    .addOnCompleteListener(deleteTask -> {

                                        createButton.setEnabled(true);
                                        createButton.setText("Create Account");

                                        Toast.makeText(
                                                this,
                                                "This phone number is already registered as a "
                                                        + finalExistingRole + ".",
                                                Toast.LENGTH_LONG
                                        ).show();
                                    });

                            return;
                        }

                        createButton.setText("Saving...");

                        Map<String, Object> account =
                                new HashMap<>();

                        account.put("ownerUid", uid);
                        account.put("phone", phone);
                        account.put("username", username);
                        account.put("role", role);
                        account.put(
                                "createdAt",
                                System.currentTimeMillis()
                        );

                        db.collection("userAccounts")
                                .document(phone)
                                .set(account)
                                .addOnSuccessListener(saveResult -> {

                                    getSharedPreferences(
                                            "GharKaamWorker",
                                            MODE_PRIVATE
                                    ).edit().clear().apply();

                                    getSharedPreferences(
                                            "GharKaamAccount",
                                            MODE_PRIVATE
                                    ).edit()
                                            .putString("phone", phone)
                                            .putString("username", username)
                                            .putString("role", role)
                                            .putBoolean("workerRegistered", false)
                                            .apply();

                                    Toast.makeText(
                                            this,
                                            "Account created successfully!",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    startActivity(
                                            new android.content.Intent(
                                                    LoginActivity.this,
                                                    MainActivity.class
                                            )
                                    );

                                    finish();
                                })
                                .addOnFailureListener(e -> {

                                    createButton.setEnabled(true);
                                    createButton.setText("Create Account");

                                    Toast.makeText(
                                            this,
                                            "Could not save account: "
                                                    + e.getMessage(),
                                            Toast.LENGTH_LONG
                                    ).show();
                                });
                    })
                    .addOnFailureListener(e -> {

                        createButton.setEnabled(true);
                        createButton.setText("Create Account");

                        // Delete the newly created Firebase account
                        // if the Firestore check itself failed.
                        if (auth.getCurrentUser() != null) {
                            auth.getCurrentUser()
                                    .delete()
                                    .addOnCompleteListener(deleteTask -> {

                                        Toast.makeText(
                                                this,
                                                "Could not check phone number: "
                                                        + e.getMessage(),
                                                Toast.LENGTH_LONG
                                        ).show();
                                    });
                        } else {
                            Toast.makeText(
                                    this,
                                    "Could not check phone number: "
                                            + e.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });

        }).addOnFailureListener(e -> {

            createButton.setEnabled(true);
            createButton.setText("Create Account");

            Toast.makeText(
                    this,
                    "Could not create account: " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        });
    }

    private void loginUser() {

        String phone = getPhone();
        String username = getUsername();
        String password = getPassword();

        if (!validate(phone, username, password)) {
            return;
        }

        loginButton.setEnabled(false);
        loginButton.setText("Logging in...");

        auth.signInWithEmailAndPassword(
                firebaseEmail(username),
                password
        ).addOnSuccessListener(result -> {

            db.collection("userAccounts")
                    .document(phone)
                    .get()
                    .addOnSuccessListener(snapshot -> {

                        if (!snapshot.exists()) {

                            auth.signOut();

                            loginButton.setEnabled(true);
                            loginButton.setText("Login");

                            Toast.makeText(
                                    this,
                                    "Phone number is not registered for this account.",
                                    Toast.LENGTH_LONG
                            ).show();

                            return;
                        }

                        String storedUid =
                                snapshot.getString("ownerUid");

                        String currentUid =
                                auth.getCurrentUser() != null
                                        ? auth.getCurrentUser().getUid()
                                        : "";

                        if (storedUid == null ||
                                !storedUid.equals(currentUid)) {

                            auth.signOut();

                            loginButton.setEnabled(true);
                            loginButton.setText("Login");

                            Toast.makeText(
                                    this,
                                    "Phone number does not match this account.",
                                    Toast.LENGTH_LONG
                            ).show();

                            return;
                        }

                        String role =
                                snapshot.getString("role");

                        if (role == null) {
                            role = "client";
                        }

                        getSharedPreferences(
                                "GharKaamAccount",
                                MODE_PRIVATE
                        ).edit()
                                .putString("phone", phone)
                                .putString("username", username)
                                .putString("role", role)
                                .apply();

                        Toast.makeText(
                                this,
                                "Login successful!",
                                Toast.LENGTH_LONG
                        ).show();

                        startActivity(
                                new android.content.Intent(
                                        LoginActivity.this,
                                        MainActivity.class
                                )
                        );

                        finish();

                    })
                    .addOnFailureListener(e -> {

                        auth.signOut();

                        loginButton.setEnabled(true);
                        loginButton.setText("Login");

                        Toast.makeText(
                                this,
                                "Could not load account: "
                                        + e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    });

        }).addOnFailureListener(e -> {

            loginButton.setEnabled(true);
            loginButton.setText("Login");

            Toast.makeText(
                    this,
                    "Invalid username or password.",
                    Toast.LENGTH_LONG
            ).show();
        });
    }
}
