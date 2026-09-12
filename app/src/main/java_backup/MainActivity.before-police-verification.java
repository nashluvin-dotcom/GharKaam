package com.gharkaam.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.View;

public class MainActivity extends Activity {

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

        Button workerButton = new Button(this);
        if (isWorker) {
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

    if (isWorker) {
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

        space(root, 24);

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
