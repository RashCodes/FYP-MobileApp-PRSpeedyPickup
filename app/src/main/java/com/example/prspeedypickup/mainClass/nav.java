package com.example.prspeedypickup.mainClass;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prspeedypickup.R;
import com.example.prspeedypickup.adapters.getpostdata;
import com.example.prspeedypickup.allUserData.deliveredOrderamount;
import com.example.prspeedypickup.models.setdata;
import com.example.prspeedypickup.slider.SliderAdapter;
import com.example.prspeedypickup.slider.SliderData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Objects;

public class nav extends AppCompatActivity {
    TextView namee, show;
    Button yes, no;
    private static final int PERMISSION_REQUEST_CODE = 1;
    DrawerLayout drawerLayout;
    public static String emailss, statusche, navname;
    LinearLayout post, displayfood, bill, deliveredd, adminlay1, adminlay2, userlay1, userlay2;
    RecyclerView recyclerView;
    private getpostdata postadapter;

    ArrayList<setdata> ad = new ArrayList<>();

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        final ConstraintLayout content = findViewById(R.id.content);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        recyclerView = findViewById(R.id.revieqw);
        userlay1 = findViewById(R.id.bills);
        userlay2 = findViewById(R.id.disfood);
        adminlay1 = findViewById(R.id.postdata);
        adminlay2 = findViewById(R.id.delivered);
        deliveredd = findViewById(R.id.delivered);
        deliveredd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(nav.this, deliveredOrderamount.class);
                startActivity(intent);
            }
        });

        bill = findViewById(R.id.bills);
        bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(nav.this, allPayableBill.class);
                startActivity(intent);

            }
        });
        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();
        // initializing the slider view.
        SliderView sliderView = findViewById(R.id.slider);

        post = findViewById(R.id.postdata);
        displayfood = findViewById(R.id.disfood);
        displayfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(nav.this, Displaycategorydata.class);
                startActivity(intent);
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(nav.this, post.class);
                startActivity(intent);
            }
        });
        // adding the urls inside array list
        sliderDataArrayList.add(new SliderData(R.drawable.nav3));
        sliderDataArrayList.add(new SliderData(R.drawable.nav2));
        sliderDataArrayList.add(new SliderData(R.drawable.nav1));

        // passing this array list inside our adapter class.
        SliderAdapter adapter = new SliderAdapter(this, sliderDataArrayList);
        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        // below method is used to
        // setadapter to sliderview.
        sliderView.setSliderAdapter(adapter);
        // below method is use to set
        // scroll time in seconds.
        sliderView.setScrollTimeInSec(3);
        // to set it scrollable automatically
        // we use below method.
        sliderView.setAutoCycle(true);
        // to start autocycle below method is used.
        sliderView.startAutoCycle();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sname = preferences.getString("name", "");
        String sstatus = preferences.getString("status", "");
        String eamail = preferences.getString("email", "");
        Boolean checktrue = preferences.getBoolean("check", false);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        namee = findViewById(R.id.nameee);

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                requestPermission(); // Code for permission
            }
        } else {

            // Code for Below 23 API Oriented Device
            // Do next code
        }
        String emaill = getIntent().getStringExtra("getemail");
        String statuss = getIntent().getStringExtra("getstatus");
        String nameee = getIntent().getStringExtra("getname");


        if (checktrue) {
            emailss = eamail.replace(".", "");
            navname = sname;
            statusche = sstatus;
        } else {
            emailss = emaill.replace(".", "");
            navname = nameee;
            statusche = statuss;

        }
        if (statusche.equals("user")) {
            adminlay1.setVisibility(View.GONE);
            adminlay2.setVisibility(View.GONE);

        } else {
            userlay2.setVisibility(View.GONE);
            userlay1.setVisibility(View.GONE);
        }
        namee.setText(nameee+" "+statusche);
        DatabaseReference cateDataa1 = FirebaseDatabase.getInstance().getReference("create").child("post");
        cateDataa1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ad.clear();
                for (DataSnapshot petdatasnap : snapshot.getChildren()) {

                    setdata petData = petdatasnap.getValue(setdata.class);
                    ad.add(petData);
                    Log.i("tariqq: ", "" + ad);

                }
                postadapter = new getpostdata(nav.this, ad);
                recyclerView.setAdapter(postadapter);
                postadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(nav.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        final Dialog dialog = new Dialog(nav.this);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.bmi_info_dialog);
        yes = dialog.findViewById(R.id.yes);
        show = dialog.findViewById(R.id.textshow);
        show.setText("Are you sure you want to Sign out?");
        no = dialog.findViewById(R.id.no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nav.super.onBackPressed();
                FirebaseAuth.getInstance().signOut();
                finishAffinity();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(nav.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(nav.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(nav.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(nav.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }


}