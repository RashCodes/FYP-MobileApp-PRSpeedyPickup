package com.example.prspeedypickup.mainClass;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prspeedypickup.R;
import com.example.prspeedypickup.adapters.getpostdataforcategories;
import com.example.prspeedypickup.models.setdata;
import com.example.prspeedypickup.slider.SliderAdapter;
import com.example.prspeedypickup.slider.SliderData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Objects;

public class Displaycategorydata extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<setdata> ad = new ArrayList<>();
    private getpostdataforcategories postadapter;
    ImageView filterdata;
    Button yes ,no ,rateus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaycategorydata);
        recyclerView = findViewById(R.id.revieqw);
        filterdata= findViewById(R.id.filter);
        filterdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(Displaycategorydata.this);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(R.layout.bmi_info_dialog);
                yes=dialog.findViewById(R.id.yes);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
//                        finishAffinity();
                    }
                });
                dialog.show();

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();
        // initializing the slider view.
        SliderView sliderView = findViewById(R.id.slider);
        // adding the urls inside array list
        sliderDataArrayList.add(new SliderData(R.drawable.backgrnd2));
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
        DatabaseReference cateDataa1 = FirebaseDatabase.getInstance().getReference("student").child("order").child(nav.emailss);
        cateDataa1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ad.clear();
                for (DataSnapshot petdatasnap : snapshot.getChildren()) {

                        setdata petData = petdatasnap.getValue(setdata.class);
                        ad.add(petData);
                        Log.i("tariqq: ", "" + ad);


                }
                postadapter = new getpostdataforcategories(Displaycategorydata.this, ad);
                recyclerView.setAdapter(postadapter);
                postadapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Displaycategorydata.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}