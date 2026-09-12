package com.gharkaam.app;
import android.content.Intent;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.Gravity;
import android.widget.*;

public class WorkerProfileActivity extends Activity {

    private int purple = Color.rgb(108,77,255);
    private int dark = Color.rgb(35,35,45);

    private int dp(int v) {
        return (int)(v * getResources().getDisplayMetrics().density + 0.5f);
    }

    private TextView section(String title, String value) {

        TextView t = new TextView(this);

        t.setText(title + "\n" + value);
        t.setTextSize(16);
        t.setTextColor(dark);
        t.setPadding(dp(16),dp(12),dp(16),dp(12));
        t.setBackgroundColor(Color.WHITE);

        return t;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String name = getIntent().getStringExtra("name");
        String work = getIntent().getStringExtra("work");
        String area = getIntent().getStringExtra("area");
        String city = getIntent().getStringExtra("city");
        String experience = getIntent().getStringExtra("experience");
        String salary = getIntent().getStringExtra("salary");
        String availability = getIntent().getStringExtra("availability");
        String languages = getIntent().getStringExtra("languages");
        String about = getIntent().getStringExtra("about");
        String photoBase64 = getIntent().getStringExtra("photoBase64");

        if (name == null) name = "Worker";
        if (work == null) work = "";
        if (area == null) area = "";
        if (city == null) city = "";
        if (experience == null) experience = "";
        if (salary == null) salary = "";
        if (availability == null) availability = "";
        if (languages == null) languages = "";
        if (about == null) about = "";

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(248,247,252));

        TextView header = new TextView(this);
        header.setText(name);
        header.setTextSize(26);
        header.setTextColor(Color.WHITE);
        header.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        header.setGravity(Gravity.CENTER);
        header.setBackgroundColor(purple);

        root.addView(header,new LinearLayout.LayoutParams(-1,dp(80)));

        ScrollView scroll = new ScrollView(this);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(16),dp(16),dp(16),dp(20));

        // Worker profile photo
        ImageView profilePhoto = new ImageView(this);
        profilePhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);

        GradientDrawable photoBg = new GradientDrawable();
        photoBg.setColor(Color.WHITE);
        photoBg.setCornerRadius(dp(60));
        profilePhoto.setBackground(photoBg);
        profilePhoto.setPadding(dp(3),dp(3),dp(3),dp(3));

        LinearLayout.LayoutParams photoParams =
                new LinearLayout.LayoutParams(dp(120),dp(120));

        photoParams.gravity = Gravity.CENTER_HORIZONTAL;
        photoParams.setMargins(0,0,0,dp(14));

        boolean photoShown = false;

        if (photoBase64 != null && !photoBase64.isEmpty()) {
            try {
                byte[] imageBytes =
                        Base64.decode(photoBase64, Base64.DEFAULT);

                Bitmap bitmap =
                        BitmapFactory.decodeByteArray(
                                imageBytes,
                                0,
                                imageBytes.length
                        );

                if (bitmap != null) {
                    profilePhoto.setImageBitmap(bitmap);
                    photoShown = true;
                }
            } catch (Exception ignored) {
            }
        }

        if (!photoShown) {
            profilePhoto.setImageResource(
                    android.R.drawable.ic_menu_camera
            );
        }

        content.addView(profilePhoto, photoParams);

        TextView intro = new TextView(this);
        intro.setText("Worker Profile");
        intro.setTextSize(21);
        intro.setTextColor(purple);
        intro.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        intro.setPadding(dp(5),0,dp(5),dp(12));

        content.addView(intro);

        content.addView(section("Work Type",work));
        content.addView(section("Location",area + ", " + city));
        content.addView(section("Experience",experience));
        content.addView(section("Expected Monthly Salary","₹" + salary));
        content.addView(section("Working Hours",
                getIntent().getStringExtra("hours") == null
                        ? ""
                        : getIntent().getStringExtra("hours")));
        content.addView(section("Languages",languages));
        content.addView(section("Availability",availability));
        content.addView(section("About",about));

        scroll.addView(content);

        root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));

        Button contact = new Button(this);
        contact.setText("Contact Worker");
        contact.setTextSize(16);
        contact.setTextColor(Color.WHITE);
        contact.setAllCaps(false);

        GradientDrawable buttonBg = new GradientDrawable();
        buttonBg.setColor(purple);
        buttonBg.setCornerRadius(dp(16));

        contact.setBackground(buttonBg);

        LinearLayout.LayoutParams contactParams =
                new LinearLayout.LayoutParams(-1,dp(56));

        contactParams.setMargins(dp(16),dp(8),dp(16),dp(16));

        root.addView(contact,contactParams);

        contact.setOnClickListener(v -> {

            String phone = getIntent().getStringExtra("phone");
            if (phone == null) phone = "";

            final String workerPhone = phone;

            new android.app.AlertDialog.Builder(this)
                    .setTitle("Contact Worker")
                    .setMessage(
                            workerPhone.isEmpty()
                                    ? "Phone number is not available."
                                    : "Phone: " + workerPhone
                    )
                    .setPositiveButton("Call Worker", (dialog, which) -> {

                        if (!workerPhone.isEmpty()) {
                            Intent callIntent = new Intent(
                                    Intent.ACTION_DIAL,
                                    android.net.Uri.parse("tel:" + workerPhone)
                            );
                            startActivity(callIntent);
                        }

                    })
                    .setNegativeButton("Close", null)
                    .show();


        });

        setContentView(root);
    }
}
