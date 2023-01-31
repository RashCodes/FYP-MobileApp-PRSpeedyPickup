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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prspeedypickup.R;
import com.example.prspeedypickup.mainClass.displayad;
import com.example.prspeedypickup.mainClass.nav;
import com.example.prspeedypickup.models.setdata;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class getpostdata extends RecyclerView.Adapter<getpostdata.myHolder> implements Filterable {
    Context mcontext;
    public getpostdata(Context context, ArrayList<setdata> data) {
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
        View view = inflater.inflate(R.layout.row, parent, false);
        return new myHolder(view);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {
        Glide.with(holder.postimg1.getContext()).load(data.get(position).getRandomkey()).into(holder.postimg1);
//        Glide.with(holder.postimg2.getContext()).load(data.get(position).getRandomkey()).into(holder.postimg2);
        setdata petDataa = data.get(position);
        holder.title1.setText(petDataa.getName());
//        holder.title2.setText(petDataa.getName());
        holder.price1.setText("Price"+petDataa.getAmount()+" SAR");
//        holder.price2.setText(petDataa.getAmount());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nav.statusche.equals("user")){
                    Intent intent = new Intent(mcontext, displayad.class);
                    intent.putExtra("title",petDataa.getName());
                    intent.putExtra("price",petDataa.getAmount());
                    intent.putExtra("detail",petDataa.getAct());
                    intent.putExtra("pic",petDataa.getRandomkey());
                    intent.putExtra("cate",petDataa.getCategory());

                    mcontext.startActivity(intent);
                }

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

        TextView price1, price2, title1, title2;
        ImageView postimg1, postimg2;
        ConstraintLayout layout;
        public myHolder(@NonNull View itemView) {
            super(itemView);
            postimg1 = itemView.findViewById(R.id.img1);
//            postimg2 = itemView.findViewById(R.id.img2);
            price1 = itemView.findViewById(R.id.txt11);
            layout = itemView.findViewById(R.id.layout);
//            price2 = itemView.findViewById(R.id.txt22);
            title1 = itemView.findViewById(R.id.txt1);
//            title2 = itemView.findViewById(R.id.txt2);
        }
    }
}