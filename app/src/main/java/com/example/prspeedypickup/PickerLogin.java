package com.example.prspeedypickup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prspeedypickup.mainClass.nav;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PickerLogin extends AppCompatActivity {

    TextInputLayout email, pass;
    Button Signin, Signinphone;
    TextView signup;
    FirebaseAuth FAuth;
    String emailid, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            email = (TextInputLayout)findViewById(R.id.plEmail);
            pass = (TextInputLayout)findViewById(R.id.plPassword);
            Signin = (Button)findViewById(R.id.pllogin);
            signup =(TextView)findViewById(R.id.pltextview4);
            Signinphone =(Button)findViewById(R.id.plphone);
            FAuth = FirebaseAuth.getInstance();

            Signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    emailid = email.getEditText().getText().toString().trim();
                    password = pass.getEditText().getText().toString().trim();
                    if (isValid()){
                        final ProgressDialog mDialog = new ProgressDialog(PickerLogin.this);
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.setCancelable(false);
                        mDialog.setMessage("Sign In Please wait...");
                        mDialog.show();
                        mDialog.dismiss();
                        FAuth.signInWithEmailAndPassword(emailid,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    String a = emailid.replace(".", "");
                                    DatabaseReference fRef = FirebaseDatabase.getInstance().getReference("user").child(a);
                                    fRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String str_email = snapshot.child("EmailId").getValue(String.class);
                                            String status = snapshot.child("role").getValue(String.class);
                                            String name = snapshot.child("First Name").getValue(String.class);
                                            String str_password = snapshot.child("Password").getValue(String.class);
                                            if (emailid.equals(str_email) && password.equals(str_password)) {
                                                Intent intent = new Intent(PickerLogin.this, nav.class);
                                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(PickerLogin.this);
                                                SharedPreferences.Editor editor = preferences.edit();
                                                editor.putString("status", status);
                                                editor.putString("email", str_email);
                                                editor.apply();
//                                                editor.putBoolean("check", true);
                                                editor.apply();
                                            intent.putExtra("getemail", str_email);
                                            intent.putExtra("getstatus", status);
                                            intent.putExtra("getname", name);
                                                startActivity(intent);
                                                finishAffinity();
                                                Toast.makeText(PickerLogin.this, "logged in as " + str_email + " " + status, Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(PickerLogin.this, "Invalid Credentials ", Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                    // Sign in success, update UI with the signed-in user's information
//                                    Log.d(String.valueOf(PickerLogin.this), "signInWithEmail:success");
//                                    Toast.makeText(PickerLogin.this,"Congratulation! You have Successfully login", Toast.LENGTH_SHORT).show();
//                                    FirebaseUser user = FAuth.getCurrentUser();
//                                    Intent Z = new Intent(PickerLogin.this,PickerToolsPanel_BottomNavigation.class);
//                                    startActivity(Z);
//                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(String.valueOf(PickerLogin.this), "Verification Failed, You haven't Verified your Email", task.getException());
                                    Toast.makeText(PickerLogin.this, "Error",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }

                }
            });

            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(PickerLogin.this,PickerRegistration.class));
                    finish();
                }
            });

            Signinphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(PickerLogin.this,PickerLoginphone.class));
                    finish();
                }
            });
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public boolean isValid(){

        email.setErrorEnabled(false);
        email.setError("");
        pass.setErrorEnabled(false);
        pass.setError("");

        boolean isValid=false,isvalidemail=false,isvalidpassword=false;
        if (TextUtils.isEmpty(emailid)) {
            email.setErrorEnabled(true);
            email.setError("Email is required");

        }else{
            if (emailid.matches(emailpattern)){
                isvalidemail=true;
            }else{
                email.setErrorEnabled(true);
                email.setError("Invalid Email Address");
            }
        }
        if (TextUtils.isEmpty(password)){
            pass.setErrorEnabled(true);
            pass.setError("Password is Required");
        }else{
            isvalidpassword=true;
        }
        isValid=(isvalidemail && isvalidpassword)?true:false;
        return isValid;
    }

}