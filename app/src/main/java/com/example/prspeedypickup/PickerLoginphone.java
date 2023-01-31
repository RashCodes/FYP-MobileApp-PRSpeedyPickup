package com.example.prspeedypickup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

public class PickerLoginphone extends AppCompatActivity {

    EditText num;
    Button sendotp,signinemail;
    TextView signup;
    CountryCodePicker cpp;
    FirebaseAuth Fauth;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginphone);

        num = (EditText) findViewById(R.id.Mobilenumber1);
        sendotp =(Button)findViewById(R.id.plpSendotp);
        cpp = (CountryCodePicker)findViewById(R.id.plpCountrycode);
        signinemail = (Button)findViewById(R.id.plpemail);
        signup = (TextView)findViewById(R.id.plpsignup);

        Fauth = FirebaseAuth.getInstance();

        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                number=num.getText().toString().trim();

                String Mobilenumber1   = cpp.getSelectedCountryCodeWithPlus()+ number;
                Intent b = new Intent(PickerLoginphone.this,Pickersendotp.class);
                b.putExtra("Mobilenumber1", Mobilenumber1);
                b.putExtra("number", number);
                startActivity(b);
                finish();


            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PickerLoginphone.this,PickerRegistration.class));
                finish();
            }
        });
        signinemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PickerLoginphone.this,PickerLogin.class));
                finish();
            }
        });

    }
}