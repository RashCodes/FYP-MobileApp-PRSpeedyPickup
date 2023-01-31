package com.example.prspeedypickup.allUserData;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prspeedypickup.R;
import com.example.prspeedypickup.adapters.deliveredorderdata;
import com.example.prspeedypickup.models.setdata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class deliveredOrderamount extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<setdata> ad = new ArrayList<>();
    private deliveredorderdata postadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivered_orderamount);
        recyclerView = findViewById(R.id.revieqw);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        DatabaseReference cateDataa1 = FirebaseDatabase.getInstance().getReference("student").child("order");
        cateDataa1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ad.clear();
                for (DataSnapshot petdatasnap : snapshot.getChildren()) {
                    for (DataSnapshot snapshot1 : petdatasnap.getChildren()){
                        setdata petData = snapshot1.getValue(setdata.class);
                        ad.add(petData);
                        Log.i("tariqq: ", "" + ad);
                    }


                }
                postadapter = new deliveredorderdata(deliveredOrderamount.this, ad);
                recyclerView.setAdapter(postadapter);
                postadapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(deliveredOrderamount.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}