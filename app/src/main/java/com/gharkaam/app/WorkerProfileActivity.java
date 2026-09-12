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

        // CLIENT SAFETY NOTICE
        LinearLayout noticeCard = new LinearLayout(this);
        noticeCard.setOrientation(LinearLayout.VERTICAL);
        noticeCard.setPadding(
                dp(16), dp(14), dp(16), dp(14)
        );
        noticeCard.setBackgroundColor(Color.rgb(255, 249, 235));

        TextView noticeTitle = new TextView(this);
        noticeTitle.setText("⚠️ Safety Notice");
        noticeTitle.setTextSize(17);
        noticeTitle.setTextColor(purple);
        noticeTitle.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        noticeCard.addView(
                noticeTitle,
                new LinearLayout.LayoutParams(-1, dp(32))
        );

        TextView noticeText = new TextView(this);
        noticeText.setText(
                "Please independently verify the worker's identity, " +
                "experience, references, and suitability before hiring. " +
                "Consider discussing the work details and meeting safely " +
                "before making a final decision."
        );
        noticeText.setTextSize(13);
        noticeText.setTextColor(Color.rgb(80, 80, 90));

        noticeCard.addView(
                noticeText,
                new LinearLayout.LayoutParams(-1, dp(72))
        );

        LinearLayout.LayoutParams noticeParams =
                new LinearLayout.LayoutParams(-1, -2);

        noticeParams.setMargins(
                0, dp(8), 0, dp(12)
        );

        content.addView(noticeCard, noticeParams);

        // POLICE VERIFICATION / CLIENT SAFETY
        LinearLayout safetyCard = new LinearLayout(this);
        safetyCard.setOrientation(LinearLayout.VERTICAL);
        safetyCard.setPadding(dp(16), dp(16), dp(16), dp(16));
        safetyCard.setBackgroundColor(Color.rgb(255, 249, 235));

        TextView safetyTitle = new TextView(this);
        safetyTitle.setText("🛡️ Police Verification & Safety");
        safetyTitle.setTextSize(18);
        safetyTitle.setTextColor(purple);
        safetyTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        safetyCard.addView(
                safetyTitle,
                new LinearLayout.LayoutParams(-1, dp(38))
        );

        // Verification recommended badge
        TextView verificationBadge = new TextView(this);
        verificationBadge.setText("✓  POLICE VERIFICATION RECOMMENDED");
        verificationBadge.setTextSize(12);
        verificationBadge.setTextColor(Color.rgb(35, 110, 65));
        verificationBadge.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        verificationBadge.setGravity(Gravity.CENTER_VERTICAL);
        verificationBadge.setPadding(
                dp(10), dp(4), dp(10), dp(4)
        );

        GradientDrawable badgeBg = new GradientDrawable();
        badgeBg.setColor(Color.rgb(225, 245, 232));
        badgeBg.setCornerRadius(dp(10));
        verificationBadge.setBackground(badgeBg);

        LinearLayout.LayoutParams badgeParams =
                new LinearLayout.LayoutParams(-1, dp(34));

        badgeParams.setMargins(0, 0, 0, dp(10));

        safetyCard.addView(
                verificationBadge,
                badgeParams
        );

        TextView safetyMessage = new TextView(this);
        safetyMessage.setText(
                "For your safety, complete the applicable police " +
                "verification process before hiring this worker. " +
                "GharKaam does not perform or certify police verification."
        );
        safetyMessage.setTextSize(13);
        safetyMessage.setTextColor(Color.rgb(80, 80, 90));
        safetyMessage.setPadding(0, 0, 0, dp(12));

        safetyCard.addView(
                safetyMessage,
                new LinearLayout.LayoutParams(-1, dp(78))
        );

        // Identity document
        TextView identityCheck = new TextView(this);
        identityCheck.setText(
                "🪪  Valid identity proof\n" +
                "    Aadhaar, PAN where applicable, or another accepted ID"
        );
        identityCheck.setTextSize(13);
        identityCheck.setTextColor(Color.rgb(60, 60, 70));
        identityCheck.setPadding(0, dp(5), 0, dp(5));

        safetyCard.addView(
                identityCheck,
                new LinearLayout.LayoutParams(-1, dp(55))
        );

        // Latest photograph
        TextView photoCheck = new TextView(this);
        photoCheck.setText(
                "📸  Latest photograph\n" +
                "    Ask the worker to provide a recent photo"
        );
        photoCheck.setTextSize(13);
        photoCheck.setTextColor(Color.rgb(60, 60, 70));
        photoCheck.setPadding(0, dp(5), 0, dp(5));

        safetyCard.addView(
                photoCheck,
                new LinearLayout.LayoutParams(-1, dp(55))
        );

        // Address
        TextView addressCheck = new TextView(this);
        addressCheck.setText(
                "🏠  Address information\n" +
                "    Check residential/address proof if required"
        );
        addressCheck.setTextSize(13);
        addressCheck.setTextColor(Color.rgb(60, 60, 70));
        addressCheck.setPadding(0, dp(5), 0, dp(5));

        safetyCard.addView(
                addressCheck,
                new LinearLayout.LayoutParams(-1, dp(55))
        );

        // Verification
        TextView policeCheck = new TextView(this);
        policeCheck.setText(
                "👮  Police verification\n" +
                "    Submit the applicable form to the concerned police station"
        );
        policeCheck.setTextSize(13);
        policeCheck.setTextColor(Color.rgb(60, 60, 70));
        policeCheck.setPadding(0, dp(5), 0, dp(10));

        safetyCard.addView(
                policeCheck,
                new LinearLayout.LayoutParams(-1, dp(58))
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

        content.addView(
                safetyCard,
                new LinearLayout.LayoutParams(-1, dp(505))
        );

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

        // Client safety acknowledgment - persisted per worker profile
        CheckBox safetyCheck = new CheckBox(this);
        safetyCheck.setText(
                "I understand and will verify this worker independently"
        );
        safetyCheck.setTextSize(13);
        safetyCheck.setTextColor(Color.rgb(60, 60, 70));
        safetyCheck.setPadding(0, 0, 0, 0);

        String workerProfileId =
                getIntent().getStringExtra("workerProfileId");

        String workerPhoneForKey =
                getIntent().getStringExtra("phone");

        String workerNameForKey =
                getIntent().getStringExtra("name");

        if (workerProfileId == null || workerProfileId.trim().isEmpty()) {
            workerProfileId =
                    "legacy_" +
                    (workerPhoneForKey == null ? "" : workerPhoneForKey) +
                    "_" +
                    (workerNameForKey == null ? "" : workerNameForKey);
        }

        final String safetyKey =
                "safety_ack_" + workerProfileId;

        android.content.SharedPreferences safetyPrefs =
                getSharedPreferences(
                        "GharKaamSafety",
                        MODE_PRIVATE
                );

        boolean alreadyAcknowledged =
                safetyPrefs.getBoolean(safetyKey, false);

        safetyCheck.setChecked(alreadyAcknowledged);

        LinearLayout.LayoutParams checkParams =
                new LinearLayout.LayoutParams(-1, dp(48));

        checkParams.setMargins(
                dp(4), 0, dp(4), dp(6)
        );

        content.addView(safetyCheck, checkParams);

        // Contact Worker is locked until the client acknowledges
        // the independent verification safety notice.
        // Restore the saved acknowledgment state for this worker.
        contact.setEnabled(alreadyAcknowledged);
        contact.setAlpha(
                alreadyAcknowledged ? 1.0f : 0.5f
        );

        safetyCheck.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    safetyPrefs.edit()
                            .putBoolean(safetyKey, isChecked)
                            .apply();

                    contact.setEnabled(isChecked);
                    contact.setAlpha(
                            isChecked ? 1.0f : 0.5f
                    );
                }
        );

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
