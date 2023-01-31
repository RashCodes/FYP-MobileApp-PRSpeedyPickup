package com.example.prspeedypickup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prspeedypickup.mainClass.nav;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class Pickersendotp extends AppCompatActivity {

    String verificationId;
    FirebaseAuth FAuth;
    Button verify , Resend;
    TextView txt;
    EditText entercode;
    String Mobilenumber1;
    String number1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickersendotp);

        Mobilenumber1 = getIntent().getStringExtra("Mobilenumber1").trim();
       number1 = getIntent().getStringExtra("number").trim();

        entercode = (EditText) findViewById(R.id.PSOcode);
        txt = (TextView) findViewById(R.id.PSOtext);
        Resend = (Button) findViewById(R.id.PSOResendotp);
        verify = (Button) findViewById(R.id.PSOVerify);
        FAuth = FirebaseAuth.getInstance();

        txt.setVisibility(View.GONE);

        sendverificationcodeToUser(Mobilenumber1);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = entercode.getText().toString().trim();
                Resend.setVisibility(View.INVISIBLE);

                if (code.isEmpty() || code.length()<6){
                    entercode.setError("Enter code...");
                    entercode.requestFocus();
                    return;
                }

                verifyCode(code);
            }
        });

        new CountDownTimer(60000,1000){

            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {

                txt.setVisibility(View.VISIBLE);
                txt.setText("Resend Code within"+millisUntilFinished/1000+ "Seconds");

            }

            /**
             * Callback fired when the time is up.
             */
            @Override
            public void onFinish() {
                Resend.setVisibility(View.VISIBLE);
                txt.setVisibility(View.INVISIBLE);

            }
        }.start();

        Resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Resend.setVisibility(View.INVISIBLE);
                Resendotp(Mobilenumber1);

                new CountDownTimer(60000,1000){

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTick(long millisUntilFinished) {

                        txt.setVisibility(View.VISIBLE);
                        txt.setText(" Resend Code within "+millisUntilFinished/1000+ " Seconds ");

                    }

                    /**
                     * Callback fired when the time is up.
                     */
                    @Override
                    public void onFinish() {
                        Resend.setVisibility(View.VISIBLE);
                        txt.setVisibility(View.INVISIBLE);

                    }
                }.start();
            }
        });

    }

    private void Resendotp (String Mobilenumber1){

        sendverificationcodeToUser(Mobilenumber1);

    }


    private void sendverificationcodeToUser(String number) {


        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FAuth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code!=null){
                entercode.setText(code); // Auto Verification
                verifyCode(code);

            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(Pickersendotp.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

    };

    private void verifyCode (String code){

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhone(credential);
    }

    private void signInWithPhone(PhoneAuthCredential credential) {

        FAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            DatabaseReference fRef = FirebaseDatabase.getInstance().getReference("user");

                            Query query = fRef.orderByChild("Mobile No").equalTo(number1);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                        String str_email = snapshot1.child("EmailId").getValue(String.class);
                                        String status = snapshot1.child("role").getValue(String.class);
                                        String name = snapshot1.child("First Name").getValue(String.class);
                                        Intent intent = new Intent(Pickersendotp.this, nav.class);
                                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Pickersendotp.this);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("status", status);
                                        editor.putString("email", str_email);
                                        editor.apply();
                                        editor.apply();
                                        intent.putExtra("getemail", str_email);
                                        intent.putExtra("getstatus", status);
                                        intent.putExtra("getname", name);
                                        Log.i("imam", "onDataChange: "+str_email);
                                        startActivity(intent);
                                        finishAffinity();
                                        Toast.makeText(Pickersendotp.this, "logged in as " + status + " ", Toast.LENGTH_LONG).show();
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }else{
                            ReusableCodeForAll.ShowAlert(Pickersendotp.this,"Error",task.getException().getMessage());
                        }

                    }
                });
    }
}