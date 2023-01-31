package com.example.prspeedypickup;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenu extends AppCompatActivity {

    Button SignInwithEmail,SignInwithPhone,SignUp;
    ImageView imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        final Animation zoomin = AnimationUtils.loadAnimation(this,R.anim.zoomin);
        final Animation zoomout = AnimationUtils.loadAnimation(this,R.anim.zoomout);

        imageView2=findViewById(R.id.imageView2);
        imageView2.setAnimation(zoomin);
        imageView2.setAnimation(zoomout);

        zoomout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView2.startAnimation(zoomin);

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
                imageView2.startAnimation(zoomout);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        SignInwithEmail=(Button)findViewById(R.id.SignInwithEmail);
        SignInwithPhone=(Button)findViewById(R.id.SignInwithPhone);
        SignUp=(Button)findViewById(R.id.SignUp);

        SignInwithEmail.setOnClickListener(view -> {
            Intent SignInwithEmail = new Intent(MainMenu.this,ChooseOne.class);
            SignInwithEmail.putExtra("Home","Email");
            startActivity(SignInwithEmail);
            finish();

        });

        SignInwithPhone.setOnClickListener(view -> {
            Intent SignInwithPhone = new Intent(MainMenu.this,ChooseOne.class);
            SignInwithPhone.putExtra("Home", "Phone");
            startActivity(SignInwithPhone);
            finish();
        });

        SignUp.setOnClickListener(view -> {
            Intent SignUp = new Intent(MainMenu.this,ChooseOne.class);
            SignUp.putExtra("Home", "SignUp");
            startActivity(SignUp);
            finish();

        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}