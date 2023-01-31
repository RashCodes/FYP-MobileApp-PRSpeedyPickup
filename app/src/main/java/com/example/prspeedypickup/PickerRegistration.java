package com.example.prspeedypickup;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Objects;

public class PickerRegistration extends AppCompatActivity {

    TextInputLayout Fname,Lname,Email,Pass,Cpass,Mobileno;
    Button Signup,Emaill,Phone;
    CountryCodePicker Cpp;
    FirebaseAuth FAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String fname,lname,emailid,password,confpassword,mobile;
    String role="PickerMan";
    private boolean phoneNo2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Fname = (TextInputLayout) findViewById(R.id.PRFname);
        Lname = (TextInputLayout) findViewById(R.id.PRLname);
        Email = (TextInputLayout) findViewById(R.id.PRemailid);
        Pass = (TextInputLayout) findViewById(R.id.PRpassword);
        Cpass = (TextInputLayout) findViewById(R.id.PRconfirmpassword);
        Mobileno = (TextInputLayout) findViewById(R.id.phoneNo2);


        Signup = (Button) findViewById(R.id.PRSignup);
        Emaill = (Button) findViewById(R.id.PRemail);
        Phone = (Button) findViewById(R.id.PRphone);

        Cpp = (CountryCodePicker) findViewById(R.id.PRCountryCode);

        databaseReference = FirebaseDatabase.getInstance().getReference("PickerMan");
        FAuth = FirebaseAuth.getInstance();

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname = Objects.requireNonNull(Fname.getEditText()).getText().toString().trim();
                lname = Objects.requireNonNull(Lname.getEditText()).getText().toString().trim();
                emailid = Objects.requireNonNull(Email.getEditText()).getText().toString().trim();
                mobile = Objects.requireNonNull(Mobileno.getEditText()).getText().toString().trim();
                password = Objects.requireNonNull(Pass.getEditText()).getText().toString().trim();
                confpassword = Objects.requireNonNull(Cpass.getEditText()).getText().toString().trim();
                if (isValid()) {
                    final ProgressDialog mDialog = new ProgressDialog(PickerRegistration.this);
                    mDialog.setCancelable(false);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setMessage("Registration in progress please wait...");
                    mDialog.show();
                    mDialog.dismiss();
                    FAuth.createUserWithEmailAndPassword(emailid, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String useridd = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(useridd);
                                final HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("Role",role);
                                databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        HashMap<String, String> hashMap1 = new HashMap<>();
                                        hashMap1.put("First Name", fname);
                                        hashMap1.put("Last Name", lname);
                                        hashMap1.put("EmailId", emailid);
                                        hashMap1.put("uid", FirebaseAuth.getInstance().getUid());
                                        hashMap1.put("Mobile No", mobile);
                                        hashMap1.put("Password", password);
                                        hashMap1.put("role", "user");
                                        hashMap1.put("Confirm Password", confpassword);
                                        String a = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "");
                                        firebaseDatabase.getInstance().getReference("user")
                                                .child(a)
                                                .setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        mDialog.dismiss();
                                                        Objects.requireNonNull(FAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(PickerRegistration.this);
                                                                    builder.setMessage("You Have Registered! Make Sure To Verify Your Email");
                                                                    builder.setCancelable(false);
                                                                    builder.setPositiveButton("Ok", (dialog, which) -> {
                                                                        dialog.dismiss();
                                                                        String phoneNo2 = Cpp.getSelectedCountryCodeWithPlus() + mobile;
                                                                        Intent intent = new Intent(PickerRegistration.this, PickerVerifyPhone.class);
                                                                        intent.putExtra("phoneNo2", phoneNo2);
                                                                        startActivity(intent);
                                                                    });
                                                                    AlertDialog Alert = builder.create();
                                                                    Alert.show();
                                                                } else {
                                                                    mDialog.dismiss();
                                                                    ReusableCodeForAll.ShowAlert(PickerRegistration.this, "Error", Objects.requireNonNull(task.getException()).getMessage());
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                    }
                                });
                            }else{
                                mDialog.dismiss();
                                ReusableCodeForAll.ShowAlert(PickerRegistration.this, "Error", Objects.requireNonNull(task.getException()).getMessage());
                            }
                        }
                    });
                }


            }
        });

        Emaill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PickerRegistration.this,PickerLogin.class));
                finish();
            }
        });

        Phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PickerRegistration.this,PickerLoginphone.class));
                finish();
            }
        });
    }

    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public boolean isValid(){

        Fname.setErrorEnabled(false);
        Fname.setError("");
        Lname.setErrorEnabled(false);
        Lname.setError("");
        Email.setErrorEnabled(false);
        Email.setError("");
        Pass.setErrorEnabled(false);
        Pass.setError("");
        Cpass.setErrorEnabled(false);
        Cpass.setError("");
        Mobileno.setErrorEnabled(false);
        Mobileno.setError("");



        boolean isValid=false,isValidfname=false,isValidlname=false,isValidemail=false,isValidpassword=false,isValidconfpassword=false,isValidmobilenumber=false;
        if (TextUtils.isEmpty(fname)){
            Fname.setErrorEnabled(true);
            Fname.setError("Enter First Name");
        }else{
            isValidfname = true;
        }
        if (TextUtils.isEmpty(lname)){
            Lname.setErrorEnabled(true);
            Lname.setError("Enter Last Name");
        }else{
            isValidlname = true;
        }
        if (TextUtils.isEmpty(emailid)){
            Email.setErrorEnabled(true);
            Email.setError("Email is Required");
        }else{
            if (emailid.matches(emailpattern)){
                isValidemail = true;

            }else{
                Email.setErrorEnabled(true);
                Email.setError("Enter a Vaild Email Id");

            }
        }
        if (TextUtils.isEmpty(password)){
            Pass.setErrorEnabled(true);
            Pass.setError("Enter Password");
        }else{
            if (password.length()<8){
                Pass.setErrorEnabled(true);
                Pass.setError("Password is weak");
            }else{
                isValidpassword = true;
            }
        }
        if (TextUtils.isEmpty(confpassword)){
            Cpass.setErrorEnabled(true);
            Cpass.setError("Enter Password Again");
        }else{
            if (!password.equals(confpassword)){
                Cpass.setErrorEnabled(true);
                Cpass.setError("Password Dosen't Match");
            }else{
                isValidconfpassword = true;

            }
        }
        if (TextUtils.isEmpty(mobile)){
            Mobileno.setErrorEnabled(true);
            Mobileno.setError("Mobile Number is Required");
        }else {
            if (mobile.length() < 10) {
                Mobileno.setErrorEnabled(true);
                Mobileno.setError("Invaild Mobile Number");
            } else {
                isValidmobilenumber = true;
            }
        }
        isValid = isValidconfpassword && isValidpassword && isValidemail && isValidmobilenumber && isValidfname && isValidlname;
        return isValid;
    }

}