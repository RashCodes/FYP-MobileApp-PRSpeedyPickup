package com.example.prspeedypickup.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prspeedypickup.R;
import com.example.prspeedypickup.mainClass.displayad;
import com.example.prspeedypickup.mainClass.nav;
import com.example.prspeedypickup.models.setdata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class getpostdataforcategories extends RecyclerView.Adapter<getpostdataforcategories.myHolder> implements Filterable {
    Context mcontext;
    public getpostdataforcategories(Context context, ArrayList<setdata> data) {
        this.data = data;
        mcontext=context;
    }
    ArrayList<setdata> data;
    String name;
    FirebaseFirestore fstore;
    String role;
    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.rowforcategories, parent, false);
        return new myHolder(view);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {
        Glide.with(holder.postimg1.getContext()).load(data.get(position).getRandomkey()).into(holder.postimg1);
//        Glide.with(holder.postimg2.getContext()).load(data.get(position).getRandomkey()).into(holder.postimg2);
        setdata petDataa = data.get(position);
        holder.title1.setText(petDataa.getName());
        holder.quan.setText("Quantity "+petDataa.getQuantity());
        holder.price1.setText("Price "+petDataa.getAmount()+" SAR");
        holder.cancelorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference cateData = (DatabaseReference) FirebaseDatabase.getInstance().getReference("student").child("order");
                Query dummyQuery = cateData
                        .child(nav.emailss)
                        .orderByChild("name")
                        .equalTo(petDataa.getName());

                dummyQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dummySnapshot: dataSnapshot.getChildren()) {
                            dummySnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, displayad.class);
                intent.putExtra("title",petDataa.getName());
                intent.putExtra("price",petDataa.getAmount());
                intent.putExtra("detail",petDataa.getAct());
                intent.putExtra("pic",petDataa.getRandomkey());
                intent.putExtra("cate",petDataa.getCategory());
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<setdata> filterlist = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                filterlist.addAll(data);
            }
            return null;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
        }
    };
    public class myHolder extends RecyclerView.ViewHolder {
        TextView price1, cancelorder, quan,title1, location;
        ImageView postimg1, postimg2;
        LinearLayout layout;
        public myHolder(@NonNull View itemView) {
            super(itemView);
            postimg1 = itemView.findViewById(R.id.img1);
            layout = itemView.findViewById(R.id.itemlinear);
            cancelorder = itemView.findViewById(R.id.cancl);
            price1 = itemView.findViewById(R.id.txt11);
            quan = itemView.findViewById(R.id.quan);
            title1 = itemView.findViewById(R.id.txt1);
//            title2 = itemView.findViewById(R.id.txt2);
        }
    }
}