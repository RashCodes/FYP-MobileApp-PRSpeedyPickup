package com.example.prspeedypickup;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ChooseOne extends AppCompatActivity {

    Button Pickup;
    Intent intent;
    String type;
    ImageView imageView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_one);

        final Animation zoomin = AnimationUtils.loadAnimation(this,R.anim.zoomin);
        final Animation zoomout = AnimationUtils.loadAnimation(this,R.anim.zoomout);

        imageView4=findViewById(R.id.imageView4);
        imageView4.setAnimation(zoomin);
        imageView4.setAnimation(zoomout);

        intent = getIntent();
        type = intent.getStringExtra("Home").trim();

               Pickup = findViewById(R.id.Picker);


            Pickup.setOnClickListener(v -> {

                if (type.equals("Email")){
                    Intent loginEmailPickup = new Intent(ChooseOne.this, PickerLogin.class);
                    startActivity(loginEmailPickup);
                    finish();
                }
                if (type.equals("Phone")){
                    Intent loginPhonePickup = new Intent(ChooseOne.this, PickerLoginphone.class);
                    startActivity(loginPhonePickup);
                    finish();
                }
                if (type.equals("SignUp")){
                    Intent RegisterPickup = new Intent(ChooseOne.this, PickerRegistration.class);
                    startActivity(RegisterPickup);

                }

            });


        zoomout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView4.startAnimation(zoomin);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        zoomin.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView4.startAnimation(zoomout);

            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }}

