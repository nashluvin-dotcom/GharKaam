package com.gharkaam.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.*;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class WorkerRegistrationActivity extends Activity {

    private int purple = Color.rgb(108, 77, 255);
    private int dark = Color.rgb(35, 35, 45);

    private android.net.Uri selectedPhotoUri = null;
    private android.net.Uri selectedResumeUri = null;

    private int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density + 0.5f);
    }

    private GradientDrawable box(int color) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(dp(14));
        g.setStroke(dp(1), Color.rgb(225, 225, 230));
        return g;
    }

    private TextView label(String text) {
        TextView t = new TextView(this);
        t.setText(text);
        t.setTextColor(dark);
        t.setTextSize(14);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setPadding(0, dp(10), 0, dp(5));
        return t;
    }

    private EditText field(String hint) {
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setTextSize(15);
        e.setSingleLine(true);
        e.setPadding(dp(14), 0, dp(14), 0);
        e.setBackground(box(Color.WHITE));
        return e;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(248, 247, 252));

        TextView header = new TextView(this);
        header.setText("GharKaam\nWorker Registration");
        header.setTextColor(Color.WHITE);
        header.setTextSize(23);
        header.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        header.setGravity(Gravity.CENTER);
        header.setPadding(dp(15), dp(20), dp(15), dp(20));
        header.setBackground(box(purple));

        root.addView(header, new LinearLayout.LayoutParams(-1, dp(105)));

        ScrollView scroll = new ScrollView(this);

        LinearLayout form = new LinearLayout(this);
        form.setOrientation(LinearLayout.VERTICAL);
        form.setPadding(dp(20), dp(10), dp(20), dp(30));

        scroll.addView(form);

        form.addView(label("Full Name"));
        EditText name = field("Enter your full name");
        form.addView(name, new LinearLayout.LayoutParams(-1, dp(52)));

        form.addView(label("Mobile Number"));
        EditText phone = field("10-digit mobile number");
        phone.setInputType(2);
        form.addView(phone, new LinearLayout.LayoutParams(-1, dp(52)));

        form.addView(label("Age"));
        EditText age = field("Your age");
        age.setInputType(2);
        form.addView(age, new LinearLayout.LayoutParams(-1, dp(52)));

        form.addView(label("Gender"));
        Spinner gender = new Spinner(this);
        gender.setBackground(box(Color.WHITE));

        String[] genders = {
                "Select gender",
                "Female",
                "Male",
                "Other"
        };

        gender.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                genders
        ));

        form.addView(gender, new LinearLayout.LayoutParams(-1, dp(52)));

        form.addView(label("Work Type"));
        Spinner work = new Spinner(this);
        work.setBackground(box(Color.WHITE));

        String[] works = {
                "Select type of work",
                "Maid",
                "Cook",
                "Cleaner",
                "Babysitter",
                "Elder Care",
                "Driver",
                "Gardener",
                "Housekeeper",
                "Security Guard",
                "Caretaker",
                "Laundry",
                "Ironing",
                "Pet Care",
                "Nurse / Patient Care",
                "Helper",
                "Plumber",
                "Electrician",
                "Carpenter",
                "Painter",
                "Other"
        };

        work.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                works
        ));

        form.addView(work, new LinearLayout.LayoutParams(-1, dp(52)));

        form.addView(label("Experience"));
        EditText experience = field("Example: 3 years");
        form.addView(experience, new LinearLayout.LayoutParams(-1, dp(52)));

        form.addView(label("Expected Monthly Salary"));
        EditText salary = field("Example: 12000");
        salary.setInputType(2);
        form.addView(salary, new LinearLayout.LayoutParams(-1, dp(52)));

        form.addView(label("Working Hours"));
        EditText hours = field("Example: 9 AM - 5 PM");
        form.addView(hours, new LinearLayout.LayoutParams(-1, dp(52)));

        form.addView(label("Area / Locality"));
        EditText area = field("Enter your area");
        form.addView(area, new LinearLayout.LayoutParams(-1, dp(52)));

        form.addView(label("City"));
        EditText city = field("Enter your city");
        form.addView(city, new LinearLayout.LayoutParams(-1, dp(52)));

        form.addView(label("Languages"));
        EditText languages = field("Hindi, Marathi, English");
        form.addView(languages, new LinearLayout.LayoutParams(-1, dp(52)));

        form.addView(label("About You"));

        EditText about = new EditText(this);
        about.setHint("Tell clients about yourself");
        about.setTextSize(15);
        about.setGravity(Gravity.TOP);
        about.setPadding(dp(14), dp(12), dp(14), dp(12));
        about.setBackground(box(Color.WHITE));

        form.addView(about, new LinearLayout.LayoutParams(-1, dp(110)));

        form.addView(label("Availability"));

        Spinner availability = new Spinner(this);
        availability.setBackground(box(Color.WHITE));

        String[] availabilityList = {
                "Select availability",
                "Available immediately",
                "Available from next week",
                "Currently working",
                "Looking for part-time work"
        };

        availability.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                availabilityList
        ));

        form.addView(availability, new LinearLayout.LayoutParams(-1, dp(52)));

        form.addView(label("Profile Photo"));

        Button photoButton = new Button(this);
        photoButton.setText("Choose Profile Photo");
        photoButton.setAllCaps(false);
        photoButton.setTextSize(15);
        photoButton.setTextColor(purple);
        photoButton.setBackground(box(Color.WHITE));

        form.addView(photoButton,
                new LinearLayout.LayoutParams(-1, dp(52)));

        ImageView photoPreview = new ImageView(this);
        photoPreview.setImageResource(android.R.drawable.ic_menu_camera);
        photoPreview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        photoPreview.setBackground(box(Color.WHITE));

        LinearLayout.LayoutParams photoPreviewParams =
                new LinearLayout.LayoutParams(-1, dp(180));
        photoPreviewParams.setMargins(0, dp(10), 0, dp(5));

        form.addView(photoPreview, photoPreviewParams);

        TextView photoStatus = new TextView(this);
        photoStatus.setText("Profile photo is required.");
        photoStatus.setTextSize(12);
        photoStatus.setTextColor(Color.GRAY);
        form.addView(photoStatus,
                new LinearLayout.LayoutParams(-1, dp(35)));

        form.addView(label("Resume"));

        Button resumeButton = new Button(this);
        resumeButton.setText("Choose Resume (Optional)");
        resumeButton.setAllCaps(false);
        resumeButton.setTextSize(15);
        resumeButton.setTextColor(purple);
        resumeButton.setBackground(box(Color.WHITE));

        form.addView(resumeButton,
                new LinearLayout.LayoutParams(-1, dp(52)));

        TextView resumeStatus = new TextView(this);
        resumeStatus.setText("Resume is optional.");
        resumeStatus.setTextSize(12);
        resumeStatus.setTextColor(Color.GRAY);
        form.addView(resumeStatus,
                new LinearLayout.LayoutParams(-1, dp(35)));

        final android.net.Uri[] selectedPhoto = {null};
        final android.net.Uri[] selectedResume = {null};

        photoButton.setOnClickListener(v -> {
            android.content.Intent intent =
                    new android.content.Intent(
                            android.content.Intent.ACTION_OPEN_DOCUMENT
                    );
            intent.setType("image/*");
            intent.addCategory(
                    android.content.Intent.CATEGORY_OPENABLE
            );
            startActivityForResult(intent, 1001);
        });

        resumeButton.setOnClickListener(v -> {
            android.content.Intent intent =
                    new android.content.Intent(
                            android.content.Intent.ACTION_OPEN_DOCUMENT
                    );
            intent.setType("application/pdf");
            intent.addCategory(
                    android.content.Intent.CATEGORY_OPENABLE
            );
            startActivityForResult(intent, 1002);
        });

        Button save = new Button(this);
        save.setText("Register Worker");
        save.setTextSize(16);
        save.setTextColor(Color.WHITE);
        save.setAllCaps(false);
        save.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        save.setBackground(box(purple));
        save.setClickable(true);
        save.setFocusable(true);

        LinearLayout.LayoutParams saveParams =
                new LinearLayout.LayoutParams(-1, dp(56));

        saveParams.setMargins(0, dp(25), 0, dp(10));

        form.addView(save, saveParams);

        TextView note = new TextView(this);
        note.setText("Registration is FREE during testing.");
        note.setTextSize(13);
        note.setTextColor(Color.GRAY);
        note.setGravity(Gravity.CENTER);

        form.addView(note, new LinearLayout.LayoutParams(-1, dp(45)));

        root.addView(scroll, new LinearLayout.LayoutParams(
                -1, 0, 1
        ));

        setContentView(root);

        save.setOnClickListener(v -> {

            save.setEnabled(false);
            save.setText("Saving...");

            String n = name.getText().toString().trim();
            String p = phone.getText().toString().trim();
            String a = area.getText().toString().trim();

            if (n.isEmpty()) {
                name.setError("Enter your name");
                name.requestFocus();
                save.setEnabled(true);
                save.setText("Register Worker");
                return;
            }

            if (p.length() != 10) {
                phone.setError("Enter a valid 10-digit number");
                phone.requestFocus();
                save.setEnabled(true);
                save.setText("Register Worker");
                return;
            }

            if (a.isEmpty()) {
                area.setError("Enter your area");
                area.requestFocus();
                save.setEnabled(true);
                save.setText("Register Worker");
                return;
            }

            if (selectedPhotoUri == null) {
                Toast.makeText(
                        this,
                        "Please choose a profile photo.",
                        Toast.LENGTH_LONG
                ).show();
                save.setEnabled(true);
                save.setText("Register Worker");
                return;
            }

            // Save the original selected photo locally.
            String localPhotoPath = savePhotoLocally(selectedPhotoUri);

            if (localPhotoPath == null) {
                Toast.makeText(
                        this,
                        "Could not save profile photo. Please choose another photo.",
                        Toast.LENGTH_LONG
                ).show();
                save.setEnabled(true);
                save.setText("Register Worker");
                return;
            }

            // Create a small compressed copy for sharing with clients.
            String photoBase64 = photoToBase64(selectedPhotoUri);

            if (photoBase64 == null) {
                Toast.makeText(
                        this,
                        "Could not prepare profile photo. Please choose another photo.",
                        Toast.LENGTH_LONG
                ).show();
                save.setEnabled(true);
                save.setText("Register Worker");
                return;
            }

            String ageValue = age.getText().toString().trim();
            String genderValue = gender.getSelectedItem().toString();
            String workValue = work.getSelectedItem().toString();
            String experienceValue = experience.getText().toString().trim();
            String salaryValue = salary.getText().toString().trim();
            String hoursValue = hours.getText().toString().trim();
            String cityValue = city.getText().toString().trim();
            String languagesValue = languages.getText().toString().trim();
            String aboutValue = about.getText().toString().trim();
            String availabilityValue = availability.getSelectedItem().toString();

            Map<String, Object> worker = new HashMap<>();

        com.google.firebase.auth.FirebaseUser currentUser =
                com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(
                    this,
                    "Please login first.",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        worker.put("ownerUid", currentUser.getUid());
            worker.put("name", n);
            worker.put("phone", p);
            worker.put("age", ageValue);
            worker.put("gender", genderValue);
            worker.put("workType", workValue);
            worker.put("experience", experienceValue);
            worker.put("salary", salaryValue);
            worker.put("hours", hoursValue);
            worker.put("area", a);
            worker.put("city", cityValue);
            worker.put("languages", languagesValue);
            worker.put("about", aboutValue);
            worker.put("availability", availabilityValue);
            worker.put("photoLocalPath", localPhotoPath);
            worker.put("photoBase64", photoBase64);

            // Resume remains optional. We save only its selected URI/path for now.
            if (selectedResumeUri != null) {
                worker.put("resumeSelected", true);
            } else {
                worker.put("resumeSelected", false);
            }

            worker.put("createdAt", System.currentTimeMillis());

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // The phone number and account were already created during
            // GharKaam account registration. Do not create them again.
            String accountPhone = getSharedPreferences(
                    "GharKaamAccount",
                    MODE_PRIVATE
            ).getString("phone", "");

            String accountRole = getSharedPreferences(
                    "GharKaamAccount",
                    MODE_PRIVATE
            ).getString("role", "");

            if (!"worker".equalsIgnoreCase(accountRole)) {
                save.setEnabled(true);
                save.setText("Register Worker");

                Toast.makeText(
                        this,
                        "Only Worker accounts can register a worker profile.",
                        Toast.LENGTH_LONG
                ).show();
                return;
            }

            // Use the phone attached to the logged-in account.
            // This prevents a worker from registering a different phone.
            if (!accountPhone.isEmpty() && !accountPhone.equals(p)) {
                save.setEnabled(true);
                save.setText("Register Worker");

                Toast.makeText(
                        this,
                        "Please use your registered account phone number: "
                                + accountPhone,
                        Toast.LENGTH_LONG
                ).show();
                return;
            }

            db.collection("userAccounts")
                    .document(p)
                    .get()
                    .addOnSuccessListener(accountSnapshot -> {

                        if (!accountSnapshot.exists()) {
                            save.setEnabled(true);
                            save.setText("Register Worker");

                            Toast.makeText(
                                    this,
                                    "Worker account was not found. Please login again.",
                                    Toast.LENGTH_LONG
                            ).show();
                            return;
                        }

                        String storedUid =
                                accountSnapshot.getString("ownerUid");

                        String storedRole =
                                accountSnapshot.getString("role");

                        if (!currentUser.getUid().equals(storedUid)
                                || !"worker".equalsIgnoreCase(storedRole)) {

                            save.setEnabled(true);
                            save.setText("Register Worker");

                            Toast.makeText(
                                    this,
                                    "This account is not registered as a Worker.",
                                    Toast.LENGTH_LONG
                            ).show();
                            return;
                        }

                        db.collection("workers")
                                .add(worker)
                                .addOnSuccessListener(documentReference -> {

                                    // Keep the existing account information
                                    // and only add worker-profile information.
                                    Map<String, Object> accountUpdate =
                                            new HashMap<>();

                                    accountUpdate.put(
                                            "workerProfileId",
                                            documentReference.getId()
                                    );
                                    accountUpdate.put(
                                            "workerRegistered",
                                            true
                                    );

                                    db.collection("userAccounts")
                                            .document(p)
                                            .update(accountUpdate);

                                    getSharedPreferences(
                                            "GharKaamAccount",
                                            MODE_PRIVATE
                                    ).edit()
                                            .putBoolean("workerRegistered", true)
                                            .apply();

                                    getSharedPreferences(
                                            "GharKaamWorker",
                                            MODE_PRIVATE
                                    ).edit()
                                            .putString("name", n)
                                            .putString("phone", p)
                                            .putString("age", ageValue)
                                            .putString("gender", genderValue)
                                            .putString("workType", workValue)
                                            .putString("experience", experienceValue)
                                            .putString("salary", salaryValue)
                                            .putString("hours", hoursValue)
                                            .putString("area", a)
                                            .putString("city", cityValue)
                                            .putString("languages", languagesValue)
                                            .putString("about", aboutValue)
                                            .putString("availability", availabilityValue)
                                            .putString("photoLocalPath", localPhotoPath)
                                            .putBoolean(
                                                    "resumeSelected",
                                                    selectedResumeUri != null
                                            )
                                            .putBoolean("profileSaved", true)
                                            .putString(
                                                    "workerProfileId",
                                                    documentReference.getId()
                                            )
                                            .apply();

                                    save.setEnabled(true);
                                    save.setText("Register Worker");

                                    Toast.makeText(
                                            this,
                                            "Worker profile registered online!",
                                            Toast.LENGTH_LONG
                                    ).show();

                                    startActivity(new Intent(
                                            WorkerRegistrationActivity.this,
                                            MyWorkerProfileActivity.class
                                    ));
                                    finish();
                                })
                                .addOnFailureListener(e -> {

                                    save.setEnabled(true);
                                    save.setText("Register Worker");

                                    Toast.makeText(
                                            this,
                                            "Firestore error: " + e.getMessage(),
                                            Toast.LENGTH_LONG
                                    ).show();
                                });
                    })
                    .addOnFailureListener(e -> {

                        save.setEnabled(true);
                        save.setText("Register Worker");

                        Toast.makeText(
                                this,
                                "Could not verify worker account: "
                                        + e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    });
        });
    }

    private String savePhotoLocally(android.net.Uri uri) {
        if (uri == null) {
            return null;
        }

        try {
            java.io.InputStream input =
                    getContentResolver().openInputStream(uri);

            if (input == null) {
                return null;
            }

            java.io.File photoDir =
                    new java.io.File(getFilesDir(), "worker_profile");

            if (!photoDir.exists() && !photoDir.mkdirs()) {
                input.close();
                return null;
            }

            java.io.File photoFile =
                    new java.io.File(photoDir, "profile_photo.jpg");

            java.io.FileOutputStream output =
                    new java.io.FileOutputStream(photoFile);

            byte[] buffer = new byte[8192];
            int length;

            while ((length = input.read(buffer)) != -1) {
                output.write(buffer, 0, length);
            }

            output.flush();
            output.close();
            input.close();

            return photoFile.getAbsolutePath();

        } catch (Exception e) {
            return null;
        }
    }

    private String photoToBase64(android.net.Uri uri) {
        if (uri == null) {
            return null;
        }

        try {
            android.graphics.BitmapFactory.Options options =
                    new android.graphics.BitmapFactory.Options();

            options.inJustDecodeBounds = true;

            java.io.InputStream boundsInput =
                    getContentResolver().openInputStream(uri);

            if (boundsInput == null) {
                return null;
            }

            android.graphics.BitmapFactory.decodeStream(
                    boundsInput,
                    null,
                    options
            );

            boundsInput.close();

            int width = options.outWidth;
            int height = options.outHeight;

            if (width <= 0 || height <= 0) {
                return null;
            }

            int maxSize = 500;
            int sample = 1;

            while ((width / sample) > maxSize ||
                    (height / sample) > maxSize) {
                sample *= 2;
            }

            android.graphics.BitmapFactory.Options decodeOptions =
                    new android.graphics.BitmapFactory.Options();

            decodeOptions.inSampleSize = sample;
            decodeOptions.inPreferredConfig =
                    android.graphics.Bitmap.Config.RGB_565;

            java.io.InputStream input =
                    getContentResolver().openInputStream(uri);

            if (input == null) {
                return null;
            }

            android.graphics.Bitmap bitmap =
                    android.graphics.BitmapFactory.decodeStream(
                            input,
                            null,
                            decodeOptions
                    );

            input.close();

            if (bitmap == null) {
                return null;
            }

            java.io.ByteArrayOutputStream output =
                    new java.io.ByteArrayOutputStream();

            bitmap.compress(
                    android.graphics.Bitmap.CompressFormat.JPEG,
                    60,
                    output
            );

            bitmap.recycle();

            byte[] bytes = output.toByteArray();
            output.close();

            return android.util.Base64.encodeToString(
                    bytes,
                    android.util.Base64.NO_WRAP
            );

        } catch (Exception e) {
            return null;
        }
    }

    private ImageView findPhotoPreview() {
        android.view.View rootView = findViewById(android.R.id.content);
        return findImageView(rootView);
    }

    private ImageView findImageView(android.view.View view) {
        if (view instanceof ImageView) {
            return (ImageView) view;
        }

        if (view instanceof android.view.ViewGroup) {
            android.view.ViewGroup group =
                    (android.view.ViewGroup) view;

            for (int i = 0; i < group.getChildCount(); i++) {
                ImageView result =
                        findImageView(group.getChildAt(i));

                if (result != null) {
                    return result;
                }
            }
        }

        return null;
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            android.content.Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null) {
            return;
        }

        android.net.Uri uri = data.getData();

        if (requestCode == 1001) {
            selectedPhotoUri = uri;

            ImageView preview = findPhotoPreview();
            if (preview != null) {
                preview.setImageURI(uri);
                preview.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            android.widget.Toast.makeText(
                    this,
                    "Profile photo selected.",
                    android.widget.Toast.LENGTH_SHORT
            ).show();
        } else if (requestCode == 1002) {
            selectedResumeUri = uri;
            android.widget.Toast.makeText(
                    this,
                    "Resume selected.",
                    android.widget.Toast.LENGTH_SHORT
            ).show();
        }
    }
}
