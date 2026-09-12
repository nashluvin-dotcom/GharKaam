package com.gharkaam.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.Button;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.View;

public class MainActivity extends Activity {

    private Button workerButton;


    private final int PURPLE = Color.rgb(108, 77, 255);
    private final int DARK = Color.rgb(35, 35, 45);
    private final int GREY = Color.rgb(105, 105, 115);

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private TextView makeText(String value, float size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);

        if (bold) {
            t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        }

        return t;
    }

    private GradientDrawable background(int color, float radius) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(dp(radius));
        return g;
    }

    private void space(LinearLayout parent, int height) {
        View v = new View(this);
        parent.addView(v, new LinearLayout.LayoutParams(1, dp(height)));
    }

    private LinearLayout trustBox(String icon, String title) {

        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setGravity(Gravity.CENTER);

        TextView iconView = makeText(icon, 22, DARK, false);
        iconView.setGravity(Gravity.CENTER);

        box.addView(iconView,
                new LinearLayout.LayoutParams(-1, dp(32)));

        TextView titleView = makeText(title, 11, GREY, true);
        titleView.setGravity(Gravity.CENTER);

        box.addView(titleView,
                new LinearLayout.LayoutParams(-1, dp(38)));

        return box;
    }

    private void refreshWorkerButton() {

        String accountRole = getSharedPreferences(
                "GharKaamAccount",
                MODE_PRIVATE
        ).getString("role", "");

        boolean isWorker =
                "worker".equalsIgnoreCase(accountRole);

        boolean hasWorkerProfile =
                getSharedPreferences(
                        "GharKaamWorker",
                        MODE_PRIVATE
                ).getBoolean("profileSaved", false);

        boolean isRegisteredWorker =
                isWorker && hasWorkerProfile;

        if (workerButton != null) {
            if (isRegisteredWorker) {
                workerButton.setText("My Worker Profile");
            } else {
                workerButton.setText("Register as Worker");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshWorkerButton();
    }

    private void deleteAuthAccount(
            com.google.firebase.auth.FirebaseUser user
    ) {

        user.delete()
                .addOnSuccessListener(result -> {

                    getSharedPreferences(
                            "GharKaamWorker",
                            MODE_PRIVATE
                    ).edit().clear().apply();

                    getSharedPreferences(
                            "GharKaamAccount",
                            MODE_PRIVATE
                    ).edit().clear().apply();

                    Toast.makeText(
                            this,
                            "Account deleted successfully.",
                            Toast.LENGTH_LONG
                    ).show();

                    startActivity(new Intent(
                            MainActivity.this,
                            LoginActivity.class
                    ));

                    finish();
                })
                .addOnFailureListener(e -> {

                    if (e instanceof com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException) {

                        Toast.makeText(
                                this,
                                "For security, please log in again before deleting your account.",
                                Toast.LENGTH_LONG
                        ).show();

                    } else {

                        Toast.makeText(
                                this,
                                "Could not delete login account: "
                                        + e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(Color.rgb(248, 247, 252));

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(20), dp(20), dp(20), dp(30));

        scroll.addView(root);

        // HEADER
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setGravity(Gravity.CENTER);
        header.setPadding(dp(20), dp(22), dp(20), dp(22));
        header.setBackground(background(PURPLE, 24));

        TextView logo = makeText("GharKaam", 31, Color.WHITE, true);
        logo.setGravity(Gravity.CENTER);

        header.addView(logo,
                new LinearLayout.LayoutParams(-1, dp(42)));

        TextView tagline = makeText(
                "Find trusted household help near you",
                15,
                Color.WHITE,
                false
        );
        tagline.setGravity(Gravity.CENTER);

        header.addView(tagline,
                new LinearLayout.LayoutParams(-1, dp(30)));

        root.addView(header,
                new LinearLayout.LayoutParams(-1, dp(105)));

        space(root, 22);

        // WELCOME
        TextView welcome = makeText(
                "Welcome to GharKaam",
                24,
                DARK,
                true
        );
        welcome.setGravity(Gravity.CENTER);

        root.addView(welcome,
                new LinearLayout.LayoutParams(-1, dp(38)));

        TextView description = makeText(
                "Connect households with reliable local workers.",
                14,
                GREY,
                false
        );
        description.setGravity(Gravity.CENTER);

        root.addView(description,
                new LinearLayout.LayoutParams(-1, dp(30)));

        space(root, 20);

        // ACCOUNT ROLE
        String accountRole = getSharedPreferences(
                "GharKaamAccount",
                MODE_PRIVATE
        ).getString("role", "");

        boolean isWorker = "worker".equalsIgnoreCase(accountRole);

        boolean hasWorkerProfile = getSharedPreferences(
                "GharKaamWorker",
                MODE_PRIVATE
        ).getBoolean("profileSaved", false);

        boolean isRegisteredWorker = isWorker && hasWorkerProfile;

        boolean isClient = "client".equalsIgnoreCase(accountRole);

        // WORKER CARD
        LinearLayout workerCard = new LinearLayout(this);
        workerCard.setOrientation(LinearLayout.VERTICAL);
        workerCard.setPadding(dp(20), dp(17), dp(20), dp(17));
        workerCard.setBackground(background(Color.WHITE, 20));
        workerCard.setElevation(dp(3));

        TextView workerTitle = makeText(
                "👩‍🍳  Looking for household work?",
                18,
                DARK,
                true
        );

        workerCard.addView(workerTitle,
                new LinearLayout.LayoutParams(-1, dp(35)));

        TextView workerInfo = makeText(
                "Create your profile and get discovered by nearby clients.",
                13,
                GREY,
                false
        );

        workerCard.addView(workerInfo,
                new LinearLayout.LayoutParams(-1, dp(38)));

        workerButton = new Button(this);
        if (isRegisteredWorker) {
            workerButton.setText("My Worker Profile");
        } else {
            workerButton.setText("Register as Worker");
        }
        workerButton.setTextSize(15);
        workerButton.setTextColor(Color.WHITE);
        workerButton.setAllCaps(false);
        workerButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        workerButton.setBackground(background(PURPLE, 14));
        workerButton.setMinHeight(0);
        workerButton.setMinimumHeight(0);

        workerCard.addView(workerButton,
                new LinearLayout.LayoutParams(-1, dp(52)));

        root.addView(workerCard,
                new LinearLayout.LayoutParams(-1, dp(150)));

        // Client accounts cannot register as workers.
        if (isClient) {
            workerCard.setVisibility(View.GONE);
        }


workerButton.setOnClickListener(v -> {

    String currentRole = getSharedPreferences(
            "GharKaamAccount",
            MODE_PRIVATE
    ).getString("role", "");

    boolean currentIsWorker =
            "worker".equalsIgnoreCase(currentRole);

    boolean currentHasWorkerProfile =
            getSharedPreferences(
                    "GharKaamWorker",
                    MODE_PRIVATE
            ).getBoolean("profileSaved", false);

    boolean currentIsRegisteredWorker =
            currentIsWorker && currentHasWorkerProfile;

    if (currentIsRegisteredWorker) {
        startActivity(new Intent(
                MainActivity.this,
                MyWorkerProfileActivity.class
        ));
    } else {
        startActivity(new Intent(
                MainActivity.this,
                WorkerRegistrationActivity.class
        ));
    }
});



        space(root, 16);

        // CLIENT CARD
        LinearLayout clientCard = new LinearLayout(this);
        clientCard.setOrientation(LinearLayout.VERTICAL);
        clientCard.setPadding(dp(20), dp(17), dp(20), dp(17));
        clientCard.setBackground(background(Color.WHITE, 20));
        clientCard.setElevation(dp(3));

        TextView clientTitle = makeText(
                "🏠  Need a household worker?",
                18,
                DARK,
                true
        );

        clientCard.addView(clientTitle,
                new LinearLayout.LayoutParams(-1, dp(35)));

        TextView clientInfo = makeText(
                "Find maids, cooks, cleaners and other local workers.",
                13,
                GREY,
                false
        );

        clientCard.addView(clientInfo,
                new LinearLayout.LayoutParams(-1, dp(38)));

        Button clientButton = new Button(this);
        clientButton.setText("Find a Worker");
        clientButton.setTextSize(15);
        clientButton.setTextColor(PURPLE);
        clientButton.setAllCaps(false);
        clientButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        clientButton.setBackground(
                background(Color.rgb(238, 234, 255), 14)
        );
        clientButton.setMinHeight(0);
        clientButton.setMinimumHeight(0);

        clientCard.addView(clientButton,
                new LinearLayout.LayoutParams(-1, dp(52)));

        root.addView(clientCard,
                new LinearLayout.LayoutParams(-1, dp(150)));

        // Worker accounts cannot search for workers.
        if (isWorker) {
            clientCard.setVisibility(View.GONE);
        }


        clientButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FindWorkerActivity.class));
        });

        // CLIENT SAFETY / POLICE VERIFICATION
        LinearLayout safetyCard = new LinearLayout(this);
        safetyCard.setOrientation(LinearLayout.VERTICAL);
        safetyCard.setPadding(dp(20), dp(18), dp(20), dp(18));
        safetyCard.setBackground(background(Color.rgb(255, 249, 235), 20));
        safetyCard.setElevation(dp(3));

        TextView safetyTitle = makeText(
                "🛡️  Police Verification & Safety",
                18,
                DARK,
                true
        );

        safetyCard.addView(
                safetyTitle,
                new LinearLayout.LayoutParams(-1, dp(38))
        );

        TextView safetyInfo = makeText(
                "Before hiring a worker, please ask them to bring " +
                "a valid identity document such as Aadhaar or, " +
                "where applicable, PAN/another accepted ID, " +
                "and a latest photograph. Complete the applicable " +
                "police verification before employment.",
                13,
                GREY,
                false
        );

        safetyInfo.setPadding(0, dp(4), 0, dp(10));

        safetyCard.addView(
                safetyInfo,
                new LinearLayout.LayoutParams(-1, dp(100))
        );

        Button formButton = new Button(this);
        formButton.setText("📄 Download Verification Form");
        formButton.setTextSize(14);
        formButton.setAllCaps(false);

        formButton.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        android.net.Uri.parse(
                                "https://citizen.mahapolice.gov.in/Citizen/DownloadOfflineEForm.aspx"
                        )
                );
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(
                        this,
                        "Unable to open verification form.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        safetyCard.addView(
                formButton,
                new LinearLayout.LayoutParams(-1, dp(50))
        );

        Button stationButton = new Button(this);
        stationButton.setText("📍 Find Nearest Police Station");
        stationButton.setTextSize(14);
        stationButton.setAllCaps(false);

        stationButton.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        android.net.Uri.parse(
                                "https://www.google.com/maps/search/?api=1&query=police+station"
                        )
                );
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(
                        this,
                        "Unable to open maps.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        safetyCard.addView(
                stationButton,
                new LinearLayout.LayoutParams(-1, dp(50))
        );

        Button checklistButton = new Button(this);
        checklistButton.setText("✓ Verification Checklist");
        checklistButton.setTextSize(14);
        checklistButton.setAllCaps(false);

        checklistButton.setOnClickListener(v -> {

            new android.app.AlertDialog.Builder(this)
                    .setTitle("Police Verification Checklist")
                    .setMessage(
                            "Before hiring, check the applicable requirements:\n\n" +
                            "☐ Valid identity proof\n" +
                            "   Aadhaar, PAN where applicable, or another accepted ID\n\n" +
                            "☐ Latest photograph\n\n" +
                            "☐ Residential/address proof if required\n\n" +
                            "☐ Complete the applicable police verification process\n\n" +
                            "☐ Keep the verification acknowledgement/record safely\n\n" +
                            "Requirements may vary by police jurisdiction. " +
                            "Confirm the current requirements with the concerned police station."
                    )
                    .setPositiveButton("OK", null)
                    .show();
        });

        safetyCard.addView(
                checklistButton,
                new LinearLayout.LayoutParams(-1, dp(50))
        );

        root.addView(
                safetyCard,
                new LinearLayout.LayoutParams(-1, dp(310))
        );

        space(root, 20);

        // WHY GHARKAAM
        TextView why = makeText(
                "Why GharKaam?",
                19,
                DARK,
                true
        );
        why.setGravity(Gravity.CENTER);

        root.addView(why,
                new LinearLayout.LayoutParams(-1, dp(32)));

        space(root, 8);

        LinearLayout trustRow = new LinearLayout(this);
        trustRow.setOrientation(LinearLayout.HORIZONTAL);
        trustRow.setGravity(Gravity.CENTER);

        trustRow.addView(
                trustBox("🔎", "Easy\nSearch"),
                new LinearLayout.LayoutParams(0, dp(70), 1)
        );

        trustRow.addView(
                trustBox("📍", "Local\nWorkers"),
                new LinearLayout.LayoutParams(0, dp(70), 1)
        );

        trustRow.addView(
                trustBox("🤝", "Simple &\nSafe"),
                new LinearLayout.LayoutParams(0, dp(70), 1)
        );

        root.addView(trustRow,
                new LinearLayout.LayoutParams(-1, dp(70)));

        space(root, 12);

        Button logoutButton = new Button(this);
        logoutButton.setText("Logout");
        logoutButton.setTextSize(14);
        logoutButton.setAllCaps(false);
        logoutButton.setTextColor(PURPLE);
        logoutButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        logoutButton.setBackground(
                background(Color.rgb(238, 234, 255), 14)
        );
        logoutButton.setMinHeight(0);
        logoutButton.setMinimumHeight(0);

        LinearLayout.LayoutParams logoutParams =
                new LinearLayout.LayoutParams(-1, dp(52));
        logoutParams.setMargins(dp(24), dp(10), dp(24), dp(10));

        root.addView(logoutButton, logoutParams);

        logoutButton.setOnClickListener(v -> {
            com.google.firebase.auth.FirebaseAuth.getInstance().signOut();

            startActivity(new Intent(
                    MainActivity.this,
                    LoginActivity.class
            ));
            finish();
        });

        Button deleteAccountButton = new Button(this);
        deleteAccountButton.setText("Delete My Account");
        deleteAccountButton.setTextSize(14);
        deleteAccountButton.setAllCaps(false);
        deleteAccountButton.setTextColor(Color.rgb(190, 40, 40));
        deleteAccountButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        deleteAccountButton.setBackground(
                background(Color.rgb(255, 235, 235), 14)
        );
        deleteAccountButton.setMinHeight(0);
        deleteAccountButton.setMinimumHeight(0);

        LinearLayout.LayoutParams deleteParams =
                new LinearLayout.LayoutParams(-1, dp(52));
        deleteParams.setMargins(dp(24), dp(4), dp(24), dp(10));

        root.addView(deleteAccountButton, deleteParams);

        deleteAccountButton.setOnClickListener(v -> {

            new android.app.AlertDialog.Builder(this)
                    .setTitle("Delete Account?")
                    .setMessage(
                            "This will permanently delete your GharKaam account " +
                            "and worker profile data. This action cannot be undone."
                    )
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Delete", (dialog, which) -> {

                        com.google.firebase.auth.FirebaseAuth auth =
                                com.google.firebase.auth.FirebaseAuth.getInstance();

                        com.google.firebase.auth.FirebaseUser user =
                                auth.getCurrentUser();

                        if (user == null) {
                            Toast.makeText(
                                    this,
                                    "No logged-in account found.",
                                    Toast.LENGTH_LONG
                            ).show();
                            return;
                        }

                        deleteAccountButton.setEnabled(false);
                        deleteAccountButton.setText("Deleting...");

                        String uid = user.getUid();

                        com.google.firebase.firestore.FirebaseFirestore
                                .getInstance()
                                .collection("workers")
                                .whereEqualTo("ownerUid", uid)
                                .get()
                                .addOnSuccessListener(workerSnapshot -> {

                                    com.google.android.gms.tasks.Task<Void>
                                            deleteWorkersTask =
                                            com.google.android.gms.tasks.Tasks.forResult(null);

                                    for (com.google.firebase.firestore.DocumentSnapshot
                                            workerDoc : workerSnapshot.getDocuments()) {

                                        deleteWorkersTask =
                                                com.google.android.gms.tasks.Tasks.whenAll(
                                                        deleteWorkersTask,
                                                        workerDoc.getReference().delete()
                                                );
                                    }

                                    deleteWorkersTask.addOnSuccessListener(workerDeleteResult -> {

                                        String phone =
                                                getSharedPreferences(
                                                        "GharKaamAccount",
                                                        MODE_PRIVATE
                                                ).getString("phone", "");

                                        if (phone.isEmpty()) {
                                            phone = workerSnapshot.isEmpty()
                                                    ? ""
                                                    : workerSnapshot.getDocuments()
                                                            .get(0)
                                                            .getString("phone");
                                        }

                                        final String accountPhone = phone;

                                        if (accountPhone.isEmpty()) {
                                            deleteAuthAccount(user);
                                            return;
                                        }

                                        com.google.firebase.firestore.FirebaseFirestore
                                                .getInstance()
                                                .collection("userAccounts")
                                                .document(accountPhone)
                                                .delete()
                                                .addOnSuccessListener(accountDeleteResult -> {
                                                    deleteAuthAccount(user);
                                                })
                                                .addOnFailureListener(e -> {

                                                    deleteAccountButton.setEnabled(true);
                                                    deleteAccountButton.setText(
                                                            "Delete My Account"
                                                    );

                                                    Toast.makeText(
                                                            this,
                                                            "Could not delete account data: "
                                                                    + e.getMessage(),
                                                            Toast.LENGTH_LONG
                                                    ).show();
                                                });

                                    }).addOnFailureListener(e -> {

                                        deleteAccountButton.setEnabled(true);
                                        deleteAccountButton.setText(
                                                "Delete My Account"
                                        );

                                        Toast.makeText(
                                                this,
                                                "Could not delete worker data: "
                                                        + e.getMessage(),
                                                Toast.LENGTH_LONG
                                        ).show();
                                    });

                                })
                                .addOnFailureListener(e -> {

                                    deleteAccountButton.setEnabled(true);
                                    deleteAccountButton.setText(
                                            "Delete My Account"
                                    );

                                    Toast.makeText(
                                            this,
                                            "Could not find account data: "
                                                    + e.getMessage(),
                                            Toast.LENGTH_LONG
                                    ).show();
                                });
                    })
                    .show();
        });

        TextView footer = makeText(
                "GharKaam • Connecting homes with helpers",
                12,
                Color.rgb(145, 145, 155),
                false
        );
        footer.setGravity(Gravity.CENTER);

        root.addView(footer,
                new LinearLayout.LayoutParams(-1, dp(30)));

        setContentView(scroll);
    }
}
