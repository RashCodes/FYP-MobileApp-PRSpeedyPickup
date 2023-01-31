package com.example.prspeedypickup.mainClass;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.prspeedypickup.R;
import com.example.prspeedypickup.models.setdata;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class displayad extends AppCompatActivity {
    TextView detail, price,quan, location,date,title;
    ImageView img;
    LinearLayout order, getmessage, getviewno;
    String userEmail;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayad);


        detail = findViewById(R.id.detailad);
        price = findViewById(R.id.pricead);
        quan = findViewById(R.id.textquan);
        img = findViewById(R.id.viewpost);

        title = findViewById(R.id.titlead);
//        getmessage = findViewById(R.id.message);
        String getdetail = getIntent().getExtras().getString("detail");
        String getprice = getIntent().getExtras().getString("price");
        String gettitle = getIntent().getExtras().getString("title");
        String picture = getIntent().getExtras().getString("pic");
        String searchcategory = getIntent().getExtras().getString("cate");
        Glide.with(getApplicationContext()).load(picture).into(img);
        DatabaseReference createpost = FirebaseDatabase.getInstance().getReference().child("student").child("order").child(nav.emailss);

        detail.setText("Description \n\n"+getdetail);
        price.setText("Price "+getprice+" SAR");
        title.setText(gettitle);
        order = findViewById(R.id.order);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a =  quan.getText().toString();
                int res1 = Integer.parseInt(getprice);
                int res2 = Integer.parseInt(a);
                int res3 = res1*res2;
                Random rnd = new Random();
                int number = rnd.nextInt(999999);
                String ran = String.valueOf(number);
                String  res4 = String.valueOf(res3);

                setdata pd = new setdata(gettitle, picture, getdetail, res4,nav.emailss, a,ran, nav.navname);
                createpost.push().setValue(pd);
                Toast.makeText(displayad.this, "Order successfully placed", Toast.LENGTH_SHORT).show();
            }
        });







    }
}