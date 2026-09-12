package com.gharkaam.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.util.Base64;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.widget.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class EditWorkerProfileActivity extends Activity {

    private final int PURPLE = Color.rgb(108, 77, 255);
    private final int DARK = Color.rgb(35, 35, 45);

    private EditText name, phone, age, gender, workType;
    private EditText experience, salary, hours, area, city;
    private EditText languages, about, availability;

    private String workerDocumentId = "";
    private String existingPhotoBase64 = "";
    private String newPhotoBase64 = null;
    private ImageView photoPreview;
    private static final int PHOTO_REQUEST = 2001;

    private int dp(int v) {
        return (int)(v * getResources().getDisplayMetrics().density + 0.5f);
    }

    private EditText field(
            LinearLayout parent,
            String hint
    ) {
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setTextSize(16);
        e.setTextColor(DARK);
        e.setHintTextColor(Color.rgb(130, 130, 140));
        e.setPadding(dp(14), dp(8), dp(14), dp(8));

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(-1, dp(55));
        p.setMargins(0, 0, 0, dp(10));

        parent.addView(e, p);
        return e;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(248, 247, 252));

        TextView header = new TextView(this);
        header.setText("Edit Worker Profile");
        header.setTextSize(24);
        header.setTextColor(Color.WHITE);
        header.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        header.setGravity(Gravity.CENTER);
        header.setBackgroundColor(PURPLE);

        root.addView(header,
                new LinearLayout.LayoutParams(-1, dp(80)));

        ScrollView scroll = new ScrollView(this);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(16), dp(16), dp(16), dp(20));

        TextView photoTitle = new TextView(this);
        photoTitle.setText("Profile Photo");
        photoTitle.setTextSize(17);
        photoTitle.setTextColor(DARK);
        photoTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        photoTitle.setPadding(0, 0, 0, dp(8));
        content.addView(photoTitle);

        photoPreview = new ImageView(this);
        photoPreview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        photoPreview.setImageResource(android.R.drawable.ic_menu_camera);

        GradientDrawable photoBackground = new GradientDrawable();
        photoBackground.setColor(Color.WHITE);
        photoBackground.setCornerRadius(dp(60));
        photoPreview.setBackground(photoBackground);

        LinearLayout.LayoutParams photoParams =
                new LinearLayout.LayoutParams(dp(120), dp(120));
        photoParams.gravity = Gravity.CENTER_HORIZONTAL;
        photoParams.setMargins(0, 0, 0, dp(10));

        content.addView(photoPreview, photoParams);

        Button choosePhoto = new Button(this);
        choosePhoto.setText("Choose Profile Photo");
        choosePhoto.setTextSize(15);
        choosePhoto.setAllCaps(false);
        choosePhoto.setTextColor(DARK);

        content.addView(
                choosePhoto,
                new LinearLayout.LayoutParams(-1, dp(52))
        );

        TextView photoStatus = new TextView(this);
        photoStatus.setText("Choose a new photo to replace the current one.");
        photoStatus.setTextSize(13);
        photoStatus.setTextColor(Color.GRAY);
        photoStatus.setPadding(0, dp(4), 0, dp(14));
        content.addView(photoStatus);

        choosePhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, PHOTO_REQUEST);
        });

        name = field(content, "Full Name");
        phone = field(content, "Mobile Number");
        age = field(content, "Age");
        gender = field(content, "Gender");
        workType = field(content, "Work Type");
        experience = field(content, "Experience");
        salary = field(content, "Expected Monthly Salary");
        hours = field(content, "Working Hours");
        area = field(content, "Area / Locality");
        city = field(content, "City");
        languages = field(content, "Languages");
        about = field(content, "About You");
        availability = field(content, "Availability");

        Button save = new Button(this);
        save.setText("Save Changes");
        save.setTextSize(16);
        save.setTextColor(Color.WHITE);
        save.setAllCaps(false);
        save.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        save.setBackgroundColor(PURPLE);

        content.addView(save,
                new LinearLayout.LayoutParams(-1, dp(55)));

        scroll.addView(content);

        root.addView(scroll,
                new LinearLayout.LayoutParams(-1, 0, 1));

        setContentView(root);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null) {
            Toast.makeText(
                    this,
                    "Please login again.",
                    Toast.LENGTH_LONG
            ).show();
            finish();
            return;
        }

        String uid = auth.getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("workers")
                .whereEqualTo("ownerUid", uid)
                .limit(1)
                .get()
                .addOnSuccessListener(snapshot -> {

                    if (snapshot.isEmpty()) {
                        Toast.makeText(
                                this,
                                "Worker profile not found.",
                                Toast.LENGTH_LONG
                        ).show();
                        return;
                    }

                    QueryDocumentSnapshot doc =
                            (QueryDocumentSnapshot) snapshot.getDocuments().get(0);

                    workerDocumentId = doc.getId();

                    name.setText(safe(doc.getString("name")));
                    phone.setText(safe(doc.getString("phone")));
                    age.setText(safe(doc.getString("age")));
                    gender.setText(safe(doc.getString("gender")));
                    workType.setText(safe(doc.getString("workType")));
                    experience.setText(safe(doc.getString("experience")));
                    salary.setText(safe(doc.getString("salary")));
                    hours.setText(safe(doc.getString("hours")));
                    area.setText(safe(doc.getString("area")));
                    city.setText(safe(doc.getString("city")));
                    languages.setText(safe(doc.getString("languages")));
                    about.setText(safe(doc.getString("about")));
                    availability.setText(safe(doc.getString("availability")));

                    existingPhotoBase64 =
                            safe(doc.getString("photoBase64"));

                    showPhoto(existingPhotoBase64);

                })
                .addOnFailureListener(e ->
                        Toast.makeText(
                                this,
                                "Could not load profile: " + e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show()
                );

        save.setOnClickListener(v -> {

            if (workerDocumentId.isEmpty()) {
                Toast.makeText(
                        this,
                        "Profile is still loading. Please wait.",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            String newName = name.getText().toString().trim();
            String newPhone = phone.getText().toString().trim();
            String newAge = age.getText().toString().trim();
            String newGender = gender.getText().toString().trim();
            String newWork = workType.getText().toString().trim();
            String newExperience = experience.getText().toString().trim();
            String newSalary = salary.getText().toString().trim();
            String newHours = hours.getText().toString().trim();
            String newArea = area.getText().toString().trim();
            String newCity = city.getText().toString().trim();
            String newLanguages = languages.getText().toString().trim();
            String newAbout = about.getText().toString().trim();
            String newAvailability = availability.getText().toString().trim();

            if (newName.isEmpty()) {
                name.setError("Enter your name");
                name.requestFocus();
                return;
            }

            if (!newPhone.matches("\\d{10}")) {
                phone.setError("Enter a valid 10 digit phone number");
                phone.requestFocus();
                return;
            }

            if (newArea.isEmpty()) {
                area.setError("Enter your area/locality");
                area.requestFocus();
                return;
            }

            Map<String, Object> updates = new HashMap<>();

            updates.put("name", newName);
            updates.put("phone", newPhone);
            updates.put("age", newAge);
            updates.put("gender", newGender);
            updates.put("workType", newWork);
            updates.put("experience", newExperience);
            updates.put("salary", newSalary);
            updates.put("hours", newHours);
            updates.put("area", newArea);
            updates.put("city", newCity);
            updates.put("languages", newLanguages);
            updates.put("about", newAbout);
            updates.put("availability", newAvailability);

            if (newPhotoBase64 != null && !newPhotoBase64.isEmpty()) {
                updates.put("photoBase64", newPhotoBase64);
            }

            save.setEnabled(false);
            save.setText("Saving...");

            FirebaseFirestore.getInstance()
                    .collection("workers")
                    .document(workerDocumentId)
                    .update(updates)
                    .addOnSuccessListener(unused -> {

                        getSharedPreferences(
                                "GharKaamWorker",
                                MODE_PRIVATE
                        ).edit()
                                .putString("name", newName)
                                .putString("phone", newPhone)
                                .putString("age", newAge)
                                .putString("gender", newGender)
                                .putString("workType", newWork)
                                .putString("experience", newExperience)
                                .putString("salary", newSalary)
                                .putString("hours", newHours)
                                .putString("area", newArea)
                                .putString("city", newCity)
                                .putString("languages", newLanguages)
                                .putString("about", newAbout)
                                .putString("availability", newAvailability)
                                .apply();

                        Toast.makeText(
                                this,
                                "Profile updated successfully!",
                                Toast.LENGTH_LONG
                        ).show();

                        finish();
                    })
                    .addOnFailureListener(e -> {

                        save.setEnabled(true);
                        save.setText("Save Changes");

                        Toast.makeText(
                                this,
                                "Could not update profile: "
                                        + e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    });
        });
    }

    private void showPhoto(String base64) {
        if (photoPreview == null) {
            return;
        }

        if (base64 == null || base64.isEmpty()) {
            photoPreview.setImageResource(
                    android.R.drawable.ic_menu_camera
            );
            return;
        }

        try {
            byte[] bytes =
                    Base64.decode(base64, Base64.DEFAULT);

            Bitmap bitmap =
                    BitmapFactory.decodeByteArray(
                            bytes, 0, bytes.length
                    );

            if (bitmap != null) {
                photoPreview.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            photoPreview.setImageResource(
                    android.R.drawable.ic_menu_camera
            );
        }
    }

    private String photoToBase64(Uri uri) {
        if (uri == null) {
            return null;
        }

        try {
            BitmapFactory.Options bounds =
                    new BitmapFactory.Options();

            bounds.inJustDecodeBounds = true;

            java.io.InputStream input =
                    getContentResolver().openInputStream(uri);

            if (input == null) {
                return null;
            }

            BitmapFactory.decodeStream(input, null, bounds);
            input.close();

            if (bounds.outWidth <= 0 || bounds.outHeight <= 0) {
                return null;
            }

            int maxSize = 500;
            int sample = 1;

            while ((bounds.outWidth / sample) > maxSize ||
                    (bounds.outHeight / sample) > maxSize) {
                sample *= 2;
            }

            BitmapFactory.Options options =
                    new BitmapFactory.Options();

            options.inSampleSize = sample;
            options.inPreferredConfig = Bitmap.Config.RGB_565;

            input = getContentResolver().openInputStream(uri);

            if (input == null) {
                return null;
            }

            Bitmap bitmap =
                    BitmapFactory.decodeStream(
                            input, null, options
                    );

            input.close();

            if (bitmap == null) {
                return null;
            }

            java.io.ByteArrayOutputStream output =
                    new java.io.ByteArrayOutputStream();

            bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    60,
                    output
            );

            bitmap.recycle();

            byte[] bytes = output.toByteArray();
            output.close();

            return Base64.encodeToString(
                    bytes,
                    Base64.NO_WRAP
            );

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data
    ) {
        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (requestCode == PHOTO_REQUEST &&
                resultCode == RESULT_OK &&
                data != null &&
                data.getData() != null) {

            Uri uri = data.getData();

            String encoded = photoToBase64(uri);

            if (encoded == null) {
                Toast.makeText(
                        this,
                        "Could not prepare photo. Please choose another photo.",
                        Toast.LENGTH_LONG
                ).show();
                return;
            }

            newPhotoBase64 = encoded;
            showPhoto(encoded);

            Toast.makeText(
                    this,
                    "New profile photo selected.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}
