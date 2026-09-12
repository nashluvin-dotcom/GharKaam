package com.gharkaam.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.*;

public class MyWorkerProfileActivity extends Activity {

    private final int PURPLE = Color.rgb(108, 77, 255);
    private final int DARK = Color.rgb(35, 35, 45);

    private int dp(int v) {
        return (int)(v * getResources().getDisplayMetrics().density + 0.5f);
    }

    private String get(String key) {
        return getSharedPreferences(
                "GharKaamWorker",
                MODE_PRIVATE
        ).getString(key, "");
    }

    private String safe(String value) {
        return value == null || value.trim().isEmpty()
                ? "Not provided"
                : value;
    }

    private void addProfile(
            LinearLayout content,
            String title,
            String value
    ) {
        TextView t = new TextView(this);
        t.setText(title + "\n" + safe(value));
        t.setTextSize(16);
        t.setTextColor(DARK);
        t.setPadding(
                dp(16),
                dp(12),
                dp(16),
                dp(12)
        );
        t.setBackgroundColor(Color.WHITE);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(-1, -2);

        p.setMargins(0, 0, 0, dp(8));
        content.addView(t, p);
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            android.content.Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001 &&
                resultCode == RESULT_OK) {

            recreate();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(248, 247, 252));

        TextView header = new TextView(this);
        header.setText("My Worker Profile");
        header.setTextSize(24);
        header.setTextColor(Color.WHITE);
        header.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );
        header.setGravity(Gravity.CENTER);
        header.setBackgroundColor(PURPLE);

        root.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(80)
                )
        );

        ScrollView scroll = new ScrollView(this);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(
                dp(16),
                dp(16),
                dp(16),
                dp(20)
        );

        boolean profileSaved = getSharedPreferences(
                "GharKaamWorker",
                MODE_PRIVATE
        ).getBoolean("profileSaved", false);

        if (!profileSaved) {

            TextView status = new TextView(this);
            status.setText(
                    "No worker profile found.\n\n" +
                    "Please register as a Worker first."
            );
            status.setTextSize(17);
            status.setTextColor(DARK);
            status.setGravity(Gravity.CENTER);
            status.setPadding(
                    dp(16),
                    dp(40),
                    dp(16),
                    dp(40)
            );

            content.addView(status);

        } else {

            addProfile(content, "Name", get("name"));
            addProfile(content, "Phone", get("phone"));
            addProfile(content, "Age", get("age"));
            addProfile(content, "Gender", get("gender"));
            addProfile(content, "Work Type", get("workType"));
            addProfile(content, "Experience", get("experience"));

            String salary = get("salary");
            addProfile(
                    content,
                    "Expected Monthly Salary",
                    salary.isEmpty()
                            ? "Not provided"
                            : "₹" + salary
            );

            addProfile(content, "Working Hours", get("hours"));

            String location =
                    safe(get("area")) +
                    ", " +
                    safe(get("city"));

            addProfile(content, "Location", location);
            addProfile(content, "Languages", get("languages"));
            addProfile(content, "Availability", get("availability"));
            addProfile(content, "About You", get("about"));
        }

        scroll.addView(content);

        root.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        Button editButton = new Button(this);
        editButton.setText("Edit Profile");
        editButton.setTextSize(16);
        editButton.setTextColor(Color.WHITE);
        editButton.setAllCaps(false);
        editButton.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        GradientDrawable editBg = new GradientDrawable();
        editBg.setColor(PURPLE);
        editBg.setCornerRadius(dp(16));
        editButton.setBackground(editBg);

        LinearLayout.LayoutParams editParams =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(56)
                );

        editParams.setMargins(
                dp(16),
                dp(8),
                dp(16),
                dp(16)
        );

        root.addView(editButton, editParams);

        editButton.setOnClickListener(v -> {
            startActivityForResult(
                    new android.content.Intent(
                            MyWorkerProfileActivity.this,
                            EditWorkerProfileActivity.class
                    ),
                    1001
            );
        });

        setContentView(root);
    }
}
