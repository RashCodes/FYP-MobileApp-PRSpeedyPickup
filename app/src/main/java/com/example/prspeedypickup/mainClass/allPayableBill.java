package com.example.prspeedypickup.mainClass;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prspeedypickup.R;
import com.example.prspeedypickup.models.setdata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class allPayableBill extends AppCompatActivity {
    TextView textView;
    int res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_payable_bill);
        textView = findViewById(R.id.showbill);
        DatabaseReference strec = (DatabaseReference) FirebaseDatabase.getInstance().getReference("record").child("payment").child(nav.emailss);

        strec.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postS: dataSnapshot.getChildren() ){
                    setdata productVar = postS.getValue(setdata.class);
                  String  getres = productVar.getAmount();
                     res += Integer.parseInt(getres);
                }
                textView.setText(String.valueOf(res));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}