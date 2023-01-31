package com.example.prspeedypickup.mainClass;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prspeedypickup.R;
import com.example.prspeedypickup.models.setdata;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class post extends AppCompatActivity {
    Spinner category;
    ArrayList<String> sear = new ArrayList<>();
    LinearLayout progress_bar;
    String searchcategory;
    ArrayList<String> arrayList = new ArrayList<>();
    ImageView gallery, locat;
    public static final int PICK_IMAGE = 1;
    public Uri selectedImage;
    Bitmap bitmap;
    Button savebtn;
    TextView title,price,detail, location, show;
    String gettitle,getprice,getdetail;
    StorageReference storageReference;
    ArrayList<String> arrayLaist = new ArrayList<>();
    boolean Rash;
    String getcity;
    Button yes;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        gallery = findViewById(R.id.editbtn);
        savebtn = findViewById(R.id.savbtn);
        title = findViewById(R.id.titletxt);
        location = findViewById(R.id.loco);
        locat = findViewById(R.id.loc);
        price = findViewById(R.id.pricetxt);
        detail = findViewById(R.id.detailtxt);
        Rash = false;
        ///categorywork
        sear.add("Select Category");
        DatabaseReference cateData = FirebaseDatabase.getInstance().getReference().child("category").child("cate");
        storageReference = FirebaseStorage.getInstance().getReference();
        //categoryworkend//
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, PICK_IMAGE);
            }
        });
        ///save button
        savebtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Rash = true;
                    DatabaseReference createpost = FirebaseDatabase.getInstance().getReference().child("create").child("post");
                    getdetail = detail.getText().toString();
                    getprice = price.getText().toString();
                    gettitle = title.getText().toString();

                    if (!getprice.isEmpty() && !gettitle.isEmpty() && !getdetail.isEmpty() && selectedImage != null ) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.WEBP, 100, stream);
                        final ProgressDialog pdd = new ProgressDialog(post.this);
                        pdd.setTitle("Uploading Data...");
                        pdd.show();
                        final String randomkey = UUID.randomUUID().toString();
                        StorageReference ref = storageReference.child("image/" + randomkey);
                        ref.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        createpost.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                Dialog dialog = new Dialog(post.this);
                                                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                                                dialog.setContentView(R.layout.prompt);
                                                Button ok = dialog.findViewById(R.id.yes);
                                                TextView msg = dialog.findViewById(R.id.textshow);
                                                if (Rash) {
                                                    setdata pd = new setdata(gettitle, uri.toString(), getdetail, getprice,searchcategory);
                                                    createpost.push().setValue(pd);
                                                    Rash = false;
                                                }
                                                msg.setText("Data inserted Successfully");
                                                ok.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                                dialog.show();
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });
                                    }
                                });
                                Snackbar.make(findViewById(android.R.id.content), "Image Uploaded.", Snackbar.LENGTH_LONG).show();
                                pdd.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Dialog dialog = new Dialog(post.this);
                                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                                dialog.setContentView(R.layout.prompt);
                                Button ok = dialog.findViewById(R.id.yes);
                                TextView msg = dialog.findViewById(R.id.textshow);
                                msg.setText("Failed To Upload");
                                ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                                pdd.dismiss();
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                double progresspercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                                pdd.setMessage("Percentage: " + (int) progresspercent + "%");
                            }
                        });
                    } else {
                        Dialog dialog = new Dialog(post.this);
                        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.setContentView(R.layout.prompt);
                        Button ok = dialog.findViewById(R.id.yes);
                        TextView msg = dialog.findViewById(R.id.textshow);
                        msg.setText("Missing Fields (Text/Picture)");
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }

            }});
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                Toast.makeText(post.this, "Image has been selected", Toast.LENGTH_SHORT).show();
                gallery.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}