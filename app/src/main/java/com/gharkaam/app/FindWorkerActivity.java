package com.gharkaam.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.*;
import android.view.View;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.DocumentSnapshot;

public class FindWorkerActivity extends Activity {

    private int purple = Color.rgb(108, 77, 255);
    private int dark = Color.rgb(35, 35, 45);
    private LinearLayout list;

    private int dp(int v) {
        return (int)(v * getResources().getDisplayMetrics().density + 0.5f);
    }

    private GradientDrawable bg(int color) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(dp(16));
        return g;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(248,247,252));

        TextView header = new TextView(this);
        header.setText("GharKaam\nFind a Worker");
        header.setTextColor(Color.WHITE);
        header.setTextSize(23);
        header.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        header.setGravity(Gravity.CENTER);
        header.setPadding(dp(15),dp(18),dp(15),dp(18));
        header.setBackground(bg(purple));

        root.addView(header,new LinearLayout.LayoutParams(-1,dp(105)));

        LinearLayout searchRow = new LinearLayout(this);
        searchRow.setPadding(dp(16),dp(14),dp(16),dp(5));

        EditText search = new EditText(this);
        search.setHint("Search by area or work type");
        search.setSingleLine(true);
        search.setTextSize(15);
        search.setPadding(dp(14),0,dp(14),0);
        search.setBackground(bg(Color.WHITE));

        searchRow.addView(search,new LinearLayout.LayoutParams(0,dp(52),1));

        Button searchButton = new Button(this);
        searchButton.setText("Search");
        searchButton.setTextColor(Color.WHITE);
        searchButton.setAllCaps(false);
        searchButton.setBackground(bg(purple));

        LinearLayout.LayoutParams sb =
                new LinearLayout.LayoutParams(dp(90),dp(52));
        sb.setMargins(dp(8),0,0,0);

        searchRow.addView(searchButton,sb);
        root.addView(searchRow);

        Spinner workFilter = new Spinner(this);

        String[] workTypes = {
                "All Work Types",
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

        ArrayAdapter<String> filterAdapter =
                new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_spinner_item,
                        workTypes
                );

        filterAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        workFilter.setAdapter(filterAdapter);
        workFilter.setPadding(dp(16),0,dp(16),0);

        root.addView(
                workFilter,
                new LinearLayout.LayoutParams(-1,dp(52))
        );

        ScrollView scroll = new ScrollView(this);

        list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        list.setPadding(dp(16),dp(10),dp(16),dp(30));

        scroll.addView(list);

        root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));

        setContentView(root);

        loadWorkers("", "All Work Types");

        searchButton.setOnClickListener(v ->
                loadWorkers(
                        search.getText().toString().trim(),
                        workFilter.getSelectedItem().toString()
                )
        );

        workFilter.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(
                            android.widget.AdapterView<?> parent,
                            View view,
                            int position,
                            long id) {

                        loadWorkers(
                                search.getText().toString().trim(),
                                workFilter.getSelectedItem().toString()
                        );
                    }

                    @Override
                    public void onNothingSelected(
                            android.widget.AdapterView<?> parent) {
                    }
                }
        );
    }

    private void loadWorkers(String keyword, String workFilter) {

        list.removeAllViews();

        TextView loading = new TextView(this);
        loading.setText("Finding workers...");
        loading.setTextSize(16);
        loading.setGravity(Gravity.CENTER);
        loading.setPadding(0,dp(30),0,dp(30));
        list.addView(loading);

        FirebaseFirestore.getInstance()
                .collection("workers")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(snapshot -> {

                    list.removeAllViews();

                    int count = 0;

                    for (DocumentSnapshot doc : snapshot.getDocuments()) {

                        String name = doc.getString("name");
                        String phone = doc.getString("phone");
                        String work = normalizeWorkType(
                                doc.getString("workType")
                        );
                        String area = doc.getString("area");
                        String city = doc.getString("city");
                        String experience = doc.getString("experience");
                        String hours = doc.getString("hours");
                        String salary = doc.getString("salary");
                        String availability = doc.getString("availability");
                        String languages = doc.getString("languages");
                        String about = doc.getString("about");
                        String photoBase64 = doc.getString("photoBase64");

                        if (name == null) name = "Worker";
                        if (phone == null) phone = "";
                        if (work == null) work = "";
                        if (area == null) area = "";
                        if (city == null) city = "";
                        if (experience == null) experience = "";
                        if (salary == null) salary = "";
                        if (availability == null) availability = "";
                        if (languages == null) languages = "";
                        if (hours == null) hours = "";
                        if (about == null) about = "";

                        String searchText =
                                (name + " " + work + " " + area + " " + city)
                                        .toLowerCase();

                        if (!keyword.isEmpty() &&
                                !searchText.contains(keyword.toLowerCase())) {
                            continue;
                        }

                        if (!workFilter.equals("All Work Types") &&
                                !work.equalsIgnoreCase(workFilter)) {
                            continue;
                        }

                        addWorkerCard(
                                doc.getId(),
                                name,
                                phone,
                                work,
                                area,
                                city,
                                experience,
                                salary,
                                availability,
                                languages,
                                about,
                                hours,
                                photoBase64
                        );

                        count++;
                    }

                    if (count == 0) {
                        TextView empty = new TextView(this);
                        empty.setText("No workers found.\nTry another area or work type.");
                        empty.setTextSize(16);
                        empty.setTextColor(Color.GRAY);
                        empty.setGravity(Gravity.CENTER);
                        empty.setPadding(0,dp(60),0,dp(60));
                        list.addView(empty);
                    }
                })
                .addOnFailureListener(e -> {

                    list.removeAllViews();

                    TextView error = new TextView(this);
                    error.setText("Unable to load workers.\n\n" + e.getMessage());
                    error.setTextSize(15);
                    error.setTextColor(Color.RED);
                    error.setGravity(Gravity.CENTER);
                    error.setPadding(dp(20),dp(40),dp(20),dp(40));

                    list.addView(error);
                });
    }

    private String normalizeWorkType(String work) {

        String w = work == null ? "" : work.trim();

        if (w.equalsIgnoreCase("Maid / House Cleaning")) {
            return "Maid";
        }

        if (w.equalsIgnoreCase("Cooking")) {
            return "Cook";
        }

        if (w.equalsIgnoreCase("Babysitting")) {
            return "Babysitter";
        }

        if (w.equalsIgnoreCase("Washing / Laundry")) {
            return "Laundry";
        }

        if (w.equalsIgnoreCase("All Household Work")) {
            return "Housekeeper";
        }

        return w;
    }

    private void addWorkerCard(
            String id,
            String name,
            String phone,
            String work,
            String area,
            String city,
            String experience,
            String salary,
            String availability,
            String languages,
            String about,
            String hours,
            String photoBase64
    ) {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(18),dp(16),dp(18),dp(16));
        card.setBackground(bg(Color.WHITE));

        LinearLayout.LayoutParams cp =
                new LinearLayout.LayoutParams(-1,-2);

        cp.setMargins(0,0,0,dp(14));

        TextView title = new TextView(this);
        title.setText(name);
        title.setTextSize(20);
        title.setTextColor(dark);
        title.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        // Availability badge
        TextView availabilityBadge = new TextView(this);

        boolean available =
                availability.toLowerCase().contains("available") &&
                !availability.toLowerCase().contains("not available");

        availabilityBadge.setText(
                available ? "● Available" : "● Not Available"
        );

        availabilityBadge.setTextSize(14);
        availabilityBadge.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        availabilityBadge.setPadding(dp(12),dp(6),dp(12),dp(6));
        availabilityBadge.setGravity(Gravity.CENTER);

        GradientDrawable badgeBg = new GradientDrawable();
        badgeBg.setColor(
                available
                        ? Color.rgb(225,245,234)
                        : Color.rgb(245,230,230)
        );
        badgeBg.setCornerRadius(dp(20));
        availabilityBadge.setBackground(badgeBg);

        card.addView(
                availabilityBadge,
                new LinearLayout.LayoutParams(-2,dp(36))
        );

        // Profile photo
        ImageView photo = new ImageView(this);
        photo.setScaleType(ImageView.ScaleType.CENTER_CROP);

        LinearLayout.LayoutParams photoParams =
                new LinearLayout.LayoutParams(dp(90),dp(90));

        photoParams.gravity = Gravity.CENTER_HORIZONTAL;
        photoParams.setMargins(0,0,0,dp(10));

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
                    photo.setImageBitmap(bitmap);
                    photoShown = true;
                }
            } catch (Exception ignored) {
            }
        }

        if (!photoShown) {
            photo.setImageResource(android.R.drawable.ic_menu_camera);
        }

        card.addView(photo, photoParams);

        card.addView(title);

        TextView workText = new TextView(this);
        workText.setText("🧹 " + work);
        workText.setTextSize(15);
        workText.setTextColor(purple);
        workText.setPadding(0,dp(6),0,dp(2));

        card.addView(workText);

        TextView details = new TextView(this);
        details.setText(
                "📍 " + area + ", " + city +
                "\n⭐ Experience: " + experience +
                "\n💰 Expected salary: ₹" + salary +
                "\n🕐 " + availability
        );

        details.setTextSize(14);
        details.setTextColor(Color.DKGRAY);
        details.setPadding(0,dp(8),0,dp(8));

        card.addView(details);

        Button view = new Button(this);
        view.setText("View Profile");
        view.setTextColor(Color.WHITE);
        view.setAllCaps(false);
        view.setBackground(bg(purple));

        card.addView(view,new LinearLayout.LayoutParams(-1,dp(48)));

        view.setOnClickListener(v -> {

            Intent intent =
                    new Intent(FindWorkerActivity.this,
                            WorkerProfileActivity.class);

            intent.putExtra("workerProfileId",id);
            intent.putExtra("name",name);
            intent.putExtra("work",work);
            intent.putExtra("area",area);
            intent.putExtra("city",city);
            intent.putExtra("experience",experience);
            intent.putExtra("phone",phone);
            intent.putExtra("salary",salary);
            intent.putExtra("availability",availability);
            intent.putExtra("languages",languages);
            intent.putExtra("hours",hours);
            intent.putExtra("about",about);
            intent.putExtra("photoBase64",photoBase64);

            startActivity(intent);
        });

        list.addView(card,cp);
    }
}
